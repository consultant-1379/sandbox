#!/bin/bash

. /tmp/helper_function.sh

if [[ "${PROXY_REQUIRED}" == "true" ]]; then
    find /etc/yum.repos.d/ -type f -name "enm*" -exec sh -c 'echo "proxy = _none_" >> {}' \;
fi

# Add Ericsson YUM repository & update packages.
cat > /etc/yum.repos.d/ericsson.repo <<EOF
[rhel-x86_64-server-7]
name=rhel-x86_64-server-7
baseurl=http://yum.linux.ericsson.se/repos/rhel-x86_64-server-7
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-redhat-release
proxy=_none_
EOF

yum clean all

# Install MC.
yum -y install mc

# Install wget.
yum -y install wget

# Make ssh faster by not waiting on DNS.
echo "UseDNS no" >> /etc/ssh/sshd_config

# NTP for useful times in logs.
yum -y install ntp ntpdate ntp-doc
systemctl enable ntpd.service

cat > /etc/ntp.conf << EOM
server ntp1.ericsson.se
server ntp2.ericsson.se
EOM

ntpdate ntp1.ericsson.se

systemctl start ntpd.service

