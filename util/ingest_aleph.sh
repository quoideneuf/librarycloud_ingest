#!/bin/bash

# Script to kick of an Aleph ingest
# 
# Usage:    ingest-aleph.sh [DATA_FILE] [SQS_ENVIRONMENT]
# Example:  ingest-aleph.sh ab.bib.00.20140808.full.mrc test
# 
# TODO: Make more robust

SOURCE_FILE_PATH=$1
SOURCE_FILE_NAME=$(basename $SOURCE_FILE_PATH)
SQS_ENVIRONMENT=$2
TARGET_BUCKET=harvard.librarycloud.upload.$SQS_ENVIRONMENT.aleph
COMMAND_BUCKET=harvard.librarycloud.command.$SQS_ENVIRONMENT.aleph

if [ $# -ne 2 ]; then
    echo "Usage: ingest-aleph.sh [DATA_FILE] [SQS_ENVIRONMENT]"
    exit 1
fi

# Create buckets (it's not a problem if the bucket already exists)
aws s3 mb s3://$COMMAND_BUCKET
aws s3 mb s3://$TARGET_BUCKET

# Copy data file to target
aws s3 cp $SOURCE_FILE_PATH s3://$TARGET_BUCKET

# Create download URL
SOURCE_FILE_URL=`sign_s3_url.bash --bucket $TARGET_BUCKET --file-path $SOURCE_FILE_NAME`

# Need to escape ampersands in the replacement string, or sed will do odd stuff
SED_FILE_URL=`echo $SOURCE_FILE_URL | sed 's|&|\\\&|g'`

# Create ingest command
sed -e "s|TARGET|$SED_FILE_URL|" > $SOURCE_FILE_NAME.command.xml <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<lib_comm_message>
    <command>INGEST</command>
    <payload>
        <source>aleph</source>
        <format>marc21</format>
        <filepath>TARGET</filepath>
    </payload>
</lib_comm_message>
EOF
 
if [ ! -f "$SOURCE_FILE_NAME.command.xml" ]; then
  echo "Could not create command file"
  exit 1
fi

# Copy ingest command to target queue
aws sqs create-queue --queue-name=$SQS_ENVIRONMENT-aleph-command
aws sqs send-message --queue=http://sqs.us-east-1.amazonaws.com/$SQS_ENVIRONMENT-aleph-command --message-body="`cat $SOURCE_FILE_NAME.command.xml`"
