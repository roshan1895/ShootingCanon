package com.example.shootingcanon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Missile {
    int x,y;
    int mvelocity;
    Bitmap missile;
    public  Missile(Context context)
    {missile= BitmapFactory.decodeResource(context.getResources(),R.drawable.missile);
    x=GameView.dwidth/2-getMissileWidth()/2;
    y=GameView.dheight-GameView.tankheight-getMissileHeight()/2;
    mvelocity=50;

    }
    public  int getMissileWidth()
    {
        return  missile.getWidth();
    }
    public  int getMissileHeight()
    {
        return  missile.getHeight();
    }
}
