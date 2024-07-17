package com.example.covayapp.chess;

import com.example.covayapp.graphic.Vector;
import java.util.ArrayList;
import java.util.List;

public class PieceInfo {
    private List<Vector> mAdjacentVectorList = new ArrayList<>();
    private PieceStatus mStaus = PieceStatus.NO_SELECT;
    private String mTag = "";
    private Vector mVec;
    private int rp;

    public PieceInfo(Vector v, String tag, int rp2, PieceStatus s, List<Vector> list) {
        this.mVec = v;
        this.mStaus = s;
        this.mAdjacentVectorList = list;
        this.rp = rp2;
        this.mTag = tag;
    }

    public int getRp() {
        return this.rp;
    }

    public Vector getVector() {
        return this.mVec;
    }

    public PieceStatus getCurrentStaus() {
        return this.mStaus;
    }

    public void setCurrentStaus(PieceStatus currentStaus) {
        this.mStaus = currentStaus;
    }

    public List<Vector> getAdjacentVectorList() {
        return this.mAdjacentVectorList;
    }

    public boolean isAdjacentContain(Vector vv) {
        for (Vector v : this.mAdjacentVectorList) {
            if (v.equals(vv)) {
                return true;
            }
        }
        return false;
    }

    public boolean isContainPoint(int x, int y) {
        float r = ((float) this.rp) * 1.5f;
        if (((float) x) <= this.mVec.x() - r || ((float) x) >= this.mVec.x() + r || ((float) y) <= this.mVec.y() - r || ((float) y) >= this.mVec.y() + r) {
            return false;
        }
        return true;
    }

    public String getTag() {
        return this.mTag;
    }
}
