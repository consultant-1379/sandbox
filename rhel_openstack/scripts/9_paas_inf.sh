. /tmp/helper_function.sh

yum -y install mesos-0.28.2-2.0.27.centos701406 marathon-1.1.0-1.0.471.el7.x86_64

echo "Modifying mesos configuration..."
sed -i -e "s/zk:\/\/localhost:2181\/mesos/zk:\/\/$(get_internal_address):5181\/mesos/g" /etc/mesos/zk

echo "Configuring Mesos slave"
echo "docker,mesos" > /etc/mesos-slave/containerizers
echo "ports(*):[31000-32000]" > /etc/mesos-slave/resources

echo "Configuring Mesos master"
echo "AIA Mesos Cluster" > /etc/mesos-master/cluster

