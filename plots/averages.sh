#! /bin/bash
# halla las medias de los tiempos para cada n distinta
cd ..
for N in `ls -v tiempos`;           # para todas las ejecuciones (N nodos)
do
	sum=0
	first=true
    second=true                     # de 12 lineas, la primera es el numero de aristas
    for T in $(cat tiempos/$N);     # hay 11 ejecuciones: 11 tiempos distintos
    do
    	if $first; then             # no procesar numero de aristas ni
    		if $second; then        # primera medida (siempre mucho más mayor que las demás)
                second=false
            else 
                first=false
            fi
    	else
			sum=$(awk '{print $1+$2}' <<< "${sum} ${T}") # acumular tiempos
		fi
    done
    echo "$N $(awk '{print $1/10}' <<< "${sum}")"        # media entre 10
done
