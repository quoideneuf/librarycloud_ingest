 #!/bin/bash

# Script to kick of an Aleph ingest
# 
# Usage: 	ingest-aleph.sh [DATA_FILE] [USERNAME@SERVER]
# Example: 	ingest-aleph.sh ab.bib.00.20140808.full.mrc librarycloud@ingest01.librarycloud.lib.harvard.edu
# 
# TODO: Make more robust

SOURCE_FILE=$1
TARGET_SERVER=$2
TARGET_COMMAND_DIRECTORY=/var/lib/librarycloud/files/ingest-aleph
TARGET_FILE_DIRECTORY=/var/lib/librarycloud/files/dropbox


if [ $# -ne 2 ]; then
    echo "Usage: ingest-aleph.sh [DATA_FILE] [USERNAME@SERVER]"
    exit 1
fi

# Copy file to target
scp $1 $2:$TARGET_FILE_DIRECTORY

# Create ingest command
sed -e "s|TARGET|$TARGET_COMMAND_DIRECTORY/$SOURCE_FILE|" > $1.command.xml <<EOF
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
 
if [ ! -f "$1.command.xml" ]; then
  echo "Could not create command file"
  exit 1
fi

# Copy ingest command to target
scp $1.command.xml $2:$TARGET_COMMAND_DIRECTORY
