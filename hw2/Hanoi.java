import java.util.*;

public class Hanoi {

    public static List<String> getSuccessor(String[] hanoi) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < hanoi.length; i++) {
            for (int j = Math.max(0, i - 1); j <= Math.min(hanoi.length - 1, i + 1); j++) {
                if (i != j) {
                    int topNum = Character.getNumericValue(hanoi[i].charAt(0));
                    int topNum2 = Character.getNumericValue(hanoi[j].charAt(0));
                    if (topNum > 0 && (topNum < topNum2 || topNum2 == 0)) {
                        String[] temp = hanoi.clone();
                        if (hanoi[i].length() == 1) {
                            temp[i] = "0";
                        } else {
                            temp[i] = temp[i].substring(1);
                        }
                        if (topNum2 == 0) {
                            temp[j] = "" + topNum;
                        } else {
                            temp[j] = "" + topNum + temp[j];
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int k = 0; k < temp.length; k++) {
                            if (k != 0) {
                                sb.append(" ");
                            }
                            sb.append(temp[k]);
                        }

                        result.add(sb.toString());
                    }
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            return;
        }

        List<String> sucessors = getSuccessor(args);
        for (int i = 0; i < sucessors.size(); i++) {
            System.out.println(sucessors.get(i));
        }
    }

}