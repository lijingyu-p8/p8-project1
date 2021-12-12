package com.lijingyu.juc.ato;

import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.DoubleBinaryOperator;
import java.util.function.LongBinaryOperator;

public class LongAccumulatorDemo {
    LongAdder longAdder = new LongAdder();
    LongAccumulator longAccumulator = new LongAccumulator(new LongBinaryOperator() {
        @Override
        public long applyAsLong(long left, long right) {
            return left - right;
        }
    }, 1);

    public static void main(String[] args) {
        LongAccumulatorDemo demo = new LongAccumulatorDemo();
        demo.add_LongAccumulator();
        demo.add_LongAccumulator();
        System.out.println(demo.longAccumulator.longValue());
        LongBinaryOperator longBinaryOperator = new LongBinaryOperator() {

            @Override
            public long applyAsLong(long left, long right) {
                return left - right;
            }
        };
        LongAccumulator longAccumulator = new LongAccumulator(longBinaryOperator,1);
        longAccumulator.longValue();
        DoubleBinaryOperator doubleBinaryOperator = new DoubleBinaryOperator() {

            @Override
            public double applyAsDouble(double left, double right) {
                return left - right;
            }
        };
        DoubleAccumulator doubleAccumulator = new DoubleAccumulator(doubleBinaryOperator,0);
        doubleAccumulator.doubleValue();
    }

    public void add_LongAdder() {
        longAdder.increment();
    }

    public void add_LongAccumulator() {
        longAccumulator.accumulate(1);
    }
}