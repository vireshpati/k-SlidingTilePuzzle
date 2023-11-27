import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class Solver {

    public static State bfs(Board initial) {
        Queue<State> queue = new LinkedList<>();
        State start = State.find(initial);
        queue.offer(start);
        while (!queue.isEmpty()) {
            State current = queue.poll();
            if (current.isGoal()) return current;
            if (!current.isExplored()) {
                current.explored(true);
                for (Direction direction : current.actions()) {
                    State next = State.find(current.next(direction).board());
                    if (!next.isExplored()) {
                        next.update(current, direction);
                        queue.offer(next);
                    }
                }
            }
        }
        return null;
    }

    public static State dfs(Board initial) {

        Stack<State> stack = new Stack<>();
        State start = State.find(initial);
        stack.push(start);
        while (!stack.isEmpty()) {
            State current = stack.pop();
            if (current.isGoal()) return current;
            if (!current.isExplored()) {
                current.explored(true);
                for (Direction direction : current.actions()) {
                    State next = State.find(current.next(direction).board());
                    if (!next.isExplored()) {
                        next.update(current, direction);
                        stack.push(next);
                    }
                }
            }
        }
        return null;
    }

    public static State aStar(Board initial) {
        Queue<State> queue = new PriorityQueue<>();
        State start = State.find(initial);
        queue.add(start);
        while (!queue.isEmpty()) {
            State current = queue.poll();
            if (current.isGoal()) return current;
            if (!current.isExplored()) {
                current.explored(true);
                for (Direction direction : current.actions()) {
                    State next = State.find(current.next(direction).board());

                    if (!next.isExplored()) {
                        next.update(current, direction);
                        queue.add(next);
                    }
                }
            }
        }
        return null;
    }

    public static Direction[] solution(State state) {
        State rover = state;
        Stack<Direction> moves = new Stack<>();

        while (rover != null) {
            if (rover.parent() == null) break;
            moves.push(rover.direction());
            rover = rover.parent();
        }

        Direction[] solution = new Direction[moves.size()];
        int i = 0;
        while (!moves.isEmpty()) solution[i++] = moves.pop();

        return solution;

    }
}
