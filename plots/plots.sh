#! /usr/bin/gnuplot

# set title "media del tiempo transcurrido durante la ejecucion del algoritmo"
# set ylabel "tiempo promedio (ms)"
# set xlabel "nodos"
# set ytics 50000000
# set xtics 250
# set grid
# f(N, A) = (A + N)*log(N)
# plot "tiempos" u 1:2 title "dijkstra", "tiempos" u 1:(n=$1, a=$2, f(n, a)) with lines title "log"

set title "test"
set ylabel "tiempo promedio (ms)"
set xlabel "nodos"
set grid
plot "t_tiempos" u 1:2 title "dijkstra", "t_coste" u 1:2 with lines title "(a+n)log(n)"

pause -1