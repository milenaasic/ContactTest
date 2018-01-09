package com.vertial.veritel;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;


public class MyContactRecyclerViewAdapter extends RecyclerView.Adapter<MyContactRecyclerViewAdapter.ViewHolder> {

    private static final String LOG="MyConttRecViewAdapter";


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
        Log.v(LOG,"adapter konstruktor");
        filterCursor=cursor;
        mViewHolderClicked = listener;

    }

    void setFilterCursorAndFilterString(Cursor c, String filter){

        if (c!=null) {
                filterCursor = c;
                Log.v(LOG,"setovani cursor nije null");

                int n=c.getCount();
                Log.v(LOG,((Integer)n).toString());
        }

        filterString=filter;

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

                Log.v(LOG,"ima thumbnail :"+((Boolean)hasThumbnail).toString()+" , posizija "+position);
                Log.v(LOG,"ima sliku :"+((Boolean)hasPhoto).toString()+" , posizija "+position);


                //ucitavanje slike
                RequestOptions options = new RequestOptions();
                options.circleCrop();

                if(!hasThumbnail){

                    // ako je API>=21 ucitaj drawable i iseci krug
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                        GlideApp.with((Fragment) mViewHolderClicked)
                                .load(photoThumbUri)
                                .error(R.drawable.veritel_bacground_manji_opseg)
                                .circleCrop()
                                .into(holder.mItemImageView);

                    }else{
                        //nemoj da seces krug neka bude kvadrat
                        GlideApp.with((Fragment) mViewHolderClicked)
                                .load(photoThumbUri)
                                .error(R.color.colorBetweenPrimaryAndPrimaryLight3)
                                .into(holder.mItemImageView);

                    }



                    holder.mletterInCircle.setText("");

                    Log.v(LOG,photoThumbUri+" photoThumbUri");

                }else {
                    if (!hasPhoto) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {

                            GlideApp.with((Fragment) mViewHolderClicked)
                                    .load(photoUri)
                                    .error(R.drawable.veritel_bacground_manji_opseg)
                                    .circleCrop()
                                    .into(holder.mItemImageView);

                        }else {//nemoj da seces krug neka bude kvadrat
                            GlideApp.with((Fragment) mViewHolderClicked)
                                    .load(photoUri)
                                    .error(R.color.colorBetweenPrimaryAndPrimaryLight3)
                                    .into(holder.mItemImageView);
                        }


                        holder.mletterInCircle.setText("");
                        Log.v(LOG,"big photo loaded");

                    } else {


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                            GlideApp.with((Fragment) mViewHolderClicked)
                                    .load(R.drawable.veritel_background_cyan_only)
                                    .error(R.color.colorPrimary)
                                    .circleCrop()
                                    .into(holder.mItemImageView);

                        }else {

                            GlideApp.with((Fragment) mViewHolderClicked)
                                    .load(R.drawable.veritel_bacground_manji_opseg)
                                    .error(R.color.colorBetweenPrimaryAndPrimaryLight3)
                                    .into(holder.mItemImageView);

                        }

                        Log.v(LOG,"krug sa slovom");
                        char firstLetter=newName.trim().charAt(0);
                        char[] data={firstLetter};
                        holder.mletterInCircle.setText(new String(data).toUpperCase());

                    };
                }





                    Log.v(LOG, "string iz cursora" + newName);
                    Log.v(LOG,"filter string "+filterString);

                    //oboj slova koja su u upitu
                if(filterString!=null){

                    SpannableString spannableString=new SpannableString(newName);
                    ForegroundColorSpan colorSpan=new ForegroundColorSpan(Color.parseColor( "#ff4081"));

                    int startIndex=(newName.toLowerCase()).indexOf(filterString.toLowerCase());
                    Log.v(LOG,"start ineks je "+((Integer)startIndex).toString());

                    if(startIndex!=-1){

                        int lastIndex=startIndex+filterString.length();
                        Log.v(LOG,"last ineks je "+((Integer)lastIndex).toString());

                        spannableString.setSpan(colorSpan,startIndex,lastIndex,0);
                        Log.v(LOG,"uradio spannableString");
                        holder.mItemTextView.setText(spannableString);
                    }else {
                        holder.mItemTextView.setText(newName);}

                }else holder.mItemTextView.setText(newName);


                                  Log.v(LOG, "setovan text ");

                if (position==0){

                    char firstLetter=newName.charAt(0);
                    char[] data={firstLetter};
                    holder.showSeparator.setVisibility(View.GONE);

                    holder.firstLetter.setText(new String(data));
                    Log.v(LOG, "pozicija 0 "+new String(data));

                }else{
                    if(filterCursor.moveToPosition(position-1)) {
                        String lastName =filterCursor.getString(CURSOR_DISPLAY_NAME_PRIMARY);
                        Log.v(LOG, "pozicija nije 0 "+lastName);
                        char lastLetter=lastName.charAt(0);
                        char newLetter=newName.charAt(0);
                        Log.v(LOG, "pozicija nije 0 staro prvo slovo "+((Character)lastLetter).toString());
                        Log.v(LOG, "pozicija nije 0 novi prvo slovo "+((Character)newLetter).toString());

                        if (!(lastLetter==newLetter)) {
                            char data[]={newLetter};

                            holder.firstLetter.setText(new String(data));
                            holder.showSeparator.setVisibility(View.VISIBLE);

                            Log.v(LOG, "pozicija nije 0 prvo slovo "+new String(data));
                        }else {

                            holder.firstLetter.setText(new String(""));
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

        public ImageView mItemImageView;
        public TextView mItemTextView;
        public TextView firstLetter;
        public FrameLayout showSeparator;
        public TextView touchLayout;
        public TextView mletterInCircle;


        public ViewHolder(View view) {
            super(view);
            mItemImageView = (ImageView) view.findViewById(R.id.itemImageView);
            mItemTextView = (TextView) view.findViewById(R.id.itemTextView);
            firstLetter=(TextView) view.findViewById(R.id.firstLetterView);
            showSeparator=(FrameLayout)view.findViewById(R.id.hideandshow);
            touchLayout=(TextView)view.findViewById(R.id.itemTextView) ;
            touchLayout.setOnClickListener(this);
            mletterInCircle=(TextView)view.findViewById(R.id.letterInCircle);
        }


        @Override
        public void onClick(View v) {
           int position=getAdapterPosition();

            mViewHolderClicked.viewHolderClicked(v,position);
        }





    }


}
