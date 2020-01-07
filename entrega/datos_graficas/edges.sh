#! /bin/bash
# imprime el numero de aristas para cada n distinta
#dir="tiempos"
dir="tiemposdir"
for file in `ls -v $dir`; 
do
    for line in $(cat $dir/$file);      # primera linea contiene el número de aristas
    do
        echo "$file $line"
        break                           # las demás lineas son tiempos 
    done
done
