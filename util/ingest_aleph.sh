 #!/bin/bash

# Script to kick of an Aleph ingest
# 
# Usage: 	ingest-aleph.sh [DATA_FILE] [USERNAME@SERVER] [ssh key path]
# Example: 	ingest-aleph.sh ab.bib.00.20140808.full.mrc librarycloud@ingest01.librarycloud.lib.harvard.edu ~/.ssh/mykey.pem
# 
# TODO: Make more robust

SOURCE_FILE_PATH=$1
SOURCE_FILE_NAME=$(basename $SOURCE_FILE_PATH)
TARGET_SERVER=$2
TARGET_COMMAND_DIRECTORY=/var/lib/librarycloud/files/ingest-aleph
TARGET_FILE_DIRECTORY=/var/lib/librarycloud/files/dropbox
SSH_KEY_LOCATION=$3

if [ $# -ne 3 ]; then
    echo "Usage: ingest-aleph.sh [DATA_FILE] [USERNAME@SERVER] [ssh key path]"
    exit 1
fi

# Copy file to target
scp -i $SSH_KEY_LOCATION $SOURCE_FILE_PATH $TARGET_SERVER:$TARGET_FILE_DIRECTORY

# Create ingest command
sed -e "s|TARGET|$TARGET_FILE_DIRECTORY/$SOURCE_FILE_NAME|" > $SOURCE_FILE_NAME.command.xml <<EOF
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

# Copy ingest command to target
scp -i $SSH_KEY_LOCATION $SOURCE_FILE_NAME.command.xml $TARGET_SERVER:$TARGET_COMMAND_DIRECTORY
