package ru.otus.calculator;

<<<<<<< HEAD
<<<<<<< HEAD
public class Summator {
    private int sum;
    private int prevValue;
    private int prevPrevValue;
    private int sumLastThreeValues;
    private int someValue;
    private int valueCounter;

    //!!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        int value = data.getValue();
        valueCounter++;
        if (valueCounter % 6_600_000 == 0) {
            valueCounter = 0;
        }
        sum += value;

        sumLastThreeValues = value + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = value;

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (value + 1) - sum);
            someValue = Math.abs(someValue) + valueCounter;
        }
    }

    public int getSum() {
        return sum;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public int getPrevPrevValue() {
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public int getSomeValue() {
=======
import java.util.ArrayList;
import java.util.List;

=======
>>>>>>> 078add0 (Optimize the application.)
public class Summator {
    private int sum;
    private int prevValue;
    private int prevPrevValue;
    private int sumLastThreeValues;
    private int someValue;
    private int valueCounter;

    //!!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        int value = data.getValue();
        valueCounter++;
        if (valueCounter % 6_600_000 == 0) {
            valueCounter = 0;
        }
        sum += value;

        sumLastThreeValues = value + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = value;

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (value + 1) - sum);
            someValue = Math.abs(someValue) + valueCounter;
        }
    }

    public int getSum() {
        return sum;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public int getPrevPrevValue() {
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return sumLastThreeValues;
    }

<<<<<<< HEAD
    public Integer getSomeValue() {
>>>>>>> f4feb63 (Set up project.)
=======
    public int getSomeValue() {
>>>>>>> 078add0 (Optimize the application.)
        return someValue;
    }
}
