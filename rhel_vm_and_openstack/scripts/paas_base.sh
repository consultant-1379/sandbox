#
# This script installs base components.
#

# Currently, the following is installed:
#   JAVA 8
#   Zookeeper (needed for Mesos)
#   Mesos
#   Marathon


. /tmp/installer/functions.sh

cecho "\n*** Installing PaaS Components ***\n" $GREEN

## Install Zookeeper.
cecho "Installing Zookeeper..." $GREEN
yum -y install zookeeper zookeeper-server

cecho "Zookeeper initialization..." $GREEN
service zookeeper-server init
service zookeeper-server start
systemctl enable zookeeper-server

cecho "Removing running level from the zookeeper-server service..." $BLUE
/sbin/chkconfig --level 2 zookeeper-server off

sleep 10

cecho "Installing Mesos..." $GREEN
yum -y install mesos-0.28.2-2.0.27.centos701406 marathon-1.1.0-1.0.471.el7.x86_64

cecho "Configuring Mesos slave"
echo "docker,mesos" > /etc/mesos-slave/containerizers
echo "ports(*):[31000-32000]" > /etc/mesos-slave/resources

cecho "Configuring Mesos master"
echo "AIA Sandbox Mesos Cluster" > /etc/mesos-master/cluster

cecho "Starting Mesos master and slave" $GREEN
systemctl enable mesos-master.service
systemctl enable mesos-slave.service

cecho "Starting Marathon" $GREEN
systemctl enable marathon.service
