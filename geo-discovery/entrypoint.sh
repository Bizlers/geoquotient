#!/bin/sh

while ! nc -z $CONFIG_SERVER_HOST 8888 ; do
    echo "Waiting for upcoming Config Server"
    sleep 2
done

# Run the service
/opt/lib/geoq/geo-discovery-server/bin/geo-discovery