#! /usr/bin/gnuplot

set title "algoritmo de dijkstra con grafo dirigido (media de 6)"
set ylabel "tiempo promedio (ms)"
set xlabel "nodos"
set ytics 10000000
set xtics 250
set grid

f(n, a) = (a + n) * log(n) 
z=0.7

plot "t_dir" u 1:2 title "dijkstra",\
	 "c_dir" u 1:(n=$1, a=$2, f(n,a)) w lines title "(a+n)log(n)" #, "c_dir" u 1:(n=$1, a=$2, z*f(n,a)) w lines title sprintf("%g(a+n)log(n)", z)

pause -1
