#! /bin/bash
# halla (a+n)log(n) para cada n distinta
cd ..
for file in `ls -v tiempos`; 
do
    for line in $(cat tiempos/$file);	# primera linea contiene el número de aristas
    do
        echo "$file $(awk '{print ($1+$2)*log($2)}' <<< "${line} ${file}")"
        break							# las demás lineas son tiempos 
    done
done
