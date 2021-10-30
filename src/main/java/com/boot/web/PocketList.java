package com.boot.web;

public class PocketList {
    private int numOne;
    private int numTwo;

    private int numThree;
    private int numFour;

    public PocketList() {
    }

    public PocketList(int numOne, int numTwo, int numThree, int numFour) {
        this.numOne = numOne;
        this.numTwo = numTwo;
        this.numThree = numThree;
        this.numFour = numFour;
    }

    public int getNumOne() {
        return numOne;
    }

    public void setNumOne(int numOne) {
        this.numOne = numOne;
    }

    public int getNumTwo() {
        return numTwo;
    }

    public void setNumTwo(int numTwo) {
        this.numTwo = numTwo;
    }

    public int getNumThree() {
        return numThree;
    }

    public void setNumThree(int numThree) {
        this.numThree = numThree;
    }

    public int getNumFour() {
        return numFour;
    }

    public void setNumFour(int numFour) {
        this.numFour = numFour;
    }
}
