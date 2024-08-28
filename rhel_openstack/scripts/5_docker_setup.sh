#!/bin/bash
. /tmp/helper_function.sh

echo "*** Setting up AIA docker containers ***"

tar cfzT docker.tar.gz /dev/null

cp docker.tar.gz /root/docker.tar.gz
