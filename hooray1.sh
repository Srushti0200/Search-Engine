#!/bin/bash

#path to jar file
JAR_FILE="/home/talentum/shared/Search_Engine/SEARCH_ENGINE.jar"


hadoop fs -rm -R /user/talentum/output > /dev/null 2>&1 

#check if .jar file exists
if [[ ! -f "$JAR_FILE" ]]; then
	echo "ERROR:Jar not found"
	exit 1
fi

#execute jar
hadoop jar "$JAR_FILE" Indexer "EnWikiSubset/" 2> /dev/null &

# Capture the process ID (PID) of the Indexer
pid=$!

# Print a message indicating that the process is running
echo -n "Message: Initializing the Indexer Engine"

# Loop to continuously check if the Indexer process is still running
while ps -p $pid > /dev/null; do
    echo -n "."
    # Add a sleep duration to avoid consuming too much CPU
    sleep 3
done

echo -e "\n*************************************"
echo "Indexer has completed its execution"
echo "*************************************"
