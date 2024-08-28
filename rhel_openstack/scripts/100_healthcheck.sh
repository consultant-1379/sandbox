#!/bin/bash

service_list=("kafka-server" "zeppelin" "alluxio" "spark-master" "spark-worker" "mesos-master" "mesos-slave" "mesos-executor" "marathon" "docker")

for srv in "${service_list[@]}"
do
   sudo systemctl status $srv
done
