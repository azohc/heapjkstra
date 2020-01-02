#! /bin/bash
cd ..
for file in `ls -v tiempos`;
do
	first=true
    for line in $(cat tiempos/$file);
    do
    	if $first; then
            echo "$file $line"
		fi
        break
    done
done
