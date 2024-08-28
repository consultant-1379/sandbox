#!/bin/bash

. /tmp/helper_function.sh

echo "proxy required: ${PROXY_REQUIRED}"

if [[ "${PROXY_REQUIRED}" == "true" ]]; then
    find /etc/yum.repos.d/ -type f -name "enm*" -exec sh -c 'echo "proxy = _none_" >> {}' \;
fi

MAVEN_VERSION="3.3.9"

wget http://ftp.heanet.ie/mirrors/www.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz

tar xvfz apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt

echo "Configuring maven environment..." 
echo "M2_HOME=\"/opt/apache-maven-${MAVEN_VERSION}\"" >> /etc/profile
echo "PATH=\"\${M2_HOME}/bin:\${PATH}\"" >> /etc/profile
echo "export M2_HOME PATH" >> /etc/profile

mkdir -p /home/cloud-user/.m2
chown -R cloud-user:cloud-user /home/cloud-user/.m2
cat > /home/cloud-user/.m2/settings.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <mirrors>
    <mirror>
      <id>nexus</id>
      <mirrorOf>central</mirrorOf>
      <url>https://arm101-eiffel004.lmera.ericsson.se:8443/nexus/content/groups/public</url>
    </mirror>
    <mirror>
      <id>aia</id>
      <mirrorOf>aia</mirrorOf>
      <url>https://arm101-eiffel004.lmera.ericsson.se:8443/nexus/content/groups/aia-repositories</url>
    </mirror>
  </mirrors>
  <profiles>
    <profile>
      <id>nexus</id>
      <repositories>
        <repository>
          <id>central</id>
          <url>http://central</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
          </snapshots>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <id>central</id>
          <url>http://central</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </pluginRepository>
      </pluginRepositories>
    </profile>
    <profile>
      <id>aia</id>
      <repositories>
        <repository>
          <id>aia</id>
          <url>http://aia</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
          </snapshots>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <id>aia</id>
          <url>http://aia</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </pluginRepository>
      </pluginRepositories>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>nexus</activeProfile>
    <activeProfile>aia</activeProfile>
  </activeProfiles>

  <pluginGroups>
    <pluginGroup>org.sonatype.plugins</pluginGroup>
  </pluginGroups>

</settings>
</profiles>
<activeProfiles>
  <activeProfile>nexus</activeProfile>
</activeProfiles>

<pluginGroups>
  <pluginGroup>org.sonatype.plugins</pluginGroup>
</pluginGroups>

</settings>
EOF
chown -R cloud-user:cloud-user /home/cloud-user/.m2/settings.xml

echo "Installing git..." 
yum install -y git

echo "Configuring git..." 
cat > /home/cloud-user/.gitconfig <<EOF
[user]
        name = cloud-user
[core]
        autocrlf = input
        excludesfile = /home/cloud-user/.gitignore
[rerere]
        enabled = 1
[color]
        ui = true
[gui]
        encoding = utf-8
[push]
        default = tracking
EOF

chown -R cloud-user:cloud-user /home/cloud-user/.gitconfig
cat > /home/cloud-user/.gitignore <<EOF
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

chown -R cloud-user:cloud-user /home/cloud-user/.gitignore

echo "Installing postgresql server..." 
yum install -y postgresql-server

echo "Configuring postgresql server..." 
postgresql-setup initdb

echo "Postgresql configuration value..." 
sudo cat /var/lib/pgsql/data/postgresql.conf  | grep -e listen -e standard_conforming_strings

echo "listen_addresses='*'" >> /var/lib/pgsql/data/postgresql.conf
echo 'standard_conforming_strings=off' >> /var/lib/pgsql/data/postgresql.conf

sed -i -e 's/^host\(.*\)ident/host\1trust/g' /var/lib/pgsql/data/pg_hba.conf
sed -i -e 's/^local\(.*\)peer/local\1trust/g' /var/lib/pgsql/data/pg_hba.conf
echo host    all             all             0.0.0.0/0            trust >> /var/lib/pgsql/data/pg_hba.conf

echo "Starting postgresql database..." 
systemctl restart postgresql

echo "Auto-starting postgresql database..." 
systemctl enable postgresql

# Apache httpd.
echo "Installing httpd..." 
sudo yum install -y httpd

cat >> /etc/httpd/conf/httpd.conf <<EOF
Alias "/yum/base" "/aia/yum/base"
<Directory "/aia/yum/base">
    AllowOverride None
    Options None
    Require all granted
</Directory>
EOF

setenforce 0

echo "Enabling httpd service..." 
systemctl enable httpd
systemctl start httpd

