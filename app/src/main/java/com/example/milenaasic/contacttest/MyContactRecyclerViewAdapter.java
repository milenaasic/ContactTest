package com.example.milenaasic.contacttest;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class MyContactRecyclerViewAdapter extends RecyclerView.Adapter<MyContactRecyclerViewAdapter.ViewHolder> {


    private static OnViewHolderClicked mViewHolderClicked;
    private final Cursor originalCursor;
    private Cursor filteredCursor;

    // konstante koje definišu Cursor kolone, ali koje mi mozda ovde ne trebaju, jer ovde ne vadim ništa iz kolona
    private static final int CURSOR_COLUMN_ID=0;
    private static final int CURSOR_COLUMN_LOOKUP=1;
    private static final int CURSOR_DISPLAY_NAME_PRIMARY=2;
    private static final int CURSOR_PHOTO_THUMBNAIL_URI=3;


    public MyContactRecyclerViewAdapter(Cursor cursor, OnViewHolderClicked listener) {
        originalCursor=cursor;
        mViewHolderClicked = listener;
    }


    public interface OnViewHolderClicked{
        public void viewHolderClicked(View v,int position);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contact_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        if (originalCursor!=null && originalCursor.getCount()!=0) {
           if(originalCursor.moveToPosition(position)) {
               String thumbnaulUri = originalCursor.getString(CURSOR_PHOTO_THUMBNAIL_URI);
               Glide.with((Fragment) mViewHolderClicked)
                       .load(thumbnaulUri)
                       .into(holder.mItemImageView);
           } else {
            Glide.with((Fragment) mViewHolderClicked).clear(holder.mItemImageView);
            holder.mItemImageView.setImageResource(android.R.drawable.btn_star);
        }

            if(originalCursor.moveToPosition(position)){

                String name=originalCursor.getString(CURSOR_DISPLAY_NAME_PRIMARY);
                holder.mItemTextView.setText(name);
            }



        }


    }

    @Override
    public int getItemCount() {
        return originalCursor.getCount();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mItemImageView;
        public TextView mItemTextView;


        public ViewHolder(View view) {
            super(view);
            mItemImageView = (ImageView) view.findViewById(R.id.itemImageView);
            mItemTextView = (TextView) view.findViewById(R.id.itemTextView);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
           int position=getAdapterPosition();
            mViewHolderClicked.viewHolderClicked(v,position);
        }
    }
}
