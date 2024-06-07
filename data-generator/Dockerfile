FROM python:3.9-slim
WORKDIR /usr/src/app
COPY . .
RUN pip install kafka-python
CMD ["python", "./data_generator.py"]
