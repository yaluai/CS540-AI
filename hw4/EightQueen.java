import java.util.*;
import java.lang.Math;

class State {
    char[] board;

    public State(char[] arr) {
        this.board = Arrays.copyOf(arr, arr.length);
    }

    public void printBoard() {
        System.out.println(String.valueOf(board));
    }

    public void printState(int option, int iteration, int seed) {
        if (option == 1) {
            System.out.println(calcCost());
        } else if (option == 2) {
            List<State> successors = findBestNext();
            if (!successors.isEmpty()) {
                for(State state: successors) {
                    state.printBoard();
                }
                System.out.println(successors.get(0).calcCost());
            }
        } else if (option == 3) {
            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);
            dfs(rng, 0, iteration);
        } else if (option == 4) {
            climb(iteration);
        } else if (option == 5) {
            simulatedAnnealing(seed, iteration);
        }
    }

    public void simulatedAnnealing(int seed, int iteration) {
        double annealRate = 0.95;
        double temperature = 100;
        Random rng = new Random();
        if (seed != -1) rng.setSeed(seed);
        State state = this;
        int iter = 0;
        int currentCost = state.calcCost();
        System.out.println(iter + ":" + String.valueOf(state.board) + " " + currentCost);
        if (currentCost == 0) {
            System.out.println("Solved");
            return;
        }
        while (iter < iteration) {
            int index = rng.nextInt(7);
            int value = rng.nextInt(7);
            double prob = rng.nextDouble();
            State newState = new State(state.board);
            newState.board[index] = Character.forDigit(value, 10);
            double delta = currentCost - newState.calcCost();
            if (delta > 0 || prob < Math.exp(delta/temperature)) {
                state = newState;
                iter += 1;
                currentCost = state.calcCost();
                System.out.println(iter + ":" + String.valueOf(state.board) + " " + currentCost);
                if (currentCost == 0) {
                    System.out.println("Solved");
                    return;
                }
            }
            temperature *= annealRate;
        }
    }

    public void climb(int iteration) {
        State state = this;
        boolean isLocalOptimum = true;
        for (int iter = 0; iter <= iteration; iter++) {
            int currentCost = state.calcCost();
            System.out.println(iter + ":" + String.valueOf(board) + " " + currentCost);
            List<State> states = state.findAllNext();
            if (currentCost == 0) {
                System.out.println("Solved");
                return;
            }
            for (State s : states) {
                if (s.calcCost() < currentCost) {
                    state = s;
                    isLocalOptimum = false;
                    break;
                }
            }
        }
        if (isLocalOptimum) {
            System.out.println("Local optimum");
        }
    }

    public void dfs(Random rng, int currentIter, int iteration) {
        if (currentIter > iteration) {
            return;
        }
        System.out.println(currentIter + ":" + String.valueOf(board) + " " + calcCost());
        List<State> successors = findBestNext();
        if (successors.isEmpty()) {
            System.out.println("Solved");
            return;
        }
        int r = rng.nextInt(successors.size());
        State move = successors.get(r);
        move.dfs(rng, currentIter + 1, iteration);
    }

    public List<State> findAllNext() {
        if (calcCost() == 0) {
            return new ArrayList<>();
        }
        List<State> states = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int x = Character.getNumericValue(board[i]);
            for (int j = 0; j < 8; j++) {
                if (j == x) {
                    continue;
                }
                State state = new State(this.board);
                state.board[i] = Character.forDigit(j, 10);
                states.add(state);
            }
        }
        return states;
    }

    public List<State> findBestNext() {
        int minCost = Integer.MAX_VALUE;
        Map<Integer, List<State>> costMap = new HashMap<>();
        List<State> states = findAllNext();
        if (states.isEmpty()) {
            return states;
        }
        for (State state : states) {
            int cost = state.calcCost();
            if (costMap.containsKey(cost)) {
                costMap.get(cost).add(state);
            } else {
                List<State> list = new ArrayList<>();
                list.add(state);
                costMap.put(cost, list);
            }
            if (minCost > cost) {
                minCost = cost;
            }
        }
        return costMap.get(minCost);
    }

    public int calcCost() {
        int cost = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 8; j++) {
                if (board[i] == board[j]) {
                    cost += 1;
                } else if (board[i] + i == board[j] + j) {
                    cost += 1;
                } else if (board[i] - i == board[j] - j) {
                    cost += 1;
                }
            }
        }
        return cost;
    }

}

public class EightQueen {
    public static void main(String args[]) {
        if (args.length != 2 && args.length != 3) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        int flag = Integer.valueOf(args[0]);
        int option = flag / 100;
        int iteration = flag % 100;
        char[] board = new char[8];
        int seed = -1;
        int board_index = -1;

        if (args.length == 2 && (option == 1 || option == 2 || option == 4)) {
            board_index = 1;
        } else if (args.length == 3 && (option == 3 || option == 5)) {
            seed = Integer.valueOf(args[1]);
            board_index = 2;
        } else {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        if (board_index == -1) return;
        for (int i = 0; i < 8; i++) {
            board[i] = args[board_index].charAt(i);
            int pos = board[i] - '0';
            if (pos < 0 || pos > 7) {
                System.out.println("Invalid input: queen position(s)");
                return;
            }
        }

        State init = new State(board);
        init.printState(option, iteration, seed);
    }
}