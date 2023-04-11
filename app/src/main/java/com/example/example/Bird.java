package com.example.example;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.LinkedList;

public class Bird {
    Bitmap bird = null;
    LinkedList<Rect> list = new LinkedList<>();
    Rect dist;

    public Bird(Resources res) {
        this.bird = BitmapFactory.decodeResource(res, R.drawable.bird);
        list.add(new Rect(0,0, bird.getHeight(), bird.getHeight()));
        list.add(new Rect(1 * bird.getHeight(),0, 2 * bird.getHeight(), bird.getHeight()));
        list.add(new Rect(2 * bird.getHeight(),0, 3 * bird.getHeight(), bird.getHeight()));
        list.add(new Rect(3 * bird.getHeight(),0, 4 * bird.getHeight(), bird.getHeight()));
        dist = new Rect(0, 0, bird.getHeight(), bird.getHeight());
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bird, list.getFirst(), dist, new Paint());
        list.addLast(list.getFirst());
        list.removeFirst();
    }
}
