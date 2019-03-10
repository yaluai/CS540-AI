package w;

import java.util.*;

class State {
    int cap_jug1;
    int cap_jug2;
    int curr_jug1;
    int curr_jug2;
    int goal;
    int depth;
    State parentPt;

    public State(int cap_jug1, int cap_jug2, int curr_jug1, int curr_jug2, int goal, int depth) {
        this.cap_jug1 = cap_jug1;
        this.cap_jug2 = cap_jug2;
        this.curr_jug1 = curr_jug1;
        this.curr_jug2 = curr_jug2;
        this.goal = goal;
        this.depth = depth;
        this.parentPt = null;
    }

    @Override
    public String toString() {
        return this.getOrderedPair();
    }

    public State[] getSuccessors() {
        // TO DO: get all successors and return them in proper order
        State[] successors = new State[this.depth + 1];
        State goal = this;
        for (int i = 0; i <= this.depth; i++) {
            successors[i] = goal;
            goal = goal.parentPt;
        }

        return successors;
    }

    public boolean isGoalState() {
        // TO DO: determine if the state is a goal node or not and return boolean
        if (curr_jug1 == goal || curr_jug2 == goal) {
            return true;
        } else {
            return false;
        }
    }

    public void printState(int option, int depth) {

        // TO DO: print a State based on option (flag)
        if (option == 1) {
            List<String> actions = actions();
            for (String action : actions) {
                System.out.println(action);
            }
        } else if (option == 2) {
            List<String> actions = actions();
            for (String action : actions) {
                if (action.contains(goal + "")) {
                    System.out.println(action + " " + true);
                } else {
                    System.out.println(action + " " + false);
                }
            }
        } else {
            UninformedSearch.run(this, option, depth);
        }

    }

    public String getOrderedPair() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.curr_jug1);
        builder.append(this.curr_jug2);
        return builder.toString().trim();
    }

    // TO DO: feel free to add/remove/modify methods/fields
    public void f1(List<String> actions) {
        if (curr_jug1 < cap_jug1) {
            actions.add(cap_jug1 + "" + curr_jug2);
        }
    }

    public void f2(List<String> actions) {
        if (curr_jug2 < cap_jug2) {
            actions.add(curr_jug1 + "" + cap_jug2);
        }
    }

    public void e1(List<String> actions) {
        if (curr_jug1 != 0) {
            actions.add("0" + curr_jug2);
        }
    }

    public void e2(List<String> actions) {
        if (curr_jug2 != 0) {
            actions.add(curr_jug1 + "0");
        }
    }

    public void p12(List<String> actions) {
        if (curr_jug1 > 0 && curr_jug2 < cap_jug2) {
            if (cap_jug2 - curr_jug2 > curr_jug1) {
                actions.add("0" + (curr_jug2 + curr_jug1));
            } else {
                actions.add(curr_jug1 - (cap_jug2 - curr_jug2) + "" + cap_jug2);
            }
        }
    }

    public void p21(List<String> actions) {
        if (curr_jug1 < cap_jug1 && curr_jug2 > 0) {
            if (cap_jug1 - curr_jug1 > curr_jug2) {
                actions.add(curr_jug1 + curr_jug2 + "0");
            } else {
                actions.add(cap_jug1 + "" + (curr_jug2 - (cap_jug1 - curr_jug1)));
            }
        }
    }

    public List<String> actions() {
        List<String> actions = new ArrayList<>();
        f1(actions);
        f2(actions);
        e1(actions);
        e2(actions);
        p12(actions);
        p21(actions);
        Collections.sort(actions);
        return actions;
    }

    public void printPath() {
        System.out.println("Goal");
        State[] list = this.getSuccessors();


        System.out.print("Path ");
        for (int i = list.length - 1; i >= 0; i--) {
            System.out.print(list[i] + " ");
        }
    }

    public List<State> nextStates() {
        List<State> result = new ArrayList<>();
        for (String action: this.actions()) {
            int new_curr_jug1 = Integer.valueOf(action.charAt(0) + "");
            int new_cap_jug2 = Integer.valueOf(action.charAt(1) + "");
            State newState = new State(this.cap_jug1, this.cap_jug2, new_curr_jug1, new_cap_jug2, this.goal, this.depth + 1);
            newState.parentPt = this;
            result.add(newState);
        }
        return result;
    }


}

