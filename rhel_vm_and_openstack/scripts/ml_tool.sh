#!/bin/bash

#Installing R
yum -y install R

# workaround as R will install openjdk
update-alternatives --set java /usr/java/jdk1.8.0_66/jre/bin/java
update-alternatives --set javac /usr/java/jdk1.8.0_66/bin/javac

echo "Installing knitr libary..."
#R -e "install.packages('knitr', dependencies = TRUE, repos = 'http://cran.us.r-project.org')"

#Install Rstudio server
cd /tmp/installer
if [ ! -f rstudio-server-rhel-1.0.136-x86_64.rpm ];then
   wget https://download2.rstudio.org/rstudio-server-rhel-1.0.136-x86_64.rpm
fi
yum install -y --nogpgcheck rstudio-server-rhel-1.0.136-x86_64.rpm

echo "Adding default user for rstudio server..."
useradd rstudio && echo rstudio:rstudio | chpasswd --crypt-method=SHA512

yum install -y python34
yum install -y python34-setuptools
easy_install-3.4 pip
pip3.4 install numpy
pip3.4 install matplotlib
yum install -y python34-tkinter
pip3.4 install scipy
pip3.4 install sklearn
pip3.4 install pandas
pip3.4 install jupyter


echo "Creating notebook folder for jupyter..."
sudo -u cloud-user mkdir /home/cloud-user/notebooks

echo "Installing Anaconda2 for python2 scripts..."
cd /tmp/installer
if [ ! -f Anaconda2-4.3.0-Linux-x86_64.sh ];then
    wget https://repo.continuum.io/archive/Anaconda2-4.3.0-Linux-x86_64.sh
fi
bash Anaconda2-4.3.0-Linux-x86_64.sh -b -p /opt/anaconda2

echo "installing tensorflow..."
pip3.4 install tensorflow
/opt/anaconda2/bin/pip install tensorflow

echo "Configuring pyspark for jupyter..."
mkdir -p /opt/anaconda2/share/jupyter/kernels/pyspark
mkdir -p /usr/share/jupyter/kernels/pyspark

cat<<EOF >> /usr/share/jupyter/kernels/pyspark/kernel.json
{
 "display_name": "PySpark 2.0.2",
 "language": "python",
 "argv": [
  "/usr/bin/python3",
  "-m",
  "IPython.kernel",
  "-f",
  "{connection_file}"
 ],
 "env": {
  "SPARK_HOME": "/opt/spark-2.0.2/",
  "PYTHONPATH": "/opt/spark-2.0.2/python/:/opt/spark-2.0.2/python/lib/py4j-0.10.3-src.zip",
  "PYTHONSTARTUP": "/opt/spark-2.0.2/python/pyspark/shell.py",
  "PYSPARK_SUBMIT_ARGS": "--master local[2] pyspark-shell"
 }
}
EOF

cat<<EOF >> /opt/anaconda2/share/jupyter/kernels/pyspark/kernel.json
{
 "display_name": "PySpark 2.0.2",
 "language": "python",
 "argv": [
  "/opt/anaconda2/bin/python",
  "-m",
  "IPython.kernel",
  "-f",
  "{connection_file}"
 ],
 "env": {
  "SPARK_HOME": "/opt/spark-2.0.2/",
  "PYTHONPATH": "/opt/spark-2.0.2/python/:/opt/spark-2.0.2/python/lib/py4j-0.10.3-src.zip",
  "PYTHONSTARTUP": "/opt/spark-2.0.2/python/pyspark/shell.py",
  "PYSPARK_SUBMIT_ARGS": "--master local[2] pyspark-shell"
 }
}
EOF

echo "Enabling systemd for jupyter..."
systemctl enable jupyter2
systemctl enable jupyter3

systemctl start jupyter2
systemctl start jupyter3
