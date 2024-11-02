FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y \
    git \
    vim \
    python3.11-venv \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app


RUN python3 -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

COPY oj/requirements.txt /oj/requirements.txt
RUN pip3 install -r /oj/requirements.txt