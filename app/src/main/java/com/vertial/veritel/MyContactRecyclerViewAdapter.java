package com.vertial.veritel;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class MyContactRecyclerViewAdapter extends RecyclerView.Adapter<MyContactRecyclerViewAdapter.ViewHolder> {


    private OnViewHolderClicked mViewHolderClicked;
    private Cursor filterCursor;
    private String filterString;


    // konstante koje definišu Cursor kolone, ali koje mi mozda ovde ne trebaju, jer ovde ne vadim ništa iz kolona
    private static final int CURSOR_COLUMN_ID=0;
    private static final int CURSOR_COLUMN_LOOKUP=1;
    private static final int CURSOR_DISPLAY_NAME_PRIMARY=2;
    private static final int CURSOR_PHOTO_THUMBNAIL_URI=3;
    private static final int CURSOR_PHOTO_URI=4;


    MyContactRecyclerViewAdapter(Cursor cursor, OnViewHolderClicked listener) {

        filterCursor=cursor;
        mViewHolderClicked = listener;

    }

    void setFilterCursorAndFilterString(Cursor c, String filter){

        if (c!=null) {
                filterCursor = c;

                int n=c.getCount();

        }

        filterString=filter;

        notifyDataSetChanged();

        }




    public interface OnViewHolderClicked{

        void viewHolderClicked(View v,int position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contact_item, parent, false);


        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        if (filterCursor != null && filterCursor.getCount() != 0) {


            if (filterCursor.moveToPosition(position)) {

                int contactId=filterCursor.getInt(CURSOR_COLUMN_ID);
                Uri contactUri= ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactId);
                Uri photoThumbUri=Uri.withAppendedPath(contactUri,ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
                boolean hasThumbnail=filterCursor.isNull(CURSOR_PHOTO_THUMBNAIL_URI);
                boolean hasPhoto=filterCursor.isNull(CURSOR_PHOTO_URI);

                String newNameOriginal = filterCursor.getString(CURSOR_DISPLAY_NAME_PRIMARY);
                String newName;
                int maxLength=37;
                if (newNameOriginal.length()>maxLength){
                    newName=newNameOriginal.substring(0,maxLength-4)+"...";
                }else{
                    newName=newNameOriginal;
                }



                if(!hasThumbnail){


                        GlideApp.with((Fragment) mViewHolderClicked)
                                .load(photoThumbUri)
                                .error(R.drawable.checkbox)
                                .circleCrop()
                                .into(holder.mItemImageView);


                    holder.mletterInCircle.setText("");


                }else {
                    if (!hasPhoto) {

                            GlideApp.with((Fragment) mViewHolderClicked)
                                    .load(photoUri)
                                    .error(R.drawable.checkbox)
                                    .circleCrop()
                                    .into(holder.mItemImageView);


                        holder.mletterInCircle.setText("");


                    } else {

                            GlideApp.with((Fragment) mViewHolderClicked)
                                    .load(R.drawable.checkbox)
                                    .error(R.color.colorPrimary)
                                    .circleCrop()
                                    .into(holder.mItemImageView);


                        char firstLetter=newName.trim().charAt(0);
                        char[] data={firstLetter};
                        holder.mletterInCircle.setText(new String(data).toUpperCase());

                    }
                }







                    //oboj slova koja su u upitu
                if(filterString!=null){

                    SpannableString spannableString=new SpannableString(newName);
                    ForegroundColorSpan colorSpan=new ForegroundColorSpan(Color.parseColor( "#ff4081"));

                    int startIndex=(newName.toLowerCase()).indexOf(filterString.toLowerCase());


                    if(startIndex!=-1){

                        int lastIndex=startIndex+filterString.length();


                        spannableString.setSpan(colorSpan,startIndex,lastIndex,0);

                        holder.mItemTextView.setText(spannableString);
                    }else {
                        holder.mItemTextView.setText(newName);}

                }else holder.mItemTextView.setText(newName);




                if (position==0){

                    char firstLetter=newName.charAt(0);
                    char[] data={firstLetter};
                    holder.showSeparator.setVisibility(View.GONE);

                    holder.firstLetter.setText(new String(data));


                }else{
                    if(filterCursor.moveToPosition(position-1)) {
                        String lastName =filterCursor.getString(CURSOR_DISPLAY_NAME_PRIMARY);

                        char lastLetter=lastName.charAt(0);
                        char newLetter=newName.charAt(0);


                        if (!(lastLetter==newLetter)) {
                            char data[]={newLetter};

                            holder.firstLetter.setText(new String(data));
                            holder.showSeparator.setVisibility(View.VISIBLE);

                        }else {

                            holder.firstLetter.setText("");
                            holder.showSeparator.setVisibility(View.GONE);
                        }
                        filterCursor.moveToPosition(position);
                    }
                }



            }
        }
    }


    @Override
    public int getItemCount() {
        if(filterCursor==null){
            return 0;
        }else return filterCursor.getCount();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       private ImageView mItemImageView;
        private TextView mItemTextView;
        private TextView firstLetter;
        private FrameLayout showSeparator;
        private TextView touchLayout;
        private TextView mletterInCircle;


        @SuppressLint("CutPasteId")
         ViewHolder(View view) {
            super(view);
            mItemImageView = view.findViewById(R.id.itemImageView);
            mItemTextView = view.findViewById(R.id.itemTextView);
            firstLetter=view.findViewById(R.id.firstLetterView);
            showSeparator=view.findViewById(R.id.hideandshow);
            touchLayout=view.findViewById(R.id.itemTextView) ;
            touchLayout.setOnClickListener(this);
            mletterInCircle=view.findViewById(R.id.letterInCircle);
        }


        @Override
        public void onClick(View v) {
           int position=getAdapterPosition();

            mViewHolderClicked.viewHolderClicked(v,position);
        }





    }


}
