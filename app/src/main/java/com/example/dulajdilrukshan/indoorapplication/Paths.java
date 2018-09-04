package com.example.dulajdilrukshan.indoorapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;

public class Paths
{

    public void showLocation(Canvas canvas,float x,float y)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        paint.setShadowLayer(15, 0, 0, Color.BLUE);

        canvas.drawCircle(x, y, 10, paint);
    }



}

