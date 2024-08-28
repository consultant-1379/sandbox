#
# This script installs analytics base components.
#

. /tmp/installer/functions.sh

cecho "\n*** Installing Analytics Components ***\n" $GREEN

if [[ ! "${PACKER_BUILDER_TYPE}" == "openstack" ]]; then
cecho "Configuring Welcome script" $GREEN
cp /tmp/installer/files/welcome.sh /etc/profile.d/
fi

echo "Copying healthcheck scripts..."
cp /tmp/installer/files/healthcheck /usr/bin/
chmod +x /usr/bin/healthcheck

cecho "Configuring Howto portal" $GREEN  
cp -r /tmp/installer/howto/* /var/www/html

cecho "Installing confluent platform for kafka and schema registry..." $GREEN
yum -y install confluent-platform-2.11.7

systemctl enable kafka-server.service 
systemctl enable schema-registry.service

cecho "Restarting zookeeper server..." $GREEN
service zookeeper-server restart
service zookeeper-server status

cecho "Starting the KAFKA and schema registry service..." $GREEN

#systemctl start kafka-server.service
/bin/kafka-server-start /etc/kafka/server.properties &

sleep 30

#systemctl start schema-registry.service
/bin/schema-registry-start /etc/schema-registry/schema-registry.properties &

sleep 30

cecho "Installing the avro models..." $GREEN

cecho "Downloading schema jar from nexus..." $BLUE
cd /tmp/installer
if [ ! -f avro-json-2.0.20.jar ];then
   wget https://arm101-eiffel004.lmera.ericsson.se:8443/nexus/service/local/repositories/aia-releases/content/com/ericsson/component/aia/model/avro-json/2.0.20/avro-json-2.0.20.jar
fi
cecho "Decompressing the jar file..." $BLUE
/usr/bin/jar xvf avro-json-2.0.20.jar 

cecho "Copying avro schema to /schema..." $BLUE
mkdir -p /schema/
cp -R avro /schema/

cecho "Installing batch import tool for model..." $GREEN

cecho "Decompressing batch schema import tool..." $BLUE
tar xvfz batchimport.tar.gz -C /

cecho "Running the batch schema import...", $BLUE
cd /batchimport
sh startimport.sh

cecho "Installing simulator tool for kafka..." $GREEN

cecho "Decompressing simulator tool..." $BLUE
tar xvfz /tmp/installer/simulator.tar.gz -C /

cecho "stopping kafka and schema..." $GREEN
/bin/schema-registry-stop
/bin/kafka-server-stop

#install flink
mkdir /opt/flink
cd /tmp/installer
if [ ! -f flink-1.1.3-bin-hadoop26-scala_2.11.tgz ];then
    wget http://ftp.heanet.ie/mirrors/www.apache.org/dist/flink/flink-1.1.3/flink-1.1.3-bin-hadoop26-scala_2.11.tgz
fi
tar xvfz flink-1.1.3-bin-hadoop26-scala_2.11.tgz -C /opt/flink --strip-components=1
#change the default web UI port
sed -i -e 's/jobmanager.web.port: 8081/jobmanager.web.port: 28081/g' /opt/flink/conf/flink-conf.yaml
sed -i -e 's/^jobmanager.rpc.address:.*$/jobmanager.rpc.address: 172.17.0.1/g' /opt/flink/conf/flink-conf.yaml
chown -R ${USER_NAME}:${GROUP_NAME} /opt/flink
systemctl enable flink
