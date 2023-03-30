#!/bin/bash

# Count number of scripts failed,success and total
success=0
failed=0
total=0

# Traverse the /test directory and execute all .sh scripts
for file in $(find sample -type f -name "*.sh"); do
    dir=$(dirname "$file")
    lfile=$(basename "$file")
    cd "$dir"
    echo "--------------------------"
    echo "Running $file"
    echo "--------------------------"
    if bash "$lfile"; then
        echo "$file succeeded"
        (( success++ ))
    else
        echo "$file failed"
        (( failed++ ))
    fi
    (( total++ ))
    cd - >/dev/null
    echo "--------------------------"
done
echo "${success}/${total} successful"
if [[ $failed -gt 0 ]]; then
	echo "Not successful"
	exit 1
fi
echo "Successful"