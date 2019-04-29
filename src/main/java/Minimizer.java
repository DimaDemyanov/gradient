import functions.LinearFunction;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;
import scala.Tuple2;

import java.util.Arrays;


public class Minimizer {

    private SparkConf sparkConf = new SparkConf()
            .setAppName("Gradient Descent")
            .setMaster("local[*]");

    {
        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);
    }

    public void setThreadsCnt(int cnt) {
        sparkConf.setMaster("local[" + cnt + "]");
    }

    public static final int ITERATIONS = 300;

    /**
     *
     * @param startParams - parameters which start gradient descent
     * @param alpha - coefficient of gradient descent
     * @param path - path to file with data
     * @return parameters, which minimize loss function
     */

    public double [] minimize(double [] startParams, double alpha, String path){


        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD<double []> points = sc.
                textFile(path).map(
                o -> Arrays.stream(o.split(" "))
                .mapToDouble(Double::parseDouble).toArray()
        );

        Vector startParamsVector = new DenseVector(startParams);

        double error = -1, prevError = -1;

        int k = 0;
        Vector newParamsVector = null;
        while((prevError > error || prevError == -1) && k++ < ITERATIONS) {
            prevError = error;
            error = 0;
            Vector startParamsVector1 = startParamsVector;
            JavaPairRDD<Vector, Double> rdd = points.mapToPair(o -> {
                        Vector oVector = new DenseVector(o);
                        oVector = utils.Vectors.scal(o[o.length - 1] - LinearFunction.apply(startParamsVector1.toArray(),
                                Arrays.copyOfRange(o, 0, o.length - 1)),
                                oVector);
                        return new Tuple2<>(
                                oVector
                                ,
                                Math.pow((o[o.length - 1] -
                                        LinearFunction.apply(startParamsVector1.toArray(),
                                                Arrays.copyOfRange(o, 0, o.length - 1))), 2)
                        );
                    }
            );
            Tuple2<Vector, Double> tempError = rdd
                    .reduce((o1, o2) -> {
                        Vector sum = utils.Vectors.axpy(1, o1._1, o2._1);
                        return new Tuple2<>(sum, o1._2 + o2._2);
                    });
            newParamsVector = utils.Vectors.axpy(alpha, tempError._1, startParamsVector);
            error += tempError._2;
            startParamsVector = newParamsVector;
        }
        sc.close();
        return startParamsVector.toArray();
    }
}
