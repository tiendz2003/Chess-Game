package com.example.covayapp.chess;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.customview.widget.ExploreByTouchHelper;

import com.example.covayapp.R;
import com.example.covayapp.graphic.Circle;
import com.example.covayapp.graphic.CircleIntersection;
import com.example.covayapp.graphic.Vector;
import com.example.covayapp.chess.AI.AIMove;
import com.example.covayapp.chess.AI.AIMoveScenario;
import com.example.covayapp.chess.AI.AIThink;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint({"DefaultLocale"})
public class BoardView extends View {
    private Timer timer;
    private long startTime;
    private long timeRemaining = TURN_DURATION;
    private CountDownTimer countDownTimer;
    private static final long TURN_DURATION = 15000;
    private final List<AIMoveScenario> aiScenarioList;
    private Thread aiThread;
    private Vector c1;
    private Vector c4;
    private Vector c6;
    private Vector c7;
    /* access modifiers changed from: private */
    public PlayMode currentPlayMode;
    private PieceInfo currentSelectingPiece;
    private PieceInfo currentTempTargetPiece;
    /* access modifiers changed from: private */
    public PieceStatus currentTurn;
    private int depthAIThink;
    /* access modifiers changed from: private */
    public Handler handler;
    Stack<HistoryItem> historyOfPieceList;
    private boolean isInit;
    private boolean isRegreting;
    private AIMove lastAIMove;
    private AIMove lastNMove;
    private AIMove lastSMove;
    Bitmap mBitmapColon;
    List<Bitmap> mBitmapNumList;
    private int mCurrX;
    private int mCurrY;
    private MediaPlayer mpMove;
    private MediaPlayer mpSelect;
    private Vector n1;
    private Vector n2;
    private Vector n3;
    private Vector n4;
    private Vector n5;
    private Vector n6;
    private Vector n7;
    private Paint paintBackground;
    private Paint paintBoard;
    private Paint paintN;
    private Paint paintNS;
    private Paint paintNT;
    private Paint paintNoSelect;
    private Paint paintS;
    private Paint paintSS;
    private Paint paintST;
    private Paint paintText;
    private BoardActivity parentActivity;
    ArrayList<PieceInfo> pieceList;
    private int rb;
    private RectF recs1;
    private RectF recs2;
    private RectF recs3;
    private RectF recs4;
    private int restCountN;
    private int restCountS;
    private int rp;
    private int rs;
    private Vector s1;
    private Vector s2;
    private Vector s3;
    private Vector s4;
    private Vector s5;
    private Vector s6;
    private Vector s7;
    private int screenHeight;
    private float startAngel1;
    private float startAngel2;
    private float startAngel3;
    private float startAngel4;

    private final int strokeWidthOfSelecting;
    private double sweepAngle;
    private Vector timeInfoVec;
    private final TimerTask timeTask;

    /* access modifiers changed from: private */
    public Handler timerHandler;
    private Vector turnInfoVec;
    HashMap<Vector, PieceInfo> v2p;
    /* access modifiers changed from: private */
    public PieceStatus winner;

    public PlayMode getCurrentPlayMode() {
        return this.currentPlayMode;
    }

    public Stack<HistoryItem> getHistoryOfPieceList() {
        return this.historyOfPieceList;
    }

    public void setParentActivity(BoardActivity a) {
        this.parentActivity = a;
    }

/*    public Bitmap getBitmapWithTransparentBG(Bitmap srcBitmap, int bgColor) {
        Bitmap result = srcBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int nWidth = result.getWidth();
        int nHeight = result.getHeight();
        for (int y = 0; y < nHeight; y++) {
            for (int x = 0; x < nWidth; x++) {
                if (result.getPixel(x, y) == bgColor) {
                    result.setPixel(x, y, 0);
                }
            }
        }
        return result;
    }*/
    private static class BoardHandler extends Handler {
        private final WeakReference<BoardView> boardViewWeakReference;

        BoardHandler(BoardView boardView) {
            boardViewWeakReference = new WeakReference<>(boardView);
        }
        @Override
        public void handleMessage(@NonNull  Message msg) {
            BoardView boardView = boardViewWeakReference.get();
            if (boardView != null) {
                Log.i(TAG, "handleMessage");
                boardView.invalidate();
            }
        }
    }
    private static class TimerHandler extends Handler {
        private final WeakReference<BoardView> boardViewWeakReference;

        TimerHandler(BoardView boardView) {
            boardViewWeakReference = new WeakReference<>(boardView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BoardView boardView = boardViewWeakReference.get();
            if (boardView != null) {
                boardView.invalidate();
            }
        }
    }
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mBitmapNumList = new ArrayList<>();
        this.v2p = new HashMap<>();
        this.pieceList = new ArrayList<>();
        this.historyOfPieceList = new Stack<>();
        this.aiScenarioList = new ArrayList<>();
        this.currentPlayMode = PlayMode.SINGLE_0;
        this.depthAIThink = 1;
        this.mCurrX = 0;
        this.mCurrY = 0;
        this.strokeWidthOfSelecting = 10;
        this.currentTurn = null;
        this.currentSelectingPiece = null;
        this.currentTempTargetPiece = null;
        this.winner = null;
        this.restCountN = 0;
        this.restCountS = 0;
        this.lastAIMove = null;
        this.lastSMove = null;
        this.lastNMove = null;
        this.isInit = false;
        this.isRegreting = false;
        this.parentActivity = null;
        this.mpMove = null;
        this.mpSelect = null;
        this.aiThread = null;
        this.handler = new BoardHandler(this);

        this.timer = null;
        this.timerHandler = new TimerHandler(this);

