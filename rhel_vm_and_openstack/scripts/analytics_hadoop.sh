#
# This script installs hadoop
#

. /tmp/installer/functions.sh

cecho "\n*** Installing hadoop Components ***\n" $GREEN

cecho "Installing hadoop-conf-pseudo..." $GREEN
yum -y install hadoop-conf-pseudo
rpm -ql hadoop-conf-pseudo

cecho "Formatting the NameNode..." $GREEN
sudo -u hdfs hdfs namenode -format

sed -i -e "s/hdfs:\/\/localhost:8020/hdfs:\/\/0.0.0.0:8020/g" /etc/hadoop/conf/core-site.xml

cecho "starting the HDFS..." $GREEN
for x in `cd /etc/init.d ; ls hadoop-hdfs-*` ; do sudo service $x start ; done

cecho "Checking the HDFS service status..." $BLUE
for x in `cd /etc/init.d ; ls hadoop-hdfs-*` ; do sudo service $x status ; done

cecho "Autostarting the HDFS service status..." $BLUE
for x in `cd /etc/init.d ; ls hadoop-hdfs-*` ; do /sbin/chkconfig --add $x ; done

cecho "Removing running level from the HDFS service status..." $BLUE
for x in `cd /etc/init.d ; ls hadoop-hdfs-*` ; do /sbin/chkconfig --level 2 $x off ; done

cecho "Creating the directories needed for hadoop processes..." $GREEN
sudo /usr/lib/hadoop/libexec/init-hdfs.sh

cecho "Verifying the HDFS File structure..." $GREEN
sudo -u hdfs hadoop fs -ls -R /

cecho "starting the YARN..." $GREEN
sudo service hadoop-yarn-resourcemanager start
sudo service hadoop-yarn-nodemanager start

cecho "Configuring the auto-start of YARN..." $BLUE
/sbin/chkconfig --add hadoop-yarn-resourcemanager
/sbin/chkconfig --add hadoop-yarn-nodemanager

cecho "Removing running level from the yarn service..." $BLUE
for x in `cd /etc/init.d ; ls hadoop-yarn-*` ; do /sbin/chkconfig --level 2 $x off ; done

cecho "Creating user directories for vagrant..." $GREEN
sudo -u hdfs hadoop fs -mkdir /user/${USER_NAME}
sudo -u hdfs hadoop fs -chown vagrant /user/${USER_NAME}

cecho "Installing Apache Spark" $GREEN
yum -y install spark-core spark-master spark-worker spark-python

sed -i "s/.*export STANDALONE_SPARK_MASTER_HOST.*/export STANDALONE_SPARK_MASTER_HOST=0.0.0.0/" /etc/spark/conf/spark-env.sh
echo "export SPARK_LOCAL_IP=0.0.0.0" >> /etc/spark/conf/spark-env.sh 
echo "export SPARK_MASTER_IP=0.0.0.0" >> /etc/spark/conf/spark-env.sh

cp /etc/spark/conf/slaves.template /etc/spark/conf/slaves

echo "Installing separate spark for zeppelin..."
if [[ -z "${SPARK_VERSION}" ]];then
   echo "spark version envrionment variable is empty, set a default version"
   SPARK_VERSION="2.0.2"
fi

cd /tmp/installer
wget http://ftp.heanet.ie/mirrors/www.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop2.6.tgz

mkdir /opt/spark-${SPARK_VERSION}
tar xvfz spark-${SPARK_VERSION}-bin-hadoop2.6.tgz -C /opt/spark-${SPARK_VERSION} --strip-components=1

# Install Zeppelin.
cecho "Installing Apache Zeppelin" $GREEN

if [[ -z "${ZEPPELIN_VERSION}" ]];then
   echo "zeppelin version envrionment variable is empty, set a default version"
   ZEPPELIN_VERSION="0.6.2"
fi

cd /tmp/installer
mkdir -p /opt/zeppelin
if [ ! -f zeppelin-${ZEPPELIN_VERSION}-bin-all.tgz ];then
   wget http://ftp.heanet.ie/mirrors/www.apache.org/dist/zeppelin/zeppelin-${ZEPPELIN_VERSION}/zeppelin-${ZEPPELIN_VERSION}-bin-all.tgz
