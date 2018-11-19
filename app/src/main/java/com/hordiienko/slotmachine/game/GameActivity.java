package com.hordiienko.slotmachine.game;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hordiienko.slotmachine.Constants;
import com.hordiienko.slotmachine.R;
import com.hordiienko.slotmachine.game_surface.GameSurface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends Activity {
    @BindView(R.id.textBalance)
    TextView textBalance;

    @BindView(R.id.gameSurface)
    GameSurface gameSurface;

    @BindView(R.id.buttonBet)
    Button buttonBet;

    @BindView(R.id.buttonGrid)
    ImageButton buttonGrid;

    private int grid;
    private int bet;
    private GameLogic gameLogic;
    private Animation balanceAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ButterKnife.bind(this);

        Constants.density = getResources().getDisplayMetrics().density;

        gameSurface.setZOrderOnTop(true);

        balanceAnim = AnimationUtils.loadAnimation(this, R.anim.balance_plus);

        gameLogic = new GameLogic(this, gameSurface);

        grid = Constants.GAME_DEFAULT_GRID;
        bet = Constants.GAME_DEFAULT_BET;

        updateBetButton();
        updateGrid();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSurface.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameSurface.stop();
    }

    @OnClick(R.id.buttonSpin)
    void onClickSpin() {
        gameLogic.onClickSpin();
    }

    @OnClick(R.id.buttonBet)
    void onClickBet() {
        PopupMenu popupMenu = new PopupMenu(this, buttonBet);
        popupMenu.inflate(R.menu.menu_bets);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.bet1:
                    bet = 1;
                    break;
                case R.id.bet2:
                    bet = 2;
                    break;
                case R.id.bet5:
                    bet = 5;
                    break;
                case R.id.bet10:
                    bet = 10;
                    break;
                case R.id.bet20:
                    bet = 20;
                    break;
                default:
                    return false;
            }

            updateBetButton();

            return true;
        });

        popupMenu.show();
    }

    @OnClick(R.id.buttonGrid)
    void onClickGrid() {
        PopupMenu popupMenu = new PopupMenu(this, buttonGrid);
        popupMenu.inflate(R.menu.menu_grid);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.grid5x3:
                    grid = Constants.GAME_GRID_5X3;
                    break;
                case R.id.grid3x3:
                    grid = Constants.GAME_GRID_3X3;
                    break;
                default:
                    return false;
            }

            updateGrid();

            return true;
        });

        popupMenu.show();
    }

    public void updateBalance(int balance) {
        int currentBalance = -1;

        try {
            currentBalance = Integer.parseInt(textBalance.getText().toString());
        } catch (NumberFormatException ignored) {}

        textBalance.setText(String.valueOf(balance));

        if (currentBalance != -1 && balance > currentBalance) {
            textBalance.startAnimation(balanceAnim);
        }
    }

    public void showNoBalance() {
        Toast.makeText(this, R.string.no_balance, Toast.LENGTH_SHORT).show();
    }

    public int getBet() {
        return bet;
    }

    private void updateBetButton() {
        buttonBet.setText(String.valueOf(bet));
    }

    private void updateGrid() {
        gameLogic.setGrid(Constants.GAME_GRIDS[grid]);
    }

}
