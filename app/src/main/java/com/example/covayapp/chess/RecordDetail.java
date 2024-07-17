package com.example.covayapp.chess;


public class RecordDetail {
    int giveup;
    int lose;
    int win;

    public RecordDetail(int win2, int lose2, int giveup2) {
        this.win = win2;
        this.lose = lose2;
        this.giveup = giveup2;
    }

    /* access modifiers changed from: package-private */
    public void addWin() {
        this.win++;
    }

    /* access modifiers changed from: package-private */
    public void addLose() {
        this.lose++;
    }

    /* access modifiers changed from: package-private */
    public void addGiveup() {
        this.giveup++;
    }

    public String toString() {
        return "RecordDetail [win=" + this.win + ", lose=" + this.lose + ", giveup=" + this.giveup + "]";
    }
}
