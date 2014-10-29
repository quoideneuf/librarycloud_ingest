#!/bin/bash
if [ $# -ne 2 ]; then
echo "Usage: iterate_oasis.sh [SOURCE_FILE_PATH] [COMMAND_FILE_PATH]"
exit 1
fi
SCRIPT_PATH=`dirname $0`
for filename in `ls $1`
do
     #$SCRIPT_PATH/ingest_oasis.sh c:/temp/oasis $filename c:/temp/oasiscommand
     $SCRIPT_PATH/ingest_oasis.sh $1 $filename $2
done
