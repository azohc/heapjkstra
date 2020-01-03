#! /usr/bin/gnuplot

set title "algoritmo de dijkstra con grafo dirigido (media de 6)"
set ylabel "tiempo promedio (ms)"
set xlabel "nodos"
set ytics 10000000
set xtics 250
set grid
plot "t_dir" u 1:2 title "dijkstra", "c_dir" u 1:2 with lines title "(a+n)log(n)"
pause -1