package com.example.milenaasic.contacttest;

import android.content.ContentUris;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.StringLoader;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;


public class MyContactRecyclerViewAdapter extends RecyclerView.Adapter<MyContactRecyclerViewAdapter.ViewHolder> {

    private static final String LOG="MyConttRecViewAdapter";
    private static OnViewHolderClicked mViewHolderClicked;
    private Cursor filterCursor;


    // konstante koje definišu Cursor kolone, ali koje mi mozda ovde ne trebaju, jer ovde ne vadim ništa iz kolona
    private static final int CURSOR_COLUMN_ID=0;
    private static final int CURSOR_COLUMN_LOOKUP=1;
    private static final int CURSOR_DISPLAY_NAME_PRIMARY=2;
    private static final int CURSOR_PHOTO_THUMBNAIL_URI=3;



    MyContactRecyclerViewAdapter(Cursor cursor, OnViewHolderClicked listener) {
        Log.v(LOG,"adapter konstruktor");
        filterCursor=cursor;
        mViewHolderClicked = listener;

    }

    void setFilterCursor(Cursor c){
        /*if (originalCursor==null){
            Log.v(LOG,"OrigCursor je null");
            if (c==null){ Log.v(LOG,"c Cursor je null");}*/

            if (c!=null) {
                filterCursor = c;
                Log.v(LOG,"setovani cursor nije null");
                int n=c.getCount();
                Log.v(LOG,((Integer)n).toString());
            }
        
        notifyDataSetChanged();
        Log.v(LOG,"notifyDataset");
        }


    public interface OnViewHolderClicked{
        public void viewHolderClicked(View v,int position);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contact_item, parent, false);
        Log.v(LOG,"onCreateViewHolder");

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.v(LOG, "onBind bilo kako");

        if (filterCursor != null && filterCursor.getCount() != 0) {
            Log.v(LOG, "onBind origCursor nije null i count nije 0");
            Log.v(LOG, ((Integer) position).toString() + "cursor pozicija");

            if (filterCursor.moveToPosition(position)) {

                int contactId=filterCursor.getInt(CURSOR_COLUMN_ID);
                Uri contactUri= ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactId);
                Uri photoThumbUri=Uri.withAppendedPath(contactUri,ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                RequestOptions options = new RequestOptions();
                options.circleCrop();

                RequestOptions options1 = new RequestOptions();

                options.fallback(R.drawable.slika0_thumb);

                GlideApp.with((Fragment) mViewHolderClicked)
                        .load(photoThumbUri)
                        .circleCrop()
                        .error(R.drawable.icons8_user)
                        .into(holder.mItemImageView);

                Log.v(LOG,photoThumbUri+" photoThumbUri");

                    String name = filterCursor.getString(CURSOR_DISPLAY_NAME_PRIMARY);
                    Log.v(LOG, "string iz cursora" + name);
                    holder.mItemTextView.setText(name);
                    Log.v(LOG, "setovan text ");



            }
        }
    }






    @Override
    public int getItemCount() {
        if(filterCursor==null){
            return 0;
        }else return filterCursor.getCount();
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
