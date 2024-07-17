package com.example.covayapp.chess.AI;


import com.example.covayapp.graphic.Vector;
import com.example.covayapp.chess.PieceInfo;
import com.example.covayapp.chess.PieceStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AIMoveScenario {
    int adjacentNum;
    List<AIMove> aiMoveList = new ArrayList<>();
    int depth;
    List<PieceInfo> listPieceInfo;
    PieceStatus nextTurn;
    int restN;
    int restS;
    HashMap<Vector, PieceInfo> v2pClone;

    public List<AIMove> getAiMoveList() {
        return this.aiMoveList;
    }

    public void setAiMoveList(List<AIMove> aiMoveList2) {
        this.aiMoveList = aiMoveList2;
    }

    public void removeFirstAiMoveList() {
        this.aiMoveList.remove(0);
    }

    public int getRestN() {
        return this.restN;
    }

    public void setRestN(int restN2) {
        this.restN = restN2;
    }

    public int getRestS() {
        return this.restS;
    }

    public void setRestS(int restS2) {
        this.restS = restS2;
    }

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth2) {
        this.depth = depth2;
    }

    public List<PieceInfo> getListPieceInfo() {
        return this.listPieceInfo;
    }

    public void setListPieceInfo(List<PieceInfo> listPieceInfo2) {
        this.listPieceInfo = listPieceInfo2;
    }

    public HashMap<Vector, PieceInfo> getV2pClone() {
        return this.v2pClone;
    }

    public void setV2pClone(HashMap<Vector, PieceInfo> v2pClone2) {
        this.v2pClone = v2pClone2;
    }

    public PieceStatus getNextTurn() {
        return this.nextTurn;
    }

    public void setNextTurn(PieceStatus nextTurnStatus) {
        this.nextTurn = nextTurnStatus;
    }

    public int getAdjacentNum() {
        return this.adjacentNum;
    }

    public void setAdjacentNum(int adjacentNum2) {
        this.adjacentNum = adjacentNum2;
    }
}