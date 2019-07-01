package com.example.exempleforproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class SpacecraftView extends View {

    private Bitmap spacecraft;
    private int spX = 10;
    private int spY;
    private int spSpeed;
    private boolean touch;
    private int canvasWidth, canvasHeight;

    private int enemy2X, enemy2Y, enemy2Speed = 10;
    private Bitmap enemy2;

    private int enemy1X, enemy1Y, enemy1Speed = 20;
    private Bitmap enemy1;

    private Bitmap enemy[] = new Bitmap[2];

    private Bitmap background;
    private Paint scorePaint = new Paint();
    private Bitmap energyBall;
    private int energyBallX, energyBallY, energyBallSpeed = 15;
    private int score, lifeCounter;
    private Bitmap life[] = new Bitmap[2];


    public SpacecraftView(Context context) {
        super(context);

        spacecraft = BitmapFactory.decodeResource(getResources(), R.drawable.spacecraft);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.space);
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        energyBall = BitmapFactory.decodeResource(getResources(), R.drawable.enrgy_ball);

        enemy1 = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_1);
        enemy2 = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_2);
        enemy[0] = enemy1;
        enemy[1] = enemy2;

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey1);

        spY = 300;
        score = 0;
        lifeCounter = 5;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        canvas.drawBitmap(background, 0, 0, null);

        int minspY = spacecraft.getHeight();
        int maxspY = canvasHeight - spacecraft.getHeight() * 2;
        spY += spSpeed;
        if (spY < minspY){
            spY = minspY;
        }if (spY > maxspY){
            spY = maxspY;
        }

        spSpeed += 1;

        if (touch){
            canvas.drawBitmap(spacecraft, spX, spY, null);
            touch = false;
        }else {
            canvas.drawBitmap(spacecraft, spX, spY, null);
        }


        enemy1X -= enemy1Speed;
        enemy2X -= enemy2Speed;

        if (hitCheck(enemy1X, enemy1Y)){
            lifeCounter -= 2;
            if (lifeCounter <= 0){
                gameOverIntent();
            }
        }
        if (enemy1X < 0 || hitCheck(enemy1X, enemy1Y)){
            enemy1X = canvasWidth + 20;
            enemy1Y = (int) Math.floor(Math.random() * maxspY) + minspY;
        }

        if (hitCheck(enemy2X, enemy2Y)){
            lifeCounter --;
            if (lifeCounter <= 0){
                gameOverIntent();
            }
        }
        if (enemy2X < 0 || hitCheck(enemy2X, enemy2Y)){
            enemy2X = canvasWidth + 20;
            enemy2Y = (int) Math.floor(Math.random() * maxspY) + minspY;
        }

        canvas.drawBitmap(enemy[0], enemy1X, enemy1Y, null);
        canvas.drawBitmap(enemy[1], enemy2X, enemy2Y, null);

        energyBallX -= energyBallSpeed;
        if (hitCheck(energyBallX, energyBallY)){
            score += 10;
        }
        if (energyBallX < 0 || hitCheck(energyBallX, energyBallY)){
            energyBallX = canvasWidth + 20;
            energyBallY = (int) Math.floor(Math.random() * maxspY) + minspY;
        }

        canvas.drawBitmap(energyBall, energyBallX, energyBallY, null);

        canvas.drawText("Score: " + score, 20, 60, scorePaint);

        for (int i = 0; i < 5; i++) {
            int x = (int) (canvasWidth - (100 + life[0].getWidth() * 1.2 * i));
            int y = 30;
            if (i < lifeCounter){
                canvas.drawBitmap(life[0], x, y, null);

            }else{
                canvas.drawBitmap(life[1], x, y, null);
            }
        }
    }

    public boolean hitCheck(int x, int y){

        if (spX < x && (x+50) < (spX + spacecraft.getWidth()) && spY < (y + 100) && y <= (spY + spacecraft.getHeight())){
            return true;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            touch = true;
            spSpeed = -15;
        }
        return true;
    }

    private void gameOverIntent(){
        Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();
        Intent gameOvetIntent = new Intent(getContext(), GameOverActivity.class);
        gameOvetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        gameOvetIntent.putExtra("score", score);
        getContext().startActivity(gameOvetIntent);
    }
}
