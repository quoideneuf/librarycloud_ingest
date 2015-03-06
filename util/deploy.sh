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
if [ $? -eq 0 ]
  echo "ERROR: Could not stop librarycloud service before deployment"
  exit 1
fi

ssh $TARGET_SERVER cd $TARGET_DIRECTORY
if [ $? -eq 0 ]
  echo "ERROR: Librarycloud directory ($TARGET_DIRECTORY) does not exist or is not accessible"
  exit 1
fi

ssh $TARGET_SERVER git status >/dev/null 2>&1
if [ $? -eq 0 ]
  echo "ERROR: Librarycloud directory ($TARGET_DIRECTORY) is not a git repository"
  exit 1
fi

ssh $TARGET_SERVER sudo git fetch && sudo git checkout $RELEASE_TAG
if [ $? -eq 0 ]
  echo "ERROR: Could not checkout new release from git"
  exit 1
fi

ssh $TARGET_SERVER sudo service librarycloud start
if [ $? -eq 0 ]
  echo "WARNING: Could not restart librarycloud service"
  exit 1
fi


# if [ ! -f $SOURCE_FILE_PATH ]; then
#   echo "Data file does not exist"
#   exit 1
# fi

# # Copy data file to target, creating bucket if it doesn't already exist
# if ! aws s3 cp $SOURCE_FILE_PATH s3://$TARGET_BUCKET/$TARGET_FILE_NAME; then
#     echo "Creating bucket $TARGET_BUCKET"
#     aws s3 mb s3://$TARGET_BUCKET
#     aws s3api put-bucket-lifecycle --bucket $TARGET_BUCKET --lifecycle-configuration '{"Rules":[{"Status":"Enabled","Prefix":"","Expiration":{"Days":30},"ID":"Delete old items"}]}'
#     if ! aws s3 cp $SOURCE_FILE_PATH s3://$TARGET_BUCKET/$TARGET_FILE_NAME; then
#         echo "Error uploading file"
#         exit 1
#     fi
# fi

# # Create download URL
# SOURCE_FILE_URL=`sign_s3_url.bash --bucket $TARGET_BUCKET --file-path $TARGET_FILE_NAME --minute-expire 1440`

# # Need to escape ampersands in the replacement string, or sed will do odd stuff
# SED_FILE_URL=`echo $SOURCE_FILE_URL | sed 's|&|\\\&amp;|g'`

# # Create ingest command
# (sed -e "s|TARGET|$SED_FILE_URL|" | sed -e "s|SOURCE|$DATA_SOURCE_NAME|") > $SOURCE_FILE_NAME.command.xml <<EOF
# <?xml version="1.0" encoding="UTF-8"?>
# <lib_comm_message>
#     <command>INGEST</command>
#     <payload>
#         <source>SOURCE</source>
#         <format>UNUSED_FORMAT</format>
#         <filepath>TARGET</filepath>
#     </payload>
# </lib_comm_message>
# EOF
 
# if [ ! -f "$SOURCE_FILE_NAME.command.xml" ]; then
#   echo "Could not create command file"
#   exit 1
# fi

# # Copy ingest command to target queue
# aws sqs create-queue --queue-name=$SQS_ENVIRONMENT-$INGEST_INSTRUCTION-$DATA_SOURCE_NAME
# aws sqs send-message --queue=http://sqs.us-east-1.amazonaws.com/$SQS_ENVIRONMENT-$INGEST_INSTRUCTION-$DATA_SOURCE_NAME --message-body="$(<$SOURCE_FILE_NAME.command.xml)"

# rm $SOURCE_FILE_NAME.command.xml

