#!/bin/bash

. /tmp/helper_function.sh

echo "proxy required: ${PROXY_REQUIRED}"

if [[ "${PROXY_REQUIRED}" == "true" ]]; then
    echo proxy=http://www-proxy.ericsson.se:8080 >> /etc/yum.conf
fi

cat > /etc/yum.repos.d/docker.repo <<EOF
[dockerrepo]
name=Docker Repository
baseurl=https://yum.dockerproject.org/repo/main/centos/7
enabled=1
gpgcheck=1
gpgkey=https://yum.dockerproject.org/gpg
EOF

yum clean all
yum -y install docker-engine

# Some config settings...
groupadd docker
usermod -a -G docker cloud-user

cat >> /etc/security/limits.conf <<EOF
*        hard    nproc           16384
*        soft    nproc           16384
*        hard    nofile          16384
*        soft    nofile          16384
EOF

echo "Enabling Docker service..."
systemctl enable docker.service
systemctl start docker.service

