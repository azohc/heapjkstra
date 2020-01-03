#! /bin/bash
# halla (a+n)log(n) para cada n distinta
dir="tiempos"
cd ..
for file in `ls -v $dir`; 
do
    for line in $(cat $dir/$file);		# primera linea contiene el número de aristas
    do
        echo "$file $(awk '{print ($1+$2)*log($2)}' <<< "${line} ${file}")"
        break							# las demás lineas son tiempos 
    done
done
