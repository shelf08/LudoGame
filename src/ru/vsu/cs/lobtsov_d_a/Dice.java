package ru.vsu.cs.lobtsov_d_a;
import java.util.Random;

class Dice {

    // nextInt() gives an int from 0 to 5, adding 1
    // to make it in the range of 1-6
    int roll() {
        return new Random().nextInt(6) + 1;
    }
}
