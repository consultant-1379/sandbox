# Base install.

. /tmp/installer/functions.sh

if [[ "${PACKER_BUILDER_TYPE}" == "openstack" ]]; then
cecho "\n *** Base installation ***\n" $GREEN

if [[ ! -z "${SANDBOX_VERSION}" ]];then
    echo ${SANDBOX_VERSION} > /etc/aia_version
fi

sed -i "s/^.*requiretty/#Defaults requiretty/" /etc/sudoers

# Add Ericsson YUM repository & update packages.
cat > /etc/yum.repos.d/ericsson.repo <<EOF
[rhel-x86_64-server-7]
name=rhel-x86_64-server-7
baseurl=http://yum.linux.ericsson.se/repos/rhel-x86_64-server-7
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-redhat-release
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

echo ${USER_NAME} | passwd ${USER_NAME} --stdin
fi
