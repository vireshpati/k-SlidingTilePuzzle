import java.util.HashMap;

public class State implements Comparable<State> {

    private static final HashMap<Board, State> states = new HashMap<>();
    private static int a, l;
    private static double b;
    private final Board board;
    private State parent;
    private Direction direction;
    private int cost;
    private boolean explored;

    public State(Board board) {
        this.board = board;
        this.parent = null;
        this.direction = null;
        this.cost = 0;
        this.explored = false;
    }

    public static State find(Board board) {
        State state = states.get(board);
        if (state == null) {
            state = new State(board);
            states.put(board, state);
        }
        return state;
    }

    public static double[] stats(State state) {

        int depth = state.cost();
        int explored = 0;
        for (State s : states.values()) {
            if (s.isExplored()) explored++;
        }
        int expanded = states.size();
        double branchingFactor = Math.round(Math.pow(expanded, (double) 1 / depth) * 100.0) / 100.0;


        return new double[]{depth, explored, expanded, branchingFactor};
    }

    public static void configure(int A, double B, int L) {
        a = A;
        b = B;
        l = L;
    }

    public Board board() {
        return this.board;
    }

    public State parent() {
        return this.parent;
    }

    public Direction direction() {
        return this.direction;
    }

    public int cost() {
        return this.cost;
    }

    public boolean isExplored() {
        return explored;
    }

    public boolean isGoal() {
        return board.isGoal();
    }

    public void explored(boolean explored) {
        this.explored = explored;
    }

    public void update(State parent, Direction direction) {
        this.parent = parent;
        this.direction = direction;
        this.cost = parent.cost + 1;
        states.put(board, this);
    }

    public State next(Direction direction) {
        return new State(new Board(board, direction));
    }

    public Direction[] actions() {
        return board.moves(board.empty());
    }

    @Override
    public int compareTo(State o) {
        return Double.compare(evaluate(), o.evaluate());
    }

    public double evaluate() {
        return a * cost + b * heuristic();
    }

    private double heuristic() {

        double distance = 0;

        switch (l) {

            case 0 -> {
                int[] goal = Board.goal(board.rows(), board().columns());
                for (int i = 0; i < goal.length; i++) {
                    if (board.get(i) - goal[i] != 0) distance++;
                }
            }

            case 1 -> {
                for (int i = 0; i < board.len(); i++) {
                    int correctIndex = (board.get(i) + board.len() - 1) % board.len();
                    distance += Math.abs(board.row(correctIndex) - board.row(i)) + Math.abs(board.column(correctIndex) - board.column(i));
                }
            }

            case 2 -> {
                for (int i = 0; i < board.len(); i++) {
                    int correctIndex = (board.get(i) + board.len() - 1) % board.len();
                    distance += Math.sqrt(Math.pow(board.row(correctIndex) - board.row(i), 2) + Math.pow(board.column(correctIndex) - board.column(i), 2));
                }
            }

            case 3 -> {
                // Linear conflict heuristic

                for (int i = 0; i < board.len(); i++) {
                    int correctIndex = (board.get(i) + board.len() - 1) % board.len();
                    distance += Math.abs(board.row(correctIndex) - board.row(i)) + Math.abs(board.column(correctIndex) - board.column(i));
                }

                for (int i = 0; i < board.rows(); i++) {
                    for (int j = 0; j < board.columns(); j++) {
                        for(int k = j; k < board.columns(); k++){
                            if(board.get(i,j) > board.get(i,k)){
                                distance += 2*(k-j);
                            }
                        }
                        for (int k = 0; k < board.rows(); k++) {
                            if(board.get(j,i) > board.get(k,i)){
                                distance += 2*(k-j);
                            }

                        }
                    }
                }


            }

            case 4 -> {

                // Last moves heuristic
                for (int i = 0; i < board.len(); i++) {
                    int correctIndex = (board.get(i) + board.len() - 1) % board.len();
                    distance += Math.abs(board.row(correctIndex) - board.row(i)) + Math.abs(board.column(correctIndex) - board.column(i));
                }

                int left = board.len()-1;
                int top = board.len()- board.columns();

                for (int i = 0; i < board.rows(); i++) {
                    if(board.get(i,board.columns()-1) != top){
                        if( i == board.rows()-1) return distance +2;
                    }
                }

                for (int i = 0; i < board.columns(); i++) {
                    if(board.get(board.rows()-1,i)!=left) {
                        if(i== board.columns()-1) return distance +2;
                    }
                }



            }

        }

        return distance;


    }
}