        this.timeTask = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1;
                BoardView.this.timerHandler.sendMessage(message);
            }
        };
        this.startTime = SystemClock.elapsedRealtime();
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateTimer();
                    }
                });
            }
        }, 0, 1000);
        setSoundEffectsEnabled(true);
        this.mpMove = MediaPlayer.create(getContext(), R.raw.piece_move);
        this.mpSelect = MediaPlayer.create(getContext(), R.raw.piece_select);
    }

    public BoardView(Context context) {
        super(context);
        this.mBitmapNumList = new ArrayList<>();
        this.v2p = new HashMap<>();
        this.pieceList = new ArrayList<>();
        this.historyOfPieceList = new Stack<>();
        this.aiScenarioList = new ArrayList<>();
        this.currentPlayMode = PlayMode.SINGLE_0;
        this.depthAIThink = 1;
        this.mCurrX = 0;
        this.mCurrY = 0;
        this.strokeWidthOfSelecting = 10;
        this.currentTurn = null;
        this.currentSelectingPiece = null;
        this.currentTempTargetPiece = null;
        this.winner = null;
        this.restCountN = 0;
        this.restCountS = 0;
        this.lastAIMove = null;
        this.lastSMove = null;
        this.lastNMove = null;
        this.isInit = false;
        this.isRegreting = false;
        this.parentActivity = null;
        this.mpMove = null;
        this.mpSelect = null;
        this.aiThread = null;
        this.handler = new BoardHandler(this);

        this.timer = null;
        this.timerHandler = new TimerHandler(this);

        this.timeTask = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1;
                BoardView.this.timerHandler.sendMessage(message);
            }
        };
    }

    public void setCurrentPlayMode(PlayMode currentPlayMode2) {
        this.currentPlayMode = currentPlayMode2;
    }

    public class AIThread implements Runnable {
        BoardView bv;

        public AIThread(BoardView boardView) {
            this.bv = boardView;
        }

        public void run() {
            boolean running= true;
            Log.i(GV.TAG, "run AIThread");
            while (running) {
                if (BoardView.this.currentPlayMode != PlayMode.DOUBLE && BoardView.this.currentTurn == PieceStatus.N_SIDE && BoardView.this.winner == null) {
                    Message message = Message.obtain();
                    this.bv.moveByAI();
                    BoardView.this.handler.sendMessage(message);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    running= false;
                }
            }
        }
    }

    private double getAngle(Point center, Point px, Point py) {
        float v1x = (float) (px.x - center.x);
        float v1y = (float) (px.y - center.y);
        float l1 = (float) Math.sqrt((double) ((v1x * v1x) + (v1y * v1y)));
        float v2x = (float) (py.x - center.x);
        float v2y = (float) (py.y - center.y);
        float l2 = (float) Math.sqrt((double) ((v2x * v2x) + (v2y * v2y)));
        return Math.toDegrees(Math.acos((double) (((v1x / l1) * (v2x / l2)) + ((v1y / l1) * (v2y / l2)))));
    }

    public void regret() {
        if (!this.historyOfPieceList.empty() && !this.isRegreting) {
            this.historyOfPieceList.pop();
            this.isRegreting = true;
        }
        if (!this.historyOfPieceList.empty()) {
            HistoryItem hi = this.historyOfPieceList.pop();
            this.pieceList = hi.getmPieceList();
            this.currentTurn = hi.getCurrentTurn();
            this.restCountN = hi.getRestCountN();
            this.restCountS = hi.getRestCountS();
            invalidate();
            return;
        }
        init(this.currentPlayMode);
    }

    public void init() {
        init(this.currentPlayMode);

    }

    public void init(PlayMode mode) {
        this.currentPlayMode = mode;
        if (this.currentPlayMode == PlayMode.SINGLE_0) {
            this.depthAIThink = 1;
        } else if (this.currentPlayMode == PlayMode.SINGLE_1) {
            this.depthAIThink = 3;
        } else if (this.currentPlayMode == PlayMode.SINGLE_2) {
            this.depthAIThink = 3;
        }
        this.winner = null;
        this.restCountN = 6;
        this.restCountS = 6;
        int screenWidth = getWidth();
        this.screenHeight = getHeight();
        this.currentTurn = PieceStatus.S_SIDE;
        this.pieceList.clear();
        this.historyOfPieceList.clear();
        this.v2p.clear();
        this.aiScenarioList.clear();
        this.paintText = new Paint();
        this.paintText.setStyle(Paint.Style.FILL);
        this.paintText.setColor(-1);
        this.paintText.setTextSize(50.0f);
        this.paintBackground = new Paint();
        this.paintBackground.setStyle(Paint.Style.FILL);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.REPEAT);
        paintBackground.setShader(shader);
        this.paintBoard = new Paint();
        this.paintBoard.setStyle(Paint.Style.STROKE);
        this.paintBoard.setPathEffect(new CornerPathEffect(10.0f));
        this.paintBoard.setColor(Color.GRAY);
        this.paintBoard.setStrokeWidth(6.0f);
        this.paintBoard.setAntiAlias(true);
        this.paintNoSelect = new Paint();
        this.paintNoSelect.setStyle(Paint.Style.STROKE);
        this.paintNoSelect.setColor(-1);
        this.paintN = new Paint();
        this.paintN.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paintN.setColor(Color.WHITE);
        this.paintNS = new Paint();
        this.paintNS.setStyle(Paint.Style.STROKE);
        this.paintNS.setStrokeWidth((float) this.strokeWidthOfSelecting);
        this.paintNS.setColor(-1);
        this.paintNT = new Paint();
        this.paintNT.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paintNT.setStrokeWidth((float) this.strokeWidthOfSelecting);
        this.paintNT.setColor(-16776961);
        this.paintS = new Paint();
        this.paintS.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paintS.setColor(Color.BLACK);
        this.paintSS = new Paint();
        this.paintSS.setStyle(Paint.Style.STROKE);
        this.paintSS.setStrokeWidth((float) this.strokeWidthOfSelecting);
        this.paintSS.setColor(-1);
        this.paintST = new Paint();
        this.paintST.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paintST.setStrokeWidth((float) this.strokeWidthOfSelecting);
        this.paintST.setColor(-16711936);
        this.rb = ((screenWidth / 2) * 6) / 7;
        this.rs = (this.rb) / 3;
        this.rp = (this.rs) / 3;
        this.c1 = new Vector((double) (screenWidth / 2), (double) (this.screenHeight / 2));
        this.c6 = new Vector((double) (this.c1.x() - ((float) this.rb)), (double) this.c1.y());
        this.c7 = new Vector((double) (this.c1.x() + ((float) this.rb)), (double) this.c1.y());
        this.n1 = new Vector((double) this.c1.x(), (double) (this.c1.y() - ((float) this.rb)));
        this.s1 = new Vector((double) this.c1.x(), (double) (this.c1.y() + ((float) this.rb)));
        Vector c2 = new Vector((double) (this.c1.x() - ((float) this.rs)), (double) this.c1.y());
        Vector c3 = new Vector((double) (this.c1.x() + ((float) this.rs)), (double) this.c1.y());
        this.c4 = new Vector((double) (this.c6.x() + ((float) this.rs)), (double) this.c1.y());
        Vector c5 = new Vector((double) (this.c7.x() - ((float) this.rs)), (double) this.c1.y());
        this.n6 = new Vector((double) this.c1.x(), (double) (this.n1.y() + ((float) this.rs)));
        this.s6 = new Vector((double) this.c1.x(), (double) (this.s1.y() - ((float) this.rs)));
        this.n7 = new Vector((double) this.c1.x(), (double) (this.c1.y() - ((float) this.rs)));
        this.s7 = new Vector((double) this.c1.x(), (double) (this.c1.y() + ((float) this.rs)));
        Circle circle = new Circle(this.c1, (double) this.rb);
        Circle circle2 = new Circle(this.n1, (double) this.rs);
        this.recs1 = new RectF(this.n1.x() - ((float) this.rs), this.n1.y() - ((float) this.rs), this.n1.x() + ((float) this.rs), this.n1.y() + ((float) this.rs));
        Circle circle3 = new Circle(this.s1, (double) this.rs);
        this.recs2 = new RectF(this.s1.x() - ((float) this.rs), this.s1.y() - ((float) this.rs), this.s1.x() + ((float) this.rs), this.s1.y() + ((float) this.rs));
        Circle circle4 = new Circle(this.c6, (double) this.rs);
        this.recs3 = new RectF(this.c6.x() - ((float) this.rs), this.c6.y() - ((float) this.rs), this.c6.x() + ((float) this.rs), this.c6.y() + ((float) this.rs));
        Circle circle5 = new Circle(this.c7, (double) this.rs);
        this.recs4 = new RectF(this.c7.x() - ((float) this.rs), this.c7.y() - ((float) this.rs), this.c7.x() + ((float) this.rs), this.c7.y() + ((float) this.rs));
        Vector[] va1 = new CircleIntersection(circle, circle2).getIntersectionPoints();
        this.n2 = va1[0];
        this.n3 = va1[1];
        this.sweepAngle = getAngle(new Point((int) this.n1.x(), (int) this.n1.y()), new Point((int) this.n2.x(), (int) this.n2.y()), new Point((int) this.n3.x(), (int) this.n3.y()));
        float sweepAngleRest = (float) ((180.0d - this.sweepAngle) / 2.0d);
        this.startAngel1 = sweepAngleRest;
        Vector[] va2 = new CircleIntersection(circle, circle3).getIntersectionPoints();
        this.s2 = va2[0];
        this.s3 = va2[1];
        this.startAngel2 = 180.0f + sweepAngleRest;
        Vector[] va3 = new CircleIntersection(circle, circle4).getIntersectionPoints();
        this.n4 = va3[0];
        this.s4 = va3[1];
        this.startAngel3 = 270.0f + sweepAngleRest;
        Vector[] va4 = new CircleIntersection(circle, circle5).getIntersectionPoints();
        this.n5 = va4[0];
        this.s5 = va4[1];
        this.startAngel4 = 90.0f + sweepAngleRest;
        PieceInfo pc1 = new PieceInfo(this.c1, "c1", this.rp, PieceStatus.NO_SELECT, Arrays.asList(new Vector[]{c2, c3, this.s7, this.n7}));
        this.pieceList.add(pc1);
        this.v2p.put(this.c1, pc1);
        PieceInfo pc2 = new PieceInfo(c2, "c2", this.rp, PieceStatus.NO_SELECT, Arrays.asList(new Vector[]{this.c1, this.c4, this.s7, this.n7}));
        this.pieceList.add(pc2);
        this.v2p.put(c2, pc2);
        PieceInfo pc3 = new PieceInfo(c3, "c3", this.rp, PieceStatus.NO_SELECT, Arrays.asList(new Vector[]{this.c1, c5, this.s7, this.n7}));
        this.pieceList.add(pc3);
        this.v2p.put(c3, pc3);
        PieceInfo pc4 = new PieceInfo(this.c4, "c4", this.rp, PieceStatus.NO_SELECT, Arrays.asList(new Vector[]{c2, this.c6, this.n4, this.s4}));
        this.pieceList.add(pc4);
        this.v2p.put(this.c4, pc4);
        PieceInfo pc5 = new PieceInfo(c5, "c5", this.rp, PieceStatus.NO_SELECT, Arrays.asList(new Vector[]{c3, this.c7, this.n5, this.s5}));
        this.pieceList.add(pc5);
        this.v2p.put(c5, pc5);
        PieceInfo pc6 = new PieceInfo(this.c6, "c6", this.rp, PieceStatus.NO_SELECT, Arrays.asList(new Vector[]{this.c4, this.s4, this.n4}));
        this.pieceList.add(pc6);
        this.v2p.put(this.c6, pc6);
        PieceInfo pc7 = new PieceInfo(this.c7, "c7", this.rp, PieceStatus.NO_SELECT, Arrays.asList(new Vector[]{c5, this.s5, this.n5}));
        this.pieceList.add(pc7);
        this.v2p.put(this.c7, pc7);
        PieceInfo pn1 = new PieceInfo(this.n1, "n1", this.rp, PieceStatus.N_SIDE, Arrays.asList(new Vector[]{this.n2, this.n3, this.n6}));
        this.pieceList.add(pn1);
        this.v2p.put(this.n1, pn1);
        PieceInfo pn2 = new PieceInfo(this.n2, "n2", this.rp, PieceStatus.N_SIDE, Arrays.asList(new Vector[]{this.n1, this.n4, this.n6}));
        this.pieceList.add(pn2);
        this.v2p.put(this.n2, pn2);
        PieceInfo pn3 = new PieceInfo(this.n3, "n3", this.rp, PieceStatus.N_SIDE, Arrays.asList(new Vector[]{this.n1, this.n5, this.n6}));
        this.pieceList.add(pn3);
        this.v2p.put(this.n3, pn3);
        PieceInfo pn4 = new PieceInfo(this.n4, "n4", this.rp, PieceStatus.N_SIDE, Arrays.asList(new Vector[]{this.c4, this.c6, this.n2}));
        this.pieceList.add(pn4);
        this.v2p.put(this.n4, pn4);
        PieceInfo pn5 = new PieceInfo(this.n5, "n5", this.rp, PieceStatus.N_SIDE, Arrays.asList(new Vector[]{c5, this.c7, this.n3}));
        this.pieceList.add(pn5);
        this.v2p.put(this.n5, pn5);
        PieceInfo pn6 = new PieceInfo(this.n6, "n6", this.rp, PieceStatus.N_SIDE, Arrays.asList(new Vector[]{this.n1, this.n2, this.n3, this.n7}));
        this.pieceList.add(pn6);
        this.v2p.put(this.n6, pn6);
        PieceInfo pn7 = new PieceInfo(this.n7, "n7", this.rp, PieceStatus.NO_SELECT, Arrays.asList(new Vector[]{this.c1, c2, c3, this.n6}));
        this.pieceList.add(pn7);
        this.v2p.put(this.n7, pn7);
        PieceInfo ps1 = new PieceInfo(this.s1, "s1", this.rp, PieceStatus.S_SIDE, Arrays.asList(new Vector[]{this.s2, this.s3, this.s6}));
        this.pieceList.add(ps1);
        this.v2p.put(this.s1, ps1);
        PieceInfo ps2 = new PieceInfo(this.s2, "s2", this.rp, PieceStatus.S_SIDE, Arrays.asList(new Vector[]{this.s1, this.s4, this.s6}));
        this.pieceList.add(ps2);
        this.v2p.put(this.s2, ps2);
        PieceInfo ps3 = new PieceInfo(this.s3, "s3", this.rp, PieceStatus.S_SIDE, Arrays.asList(new Vector[]{this.s1, this.s5, this.s6}));
        this.pieceList.add(ps3);
        this.v2p.put(this.s3, ps3);
        PieceInfo ps4 = new PieceInfo(this.s4, "s4", this.rp, PieceStatus.S_SIDE, Arrays.asList(new Vector[]{this.c4, this.c6, this.s2}));
        this.pieceList.add(ps4);
        this.v2p.put(this.s4, ps4);
        PieceInfo ps5 = new PieceInfo(this.s5, "s5", this.rp, PieceStatus.S_SIDE, Arrays.asList(new Vector[]{c5, this.c7, this.s3}));
        this.pieceList.add(ps5);
        this.v2p.put(this.s5, ps5);
        PieceInfo ps6 = new PieceInfo(this.s6, "s6", this.rp, PieceStatus.S_SIDE, Arrays.asList(new Vector[]{this.s1, this.s2, this.s3, this.s7}));
        this.pieceList.add(ps6);
        this.v2p.put(this.s6, ps6);
        PieceInfo ps7 = new PieceInfo(this.s7, "s7", this.rp, PieceStatus.NO_SELECT, Arrays.asList(new Vector[]{this.c1, c2, c3, this.s6}));
        this.pieceList.add(ps7);
        this.v2p.put(this.s7, ps7);
        this.turnInfoVec = new Vector((double) ((screenWidth / 2) - this.rb), (double) (((this.screenHeight / 2) - this.rb) / 2));
        this.timeInfoVec = new Vector((double) (screenWidth - 200), (double) this.turnInfoVec.y());
        this.startTime = SystemClock.elapsedRealtime();
        if (this.aiThread == null) {
            this.aiThread = new Thread(new AIThread(this));
            this.aiThread.start();
        }
        startTurnTimer();
        invalidate();
    }
    //Vẽ thời gian đếm ngược cho mỗi người chơi
    private void drawElapsedTime(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(0xFF000000); // Màu đen
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Thời gian còn lại: " + timeRemaining / 1000 + "s", (float) getWidth() / 2, (float) getHeight() / 10, textPaint);
    }
    private void updateTimer() {
        long elapsedTime = SystemClock.elapsedRealtime() - startTime;
        timeRemaining = TURN_DURATION - (elapsedTime % TURN_DURATION);
        if (timeRemaining <= 0) {
            switchTurns();
            startTime = SystemClock.elapsedRealtime();
            timeRemaining = TURN_DURATION;
        }
        invalidate();
    }
    private void startTurnTimer() {
        countDownTimer = new CountDownTimer(TURN_DURATION, 1000) {
            public void onTick(long millisUntilFinished) {
                // Update UI with the remaining time if needed
                Log.d("BoardView", "Thời gian sắp hết: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                // Hết thời gian,người chơi tiếp theo đánh
                switchTurns();
                startTurnTimer(); // Bắt đầu thời gian cho người tiếp theo
            }
        }.start();
    }
    private void resetTurnTimer() {
        countDownTimer.cancel();
        startTurnTimer();
    }

    //-----------------------------------------------------------------------------------------------
    /* access modifiers changed from: protected */
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (!this.isInit) {
            init(this.currentPlayMode);
            this.isInit = true;
        }

        canvas.drawPaint(this.paintBackground);
        drawElapsedTime(canvas);
        if (this.currentTurn == PieceStatus.N_SIDE) {
            canvas.drawCircle(this.turnInfoVec.x(), this.turnInfoVec.y(), (float) this.rp, this.paintN);
        } else {
            canvas.drawCircle(this.turnInfoVec.x(), this.turnInfoVec.y(), (float) this.rp, this.paintS);
        }
        canvas.drawCircle(this.c1.x(), this.c1.y(), (float) this.rb, this.paintBoard);
        canvas.drawCircle(this.c1.x(), this.c1.y(), (float) this.rs, this.paintBoard);
        canvas.drawLine(this.c6.x(), this.c6.y(), this.c7.x(), this.c7.y(), this.paintBoard);
        canvas.drawLine(this.n1.x(), this.n1.y(), this.s1.x(), this.s1.y(), this.paintBoard);
        canvas.drawArc(this.recs1, this.startAngel1, (float) this.sweepAngle, false, this.paintBoard);
        canvas.drawArc(this.recs2, this.startAngel2, (float) this.sweepAngle, false, this.paintBoard);
        canvas.drawArc(this.recs3, this.startAngel3, (float) this.sweepAngle, false, this.paintBoard);
        canvas.drawArc(this.recs4, this.startAngel4, (float) this.sweepAngle, false, this.paintBoard);
        for (PieceInfo pi : this.pieceList) {
            if (pi.getCurrentStaus() != PieceStatus.NO_SELECT) {
                if (pi.getCurrentStaus() == PieceStatus.N_SIDE) {
                    canvas.drawCircle(pi.getVector().x(), pi.getVector().y(), (float) this.rp, this.paintN);
                } else if (pi.getCurrentStaus() == PieceStatus.S_SIDE) {
                    canvas.drawCircle(pi.getVector().x(), pi.getVector().y(), (float) this.rp, this.paintS);
                } else if (pi.getCurrentStaus() == PieceStatus.N_SELECTING) {
                    canvas.drawCircle(pi.getVector().x(), pi.getVector().y(), (float) this.rp, this.paintNS);
                    canvas.drawCircle(pi.getVector().x(), pi.getVector().y(), (float) this.rp, this.paintN);
                } else if (pi.getCurrentStaus() == PieceStatus.S_SELECTING) {
                    canvas.drawCircle(pi.getVector().x(), pi.getVector().y(), (float) this.rp, this.paintSS);
                    canvas.drawCircle(pi.getVector().x(), pi.getVector().y(), (float) this.rp, this.paintS);
                } else if (pi.getCurrentStaus() == PieceStatus.N_TEMP) {
                    canvas.drawCircle(pi.getVector().x(), pi.getVector().y(), (float) this.rp, this.paintNT);
                    canvas.drawCircle(pi.getVector().x(), pi.getVector().y(), (float) this.rp, this.paintN);
                } else if (pi.getCurrentStaus() == PieceStatus.S_TEMP) {
                    canvas.drawCircle(pi.getVector().x(), pi.getVector().y(), (float) this.rp, this.paintST);
                    canvas.drawCircle(pi.getVector().x(), pi.getVector().y(), (float) this.rp, this.paintS);
                }
            }
        }
    }

    private void generateAIMoveScenarios(int totalDepth, int depth, HashMap<Vector, PieceInfo> vec2PieceInfo, List<PieceInfo> pieceList4AI, PieceStatus currentTurnStatus, List<AIMove> alreadyMoveList) {
        PieceStatus nextTurnStatus;
        Log.d(GV.TAG, String.format("generateAIMoveScenarios is called, %d depth", new Object[]{depth}));
        if (depth != 0) {
            for (PieceInfo pi : pieceList4AI) {
                Log.d(GV.TAG, String.format("start to check %s", new Object[]{pi.getTag()}));
                if (pi.getCurrentStaus() == currentTurnStatus) {
                    Log.d(GV.TAG, String.format("find %s is currentTurnStatus", new Object[]{pi.getTag()}));
                    Vector vFrom = pi.getVector();
                    for (Vector vTo : pi.getAdjacentVectorList()) {
                        Log.d(GV.TAG, String.format("check adjacent %s", new Object[]{vec2PieceInfo.get(vTo).getTag()}));
                        if (vec2PieceInfo.get(vTo).getCurrentStaus() == PieceStatus.NO_SELECT) {
                            Log.d(GV.TAG, String.format("%d, from %s to %s", new Object[]{Integer.valueOf(depth), vec2PieceInfo.get(vFrom).getTag(), vec2PieceInfo.get(vTo).getTag()}));
                            HashMap<Vector, PieceInfo> v2pClone = new HashMap<>();
                            ArrayList<PieceInfo> arrayList = new ArrayList<>();
                            Vector vFrom2 = null;
                            Vector vTo2 = null;
                            for (Map.Entry<Vector, PieceInfo> entry : vec2PieceInfo.entrySet()) {
                                Vector v2 = entry.getKey();
                                PieceInfo p2 = entry.getValue();
                                Vector vector = new Vector(v2.x, v2.y);
                                if (vFrom2 == null && vector.equals(vFrom)) {
                                    vFrom2 = vector;
                                }
                                if (vTo2 == null && vector.equals(vTo)) {
                                    vTo2 = vector;
                                }
                                PieceInfo piClone = new PieceInfo(p2.getVector(), p2.getTag(), p2.getRp(), p2.getCurrentStaus(), p2.getAdjacentVectorList());
                                arrayList.add(piClone);
                                v2pClone.put(vector, piClone);
                            }
                            v2pClone.get(vTo2).setCurrentStaus(currentTurnStatus);
                            v2pClone.get(vFrom2).setCurrentStaus(PieceStatus.NO_SELECT);
                            if (v2pClone.get(vFrom2).getTag().equals("c7")) {
                                v2pClone.get(vTo2);
                            }
                            int checkAnyPieceIsKilled = checkAnyPieceIsKilled(v2pClone.get(vTo), v2pClone);
                            AIMoveScenario scenario = new AIMoveScenario();
                            int rs2 = 0;
                            int rn = 0;
                            for (PieceInfo pi2 : arrayList) {
                                if (pi2.getCurrentStaus() == PieceStatus.S_SIDE) {
                                    rs2++;
                                } else if (pi2.getCurrentStaus() == PieceStatus.N_SIDE) {
                                    rn++;
                                }
                            }
                            scenario.setRestS(rs2);
                            scenario.setRestN(rn);
                            if (depth == totalDepth) {
                                int n = 0;
                                for (PieceInfo pi22 : arrayList) {
                                    if (pi22.getCurrentStaus() == PieceStatus.N_SIDE) {
                                        for (Vector v : pi22.getAdjacentVectorList()) {
                                            if (Objects.requireNonNull(this.v2p.get(v)).getCurrentStaus() == PieceStatus.NO_SELECT) {
                                                n++;
                                            }
                                        }
                                    }
                                }
                                scenario.setAdjacentNum(n);
                            }
                            List<AIMove> mList = new ArrayList<>();
                            if (alreadyMoveList != null) {
                                mList.addAll(alreadyMoveList);
                            }
                            mList.add(new AIMove(vFrom, vTo));
                            scenario.setAiMoveList(mList);
                            scenario.setDepth(depth);
                            scenario.setListPieceInfo(arrayList);
                            if (currentTurnStatus == PieceStatus.N_SIDE) {
                                nextTurnStatus = PieceStatus.S_SIDE;
                            } else {
                                nextTurnStatus = PieceStatus.N_SIDE;
                            }
                            scenario.setNextTurn(nextTurnStatus);
                            scenario.setV2pClone(v2pClone);
                            this.aiScenarioList.add(scenario);
                            generateAIMoveScenarios(totalDepth, depth - 1, v2pClone, arrayList, nextTurnStatus, mList);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void moveByAI() {
        if (this.depthAIThink <= 3) {
            this.aiScenarioList.clear();
            generateAIMoveScenarios(this.depthAIThink, this.depthAIThink, this.v2p, this.pieceList, this.currentTurn, (List<AIMove>) null);
        } else if (!this.aiScenarioList.isEmpty()) {
            ArrayList<AIMoveScenario> aiScenarioListTemp = new ArrayList<>();
            for (AIMoveScenario s : this.aiScenarioList) {
                if (s.getDepth() <= this.depthAIThink - 2 && s.getAiMoveList().size() >= 2) {
                    AIMove aiMove = s.getAiMoveList().get(0);
                    AIMove sMove = s.getAiMoveList().get(1);
                    if (!(aiMove == null || !aiMove.equals(this.lastAIMove) || sMove == null)) {
                        if (sMove.equals(this.lastSMove)) {
                            aiScenarioListTemp.add(s);
                        }
                    }
                }
            }
            Log.i(GV.TAG, String.format("before scenario : %d, before scenario : %d", new Object[]{Integer.valueOf(this.aiScenarioList.size()), Integer.valueOf(aiScenarioListTemp.size())}));
            this.aiScenarioList.clear();
            for (AIMoveScenario s8 : aiScenarioListTemp) {
                s8.setDepth(s8.getDepth() + (this.depthAIThink - 2));
                s8.removeFirstAiMoveList();
                s8.removeFirstAiMoveList();
                generateAIMoveScenarios(this.depthAIThink, 2, s8.getV2pClone(), s8.getListPieceInfo(), s8.getNextTurn(), s8.getAiMoveList());
            }
            this.aiScenarioList.addAll(aiScenarioListTemp);
        } else {
            generateAIMoveScenarios(this.depthAIThink, this.depthAIThink, this.v2p, this.pieceList, this.currentTurn, (List<AIMove>) null);
        }
        HashMap<AIMove, AIThink> move2think = new HashMap<>();
        for (AIMoveScenario s9 : this.aiScenarioList) {
            if (s9.getDepth() == 1) {
                String path = "";
                int step = 0;
                for (AIMove m : s9.getAiMoveList()) {
                    path = path + String.format("step%d:%s->%s,  ", new Object[]{Integer.valueOf(step), this.v2p.get(m.getFromVec()).getTag(), this.v2p.get(m.getToVec()).getTag()});
                    step++;
                }
                AIMove fistMove = s9.getAiMoveList().get(0);
                int rating = s9.getRestN() - s9.getRestS();
                Log.i(GV.TAG, String.format("r : %d, path : %s", new Object[]{Integer.valueOf(rating), path}));
                boolean isExist = false;
                AIThink t = null;
                Iterator<Map.Entry<AIMove, AIThink>> i$3 = move2think.entrySet().iterator();
                while (true) {
                    if (!i$3.hasNext()) {
                        break;
                    }
                    Map.Entry<AIMove, AIThink> entry = (Map.Entry<AIMove, AIThink>) i$3.next();
                    if (entry.getKey().equals(fistMove)) {
                        isExist = true;
                        t = entry.getValue();
                        break;
                    }
                }
                if (!isExist) {
                    t = new AIThink();
                    move2think.put(fistMove, t);
                }
                t.setLastDepthNodeNum(t.getLastDepthNodeNum() + 1);
                t.setTotalRating(t.getTotalRating() + rating);
                t.setAdjacentNum(s9.getAdjacentNum());
            }
        }
        float maxRating = -2.14748365E9f;
        int maxAdjacent = ExploreByTouchHelper.INVALID_ID;
        AIMove bestMove = null;
        for (Map.Entry<AIMove, AIThink> entry2 : move2think.entrySet()) {
            AIMove m2 = entry2.getKey();
            AIThink t2 = entry2.getValue();
            if (t2.getAverageRating() > maxRating) {
                maxRating = t2.getAverageRating();
                maxAdjacent = t2.getAdjacentNum();
                bestMove = m2;
            } else if (t2.getAverageRating() == maxRating && t2.getAdjacentNum() > maxAdjacent) {
                maxRating = t2.getAverageRating();
                maxAdjacent = t2.getAdjacentNum();
                bestMove = m2;
            }
            Log.i(GV.TAG, String.format("move : %s->%s, r : %f(%d/%d)", new Object[]{this.v2p.get(m2.getFromVec()).getTag(), this.v2p.get(m2.getToVec()).getTag(), Float.valueOf(t2.getAverageRating()), Integer.valueOf(t2.getTotalRating()), Integer.valueOf(t2.getLastDepthNodeNum())}));
        }
        AIMove m3 = bestMove;
        this.v2p.get(m3.getFromVec()).setCurrentStaus(PieceStatus.NO_SELECT);
        this.v2p.get(m3.getToVec()).setCurrentStaus(PieceStatus.N_SIDE);
        Log.i(GV.TAG, String.format("final move : %s->%s", new Object[]{this.v2p.get(m3.getFromVec()).getTag(), this.v2p.get(m3.getToVec()).getTag()}));
        checkAnyPieceIsKilled(this.v2p.get(m3.getToVec()), this.v2p);
        checkIsGameFinished();
        this.currentTurn = PieceStatus.S_SIDE;
        this.lastAIMove = bestMove;
    }

    private boolean isKilled(Vector vAdj, Vector vMe, PieceStatus ps, List<Vector> listCheckedV, List<Vector> listKilledV, HashMap<Vector, PieceInfo> v2pClone) {
        PieceInfo piAdj = v2pClone.get(vAdj);
        PieceInfo piMe = v2pClone.get(vMe);
        StringBuilder c = new StringBuilder();
        for (Vector v : listCheckedV) {
            c.append(v2pClone.get(v).getTag()).append(",");
        }
        StringBuilder k = new StringBuilder();
        for (Vector v2 : listKilledV) {
            k.append(v2pClone.get(v2).getTag()).append(",");
        }
        Log.d(GV.TAG, String.format("isKilled check %s %s, %s, %s", new Object[]{piAdj.getTag(), piMe.getTag(), c.toString(), k.toString()}));
        boolean isKilled = true;
        Iterator<Vector> i$ = piAdj.getAdjacentVectorList().iterator();
        while (true) {
            if (!i$.hasNext()) {
                break;
            }
            Vector vAdjAdj = (Vector) i$.next();
            if (!vAdjAdj.equals(vMe)) {
                PieceInfo piAdjAdj = v2pClone.get(vAdjAdj);
                if (piAdjAdj.getCurrentStaus() == PieceStatus.NO_SELECT) {
                    isKilled = false;
                    break;
                } else if (piAdjAdj.getCurrentStaus() != ps && !listCheckedV.contains(vAdjAdj)) {
                    listCheckedV.add(vAdjAdj);
                    if (!isKilled(vAdjAdj, vAdj, ps, listCheckedV, listKilledV, v2pClone)) {
                        isKilled = false;
                        break;
                    }
                }
            }
        }
        if (isKilled) {
            listKilledV.add(vAdj);
        }
        return isKilled;
    }

    private int checkAnyPieceIsKilled(PieceInfo pi, HashMap<Vector, PieceInfo> vec2pieceInfo) {
        PieceStatus psComp;
        int killNNum = 0;
        if (pi.getCurrentStaus() == PieceStatus.N_SIDE) {
            psComp = PieceStatus.S_SIDE;
        } else {
            psComp = PieceStatus.N_SIDE;
        }
        List<Vector> listAdjV = pi.getAdjacentVectorList();
        List<Vector> listKilledV = new ArrayList<>();
        for (Vector vAdj : listAdjV) {
            if (vec2pieceInfo.get(vAdj).getCurrentStaus() == psComp) {
                List<Vector> listCheckedV = new ArrayList<>();
                List<Vector> listKilledVTemp = new ArrayList<>();
                if (isKilled(vAdj, pi.getVector(), pi.getCurrentStaus(), listCheckedV, listKilledVTemp, vec2pieceInfo)) {
                    listKilledVTemp.add(vAdj);
                    listKilledV.addAll(listKilledVTemp);
                    String k = "";
                    for (Vector v2 : listKilledV) {
                        k = k + vec2pieceInfo.get(v2).getTag() + ",";
                    }
                    Log.d(GV.TAG, String.format("check %s and find it's killed, %s", new Object[]{vec2pieceInfo.get(vAdj).getTag(), k}));
                } else {
                    Log.d(GV.TAG, String.format("check %s and find it's not killed", new Object[]{vec2pieceInfo.get(vAdj).getTag()}));
                }
            }
        }
        for (Vector v3 : listKilledV) {
            if (vec2pieceInfo.get(v3).getCurrentStaus() == PieceStatus.N_SIDE) {
                killNNum++;
            }
            vec2pieceInfo.get(v3).setCurrentStaus(PieceStatus.NO_SELECT);
        }
        return killNNum;
    }

    private void checkIsGameFinished() {
        this.restCountS = 0;
        this.restCountN = 0;
        for (PieceInfo pi2 : this.pieceList) {
            if (pi2.getCurrentStaus() == PieceStatus.S_SIDE) {
                this.restCountS++;
            } else if (pi2.getCurrentStaus() == PieceStatus.N_SIDE) {
                this.restCountN++;
            }
        }
        if (this.restCountS <= 2) {
            this.winner = PieceStatus.N_SIDE;
        } else if (this.restCountN <= 2) {
            this.winner = PieceStatus.S_SIDE;
        }
        if (this.winner != null) {
            if (this.currentPlayMode != PlayMode.DOUBLE) {
                RecordDetail rd = Record.getRecord(getContext(), this.currentPlayMode);
                if (this.winner == PieceStatus.N_SIDE) {
                    rd.addLose();
                } else {
                    rd.addWin();
                }
                Record.saveRecord(getContext(), this.currentPlayMode, rd);
            }
            this.parentActivity.showGameFinishDialog(this.winner, this.currentPlayMode);
        }
    }

    // File: GameLogic.java

    public boolean onTouchEvent(MotionEvent event) {
        this.mCurrX = (int) event.getX();
        this.mCurrY = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown();
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp();
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove();
                break;
        }

        invalidate();
        return true;
    }
    @Override
    public boolean performClick() {
        // Do something in response to the click event
        // Reset the timer for the current turn
        startTime = SystemClock.elapsedRealtime();
        resetTurnTimer();
        return super.performClick();
    }


    private void handleActionDown() {
        Log.d(GV.TAG, String.format("ACTION_DOWN %d, %d", mCurrX, mCurrY));

        if (currentSelectingPiece != null) {
            resetCurrentSelectingPiece();
            if (currentTempTargetPiece != null) {
                clearTempTargetPiece();
            }
        } else {
            for (PieceInfo pi : pieceList) {
                if (pi.isContainPoint(mCurrX, mCurrY) && pi.getCurrentStaus() == currentTurn) {
                    selectPiece(pi);
                    break;
                }
            }
        }
    }

    private void handleActionUp() {
        Log.d(GV.TAG, String.format("ACTION_UP %d, %d", mCurrX, mCurrY));

        clearTempTargetPiece();

        if (currentSelectingPiece != null) {
            for (PieceInfo pi : pieceList) {
                if (pi.isContainPoint(mCurrX, mCurrY) && pi.getCurrentStaus() == PieceStatus.NO_SELECT && pi.isAdjacentContain(currentSelectingPiece.getVector())) {
                    updatePieceStatus(pi);
                    switchTurns();
                    mpMove.start();
                    checkAnyPieceIsKilled(pi, v2p);
                    checkIsGameFinished();
                    updateHistory(pi);
                    isRegreting = false;
                    return;
                }
            }

            resetCurrentSelectingPiece();
        }
    }

    private void handleActionMove() {
        if (currentSelectingPiece != null) {
            for (PieceInfo pi : pieceList) {
                if (pi.isContainPoint(mCurrX, mCurrY) && pi.getCurrentStaus() == PieceStatus.NO_SELECT && pi.isAdjacentContain(currentSelectingPiece.getVector())) {
                    if (currentSelectingPiece.getCurrentStaus() == PieceStatus.N_SELECTING) {
                        pi.setCurrentStaus(PieceStatus.N_TEMP);
                    } else {
                        pi.setCurrentStaus(PieceStatus.S_TEMP);
                    }

                    clearTempTargetPiece();
                    currentTempTargetPiece = pi;
                }
            }
        }
    }

    private void resetCurrentSelectingPiece() {
        if (currentSelectingPiece.getCurrentStaus() == PieceStatus.N_SELECTING) {
            currentSelectingPiece.setCurrentStaus(PieceStatus.N_SIDE);
        } else {
            currentSelectingPiece.setCurrentStaus(PieceStatus.S_SIDE);
        }
        currentSelectingPiece = null;
    }

    private void clearTempTargetPiece() {
        if (currentTempTargetPiece != null) {
            currentTempTargetPiece.setCurrentStaus(PieceStatus.NO_SELECT);
            currentTempTargetPiece = null;
        }
    }

    private void selectPiece(PieceInfo pi) {
        if (currentTurn == PieceStatus.N_SIDE) {
            pi.setCurrentStaus(PieceStatus.N_SELECTING);
        } else {
            pi.setCurrentStaus(PieceStatus.S_SELECTING);
        }
        currentSelectingPiece = pi;
        mpSelect.start();
    }

    private void updatePieceStatus(PieceInfo piece) {
        if (currentSelectingPiece.getCurrentStaus() == PieceStatus.N_SELECTING) {
            piece.setCurrentStaus(PieceStatus.N_SIDE);
            lastNMove = new AIMove(currentSelectingPiece.getVector(), piece.getVector());
        } else {
            piece.setCurrentStaus(PieceStatus.S_SIDE);
            lastSMove = new AIMove(currentSelectingPiece.getVector(), piece.getVector());
        }
        currentSelectingPiece.setCurrentStaus(PieceStatus.NO_SELECT);
        currentSelectingPiece = null;
    }

    private void switchTurns() {
        currentTurn = (currentTurn == PieceStatus.N_SIDE) ? PieceStatus.S_SIDE : PieceStatus.N_SIDE;
        Log.d("BoardView", "Turn switched to: " + currentTurn);
    }

    private void updateHistory(PieceInfo piece) {
        HistoryItem hi = new HistoryItem();
        hi.setCurrentTurn(currentTurn);
        hi.setRestCountN(restCountN);
        hi.setRestCountS(restCountS);

        ArrayList<PieceInfo> pieceListClone = new ArrayList<>();
        for (PieceInfo pi : pieceList) {
            pieceListClone.add(new PieceInfo(pi.getVector(), pi.getTag(), pi.getRp(), pi.getCurrentStaus(), pi.getAdjacentVectorList()));
        }
        hi.setPieceList(pieceListClone);
        historyOfPieceList.push(hi);
    }

}
