package com.example.mesdeputes;

import java.io.Serializable;

public class Vote implements Serializable {
    private String titre;
    private String position;

    public Vote(String titre, String position) {
        this.titre = titre;
        this.position = position;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
