#!/bin/bash
local_host="`hostname --fqdn`"

local_ip=`ip addr | grep 'state UP' -A2 | tail -n1 | awk '{print $2}' | cut -f1 -d '/'`

sed "s/127.0.0.1/$local_ip/g"  docker-compose-template.yml > docker-compose.yml

docker-compose up -d
