package com.example.mesdeputes;

import java.io.Serializable;

public class Responsability implements Serializable {
    private String organism;
    private String function;
    private String debutFunction;

    public Responsability(String organism, String function, String debutFunction) {
        this.organism = organism;
        this.function = function;
        this.debutFunction = debutFunction;
    }

    // Getters and Setters
    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getDebutFunction() {
        return debutFunction;
    }

    public void setDebutFunction(String debutFunction) {
        this.debutFunction = debutFunction;
    }
}
