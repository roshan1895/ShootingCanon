package com.example.shootingcanon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {
    Bitmap bg,tank;
    Rect rect;
    static int dwidth,dheight;
    int planeWidth;
    Handler handler;
    Runnable runnable;
    final long UPDATE_MILIS=30;
    ArrayList<Plane>planes,planes2;
    static  int tankwidth,tankheight;
    ArrayList<Missile> missiles;
    ArrayList<Explosion> explosions;

    Context context;
    int count=0;
    SoundPool sp;
    int fire=0;
    int point=0;
    Paint scorepaint,healthPaint;
    final int TEXT_SIZE=60;
    int life=10;

    public GameView(Context context) {
        super(context);
        this.context=context;
        bg= BitmapFactory.decodeResource(getResources(),R.drawable.background);
        tank=BitmapFactory.decodeResource(getResources(),R.drawable.tank);
        Display display=((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        dwidth=size.x;
        dheight=size.y;
        rect=new Rect(0,0,dwidth,dheight);
        planes=new ArrayList<>();
        planes2=new ArrayList<>();
        missiles=new ArrayList<>();
        explosions=new ArrayList<>();
        for(int i=0;i<2;i++)
        {
            Plane plane=new Plane(context);
            planes.add(plane);
        }
        for(int i=0;i<2;i++)
        {
            Plane2 plane2=new Plane2(context);
            planes2.add(plane2);
        }

        handler=new Handler();
        runnable= new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        tankwidth=tank.getWidth();
        tankheight=tank.getHeight();
        sp=new SoundPool(3, AudioManager.STREAM_MUSIC,0);
        fire=sp.load(context,R.raw.fire,1);
        point=sp.load(context,R.raw.point,1);
        scorepaint=new Paint();
        scorepaint.setColor(Color.RED);
    scorepaint.setTextSize(TEXT_SIZE);
        scorepaint.setTextAlign(Paint.Align.LEFT);

        healthPaint=new Paint();
        healthPaint.setColor(Color.GREEN);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(life<1)
        {
//            Intent intent=new Intent(context,GameOver.class);
//            intent.putExtra("score",(count*10));
//            context.startActivity(intent);
            ((Activity) context).finish();
        }
        canvas.drawBitmap(bg,null,rect,null);
        for(int i=0;i<planes.size();i++)
        {
            canvas.drawBitmap(planes.get(i).getBitmap(),planes.get(i).planeX,planes.get(i).planeY,null);
            planes.get(i).planeFrame++;
            if(planes.get(i).planeFrame>14)
            {
                planes.get(i).planeFrame=0;

            }
            planes.get(i).planeX-=planes.get(i).velocity;
            if(planes.get(i).planeX< -planes.get(i).getWidth())
            {
                planes.get(i).resetPosition();
                life--;
            }
        }
        for(int i=0;i<planes2.size();i++)
        {
            canvas.drawBitmap(planes2.get(i).getBitmap(),planes2.get(i).planeX,planes2.get(i).planeY,null);
            planes2.get(i).planeFrame++;
            if(planes2.get(i).planeFrame>9)
            {
                planes2.get(i).planeFrame=0;

            }
            planes2.get(i).planeX+=planes2.get(i).velocity;
            if(planes2.get(i).planeX>dwidth+planes2.get(i).getWidth())
            {
                planes2.get(i).resetPosition();
                life--;
            }
        }
        for(int i=0;i<missiles.size();i++)
        {
            if(missiles.get(i).y> -missiles.get(i).getMissileHeight())
            {
                missiles.get(i).y-=missiles.get(i).mvelocity;
                canvas.drawBitmap(missiles.get(i).missile,missiles.get(i).x,missiles.get(i).y,null);
               if(missiles.get(i).x>=planes.get(0).planeX&&(missiles.get(i).x+missiles.get(i).getMissileWidth()<=(planes.get(0).planeX+planes.get(0).getWidth()))&&missiles.get(i).y>=planes.get(0).planeY&&missiles.get(i).y<=(planes.get(0).planeY+planes.get(0).getHeight()))
               { Explosion explosion=new Explosion(context);
                  explosion.expx=planes.get(0).planeX+planes.get(0).getWidth()/2-explosion.getExpolsionwidth()/2;
                  explosion.expy=planes.get(0).planeY+planes.get(0).getHeight()/2-explosion.getExplosionHeight()/2;
                  explosions.add(explosion);
                   planes.get(0).resetPosition();
                   count++;
                   missiles.remove(i);
                   if(point!=0)
                   {
                       sp.play(point,1,1,0,0,1);
                   }
               }
                else if(missiles.get(i).x>=planes.get(1).planeX&&(missiles.get(i).x+missiles.get(i).getMissileWidth()<=(planes.get(1).planeX+planes.get(1).getWidth()))&&missiles.get(i).y>=planes.get(1).planeY&&missiles.get(i).y<=(planes.get(1).planeY+planes.get(1).getHeight()))
                {  Explosion explosion=new Explosion(context);
                    explosion.expx=planes.get(1).planeX+planes.get(1).getWidth()/2-explosion.getExpolsionwidth()/2;
                    explosion.expy=planes.get(1).planeY+planes.get(1).getHeight()/2-explosion.getExplosionHeight()/2;
                    explosions.add(explosion);
                    planes.get(1).resetPosition();
                    count++;
                    missiles.remove(i);
                    if(point!=0)
                    {
                        sp.play(point,1,1,0,0,1);
                    }
                }
               else  if(missiles.get(i).x>=planes2.get(0).planeX&&(missiles.get(i).x+missiles.get(i).getMissileWidth()<=(planes2.get(0).planeX+planes2.get(0).getWidth()))&&missiles.get(i).y>=planes2.get(0).planeY&&missiles.get(i).y<=(planes2.get(0).planeY+planes2.get(0).getHeight()))
                {Explosion explosion=new Explosion(context);
                    explosion.expx=planes2.get(0).planeX+planes2.get(0).getWidth()/2-explosion.getExpolsionwidth()/2;
                    explosion.expy=planes2.get(0).planeY+planes2.get(0).getHeight()/2-explosion.getExplosionHeight()/2;
                    explosions.add(explosion);
                    planes2.get(0).resetPosition();
                    count++;
                    missiles.remove(i);
                    if(point!=0)
                    {
                        sp.play(point,1,1,0,0,1);
                    }
                }
                else if(missiles.get(i).x>=planes2.get(1).planeX&&(missiles.get(i).x+missiles.get(i).getMissileWidth()<=(planes2.get(1).planeX+planes2.get(1).getWidth()))&&missiles.get(i).y>=planes2.get(1).planeY&&missiles.get(i).y<=(planes2.get(1).planeY+planes2.get(1).getHeight()))
                {Explosion explosion=new Explosion(context);
                    explosion.expx=planes2.get(1).planeX+planes2.get(1).getWidth()/2-explosion.getExpolsionwidth()/2;
                    explosion.expy=planes2.get(1).planeY+planes2.get(1).getHeight()/2-explosion.getExplosionHeight()/2;
                    explosions.add(explosion);
                    planes2.get(1).resetPosition();
                    count++;
                    missiles.remove(i);
                    if(point!=0)
                    {
                        sp.play(point,1,1,0,0,1);
                    }
                }
            }
            else
            {
                missiles.remove(i);
            }
        }
        for (int j=0;j<explosions.size();j++)
        {
            canvas.drawBitmap(explosions.get(j).getExplosion(explosions.get(j).explosionframe),explosions.get(j).expx,explosions.get(j).expy,null);
            explosions.get(j).explosionframe++;
            if(explosions.get(j).explosionframe>8)
            {
                explosions.remove(j);
            }
        }
        canvas.drawBitmap(tank,dwidth/2-tankwidth/2,dheight-tankheight,null);
        canvas.drawText("Pt:  "+(count*10),0,TEXT_SIZE,scorepaint);
        canvas.drawRect(dwidth-110,10,dwidth-110+10*life,TEXT_SIZE,healthPaint  );
        handler.postDelayed(runnable,UPDATE_MILIS);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchx=event.getX();
        float touchy=event.getY();
        int action=event.getAction();
        if(action==MotionEvent.ACTION_DOWN)
        {
            if(touchx>=(dwidth/2-tankwidth/2)&&touchx<=(dwidth/2+tankwidth/2)&&touchy>=(dheight-tankheight))
            {
                Log.e("tank","is Tapped");
                if(missiles.size()<3)
                {
                    Missile m=new Missile(context);
                    missiles.add(m);
                    if(fire!=0)
                    {
                        sp.play(fire,1,1,0,0,1);
                    }
                }
            }
        }
        return true;
    }

    void showdialog(int score) {
        final Dialog dialog = new Dialog(context, R.style.dialog_style);
        dialog.setContentView(R.layout.place_order_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




        // if button is clicked, close the custom dialog
        dialog.show();


    }

}
