import os
import json
from fastapi import FastAPI 
from confluent_kafka import Consumer, Producer, KafkaException
import threading

app = FastAPI()

# Load configuration from environment variables
SERVICE_NAME = os.getenv('SERVICE_NAME', 'default_service')
KAFKA_BOOTSTRAP_SERVERS = os.getenv('KAFKA_BOOTSTRAP_SERVERS', 'localhost:9092')
GROUP_ID = SERVICE_NAME

# Load service-specific configuration
service_config: dict[str,dict[str,str]] = json.load(open('config.json'))


# Default to a specific service if not found
if SERVICE_NAME not in service_config:
    raise ValueError(f"Service name {SERVICE_NAME} not found in configuration")

config = service_config[SERVICE_NAME]
input_topic = config.get("input_topic")
output_topic = config.get("output_topic")
processing_function_name = config.get("processing_function")


def send_to_topic(mssg):
    pass
def create_file(mssg):
    pass
def file_count_check(mssg):
    pass
def file_size_check(mssg):
    pass
def file_delivery(mssg):
    pass

processing_function_map = {
    "send_to_topic" : send_to_topic,
    "create_file" : create_file,
    "file_count_check" : file_count_check,
    "file_size_check" :file_size_check,
    "file_delivery" : file_delivery,
}

# Initialize Kafka Consumer and Producer
consumer = Consumer({
    'bootstrap.servers': KAFKA_BOOTSTRAP_SERVERS,
    'group.id': GROUP_ID,
    'auto.offset.reset': 'earliest'
})

producer = Producer({'bootstrap.servers': KAFKA_BOOTSTRAP_SERVERS})
if input_topic:
    consumer.subscribe([input_topic])

# In-memory metrics storage
metrics = {
    'messages_received': 0,
    'messages_sent': 0
}

def consume_and_process():
    while True:
        msg = consumer.poll(timeout=1.0)
        if msg is None:
            continue
        if msg.error():
            raise KafkaException(msg.error())
        
        # Increment received message counter
        metrics['messages_received'] += 1
        
        # Process message
        key = msg.key().decode('utf-8')
        value = msg.value().decode('utf-8')

        print(msg,key,value)

        # if key == input_topic:
        #     processed_message = process_func(value)
        #
        #     # Forward the processed message
        #     producer.produce(output_topic, value=processed_message)
        #     metrics['messages_sent'] += 1
        #
        # producer.flush()

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

@app.get("/consume-message")
def consume_message():
    # For demo purpose, this might just trigger the consume method
    return {"status": "consumed"}
