package com.vertial.veritel;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import android.view.View;


class SwipeController extends ItemTouchHelper.SimpleCallback {

    ViewHolderSwipedListener mViewHolderSwipedListener;
    float radialScale=0.1f;
    float radialScaleDirection=1.0f;
    Resources resources;

    SwipeController(int dragDir,int swipeDir,ViewHolderSwipedListener swipeListener){
        super(dragDir,swipeDir);
        mViewHolderSwipedListener=swipeListener;
        resources=((Fragment)swipeListener).getResources();

    }


    public interface ViewHolderSwipedListener{

        void onViewHolderSwiped(RecyclerView.ViewHolder viewHolder,int index);

    }



    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


        int index=viewHolder.getAdapterPosition();
        mViewHolderSwipedListener.onViewHolderSwiped( viewHolder,index);
    }


    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {



        View itemView = viewHolder.itemView;

        float density=resources.getDisplayMetrics().density;
        float height=itemView.getHeight();


        int itemListHeightDpi=56;
        int divideLeftPadding=72;

        Paint p = new Paint();


        LinearGradient linearShader=new LinearGradient(itemView.getLeft()/density, itemView.getBottom()/density,
                itemView.getRight()/density , itemView.getBottom()/density,
                Color.parseColor("#00bcd4"),Color.parseColor("#00bcd4"),Shader.TileMode.MIRROR);

        Matrix matrix=new Matrix();
        float scale = incScale(0.025f);
        matrix.setScale(scale, scale,itemView.getWidth() /2, itemView.getHeight()/2);
        linearShader.setLocalMatrix(matrix);

        p.setShader(linearShader);

        RectF button = new RectF(itemView.getLeft(), itemView.getBottom()-(itemListHeightDpi*density), itemView.getRight() , itemView.getBottom());
        p.setColor(Color.parseColor("#c5cae9"));
        c.drawRoundRect(button, 0, 0, p);

        float textSize = 60;

        Paint p2=new Paint();
        p2.setTextSize(textSize);
        p2.setColor(Color.parseColor("#ff4081"));
        String text=resources.getString(R.string.swipe_message);
        float textWidth = p2.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p2);

        if(height/density>itemListHeightDpi){

            Paint p3=new Paint();
            p3.setColor(resources.getColor(R.color.divider));
            RectF line = new RectF(itemView.getLeft()+divideLeftPadding*density, itemView.getTop()+(24)*density,
                 itemView.getRight() , itemView.getTop()+(24+1)*density);

            c.drawRoundRect(line, 0, 0, p3);
        }

        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        itemView.invalidate();
    }


    private float incScale(float delta) {
        radialScale = (radialScale + delta);

        return radialScale;
    }



}



