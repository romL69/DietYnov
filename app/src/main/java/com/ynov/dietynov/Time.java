package com.ynov.dietynov;

import java.io.Serializable;

public class Time implements Serializable {
    private int total;
    private int prep;
    private int baking;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPrep() {
        return prep;
    }

    public void setPrep(int prep) {
        this.prep = prep;
    }

    public int getBaking() {
        return baking;
    }

    public void setBaking(int baking) {
        this.baking = baking;
    }

}
