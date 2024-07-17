package com.example.covayapp.chess.AI;

import androidx.annotation.NonNull;

import com.example.covayapp.graphic.Vector;

public class AIMove {
    private Vector fromVec;
    private Vector toVec;

    @NonNull
    public String toString() {
        return "AIMove " + this.fromVec + "->" + this.toVec + "]";
    }

    public Vector getFromVec() {
        return this.fromVec;
    }

    public void setFromVec(Vector fromVec2) {
        this.fromVec = fromVec2;
    }

    public Vector getToVec() {
        return this.toVec;
    }

    public void setToVec(Vector toVec2) {
        this.toVec = toVec2;
    }

    public AIMove(Vector fromVec2, Vector toVec2) {
        this.fromVec = fromVec2;
        this.toVec = toVec2;
    }

    public boolean equals(AIMove m) {
        if (m.getFromVec().x() == getFromVec().x() && m.getFromVec().y() == getFromVec().y() && m.getToVec().x() == getToVec().x() && m.getToVec().y() == getToVec().y()) {
            return true;
        }
        return false;
    }
}