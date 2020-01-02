#! /bin/bash
cd ..
for file in `ls -v tiempos`;
do
	first=true
    for line in $(cat tiempos/$file);
    do
    	if $first; then
            echo "$file $(awk '{print ($1+$2)*log($2)}' <<< "${line} ${file}")"
		fi
        break
    done
done
