. /tmp/helper_function.sh

dd if=/dev/zero of=/root/storagefile bs=1G count=20
chown cloud-user:cloud-user /root/storagefile
chmod 777 /root/storagefile

yum -y install glibc hdparm initscripts iputils irqbalance libgcc libstdc++ nss redhat-lsb-core rpm-libs sdparam shadow-utils syslinux unzip zip

echo "Installing MapR..."
yum -y install mapr-core mapr-core-internal mapr-fileserver mapr-cldb mapr-zookeeper mapr-nfs mapr-webserver mapr-gateway mapr-resourcemanager mapr-nodemanager mapr-historyserver mapr-ericsson mapr-webserver

echo "Checking installed service..."
ls -l /opt/mapr/roles/

echo "installing Hive..."
yum -y install mapr-hive mapr-hiveserver2 mapr-hivemetastore mapr-hivewebhcat

echo "Installing spark..."
yum -y install mapr-spark mapr-spark-historyserver

echo "Installing drill..."
yum -y install mapr-drill

