package generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;



public class Generator {

    public static void generate(String path, double [] params, double minX, double maxX, double eps, int n)
            throws IOException {

        FileWriter fw = new FileWriter("data/" + path);
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            double sum = 0;
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < params.length; j++) {
                double x = random.nextDouble() * (maxX - minX) + minX;
                sum += (x + (random.nextDouble() - 0.5) * 2 * eps) * params[j];
                sb.append(x).append(" ");
            }
            sb.append(sum).append('\n');
            fw.append(sb.toString());
        }
        fw.flush();
    }

    public static void main(String[] args) {
        try {
            generate("gen1.data", new double[] {2.0, 1.5, -3.4, 10.1, 4, 2.1, -10.3, 5.7},
                    -100, 100, 0.3, 10000000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
