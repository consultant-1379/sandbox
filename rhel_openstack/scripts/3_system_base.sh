#!/bin/bash

. /tmp/helper_function.sh

echo "proxy required: ${PROXY_REQUIRED}"

if [[ "${PROXY_REQUIRED}" == "true" ]]; then
   echo "http_proxy=http://www-proxy.ericsson.se:8080" >> /etc/wgetrc
   echo "https_proxy=http://www-proxy.ericsson.se:8080" >> /etc/wgetrc
fi

# Install Java 8 (required by Marathon)
# Doing this AFTER the above steps because one of them pulls in JDK 7!
echo "Downloading and installing JAVA 8..." 

# Using a slightly older version.
wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u66-b17/jdk-8u66-linux-x64.rpm"
rpm -Uvh jdk-8u66-linux-x64.rpm

# Installing Confluent platform.
echo "Installing Confluent Platform repo..." 
rpm --import http://packages.confluent.io/rpm/2.0/archive.key

# Mesos repo from Mesosphere.
echo "Installing mesosphere repo..." 
rpm -Uvh http://repos.mesosphere.com/el/7/noarch/RPMS/mesosphere-el-repo-7-1.noarch.rpm

cat > /etc/yum.repos.d/confluent.repo <<EOF
[confluent-2.0]
name=Confluent repository for 2.0.x packages
baseurl=http://packages.confluent.io/rpm/2.0
gpgcheck=1
gpgkey=http://packages.confluent.io/rpm/2.0/archive.key
enabled=1
EOF

# Set-up passwordless SSH for root in localhost
mkdir -p /root/.ssh
chmod 700 /root/.ssh
ssh-keygen -A
rm -f /root/.ssh/id_rsa
ssh-keygen -b 2048 -t rsa -f /root/.ssh/id_rsa -q -N ""
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
chmod og-wx ~/.ssh/authorized_keys

echo "net.ipv6.conf.all.disable_ipv6 = 1" >> /etc/sysctl.conf

cat > /etc/systemd/system/kafka-server.service <<EOF
[Unit]
Description=Confluent Kafka Server
After=zookeeper-server.service
Wants=zookeeper-server.service

[Service]
Type=simple
User=root
ExecStartPre=/bin/sh -c 'sleep 10'
ExecStart=/bin/kafka-server-start /etc/kafka/server.properties
ExecStop=/bin/kafka-server-stop
Restart=on-abort


[Install]
WantedBy=multi-user.target
EOF

cat > /etc/systemd/system/schema-registry.service <<EOF
[Unit]
Description=Confluent Schema Registry
After=kafka-server.service
Wants=kafka-server.service

[Service]
Type=simple
User=root
ExecStartPre=/bin/sh -c 'sleep 20'
ExecStart=/bin/schema-registry-start /etc/schema-registry/schema-registry.properties
ExecStop=/bin/schema-registry-stop
Restart=on-abort


[Install]
WantedBy=multi-user.target
EOF



