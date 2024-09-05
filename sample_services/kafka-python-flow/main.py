import os
import uuid
import logging
import json
import shutil
import pandas as pd
from fastapi import FastAPI, File, UploadFile 
from kafka import KafkaConsumer,KafkaProducer
import threading
import sys

app = FastAPI()

FILE_PATH_FOLDER = "/mnt/data"
METADATA = {'trace_id':'000-00-00'}

logger = logging.getLogger(__name__)
syslog = logging.StreamHandler(stream=sys.stdout)
filelog = logging.FileHandler("app.log","a")
formatter = logging.Formatter('| %(asctime)s | %(levelname)s | %(trace_id)s |- %(message)s')
syslog.setFormatter(formatter)
filelog.setFormatter(formatter)
logger.addHandler(syslog)
logger.addHandler(filelog)
logger = logging.LoggerAdapter(logger,METADATA)
logger.setLevel(logging.DEBUG)

# Load configuration from environment variables
SERVICE_NAME = os.getenv('SERVICE_NAME', 'default_service')
KAFKA_BOOTSTRAP_SERVERS = os.getenv('KAFKA_BOOTSTRAP_SERVERS', 'localhost:9092')
GROUP_ID = SERVICE_NAME

# Load service-specific configuration
service_config: dict[str,dict[str,str]] = json.load(open(FILE_PATH_FOLDER+'/'+'config.json'))

# Default to a specific service if not found
if SERVICE_NAME not in service_config:
    raise ValueError(f"Service name {SERVICE_NAME} not found in configuration")

config = service_config[SERVICE_NAME]
input_topic = config.get("input_topic")
output_topic = config.get("output_topic")
processing_function_name = config.get("processing_function")


def create_file(mssg):
    file_location = mssg['file_location']
    logger.info(f"Overwriting file at {file_location}")
    df = pd.DataFrame({"Name":["Esharky","Esharky Jr"],"Age":[21,12],"Job":["SDE","Student"]})
    logger.info(f"Dataframe: {df}")
    df.to_csv(file_location)
    return mssg

def file_count_check(mssg):
    logger.info("Checking File Count")
    logger.warn("File Count Mismatch")
    return mssg

def file_size_check(mssg):
    logger.info("Checking File Size")
    logger.error("File Size too high")
    return mssg

def file_delivery(mssg):
    logger.info("File delivered successfully")
    return mssg

processing_function_map = {
    "create_file" : create_file,
    "file_count_check" : file_count_check,
    "file_size_check" :file_size_check,
    "file_delivery" : file_delivery,
}

# Initialize Kafka Consumer and Producer
consumer = KafkaConsumer(
    bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
    auto_offset_reset= 'earliest',
    enable_auto_commit=True,
    value_deserializer=lambda v : json.loads(v.decode('utf-8'))
)

producer = KafkaProducer(bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,value_serializer=lambda v : json.dumps(v).encode('utf-8'))

if input_topic:
    consumer.subscribe([input_topic])

# In-memory metrics storage
metrics = {
    'messages_received': 0,
    'messages_sent': 0
}

def consume_and_process():
    global METADATA
    for msg in consumer:
        
        # Increment received message counter
        metrics['messages_received'] += 1
        
        value = msg.value

        logger.info(f"Received Message : {value}")
        
        if 'trace_id' in value:
            METADATA['trace_id'] = value['trace_id']    

        logger.debug(f"Kafka: {value=}")

        if msg.topic == input_topic:
            logger.debug(f"Message Topic : {msg.topic}")
            processed_message = processing_function_map[processing_function_name](value) if processing_function_name in processing_function_map else None
            logger.info(f"Forwarding Output: {processed_message}")
            if processed_message and output_topic:
            # Forward the processed message
                producer.send(output_topic, value=processed_message)
                metrics['messages_sent'] += 1
                producer.flush()

# Start a thread for Kafka consumer
thread = threading.Thread(target=consume_and_process)
thread.daemon = True
thread.start()

@app.get("/service-metadata")
def get_service_metadata():
    return {
        "service": SERVICE_NAME,
        "kafka_bootstrap_servers": KAFKA_BOOTSTRAP_SERVERS,
        "kafka_group_id": GROUP_ID,
        "input_topic": input_topic,
        "output_topic": output_topic,
        "processing_function": processing_function_name
    }

@app.get("/metrics")
def get_metrics():
    return metrics

if SERVICE_NAME == "ingestion-service":
    @app.post("/uploadfile")
    async def upload_file(file: UploadFile = File(...)):
        trace_id = str(uuid.uuid4())
        METADATA['trace_id'] = trace_id
        logger.info(f"Got File for {trace_id}")
        os.mkdir(f"{FILE_PATH_FOLDER}/{trace_id}")
        file_location = f"{FILE_PATH_FOLDER}/{trace_id}/{file.filename}"
        with open(file_location,"wb") as buf:
            shutil.copyfileobj(file.file,buf)
        mssg = {"trace_id":trace_id,"file_location":file_location}
        if output_topic:
            producer.produce(output_topic,json.dumps(mssg).encode('utf-8'))
        return {"trace_id":trace_id,"file_location":file_location,"message":"file sucessfully uploaded"}
