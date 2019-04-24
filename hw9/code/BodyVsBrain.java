import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BodyVsBrain {

    private static List<List<Double>> readData(String filename) throws IOException {
        List<List<Double>> result = new ArrayList<>();
        List<Double> X = new ArrayList<>();
        List<Double> Y = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine())!=null){
            if (line.contains("Body Weight")){
                continue;
            }
            String[] tokens = line.split(",");
            X.add(Double.valueOf(tokens[0]));
            Y.add(Double.valueOf(tokens[1]));
        }
        reader.close();
        result.add(X);
        result.add(Y);
        return result;
    }

    // function for question 1
    private static void computeStatistics(List<Double> X, List<Double> Y){
        double meanX = getMean(X);
        double deviationX = getStd(X);
        double meanY = getMean(Y);
        double deviationY = getStd(Y);
        System.out.println(X.size());
        System.out.printf("%.4f %.4f\n",meanX,deviationX);
        System.out.printf("%.4f %.4f\n",meanY,deviationY);
    }

    // function for question 2
    private static double meanSquareError(List<Double> X, List<Double> Y, double beta0, double beta1){
        double mse = 0;
        for (int i = 0; i < X.size(); i++) {
            mse += Math.pow(beta0 + beta1*X.get(i)-Y.get(i), 2);
        }
        return mse/X.size();
    }

    //function for question 3
    private static double[] gradient(List<Double> X, List<Double> Y, double beta0, double beta1){
        double beta0Gradient = 0;
        double beta1Gradient = 0;
        for (int i = 0; i < X.size(); i++) {
            beta0Gradient += beta0 + beta1*X.get(i)- Y.get(i);
        }
        beta0Gradient = beta0Gradient*2/X.size();
        for (int i = 0; i < X.size(); i++) {
            beta1Gradient += (beta0 + beta1*X.get(i)- Y.get(i))*X.get(i);
        }
        beta1Gradient = beta1Gradient*2/X.size();
        return new double[]{beta0Gradient, beta1Gradient};
    }

    //question 4
    private static void gradientDescent(List<Double> X, List<Double> Y, double lambda, int T){
        double beta0 = 0;
        double beta1 = 0;
        for (int i = 0; i < T; i++) {
            double[] res = gradient(X, Y, beta0, beta1);
            beta0 = beta0 - lambda*res[0];
            beta1 = beta1 - lambda*res[1];
            System.out.printf("%d %.4f %.4f %.4f\n", i+1, beta0, beta1, meanSquareError(X, Y, beta0, beta1));
        }
    }

    private static double getMean(List<Double> X){
        double meanX = 0;
        for(double a:X){
            meanX += a;
        }
        return meanX/X.size();
    }

    private static double getStd(List<Double> X){
        double meanX = getMean(X);
        double deviationX = 0;
        for(double a:X){
            deviationX += Math.pow(a-meanX, 2);
        }
        deviationX/=X.size()-1;
        deviationX = Math.sqrt(deviationX);
        return deviationX;
    }

    //question 5
    private static double[] closedForm(List<Double> X, List<Double> Y){
        double meanX = getMean(X);
        double meanY = getMean(Y);
        double beta1 = 0;
        double beta0 = 0;
        for (int i = 0; i < X.size(); i++) {
            beta1+=(X.get(i)-meanX)*(Y.get(i)-meanY);
        }
        double tmp = 0;
        for (int i = 0; i < X.size(); i++) {
            tmp+=Math.pow(X.get(i)-meanX, 2);
        }
        beta1 = beta1/tmp;
        beta0 = meanY - beta1*meanX;
        return new double[]{beta0, beta1};
        //System.out.printf("%.4f %.4f %.4f\n", beta0, beta1, meanSquareError(X,Y,beta0,beta1));
    }

    //question 6
    private static void predict(List<Double> X, List<Double> Y, double weight){
        double[] res = closedForm(X, Y);
        System.out.printf("%.4f\n", res[0]+res[1]*weight);
    }

    //question 7
    private static void normolized(List<Double> X, List<Double> Y, double lambda, int T){
        double std = getStd(X);
        double mean = getMean(X);
        for (int i = 0; i < X.size(); i++) {
            X.set(i, (X.get(i)-mean)/std);
        }
        gradientDescent(X, Y, lambda, T);
    }

    //question 8
    private static void SGD(List<Double> X, List<Double> Y, double lambda, int T){
        double std = getStd(X);
        double mean = getMean(X);
        for (int i = 0; i < X.size(); i++) {
            X.set(i, (X.get(i)-mean)/std);
        }
        int n = 10;
        double beta0 = 0;
        double beta1 = 0;
        for (int i = 0; i < T; i++) {
            List<Integer> index = getRandomIndex(n, X.size());
            List<Double> selectX = new ArrayList<>();
            List<Double> selectY = new ArrayList<>();
            for (int ind:index) {
                selectX.add(X.get(ind));
                selectY.add(Y.get(ind));
            }
            double[] res = gradient(selectX, selectY, beta0, beta1);
            beta0 = beta0 - lambda*res[0];
            beta1 = beta1 - lambda*res[1];
            System.out.printf("%d %.4f %.4f %.4f\n", i+1, beta0, beta1, meanSquareError(X, Y, beta0, beta1));
        }
    }

    private static List<Integer> getRandomIndex(int n, int size){
        Random random = new Random();
        List<Integer> v = new ArrayList<>();
        int count = 0;
        while (count<n){
            int number = random.nextInt(size);
            if (!v.contains(number)){
                v.add(number);
                count++;
            }
        }
        return v;
    }


    public static void main(String[] args) throws IOException {
        List<List<Double>> res = readData("data.csv");
        List<Double> X = res.get(0);
        List<Double> Y = res.get(1);
        int flag = Integer.valueOf(args[0]);
        if (flag==100){
            computeStatistics(X,Y);
        }else if (flag==200){
            System.out.printf("%.4f\n", meanSquareError(X,Y,Double.valueOf(args[1]),Double.valueOf(args[2])));
        }else if (flag==300){
            double[] r = gradient(X,Y,Double.valueOf(args[1]),Double.valueOf(args[2]));
            System.out.printf("%.4f\n%.4f\n", r[0], r[1]);
        }else if (flag==400){
            gradientDescent(X, Y, Double.valueOf(args[1]),Integer.valueOf(args[2]));
        }else if (flag==500){
            double[] r = closedForm(X, Y);
            System.out.printf("%.4f %.4f %.4f\n", r[0], r[1], meanSquareError(X,Y,r[0],r[1]));
        }else if (flag==600){
            predict(X,Y,Double.valueOf(args[1]));
        }else if (flag==700){
            normolized(X, Y, Double.valueOf(args[1]),Integer.valueOf(args[2]));
        }else if (flag==800){
            SGD(X, Y, Double.valueOf(args[1]),Integer.valueOf(args[2]));
        }
    }

}
