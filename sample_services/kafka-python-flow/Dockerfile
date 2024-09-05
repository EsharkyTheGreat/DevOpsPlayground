FROM python:3.10-slim

WORKDIR /app

COPY requirement.txt .

RUN pip install -r requirement.txt

COPY main.py . 

EXPOSE 8080

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8080"]
