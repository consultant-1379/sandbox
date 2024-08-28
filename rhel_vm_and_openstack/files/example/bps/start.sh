#!/bin/bash

#re-deploy the application
cp /home/cloud-user/example/bps/flow.xml /aia
cp /home/cloud-user/example/bps/bank.csv /aia
cd /home/cloud-user/example/bps
bin/undeploy_app.sh http://localhost:8888
bin/deploy_app.sh http://localhost:8888
