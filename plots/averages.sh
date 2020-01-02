#! /bin/bash
# halla las medias de los tiempos de los resultados 
cd ..
for file in `ls -v tiempos`;
do
	sum=0
	first=true
    second=true
    for line in $(cat tiempos/$file);
    do
    	if $first; then
    		if $second; then
                second=false
            else 
                first=false
            fi
    	else
			sum=$(awk '{print $1+$2}' <<< "${sum} ${line}")
		fi
    done
    echo "$file $sum"
done
