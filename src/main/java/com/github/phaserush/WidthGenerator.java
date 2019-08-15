package com.github.phaserush;

import java.util.Iterator;

import static com.github.phaserush.Util.round;

public class WidthGenerator<T extends Integer> implements Iterable<T> {
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
    public Iterator<T> iterator() {
        return new Generator<>();
    }

    private class Generator<K extends Integer> implements Iterator<K> {

        @Override
        public boolean hasNext() {
            return startWidth != endWidth;
        }

        @Override
        public K next() {
            switch (method) {
                case ADD:
                    startWidth += stepSize;
                case MULTIPLY:
                    startWidth *= stepSize;
            }
            return (K) new Integer(round(startWidth));
        }
    }

    enum StepMethod {
        ADD(),
        MULTIPLY()
    }
}
