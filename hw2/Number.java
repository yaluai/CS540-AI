import java.util.LinkedList;

public class Number {

    public static String getStep(int x, int y) {
        String result = "";
        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(x);
        int ans = 0;
        while (!queue.isEmpty() && result.isEmpty()) {
            int count = queue.size();
            while (count > 0) {
                Integer candidate = queue.remove();
                if (candidate == y) {
                    result = "" + ans;
                    break;
                }
                queue.add(candidate + 1);
                queue.add(candidate - 1);
                queue.add(candidate * 3);
                queue.add(candidate * candidate);
                count --;
            }
            ans ++;
        }

        return result;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            return;
        }

        System.out.println(getStep(Integer.parseInt(args[0]), Integer.parseInt(args[1])));

    }
}