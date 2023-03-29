#!/bin/bash

# Count number of scripts failed,success and total
success=0
failed=0
total=0

# Traverse the /test directory and execute all .sh scripts
for file in $(find test -type f -name "*.sh" ! -path "*/fixtures/*" ! -path "*/tmp/*"); do
    dir=$(dirname "$file")
    lfile=$(basename "$file")
    cd "$dir"
    echo "Running $file"
    if bash "$lfile"; then
        echo "$file succeeded"
        (( success++ ))
    else
        echo "$file failed"
        (( failed++ ))
    fi
    (( total++ ))
    cd - >/dev/null
done
echo "${success}/${total} successful"
if [[ $failed -gt 0 ]]; then
	echo "Not successful"
	exit 1
fi
echo "Successful"