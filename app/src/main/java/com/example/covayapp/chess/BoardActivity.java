package com.example.covayapp.chess;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covayapp.R;


@SuppressLint({"DrawAllocation"})
public class BoardActivity extends AppCompatActivity {

    /* access modifiers changed from: private */
    public BoardView bv;

    public void showGameFinishDialog(PieceStatus winner, PlayMode pm) {
        String finishGameContentStr;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (pm == PlayMode.DOUBLE) {
            if (winner == PieceStatus.N_SIDE) {
                finishGameContentStr = getResources().getString(R.string.finish_game_double_n_win);
            } else {
                finishGameContentStr = getResources().getString(R.string.finish_game_double_s_win);
            }
        } else if (winner == PieceStatus.N_SIDE) {
            finishGameContentStr = getResources().getString(R.string.finish_game_content_lose);
        } else {
            finishGameContentStr = getResources().getString(R.string.finish_game_content_win);
        }
        String finishGameTitleStr = getResources().getString(R.string.finish_game_title);
        String newGameStr = getResources().getString(R.string.finish_game_start_new);
        String backMainStr = getResources().getString(R.string.finish_game_back_main);
        builder.setMessage(finishGameContentStr);
        builder.setTitle(finishGameTitleStr);
        builder.setPositiveButton(newGameStr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                BoardActivity.this.bv.init();
            }
        });
        builder.setNegativeButton(backMainStr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                BoardActivity.this.finish();
            }
        });
        builder.create().show();
    }

    /* access modifiers changed from: private */
    public void showGameGiveUpWarningDialog(final boolean isBack) {
        if (!this.bv.getHistoryOfPieceList().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String exitGameContentStr = getResources().getString(R.string.exit_game_content);
            String exitGameTitleStr = getResources().getString(R.string.exit_game_title);
            String exitGameConfirmStr = getResources().getString(R.string.confirm);
            String exitGameCancelStr = getResources().getString(R.string.cancel);
            builder.setMessage(exitGameContentStr);
            builder.setTitle(exitGameTitleStr);
            builder.setPositiveButton(exitGameConfirmStr, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (BoardActivity.this.bv.getCurrentPlayMode() != PlayMode.DOUBLE) {
                        RecordDetail rd = Record.getRecord(BoardActivity.this, BoardActivity.this.bv.getCurrentPlayMode());
                        rd.addGiveup();
                        Record.saveRecord(BoardActivity.this, BoardActivity.this.bv.getCurrentPlayMode(), rd);
                    }
                    dialog.dismiss();
                    if (isBack) {
                        BoardActivity.this.finish();
                    } else {
                        BoardActivity.this.bv.init();
                    }
                }
            });
            builder.setNegativeButton(exitGameCancelStr, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else if (isBack) {
            finish();
        } else {
            this.bv.init();
        }
    }

    @Override
    public void onBackPressed() {
        // Hiển thị hộp thoại cảnh báo cho người chơi về việc bỏ cuộc
        showGameGiveUpWarningDialog(true);

        // Gọi phương thức onBackPressed của lớp cha
        super.onBackPressed();
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Button btnRestart = (Button) findViewById(R.id.button1);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BoardActivity.this.showGameGiveUpWarningDialog(false);
            }
        });
        Button btnRegret = (Button) findViewById(R.id.button2);
        btnRegret.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BoardActivity.this.bv.regret();
            }
        });
        this.bv = (BoardView) findViewById(R.id.board_view);
        Bundle b = getIntent().getExtras();
        int gameTypeLatest;
        if (b != null) {
            gameTypeLatest = b.getInt("type");
        } else {
            gameTypeLatest = 0;
        }
        if (gameTypeLatest == -1) {
            this.bv.setCurrentPlayMode(PlayMode.DOUBLE);
        } else if (gameTypeLatest == 0) {
            this.bv.setCurrentPlayMode(PlayMode.SINGLE_0);
        } else if (gameTypeLatest == 1) {
            this.bv.setCurrentPlayMode(PlayMode.SINGLE_1);
        } else if (gameTypeLatest == 2) {
            this.bv.setCurrentPlayMode(PlayMode.SINGLE_2);
        }
        this.bv.setParentActivity(this);
    }
}
