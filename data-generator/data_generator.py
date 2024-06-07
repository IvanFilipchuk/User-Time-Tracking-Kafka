import time
import json
import random
from kafka import KafkaProducer
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)
def generate_user_data():
    user_id = random.randint(1, 100)
    time_spent = random.randint(1, 300)
    return {
        'user_id': user_id,
        'time_spent': time_spent,
        'timestamp': int(time.time())
    }
def create_kafka_producer():
    return KafkaProducer(
        bootstrap_servers='kafka:9092',
        value_serializer=lambda v: json.dumps(v).encode('utf-8'),
        api_version=(0, 10)
    )
def send_user_data(producer, topic):
    while True:
        data = generate_user_data()
        logger.info(f'Wysyłanie danych: {data}')
        key = str(data['user_id']).encode('utf-8')
        producer.send(topic, key=key, value=data)
        time.sleep(1)
def main():
    topic = 'user_activity'
    producer = create_kafka_producer()
    try:
        send_user_data(producer, topic)
    except KeyboardInterrupt:
        logger.info("Zatrzymano producenta")
    finally:
        producer.close()

if __name__ == "__main__":
    main()
