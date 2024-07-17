package com.example.covayapp.chess;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

public class Record {
    private static String getPlayModeStr(PlayMode pm) {
        if (pm == PlayMode.SINGLE_0) {
            return "single0";
        }
        if (pm == PlayMode.SINGLE_1) {
            return "single1";
        }
        if (pm == PlayMode.SINGLE_2) {
            return "single2";
        }
        return null;
    }

    public static RecordDetail getRecord(Context c, PlayMode pm) {
        int w;
        int l;
        int g;
        String recordDetailStr = c.getSharedPreferences(c.getPackageName(), 0).getString(getPlayModeStr(pm), (String) null);
        if (recordDetailStr == null) {
            return new RecordDetail(0, 0, 0);
        }
        try {
            JSONObject jObject = new JSONObject(recordDetailStr);
            try {
                w = jObject.getInt("win");
            } catch (JSONException e) {
                w = 0;
            }
            try {
                l = jObject.getInt("lose");
            } catch (JSONException e2) {
                l = 0;
            }
            try {
                g = jObject.getInt("giveup");
            } catch (JSONException e3) {
                g = 0;
            }
            return new RecordDetail(w, l, g);
        } catch (JSONException e4) {
            return new RecordDetail(0, 0, 0);
        }
    }

    public static void saveRecord(Context c, PlayMode pm, RecordDetail rd) {
        c.getSharedPreferences(c.getPackageName(), 0).edit().putString(getPlayModeStr(pm),
                String.format("{\"win\":%d, \"lost\":%d, \"giveup\":%d}", new Object[]{Integer.valueOf(rd.win), Integer.valueOf(rd.lose), Integer.valueOf(rd.giveup)})).commit();
    }
}