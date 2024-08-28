#!/bin/bash

. /tmp/helper_function.sh

if [[ "${MAPR_REPO_SELF}" == "true" ]];then
rpm --import http://package.mapr.com/releases/pub/maprgpg.key

if [ ! -d /aia/yum/base ];then
    mkdir -p /aia/yum/base
fi

wget --no-cookies --no-check-certificate 'http://ericsson:eric$$on4mapr!@stage.mapr.com/ericsson/v5.2.0/redhat/mapr-v5.2.0ERICSSON.rpm.tgz'
wget --no-cookies --no-check-certificate 'http://ericsson:eric$$on4mapr!@stage.mapr.com/ericsson/MEP/MEP-1.0.0/redhat/mapr-mep-v1.0.0.201608161134.rpm.tgz'

tar xvfz mapr-v5.2.0ERICSSON.rpm.tgz -C /aia/yum/base
tar xvfz mapr-mep-v1.0.0.201608161134.rpm.tgz -C /aia/yum/base

yum install -y createrepo

setenforce 0

createrepo /aia/yum/base

cat > /etc/yum.repos.d/maprtech.repo <<EOF
[maprtech]
name=MapR Technologies, Inc.
baseurl=http://localhost/yum/base
enabled=1
gpgcheck=0
proxy=_none_
EOF

else
cat > /etc/yum.repos.d/maprtech.repo <<EOF
[maprtech]
name=MapR Technologies, Inc.
baseurl=http://${MAPR_REPO_HOST}/yum/base
enabled=1
gpgcheck=0
proxy=_none_
EOF
fi

yum clean all
