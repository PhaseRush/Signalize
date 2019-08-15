package com.github.phaserush;

import com.github.phaserush.FastSmooth.Algorithm;
import com.github.phaserush.FastSmooth.EdgeCase;

import static com.github.phaserush.FastSmooth.fastSmooth;
import static com.github.phaserush.Util.round;

public class Signalize {

    public static void main(String[] args) {
        double[] test1 = {1, 1, 1, 10, 10, 10, 1, 1, 1, 1};
        Util.print(fastSmooth(test1, 3, Algorithm.RECTANGULAR, EdgeCase.ZERO));
        Util.print(fastSmooth(test1, 3, Algorithm.TRIANGULAR, EdgeCase.DAMPEN));

        System.out.println(round(-1.5));
        System.out.println(round(1.5));

        System.out.println(Double.doubleToRawLongBits(1.50000000001));
    }
}
