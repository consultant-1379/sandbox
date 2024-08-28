#!/bin/bash

echo "Shutdowning all the service..."

service_list=("hadoop-hdfs-datanode" "hadoop-hdfs-namenode" "hadoop-hdfs-secondarynamenode" "hadoop-mapreduce-historyserver" "hadoop-yarn-nodemanager" "hadoop-yarn-resourcemanager" "hive-metastore" "hive-server2" "schema-registry" "kafka-server" "zeppelin" "alluxio" "spark-master" "spark-worker" "mesos-master" "mesos-slave" "marathon" "docker" "zookeeper-server" "docker-registry" "flink" "hue2" "jupyter2" "jupyter3")

for srv in "${service_list[@]}"
do
   systemctl stop $srv
done

if [[ "${PACKER_BUILDER_TYPE}" == "openstack" ]];then

service_list=("hadoop-hdfs-datanode" "hadoop-hdfs-namenode" "hadoop-hdfs-secondarynamenode" "hadoop-mapreduce-historyserver" "hadoop-yarn-nodemanager" "hadoop-yarn-resourcemanager" "hive-metastore" "hive-server2" "schema-registry" "kafka-server" "zeppelin" "alluxio" "spark-master" "spark-worker" "mesos-master" "mesos-slave" "marathon" "zookeeper-server" "flink" "hue2")

for srv in "${service_list[@]}"
do
   systemctl disable $srv
done

fi

service zookeeper-server init

#echo "Dirty fix to solve the service dependency problem..."
#systemctl_service_list=("schema-registry" "kafka-server" "zeppelin" "alluxio" "mesos-master" "mesos-slave" "marathon" "docker" "docker-registry")
#
#if [[ "${PACKER_BUILDER_TYPE}" == "openstack" ]];then
#for srv in "${systemctl_service_list[@]}"
#do
#   unitfile=$(systemctl cat $srv|head -n1|cut -d' ' -f2)
#   if grep -q '^After' $unitfile;then
#       sed -i '/^After/ s/$/ cloud-init.service/' $unitfile
#   else
#       sed -i '/\[Unit\]/a After\=cloud-init.service' $unitfile
#   fi
#done
#
#
#lsb_service_list=("hadoop-hdfs-datanode" "hadoop-hdfs-namenode" "hadoop-hdfs-secondarynamenode" "hadoop-mapreduce-historyserver" "hadoop-yarn-nodemanager" "hadoop-yarn-resourcemanager" "hive-metastore" "hive-server2" "spark-master" "spark-worker" "zookeeper-server")
#
#if [[ "${PACKER_BUILDER_TYPE}" == "openstack" ]];then
#for srv in "${lsb_service_list[@]}"
#do
#   unitfile=$(systemctl cat $srv|head -n1|cut -d' ' -f2)
#   if grep -q '^After' $unitfile;then
#       sed -i '/^After/ s/$/ cloud-init.service/' $unitfile
#   else
#       sed -i '/\[Unit\]/a After\=cloud-init.service' $unitfile
#   fi
#done
#fi

if [[ ! "${PACKER_BUILDER_TYPE}" == "openstack" ]];then

# Clean up and zero swap partition.
readonly swapuuid=$(/sbin/blkid -o value -l -s UUID -t TYPE=swap)
readonly swappart=$(readlink -f /dev/disk/by-uuid/"$swapuuid")
/sbin/swapoff "$swappart"
dd if=/dev/zero of="$swappart" bs=1M || echo "dd exit code $? is suppressed"
/sbin/mkswap -U "$swapuuid" "$swappart"

if [ ! "${PACKER_BUILDER_TYPE}" == "qemu" ]; then
  dd if=/dev/zero of=/EMPTY bs=1M
  rm -f /EMPTY
fi

sync

# Clean and zero disk and boot partition.
/bin/dd if=/dev/zero of=/boot/EMPTY bs=1M
/bin/rm -f /boot/EMPTY
/bin/dd if=/dev/zero of=/EMPTY bs=1M
/bin/rm -f /EMPTY
sync

sed -i /HWADDR/d /etc/sysconfig/network-scripts/ifcfg-enp0s3

fi

rm -rf /var/lib/mesos/meta/slaves/latest/

yum clean all
rm -rf /var/cache/yum
rm -r ${HOME_DIRECTORY}/shutdown.sh
rm -r ${HOME_DIRECTORY}/*rpm
rm -r ${HOME_DIRECTORY}/*gz
rm -r ${HOME_DIRECTORY}/*tgz

rm -rf /tmp/installer

if [[ "${PACKER_BUILDER_TYPE}" == "openstack" ]];then
    sed -i "/^127.0.1.1.*/d" /etc/hosts
    sed -i "/^127.0.0.1.*/d" /etc/hosts
fi
