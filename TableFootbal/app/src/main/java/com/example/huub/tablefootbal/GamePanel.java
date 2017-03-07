package com.example.huub.tablefootbal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.example.huub.tablefootbal.MainThread.canvas;

/**
 * Created by Huub on 15-2-2017.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    private MainThread thread;
    private Player player;
    private PointF playerPoint;
    private PointF playerData;
    private int playerID = 12345;

    private Stick stick;
    private Bitmap[] sticks = new Bitmap[3];

    private SensorManager sensorManager;
    private Sensor mySensor;

    private float yRotation;
    private Point screenSize;

    public GamePanel(Context context, SensorManager sensor, int deviceWidth, int deviceHeight){
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(),this);

        getSticks();

        stick = new Stick(new Rect(200,200,400,400), Color.BLACK, sticks, deviceWidth, deviceHeight);
        sensorManager = sensor;
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, mySensor, sensorManager.SENSOR_DELAY_GAME);
        setFocusable(true);

        //Init player
        player = new Player(new Rect(200,200,400,400), Color.TRANSPARENT, new PointF(800,1600/2));
        playerPoint = new PointF(150,150);
    }

    private void getSticks() {
        sticks[0] = BitmapFactory.decodeResource(getResources(), R.drawable.stick01);
        sticks[1] = BitmapFactory.decodeResource(getResources(), R.drawable.stick02);
        sticks[2] = BitmapFactory.decodeResource(getResources(), R.drawable.stick03);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        //System.out.println("x: "+event.values[0]*10);
        yRotation = event.values[0]*10;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder ){
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while (true){
            try{
                thread.setRunning(false);
                thread.join();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                playerPoint.set((int)event.getX(),(int)event.getY());
        }
        return true;
    }


    public void update(){
        playerData = player.getPosition();
        System.out.println("ID: " + playerID + " Positions: X: " + playerData.x + " Y: " + playerData.y);
        player.update(playerPoint, yRotation);
        stick.update(playerPoint.x - 100, player.getVelocity());

    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        player.draw(canvas);
        stick.draw(canvas);

    }
}
