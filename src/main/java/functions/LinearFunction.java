package functions;

public class LinearFunction {

    public static float apply(double[] x, double [] params) {
        float sum = 0;
        for (int i = 0; i < params.length; i++) {
            sum += x[i] * params[i];
        }
        return sum;
    }
}
