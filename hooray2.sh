#!/bin/bash


#path to jar file
JAR_FILE="/home/talentum/shared/Search_Engine/SEARCH_ENGINE.jar"

#startup message
echo "****************************"
echo ""WELCOME TO COSMIC CRUISE!""
echo "EXPLORE, SEARCH, SOAR!"
echo "****************************"
echo -e "\n"


# Prompt the user to input a string
echo "Enter Your Query:"
read usr_query
echo 


# Prompt the user to input an integer
while true; do
    echo "How Many Results Do You Want?"
    read user_integer_str
    if [[ $user_integer_str =~ ^[0-9]+$ ]]; then
        n_results=$user_integer_str
        break  # Break the loop if the input is a valid integer
    else
        echo "Invalid input. Please enter an integer."
    fi
done


echo

# Print a message indicating that the process is running
echo -n "Fetching results for $usr_query....."
echo 

# run jar for provided query
#execute jar
hadoop jar "$JAR_FILE" Query $n_results "$usr_query" 2> /dev/null  

# Capture the process ID (PID) of the Indexer
#pid=$!



# Loop to continuously check if the Query process is still running
#while ps -p $pid > /dev/null; do
    #echo -n "."
    # Add a sleep duration to avoid consuming too much CPU
    #sleep 2
#done

echo


#cd to java_extraction_code
cd ~/shared/Search_Engine/java_extraction_code/ 

#compile & run UrlExtractor

javac UrlExtractor.java 

echo "``````````````````````````````````````````````````````````"
java UrlExtractor
echo "``````````````````````````````````````````````````````````"




