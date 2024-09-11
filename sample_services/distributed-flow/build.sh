#!/usr/bin/env bash

mvn clean install

docker build -t distributed-flow .