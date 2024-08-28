. /tmp/helper_function.sh

yum install -y postgresql-jdbc
ln -s /usr/share/java/postgresql-jdbc.jar /opt/mapr/hive/hive-1.2/lib/

cat > /tmp/hive_metastore.sql <<EOF
CREATE USER hiveuser WITH PASSWORD 'hiveuser';
CREATE DATABASE metastore;
EOF

sudo -u postgres psql -f /tmp/hive_metastore.sql
rm -rf /tmp/hive_metastore.sql

# Install Zeppelin.
echo "Installing Apache Zeppelin..." 
mkdir -p /opt/zeppelin
wget http://ftp.heanet.ie/mirrors/www.apache.org/dist/zeppelin/zeppelin-0.6.1/zeppelin-0.6.1-bin-all.tgz
tar xvfz zeppelin-0.6.1-bin-all.tgz -C /opt/zeppelin --strip-components=1
mv /opt/zeppelin/conf/zeppelin-site.xml.template /opt/zeppelin/conf/zeppelin-site.xml
mv /opt/zeppelin/conf/zeppelin-env.sh.template /opt/zeppelin/conf/zeppelin-env.sh
# Change the Zeppelin port.
sed -i "s/8080/9090/" /opt/zeppelin/conf/zeppelin-site.xml

cat > /etc/systemd/system/zeppelin.service <<EOF
[Unit]
Description=Apache Zeppelin
After=spark-worker.service

[Service]
Type=forking
User=root
ExecStart=/opt/zeppelin/bin/zeppelin-daemon.sh start
ExecStop=/opt/zeppelin/bin/zeppelin-daemon.sh stop
Restart=on-abort


[Install]
WantedBy=multi-user.target
EOF

systemctl enable zeppelin

echo "Installing confluent platform..."
yum -y install confluent-platform-2.11.7

echo "Modifying kafka server to use new zookeeper port..."
sed -i -e 's/zookeeper.connect=localhost:2181/zookeeper.connect=localhost:5181/g' /etc/kafka/server.properties
sed -i -e 's/kafkastore.connection.url=localhost:2181/kafkastore.connection.url=localhost:5181/g' /etc/schema-registry/schema-registry.properties

echo "Starting the KAFKA and schema registry service..." 
systemctl enable kafka-server.service
systemctl enable schema-registry.service

systemctl start kafka-server.service
systemctl start schema-registry.service

su - cloud-user <<EOF
cd /home/cloud-user
git clone https://github.com/Alluxio/alluxio.git
cd alluxio/
git checkout v1.2.0
mvn clean package -Dhadoop.version=2.7.0-mapr-1602 -Pspark -DskipTests
EOF

master_name=$(get_master_name)

cp -R /home/cloud-user/alluxio /opt/mapr/
