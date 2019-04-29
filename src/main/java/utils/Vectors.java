package utils;

import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;

public class Vectors {
    public static Vector scal (double a, Vector vector) {
        double arr[] = vector.toArray();
        for (int i = 0; i < arr.length; i++) {
            arr[i] *= a;
        }
        return new DenseVector(arr);
    }

    public static Vector axpy(double a, Vector x, Vector y){
        double [] arr1 = x.toArray();
        double [] arr2 = y.toArray();
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] += arr1[i] * a;
        }
        return new DenseVector(arr2);
    }
}
