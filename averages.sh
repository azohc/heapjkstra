#! /bin/bash
cd tiempos
for file in $("ls");
do
    for line in $("cat $file");
    do
        echo $line
    done
done
