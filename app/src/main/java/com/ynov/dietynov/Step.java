package com.ynov.dietynov;

import java.io.Serializable;

public class Step implements Serializable {
    private int order;
    private String step;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
