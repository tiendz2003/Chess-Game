package com.example.covayapp.chess;


import java.util.ArrayList;

public class HistoryItem {
    private PieceStatus currentTurn;
    private ArrayList<PieceInfo> mPieceList = new ArrayList<>();
    private int restCountN = 0;
    private int restCountS = 0;

    public ArrayList<PieceInfo> getmPieceList() {
        return this.mPieceList;
    }

    public void setPieceList(ArrayList<PieceInfo> mPieceList2) {
        this.mPieceList = mPieceList2;
    }

    public PieceStatus getCurrentTurn() {
        return this.currentTurn;
    }

    public void setCurrentTurn(PieceStatus currentTurn2) {
        this.currentTurn = currentTurn2;
    }

    public int getRestCountS() {
        return this.restCountS;
    }

    public void setRestCountS(int restCountS2) {
        this.restCountS = restCountS2;
    }

    public int getRestCountN() {
        return this.restCountN;
    }

    public void setRestCountN(int restCountN2) {
        this.restCountN = restCountN2;
    }
}
