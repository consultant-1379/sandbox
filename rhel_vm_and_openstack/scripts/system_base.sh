#
# This script installs system base components, like repos,java
#

. /tmp/installer/functions.sh

echo "127.0.1.1	$(hostname)">>/etc/hosts

cecho "\n*** Installing system base Components ***\n" $GREEN


#cloudera  repo.
cecho "Setting up Cloudera CDH repository..." $GREEN
rpm -Uvh http://archive.cloudera.com/cdh5/one-click-install/redhat/7/x86_64/cloudera-cdh-5-0.x86_64.rpm

if [[ ! -z "${CLOUDERA_VERSION}" ]];then
   echo "Cloudera version is not empty, use ${CLOUDERA_VERSION}"
   sed -i -e "s/cdh\/5/cdh\/${CLOUDERA_VERSION}/g" /etc/yum.repos.d/cloudera-cdh5.repo
fi

# Install Java 8 (required by Marathon)
# Doing this AFTER the above steps because one of them pulls in JDK 7!
cecho "Downloading and installing JAVA 8..." $GREEN

cd /tmp/installer
if [ ! -f "jdk-8u66-linux-x64.rpm" ];then
# Using a slightly older version.
wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u66-b17/jdk-8u66-linux-x64.rpm"
fi
rpm -Uvh jdk-8u66-linux-x64.rpm

# Installing Confluent platform.
cecho "Installing Confluent Platform repo..." $GREEN
rpm --import http://packages.confluent.io/rpm/2.0/archive.key

# Mesos repo from Mesosphere.
cecho "Installing mesosphere repo..." $GREEN
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

# Move service unit files.
mv /tmp/installer/*.service /etc/systemd/system

echo "net.ipv6.conf.all.disable_ipv6 = 1" >> /etc/sysctl.conf 

# install scala
mkdir /opt/scala
wget http://downloads.lightbend.com/scala/2.11.8/scala-2.11.8.tgz
tar xvfz scala-2.11.8.tgz -C /opt/scala --strip-components=1
echo "SCALA_HOME=/opt/scala" >> /etc/profile
echo "PATH=\"\${PATH}:\${SCALA_HOME}/bin\"" >> /etc/profile
echo "export SCALA_HOME PATH" >> /etc/profile

# install tools
yum -y install unzip

wget https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm
rpm -ivh epel-release-latest-7.noarch.rpm

sed -i -e 's/gpgcheck=1/gpgcheck=0/g' /etc/yum.repos.d/epel.repo

cat > /etc/yum.repos.d/centos-base.repo<<EOF
[base]
name=CentOS-$releasever – Base
baseurl=http://buildlogs.centos.org/centos/7/os/x86_64-20140704-1/
gpgcheck=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
priority=1
exclude=php mysql
EOF

yum -y install libpng-devel libjpeg-devel libcurl libcurl-devel libxml2 libxml2-devel librsvg2-devel cairo-devel libwebp-devel poppler-cpp-devel ImageMagick-c++-devel


cd /tmp/installer
if [ ! -f setup-network-environment ]; then
    wget https://github.com/kelseyhightower/setup-network-environment/releases/download/v1.0.0/setup-network-environment
fi
cp setup-network-environment /usr/sbin/
chmod +x /usr/sbin/setup-network-environment

systemctl enable network-environment
systemctl start network-environment

if [[ "${PACKER_BUILDER_TYPE}" == "openstack" ]]; then
echo "Removing the redhat subscription from cloud-init..."
sed -i -e '/^.*rh_subscription$/d' /etc/cloud/cloud.cfg

echo "Enabling updating /etc/hosts from cloud-init..."
echo "manage_etc_hosts: True" >> /etc/cloud/cloud.cfg
fi
