#
# This script installs development tool
#

. /tmp/installer/functions.sh

MAVEN_VERSION="3.3.9"

cecho "\n*** Installing Development Tools ***\n" $GREEN

cecho "Installing Maven..." $GREEN

cd /tmp/installer
mkdir -p /opt/maven
if [ ! -f apache-maven-${MAVEN_VERSION}-bin.tar.gz ];then
wget http://ftp.heanet.ie/mirrors/www.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz
fi
tar xvfz apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt/maven --strip-components=1

cecho "Configuring maven environment..." $GREEN
echo "M2_HOME=\"/opt/maven\"" >> /etc/profile
echo "PATH=\"\${M2_HOME}/bin:\${PATH}\"" >> /etc/profile
echo "export M2_HOME PATH" >> /etc/profile

mkdir -p ${HOME_DIRECTORY}/.m2
chown -R ${USER_NAME}:${GROUP_NAME} ${HOME_DIRECTORY}/.m2
cp /tmp/installer/settings.xml ${HOME_DIRECTORY}/.m2/
chown -R ${USER_NAME}:${GROUP_NAME} ${HOME_DIRECTORY}/.m2/settings.xml

echo "Installing git..." 
yum install -y git

echo "Configuring git..." 
cat > ${HOME_DIRECTORY}/.gitconfig <<EOF
[user]
        name = ${USER_NAME}
[core]
        autocrlf = input
        excludesfile = ${HOME_DIRECTORY}/.gitignore
[rerere]
        enabled = 1
[color]
        ui = true
[gui]
        encoding = utf-8
[push]
        default = tracking
EOF
chown -R ${USER_NAME}:${GROUP_NAME} ${HOME_DIRECTORY}/.gitconfig

cat > ${HOME_DIRECTORY}/.gitignore <<EOF
#intellij project file
*.iml
.idea/

#eclipse project file
.classpath
.project
.settings/


#log file
*.log

#ignore the maven target folder
target/

#ignore the gitingore per project
.gitignore
EOF

chown -R ${USER_NAME}:${GROUP_NAME} ${HOME_DIRECTORY}/.gitignore

cecho "Installing postgresql server..." $GREEN
yum install -y postgresql-server

cecho "Configuring postgresql server..." $BLUE
postgresql-setup initdb

cecho "Postgresql configuration value..." $BLUE
sudo cat /var/lib/pgsql/data/postgresql.conf  | grep -e listen -e standard_conforming_strings

echo "listen_addresses='*'" >> /var/lib/pgsql/data/postgresql.conf
echo 'standard_conforming_strings=off' >> /var/lib/pgsql/data/postgresql.conf
echo 'host    all             all             0.0.0.0/0                 trust' >> /var/lib/pgsql/data/pg_hba.conf

sed -i -e 's/^host\(.*\)ident/host\1trust/g' /var/lib/pgsql/data/pg_hba.conf
sed -i -e 's/^local\(.*\)peer/local\1trust/g' /var/lib/pgsql/data/pg_hba.conf


cecho "Starting postgresql database..." $GREEN
systemctl start postgresql

cecho "Auto-starting postgresql database..." $GREEN
systemctl enable postgresql

# Apache httpd.
cecho "Installing httpd..." $GREEN
sudo yum install -y httpd

cecho "Enabling httpd service..." $GREEN
systemctl enable httpd
systemctl start httpd

