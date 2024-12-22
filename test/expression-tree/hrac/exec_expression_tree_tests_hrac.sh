#!/bin/bash

# Count number of scripts failed,success and total
success=0
failed=0
total=0

# Traverse the current directory and execute all .sh scripts
for file in $(find . -type f -name "*.hrac"); do
	cfile=${file}.bin
    echo "--------------------------"
	echo "Compiling $file to $cfile"
	java -jar ../../som-java.jar --compile --infile="${file}" --outfile="${cfile}"
    echo "Running $cfile"
	if java -jar ../../som-java.jar --run --infile="${cfile}"; then    
        echo "$file succeeded"
        (( success++ ))
    else
        echo "$file failed"
        (( failed++ ))
    fi
    (( total++ ))
    cd - >/dev/null    echo "--------------------------"
done
echo "${success}/${total} successful"
if [[ $failed -gt 0 ]]; then
	echo "Not successful"
	exit 1
fi
echo "Successful"