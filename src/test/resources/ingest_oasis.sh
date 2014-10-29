#!/bin/bash
SOURCE_FILE_PATH=$1
SOURCE_FILE_NAME=$2
COMMAND_FILE_PATH=$3
if [ $# -ne 3 ]; then
echo "Usage: ingest-oasis.sh [SOURCE_FILE_PATH] [SOURCE_FILE_NAME] [COMMAND_FILE_PATH]"
exit 1
fi
SOURCE_FULL_PATH=$SOURCE_FILE_PATH/$SOURCE_FILE_NAME 
# Need to escape ampersands in the replacement string, or sed will do odd stuff
SED_FILE_PATH=`echo $SOURCE_FULL_PATH | sed 's|&|\\\&amp;|g'`
# Create ingest command
sed -e "s|TARGET|$SED_FILE_PATH|" > $SOURCE_FILE_NAME.command.xml <<EOF
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
mkdir  -p $COMMAND_FILE_PATH
mv $SOURCE_FILE_NAME.command.xml $COMMAND_FILE_PATH