class UninformedSearch {
    static private int maxDepth = -1;
    static private boolean success = false;

    private static <T> void printIterable(Iterable<T> iterable) {
        System.out.print("[");
        Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next());
            if (iterator.hasNext()) {
                System.out.print(",");
            }
        }
        System.out.print("]");
    }

    private static void bfs(State curr_state) {
        // TO DO: run breadth-first search algorithm
        System.out.println(curr_state.toString());
        LinkedList<State> queue = new LinkedList<>();
        List<String> visited = new ArrayList<>();
        Set<String> set = new HashSet<>();
        queue.add(curr_state);
        while (!queue.isEmpty()) {
            State element = queue.pop();
            System.out.print(element + " ");
            set.add(element.toString());
            if (element.isGoalState()) {
                element.printPath();
                success = true;
                return;
            }
            for(State state: element.nextStates()) {
                if (set.add(state.toString())) {
                    queue.add(state);
                }
            }
            if (!visited.contains(element.toString())) {
                printIterable(queue);
                visited.add(element.toString());
                System.out.print(" ");
                printIterable(visited);
                System.out.println("");
            }
        }
    }

    private static void dfs(State curr_state) {
        if (maxDepth >= 0) System.out.print(maxDepth + ":");
        System.out.println(curr_state.toString());
        Stack<State> stack = new Stack<>();
        List<String> visited = new ArrayList<>();
        Set<String> set = new HashSet<>();
        stack.push(curr_state);
        while (!stack.isEmpty()) {
            State element = stack.pop();
            if (maxDepth >= 0) System.out.print(maxDepth + ":");
            System.out.print(element + " ");
            set.add(element.toString());
            if (maxDepth < 0 || element.depth < maxDepth) {
                for(State state: element.nextStates()) {
                    if (set.add(state.toString())) {
                        stack.add(state);
                    }
                }
            }
            if (element.isGoalState()) {
                element.printPath();
                success = true;
                return;
            }
            if (!visited.contains(element.toString())) {
                printIterable(stack);
                visited.add(element.toString());
                System.out.print(" ");
                printIterable(visited);
                System.out.println("");
            }
        }
    }

    private static void iddfs(State curr_state, int depth) {
        // TO DO: run IDDFS search algorithm
        for (int i = 0; i < depth; i++) {
            maxDepth = i;
            dfs(curr_state);
            if (success) {
                break;
            }
        }
    }

    public static void run(State curr_state, int option, int depth) {
        // TO DO: run either bfs, dfs or iddfs according to option (flag)
        if (option == 3) {
            bfs(curr_state);
        } else if (option == 4) {
            dfs(curr_state);
        } else {
            iddfs(curr_state, depth);
        }
    }
}

public class WaterJug {
    public static void main(String args[]) {
        if (args.length != 6) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }
        int flag = Integer.valueOf(args[0]);
        int cap_jug1 = Integer.valueOf(args[1]);
        int cap_jug2 = Integer.valueOf(args[2]);
        int curr_jug1 = Integer.valueOf(args[3]);
        int curr_jug2 = Integer.valueOf(args[4]);
        int goal = Integer.valueOf(args[5]);

        int option = flag / 100;
        int depth = flag % 100;

        if (option < 1 || option > 5) {
            System.out.println("Invalid flag input");
            return;
        }
        if (cap_jug1 > 9 || cap_jug2 > 9 || curr_jug1 > 9 || curr_jug2 > 9) {
            System.out.println("Invalid input: 2-digit jug volumes");
            return;
        }
        if (cap_jug1 < 0 || cap_jug2 < 0 || curr_jug1 < 0 || curr_jug2 < 0) {
            System.out.println("Invalid input: negative jug volumes");
            return;
        }
        if (cap_jug1 < curr_jug1 || cap_jug2 < curr_jug2) {
            System.out.println("Invalid input: jug volume exceeds its capacity");
            return;
        }
        State init = new State(cap_jug1, cap_jug2, curr_jug1, curr_jug2, goal, 0);
        init.printState(option, depth);
    }
}