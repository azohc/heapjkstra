#! /bin/bash
cd ..
for file in `ls -v tiempos`;
do
	sum=0
	first=true
    for line in $(cat tiempos/$file);
    do
    	if $first; then
    		first=false
    	else
			sum=$(awk '{print $1+$2}' <<< "${sum} ${line}")
		fi
    done
    echo "$file $sum"
done
