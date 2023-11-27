# k-SlidingTilePuzzle
Solution to k-puzzles using A-star algorithm approach.

# Heuristics 

Hamming Distance\
Manhattan Distance\
Euclidean Distance

Linear Conflicts Heuristic - Calculates the number of other tiles in a row between a tile and its correct position\
Last Moves Heuristic - Considers additional moves from final position in reverse\

# Search Algorithms

Breadth First Search\
Depth First Search\
Greedy Breadth First Search\
A Star\
Uniform Cost Search

# Command Line 

-size: sets the size of a size x size puzzle\
-rows: sets the rows of a puzzle to rows\
-cols: sets the columns of a puzzle to cols\
-up: performs a move up if possible\
-down: performs a move down if possible\
-left: performs a move left if possible\
-right: performs a move right if possible\
-stats: prints out solution statistics\
-verbose: prints out board states\
-moves: prints out solution moves\
-weight: sets weight for astar\
-l0: uses L0 heuristic\
-l1: uses L1 heuristic\
-l2: uses l2 heuristic\
-lc: uses linear conflict heuristic\
-lm: uses last moves heuristic\
-bfs: uses breadth first search\
-dfs: uses depth first search\
-gbfs: uses greedy breadth first search\
-astar: uses astar \
-ucs: uses uniform cost search

Sample: java Tester.java 3 0 7 2 8 1 6 4 5 -size 3 -astar -l1 -stats


