package com.github.phaserush;

import java.util.Arrays;

import static java.lang.Math.abs;
import static java.lang.Math.ceil;

class Util {

    static final double EPSILON = Math.ulp(1.0d);

    /**
     * sum of a range in array
     *
     * @param arr   array
     * @param start start inclusive
     * @param end   end inclusive
     * @return sum from start to end in array
     */
    static double sumRange(double[] arr, int start, int end) {
        return Arrays.stream(arr, start, end + 1).sum();
    }


    /**
     * right divide function from Matlab: https://www.mathworks.com/help/fixedpoint/ref/embedded.fi.rdivide.html
     * note: does not modify array
     *
     * @param arr    array
     * @param scalar scalar
     * @return each element in array divided by scalar
     */
    static double[] rDivide(double[] arr, double scalar) {
        return Arrays.stream(arr).map(d -> d / scalar).toArray();
    }

    /**
     * mean of range in array
     *
     * @param arr   array
     * @param start inclusive start
     * @param end   inclusive end
     * @return mean of the array from start to end
     */
    static double mean(double[] arr, int start, int end) {
        return sumRange(arr, start, end) / (end - start + 1);
    }

    static int round(double d) {
        if ((ceil(abs(d)) - abs(d)) - 0.5d < EPSILON) { // when tie
            return d < 0 ? (int) Math.floor(d) : (int) ceil(d);
        } else return (int) Math.round(d);
    }


    /**
     * print array
     *
     * @param a array to print
     */
    static void print(double[] a) {
        int iMax = a.length - 1;
        if (iMax == -1)
            System.out.println("[]");


        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(a[i]);
            if (i == iMax) {
                System.out.println(b.append(']'));
                return;
            }
            b.append(", ");
        }
    }
}