fi
tar xvfz zeppelin-${ZEPPELIN_VERSION}-bin-all.tgz -C /opt/zeppelin --strip-components=1
mv /opt/zeppelin/conf/zeppelin-site.xml.template /opt/zeppelin/conf/zeppelin-site.xml
mv /opt/zeppelin/conf/zeppelin-env.sh.template /opt/zeppelin/conf/zeppelin-env.sh

# Change the Zeppelin port.
sed -i "s/8080/9090/" /opt/zeppelin/conf/zeppelin-site.xml

# copy jar file for interpreter
cp /usr/lib/hive/lib/hive-jdbc-standalone.jar /opt/zeppelin/interpreter/jdbc
cp /usr/lib/hadoop/hadoop-common.jar /opt/zeppelin/interpreter/jdbc

# set spark in zeppelin-env.sh
cat <<EOF >>/opt/zeppelin/conf/zeppelin-env.sh
export SPARK_HOME=/opt/spark-${SPARK_VERSION}
export MASTER=local[*]
export HADOOP_CONF_DIR=/usr/lib/hadoop
export SPARK_SUBMIT_OPTIONS="--driver-memory 512M --executor-memory 1G"
export ZEPPELIN_JAVA_OPTS=""

export DEFAULT_HADOOP_HOME=/usr/lib/hadoop
export HADOOP_HOME=${HADOOP_HOME:-$DEFAULT_HADOOP_HOME}

if [ -n "$HADOOP_HOME" ]; then
  export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${HADOOP_HOME}/lib/native
fi

export HADOOP_CONF_DIR=${HADOOP_CONF_DIR:-/etc/hadoop/conf}
export PYSPARK_PYTHON=/opt/anaconda2/bin/python
EOF

systemctl enable zeppelin
systemctl start zeppelin

sed -i -e "s/\"zeppelin.python\": \"python\"/\"zeppelin.python\": \"\/usr\/bin\/python3\"/g" /opt/zeppelin/conf/interpreter.json
systemctl stop zeppelin
systemctl start zeppelin

# Install & configure Alluxio.
cecho "Installing Alluxio" $GREEN
cd /tmp/installer
mkdir -p /opt/alluxio
if [ ! -f alluxio-1.3.0-hadoop2.7-bin.tar.gz ];then
   wget  http://alluxio.org/downloads/files/1.3.0/alluxio-1.3.0-hadoop2.7-bin.tar.gz
fi
tar xvfz alluxio-1.3.0-hadoop2.7-bin.tar.gz -C /opt/alluxio --strip-components=1

cp /opt/alluxio/conf/alluxio-env.sh.template /opt/alluxio/conf/alluxio-env.sh
chmod +x /opt/alluxio/bin/alluxio-start.sh /opt/alluxio/bin/alluxio-stop.sh /opt/alluxio/bin/alluxio /opt/alluxio/bin/alluxio-workers.sh /opt/alluxio/bin/alluxio-mount.sh
sed -ie 's/^export ALLUXIO_UNDERFS_ADDRESS.*/export ALLUXIO_UNDERFS_ADDRESS=hdfs:\/\/localhost:8020/g' /opt/alluxio/conf/alluxio-env.sh
cp -f /etc/hadoop/conf/core-site.xml /opt/alluxio/conf/
chown -R ${USER_NAME}:${GROUP_NAME} /opt/alluxio
chmod 777 -R /opt/alluxio/logs

echo "Configuring Hadoop for Cloudera Hue..."
echo "Configuring hdfs-site"
sed -i 's/<\/configuration>/  <property>\n    <name>dfs.webhdfs.enabled<\/name>\n    <value>true<\/value>\n  <\/property>\n<\/configuration>/g' /etc/hadoop/conf/hdfs-site.xml

