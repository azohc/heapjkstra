#! /bin/bash
for file in $("ls tiempos");
do
    for line in $("cat $file");
    do
        echo $line
    done
done
