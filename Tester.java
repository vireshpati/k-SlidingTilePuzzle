import java.util.ArrayList;

public class Tester {

    private static int[] toArray(ArrayList<Integer> list) {
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public static void main(String[] args) {

        ArrayList<Direction> moves = new ArrayList<>();
        ArrayList<Integer> tiles = new ArrayList<>();
        String option = "";
        String solve = "";
        int columns = 4;
        int rows = 4;
        int l = 0;
        double weight = 1.0;
        boolean stats = false;
        boolean verbose = false;
        boolean steps = false;

        for (String arg : args) {

            switch (arg.toLowerCase()) {
                case "-size", "-rows", "-cols", "-columns", "-weight" -> {
                    if (option.length() > 0) {
                        System.err.println("Missing value for option: " + option);
                    }
                    option = arg;
                    continue;
                }
                case "up", "down", "left", "right" -> {
                    if (option.length() > 0) {
                        System.err.println("Missing value for option: " + option);
                    }
                    Direction move = Direction.valueOf(arg.toUpperCase());
                    moves.add(move);
                    continue;
                }
                case "-bfs", "-dfs", "-stats", "-verbose", "-ucs", "-gbfs", "-astar", "-l0", "-l1", "-l2", "-moves", "-lc", "-lm" -> option = arg;
            }

            try {
                switch (option) {
                    case "-size" -> {
                        rows = Integer.parseInt(arg);
                        columns = rows;
                    }
                    case "-rows" -> rows = Integer.parseInt(arg);
                    case "-cols", "-columns" -> columns = Integer.parseInt(arg);
                    case "-bfs", "-dfs", "-ucs", "-gbfs", "-astar" -> solve = arg;
                    case "-stats" -> stats = true;
                    case "-verbose" -> verbose = true;
                    case "-L0" -> l = 0;
                    case "-L1" -> l = 1;
                    case "-L2" -> l = 2;
                    case "-LC" -> l = 3;
                    case "-LM" -> l = 4;
                    case "-weight" -> weight = Double.parseDouble(arg);
                    case "-moves" -> steps = true;
                    default -> {
                        int tile = Integer.parseInt(arg);
                        tiles.add(tile);
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid value for option " + option + ": " + arg);
            }
            option = "";
        }

        if (option.length() > 0) {
            System.err.println("Missing value for option: " + option);
        }

        Board board;
        if (tiles.size() > 0) {
            board = new Board(rows, columns, toArray(tiles));
        } else {
            board = new Board(rows, columns);
        }

        State solutionNode, state = new State(board);
        switch (solve) {

            case "-bfs" -> solutionNode = Solver.bfs(board);
            case "-dfs" -> solutionNode = Solver.dfs(board);
            case "-ucs" -> {
                State.configure(1, 0, l);
                solutionNode = Solver.aStar(board);
            }
            case "-gbfs" -> {
                State.configure(0, 1, l);
                solutionNode = Solver.aStar(board);
            }
            case "-astar" -> {
                State.configure(1, weight, l);
                solutionNode = Solver.aStar(board);
            }
            default -> solutionNode = null;
        }

        if (verbose) {
            System.out.println();
            board.print();
            System.out.println();
        }

        Direction[] solutionSteps = Solver.solution(solutionNode);

        for (Direction direction : solutionSteps) {
            state = state.next(direction);
            if (steps) System.out.println(direction);
            if (verbose) {
                System.out.println();
                state.board().print();
                System.out.println();
            }
        }

        if (stats) {
            assert solutionNode != null;
            double[] statistics = State.stats(solutionNode);
            System.out.println("Solution Depth: " + statistics[0]);
            System.out.println("States Expanded: " + statistics[1]);
            System.out.println("States Explored: " + statistics[2]);
            System.out.println("Branching Factor: " + statistics[3]);
        }

    }


}
