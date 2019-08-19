package com.github.phaserush;

import java.util.Iterator;

import static com.github.phaserush.Util.round;

public class WidthGenerator implements Iterable {
    private int startWidth, endWidth;
    private double stepSize;
    private StepMethod method;

    public WidthGenerator(int startWidth, int endWidth, double stepSize, StepMethod method) {
        this.startWidth = startWidth;
        this.endWidth = endWidth;
        this.stepSize = stepSize;
        this.method = method;
    }

    @Override
    public Iterator iterator() {
        return new Generator();
    }

    private class Generator implements Iterator {

        @Override
        public boolean hasNext() {
            return startWidth != endWidth;
        }

        @Override
        public Integer next() {
            switch (method) {
                case ADD:
                    startWidth += stepSize;
                case MULTIPLY:
                    startWidth *= stepSize;
            }
            return round(startWidth);
        }
    }

    enum StepMethod {
        ADD(),
        MULTIPLY()
    }
}