echo "Configuring core-site"
sed -i 's/<\/configuration>/  <!-- Hue WebHDFS proxy user setting -->\n  <property>\n    <name>hadoop.proxyuser.hue.hosts<\/name>\n    <value>*<\/value>\n  <\/property>\n  <property>\n    <name>hadoop.proxyuser.hue.groups<\/name>\n    <value>*<\/value>\n  <\/property>\n<\/configuration>/g' /etc/hadoop/conf/core-site.xml
sed -i 's/<\/configuration>/  <property>\n    <name>hadoop.proxyuser.httpfs.hosts<\/name>\n    <value>*<\/value>\n  <\/property>\n  <property>\n    <name>hadoop.proxyuser.httpfs.groups<\/name>\n    <value>*<\/value>\n  <\/property>\n<\/configuration>/g' /etc/hadoop/conf/core-site.xml

cecho "Configuring Alluxio" $GREEN
/opt/alluxio/bin/alluxio format
systemctl enable alluxio

cecho "Adding vagrant to hive group..." $GREEN
usermod -a -G hive ${USER_NAME}

cecho "Installing postgresql jdbc..." $GREEN
yum install -y postgresql-jdbc

ln -s /usr/share/java/postgresql-jdbc.jar /usr/lib/hive/lib/postgresql-jdbc.jar

sudo -u postgres psql -f /tmp/installer/hive_metastore.sql
sudo -u postgres psql -f /tmp/installer/hive_metastore_permission.sql

cecho "Copying the hive-site.xml..." $GREEN
cp /tmp/installer/hive-site.xml /usr/lib/hive/conf/hive-site.xml
cp /tmp/installer/hive-site.xml /etc/hive/conf/hive-site.xml

cecho "Installing hive metastore..." $GREEN
yum install -y hive-metastore hive-server2

cecho "Restarting hive metastore..." $BLUE
service hive-metastore restart

cecho "Auto-starting hive metastore..." $BLUE
/sbin/chkconfig --add hive-metastore

cecho "Configuring spark..." $BLUE
cp /etc/hive/conf/hive-site.xml /etc/spark/conf/
cp /etc/hadoop/conf/hdfs-site.xml /etc/spark/conf/
cp /etc/hadoop/conf/core-site.xml /etc/spark/conf/

cecho "Configuring spark with alluxio..." $BLUE
cd /tmp/installer
if [ ! -f alluxio-1.3.0-spark-client-jar-with-dependencies.jar ];then
    wget http://alluxio.org/downloads/files/1.3.0/alluxio-1.3.0-spark-client-jar-with-dependencies.jar
fi
cp alluxio-1.3.0-spark-client-jar-with-dependencies.jar /usr/lib/spark/lib
echo "Removing the following line as it causes problem in zeppelin"
#echo 'export SPARK_CLASSPATH=/usr/lib/spark/lib/alluxio-1.3.0-spark-client-jar-with-dependencies.jar:$SPARK_CLASSPATH' >> /usr/lib/spark/conf/spark-env.sh

cecho "AUto starting spark master and worker..."
systemctl enable spark-master
systemctl enable spark-worker

echo "Configuring separate spark install..."
cp /etc/hive/conf/hive-site.xml /opt/spark-${SPARK_VERSION}/conf
cp /etc/hadoop/conf/hdfs-site.xml /opt/spark-${SPARK_VERSION}/conf
cp /etc/hadoop/conf/core-site.xml /opt/spark-${SPARK_VERSION}/conf


cecho "Removing running level from the spark service..." $BLUE
for x in `cd /etc/init.d ; ls spark-*` ; do /sbin/chkconfig --level 2 $x off ; done

echo "Install Cloudera Hue..."
yum -y install hue

echo "Fix for the problem with rc systemd"
/sbin/chkconfig hue off
systemctl stop hue
systemctl enable hue2

echo "Configuring Hue settings..."
sed -i '/webhdfs_url=/s/.*/      webhdfs_url=http:\/\/localhost:50070\/webhdfs\/v1\//' /etc/hue/conf/hue.ini
sed -i '/http_port=/s/.*/  http_port=5566/' /etc/hue/conf/hue.ini
sed -i '/fs_defaultfs=/s/.*/      fs_defaultfs=hdfs:\/\/localhost:8020/' /etc/hue/conf/hue.ini

