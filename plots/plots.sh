#! /usr/bin/gnuplot

set title "media del tiempo transcurrido durante la ejecucion del algoritmo"
set ylabel "tiempo promedio (ms)"
set xlabel "nodos"
set ytics 50000000
set xtics 250
set grid
plot "tiempos" u 1:2 title "dijkstra", "aristas" u 1:(n=$1, a=$2, f(n, a)) with lines title "log"
pause -1