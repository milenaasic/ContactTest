package com.example.milenaasic.contacttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



class SwipeController extends ItemTouchHelper.SimpleCallback {

    ViewHolderSwipedListener mViewHolderSwipedListener;



    SwipeController(int dragDir,int swipeDir,ViewHolderSwipedListener swipeListener){
        super(dragDir,swipeDir);
        mViewHolderSwipedListener=swipeListener;


    }



    public interface ViewHolderSwipedListener{

        void onViewHolderSwiped(int adapterPosition);


    }







    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        Log.v("swipe class","swiped");
        int index=viewHolder.getAdapterPosition();
        mViewHolderSwipedListener.onViewHolderSwiped(index);
    }


    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        //float buttonWidthWithoutPadding = 120;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF button = new RectF(itemView.getLeft()+144, itemView.getTop()+16, itemView.getRight()-32 , itemView.getBottom()-16);
        p.setColor(Color.GREEN);
        c.drawRoundRect(button, corners, corners, p);
        float textSize = 48;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);
        String text="CALL";
        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
