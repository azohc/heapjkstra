#! /usr/bin/gnuplot

set title "media del tiempo transcurrido durante la ejecucion del algoritmo"
set ylabel "tiempo promedio (ms)"
set xlabel "nodos"
set ytics 50000000
set xtics 250
set grid
plot "tiempos" u 1:2 title "dijkstra", "costes" u 1:2 with lines title "(a+n)log(n)"
pause -1