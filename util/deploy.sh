#!/bin/bash

# Script to deploy new release of the ingest system to a target server
# 
# Preconditions:
# - A tagged release exists on the librarycloud_ingest Github repository
# - AWS credentials have been loaded
# - If necessary, default username and ssh keys have been configured for each host (in ~/.ssh/config) 
# 
# Usage:    deploy.sh [RELEASE_TAG] [SERVER]
# Example:  deploy.sh v1.0.0 server.example.com

RELEASE_TAG=$1
TARGET_SERVER=$2
TARGET_DIRECTORY=/usr/local/librarycloud

if [ $# -ne 2 ]; then
    echo "Usage: deploy.sh [RELEASE_TAG] [SERVER] "
    exit 1
fi

# If the target server is running RHEL, this may fail if 'Defaults requiretty' is set
# in the /etc/sudoers file. See https://bugzilla.redhat.com/show_bug.cgi?id=1020147
ssh $TARGET_SERVER sudo service librarycloud stop
if [ $? -ne 0 ]; then
  echo "WARNING: Could not stop librarycloud service before deployment. It may not have been running."
fi

ssh $TARGET_SERVER git -C $TARGET_DIRECTORY status >/dev/null 2>&1
if [ $? -ne 0 ]; then
  echo "ERROR: Librarycloud directory ($TARGET_DIRECTORY) is not a git repository"
  exit 1
fi

ssh $TARGET_SERVER "sudo git -C $TARGET_DIRECTORY fetch && sudo git -C $TARGET_DIRECTORY checkout $RELEASE_TAG --quiet"
if [ $? -ne 0 ]; then
  echo "ERROR: Could not checkout new release from git"
  exit 1
fi

ssh $TARGET_SERVER sudo service librarycloud start
if [ $? -ne 0 ]; then
  echo "WARNING: Could not restart librarycloud service"
fi

echo "Deployment of release $RELEASE_TAG to $TARGET_SERVER complete"
