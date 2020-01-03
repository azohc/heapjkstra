#! /bin/bash
# halla las medias de los tiempos para cada n distinta
dir="tiempos"
cd ..
for N in `ls -v $dir`;           # para todas las ejecuciones (N nodos)
do
	sum=0
	first=true  # de 7 lineas, la primera es el numero de aristas
    for T in $(cat $dir/$N);     # hay 6 ejecuciones: 6 tiempos medidos
    do
    	if $first; then             # no procesar numero de aristas ni
            first=false
    	else
			sum=$(awk '{print $1+$2}' <<< "${sum} ${T}") # acumular tiempos
		fi
    done
    echo "$N $(awk '{print $1/6}' <<< "${sum}")"        # media entre 6
done
