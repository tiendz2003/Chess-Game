package com.example.covayapp.chess.AI;

public class AIThink {
    int adjacentNum = 0;
    int lastDepthNodeNum = 0;
    int totalRating = 0;

    public int getAdjacentNum() {
        return this.adjacentNum;
    }

    public void setAdjacentNum(int adjacentNum2) {
        this.adjacentNum = adjacentNum2;
    }

    public int getTotalRating() {
        return this.totalRating;
    }

    public void setTotalRating(int totalRating2) {
        this.totalRating = totalRating2;
    }

    public int getLastDepthNodeNum() {
        return this.lastDepthNodeNum;
    }

    public void setLastDepthNodeNum(int lastDepthNodeNum2) {
        this.lastDepthNodeNum = lastDepthNodeNum2;
    }

    public float getAverageRating() {
        return ((float) this.totalRating) / ((float) this.lastDepthNodeNum);
    }
}
