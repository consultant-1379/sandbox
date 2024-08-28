# Docker installation.

. /tmp/installer/functions.sh

if [[ "${PACKER_BUILDER_TYPE}" == "openstack" ]];then

cecho "\n *** Installing Docker ***\n" $GREEN

yum install -y yum-utils
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

yum -y makecache fast

#cat > /etc/yum.repos.d/docker.repo <<EOF
#[dockerrepo]
#name=Docker Repository
#baseurl=https://yum.dockerproject.org/repo/main/centos/7
#enabled=1
#gpgcheck=1
#gpgkey=https://yum.dockerproject.org/gpg
#EOF

yum -y install docker-ce

# Some config settings...
usermod -a -G docker ${USER_NAME}

cat >> /etc/security/limits.conf <<EOF
*        hard    nproc           16384
*        soft    nproc           16384
*        hard    nofile          16384
*        soft    nofile          16384
EOF

cecho "Enabling Docker service..." $GREEN
systemctl enable docker.service
systemctl start docker.service
fi
