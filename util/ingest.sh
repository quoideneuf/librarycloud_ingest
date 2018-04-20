#!/bin/bash

# Script to kick off an ingest
#
# Usage:    ingest.sh [INSTRUCTION] [SOURCE] [SQS_ENVIRONMENT] [DATA_FILE]
# Example:  ingest.sh ingest aleph test ab.bib.00.20140808.full.mrc
#
# Example (multiple files): find {DIRECTORY} | xargs -P 10 -L 1 ingest.sh oasis test
#
# TODO: Make more robust

INGEST_INSTRUCTION=$1
DATA_SOURCE_NAME=$2
SQS_ENVIRONMENT=$3
SOURCE_FILE_PATH=$4
SOURCE_FILE_NAME=$(basename $SOURCE_FILE_PATH)
TARGET_FILE_NAME=`echo $SOURCE_FILE_NAME | sed 's/#//g'`
TARGET_BUCKET=harvard.librarycloud.upload.$SQS_ENVIRONMENT.$DATA_SOURCE_NAME
COMMAND_BUCKET=harvard.librarycloud.command.$SQS_ENVIRONMENT.$DATA_SOURCE_NAME
SCRIPT_PATH=$(dirname "$0");

SQS_ENDPOINT=http://localhost:9324


if [ $# -ne 4 ]; then
    echo "Usage: ingest.sh [INSTRUCTION] [SOURCE] [SQS_ENVIRONMENT] [DATA_FILE] "
    exit 1
fi

if [ ! -f $SOURCE_FILE_PATH ]; then
  echo "Data file does not exist"
  exit 1
fi

# Copy data file to target, creating bucket if it doesn't already exist
if ! aws --endpoint-url http://localhost:4572 s3 cp $SOURCE_FILE_PATH s3://$TARGET_BUCKET/$TARGET_FILE_NAME; then
    echo "Creating bucket $TARGET_BUCKET"
    aws --endpoint-url http://localhost:4572 s3 mb s3://$TARGET_BUCKET
    aws --endpoint-url http://localhost:4572 s3api put-bucket-lifecycle --bucket $TARGET_BUCKET --lifecycle-configuration '{"Rules":[{"Status":"Enabled","Prefix":"","Expiration":{"Days":30},"ID":"Delete old items"}]}'
    if ! aws --endpoint-url http://localhost:4572 s3 cp $SOURCE_FILE_PATH s3://$TARGET_BUCKET/$TARGET_FILE_NAME; then
        echo "Error uploading file"
        exit 1
    fi
fi

# Requires: https://github.com/gdbtek/aws-tools
# Create download URL
# SOURCE_FILE_URL=`$SCRIPT_PATH/aws-tools-1.4/sign_s3_url.bash --bucket $TARGET_BUCKET --file-path $TARGET_FILE_NAME --minute-expire 1440`
echo $TARGET_BUCKET
echo $TARGET_FILE_NAME
# Need to escape ampersands in the replacement string, or sed will do odd stuff
# SED_FILE_URL=`echo $SOURCE_FILE_URL | sed 's|&|\\\&amp;|g'`
# echo $SED_FILE_URL
# Create ingest command
# (sed -e "s|TARGET|$SED_FILE_URL|" | sed -e "s|SOURCE|$DATA_SOURCE_NAME|") > $SOURCE_FILE_NAME.command.xml <<EOF
(sed -e "s|TARGET|aws-s3://$TARGET_BUCKET/$TARGET_FILE_NAME|" | sed -e "s|SOURCE|$DATA_SOURCE_NAME|") > $SOURCE_FILE_NAME.command.xml <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<lib_comm_message>
    <command>INGEST</command>
    <payload>
        <source>SOURCE</source>
        <format>UNUSED_FORMAT</format>
        <filepath>TARGET</filepath>
    </payload>
</lib_comm_message>
EOF

if [ ! -f "$SOURCE_FILE_NAME.command.xml" ]; then
  echo "Could not create command file"
  exit 1
fi

# Copy ingest command to target queue
echo $SQS_ENVIRONMENT-$INGEST_INSTRUCTION-$DATA_SOURCE_NAME
aws --endpoint-url http://localhost:9324 sqs create-queue --queue-name=$SQS_ENVIRONMENT-$INGEST_INSTRUCTION-$DATA_SOURCE_NAME

aws --endpoint-url http://localhost:9324 sqs send-message --queue-url=$SQS_ENDPOINT/queue/$SQS_ENVIRONMENT-$INGEST_INSTRUCTION-$DATA_SOURCE_NAME --message-body="$(<$SOURCE_FILE_NAME.command.xml)"

rm $SOURCE_FILE_NAME.command.xml
