package com.github.phaserush;


import java.util.Arrays;
import java.util.Iterator;

import static com.github.phaserush.Util.*;

class FastSmooth {

    /**
     * Smooths the input array
     *
     * @param input     array to smooth
     * @param generator generator function to determine window widths
     * @param edgeCase  how to handle edges (see enum decl)
     * @return smoothed input array
     */
    static double[] fastSmooth(double[] input, WidthGenerator generator, EdgeCase edgeCase) {
        Iterator iter = generator.iterator();
        if (!iter.hasNext()) return input; // none to do

        int currWidth = (int) iter.next();
        double[] arr = Arrays.copyOf(input, input.length);
        while (iter.hasNext()) {
            arr = impl(input, currWidth, edgeCase);
            currWidth = (int) iter.next();
        }

        return arr;
    }

    /**
     * Smooths input array with a window, transcribed from matlab
     *
     * @param input     input array
     * @param width     width of smoothing window
     * @param algorithm determines which smoothing algorithm is used:
     * @param edgeCase  determines how edge cases (first and last width/2 points)
     * @return smoothed input array
     */
    static double[] fastSmooth(double[] input, int width, Algorithm algorithm, EdgeCase edgeCase) {
        switch (algorithm) {
            case RECTANGULAR:
                return impl(input, width, edgeCase);
            case TRIANGULAR:
                return impl(impl(input, width, edgeCase), width, edgeCase);
            case P_GAUSSIAN_3:
                return impl(impl(impl(input, width, edgeCase), width, edgeCase), width, edgeCase);
            case P_GAUSSIAN_4:
                return impl(impl(impl(impl(input, width, edgeCase), width, edgeCase), width, edgeCase), width, edgeCase);
            case MULTIPLE_WIDTH:
            default:
                return impl(impl(impl(impl(input, (int) Math.round(width * 1.6), edgeCase), (int) Math.round(width * 1.4), edgeCase), (int) Math.round(width * 1.2), edgeCase), width, edgeCase);
        }
    }

//    static double[] recurse(double[] input, int width, int passes, EdgeCase edgeCase) {
//
//    }

    /**
     * internal helper method for single pass smoothing
     *
     * @param input    input array
     * @param width    smoothing window width
     * @param edgeCase determines how edge cases (first and last width/2 points)
     * @return single pass smoothed input
     */
    private static double[] impl(double[] input, int width, EdgeCase edgeCase) {
        double sum = sumRange(input, 0, width - 1);
        double[] s = new double[input.length];
        int halfw = round(width / 2);
        int length = input.length;

        int k; // need scope
        for (k = 0; k <= length - width - 1; k++) { // 0 to 6 inclusive
            s[k + halfw] = sum;
            sum -= input[k]; // update running window
            sum += input[k + width];
        }

        s[k + halfw] = sumRange(input, length - width, length - 1); // set off by 1 value of end of mid

        double[] smoothY = rDivide(s, width); // undo width scalar

        switch (edgeCase) { // technically dont need a switch here but this supports more edge types for future expansion
            case ZERO:
                return smoothY;
            case DAMPEN:
            default:
                int startPoint = round((width + 1) / 2); // need round?
                smoothY[0] = (input[0] + input[1]) / 2; // determine first
                for (int i = 1; i <= startPoint - 1; i++) { // determine ranges
                    smoothY[i] = mean(input, 0, 2 * i - 2); // 2 * k - 1 -1 but we need INCLUSIVE end so +1 = 2 * k -1 -1 + 1
                    smoothY[length - i] = mean(input, length - 2 * i + 2, length - 1);
                }
                smoothY[length - 1] = (input[length - 1] + input[length - 2]) / 2; // determine last
                return smoothY;
        }
    }


    /**
     * RECTANGULAR: sliding average
     * TRIANGULAR: 2 passes of sliding average
     * pseudo-Gaussian-3: 3 passes of sliding average
     * pseudo-Gaussian-4: 4 passes of sliding average
     * multiple-width: 4 passes of different sliding-average
     */
    enum Algorithm {
        RECTANGULAR(),
        TRIANGULAR(),
        P_GAUSSIAN_3(),
        P_GAUSSIAN_4(),
        MULTIPLE_WIDTH()
    }

    /**
     * ZERO: ends are zero
     * DAMPEN: ends are smoothed with progressively smaller smooths the closer to the end
     */
    enum EdgeCase {
        ZERO(),
        DAMPEN()
    }

}
