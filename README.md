# User Time Tracking on Webpage

This project consists of a user time tracking system that generates, processes, and displays the average time spent by users on a webpage over the last 30 seconds using Kafka and Kafka Streams. The project is containerized using Docker.

## Components

1. **Data Generator**: A Python script that simulates user interaction and sends the time spent data to a Kafka topic.
2. **Kafka Streams Processor**: A Java application that processes the incoming data from the Kafka topic and computes the average time spent by users.
3. **Docker Setup**: Docker containers for Kafka, Zookeeper, the data generator, and the Kafka Streams processor.

## Prerequisites

- Docker and Docker Compose
- Java Development Kit (JDK) 8 or higher
- Apache Kafka and Zookeeper
- Python 3.x

## Setup

1. **Clone the Repository**

    ```sh
    git clone https://github.com/IvanFilipchuk/User-Time-Tracking-Kafka.git
    cd user-time-tracking-kafka
    ```

2. **Build and Run with Docker Compose**

    ```sh
    docker-compose up --build
    ```
