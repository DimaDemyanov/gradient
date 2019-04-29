import functions.LinearFunction;
import org.junit.Assert;

public class Main {
    public static void main(String[] args) {
        Minimizer minimizer = new Minimizer();
        double [] trueParams = new double[] {2.0, 1.5, -3.4, 10.1, 4, 2.1, -10.3, 5.7};
        double [] startParams = new double[] {2.1, 1.5, -4.4, 8.1, 3, 2.0, -8.3, 7.7};
        minimizer.setThreadsCnt(1);
        long start = System.nanoTime();
        double [] params = minimizer.minimize(startParams, 0.0000000001f, "data/gen1.data");
        long finish = System.nanoTime();
        System.out.println((finish -start) / 1000000);
        for (int i = 0; i < trueParams.length; i++) {
            Assert.assertEquals(params[i], trueParams[i], 0.1);
        }
    }
}
