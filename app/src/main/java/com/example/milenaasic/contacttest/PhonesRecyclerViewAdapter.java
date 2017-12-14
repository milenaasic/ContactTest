package com.example.milenaasic.contacttest;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


class PhonesRecyclerViewAdapter extends RecyclerView.Adapter<PhonesRecyclerViewAdapter.ViewHolder> {

    public static final String DEBUG="PhonesRecyclerView";


    private static final int COLUMN_PHONE_NUMBER =1 ;
    private static final int COLUMN_PHONE_TYPE =2 ;


    Cursor phoneListCursor;
    private static OnPhoneViewHolderClicked onPhoneViewHolderClicked;



    public PhonesRecyclerViewAdapter(Cursor cursor,OnPhoneViewHolderClicked listener) {

        phoneListCursor=cursor;
        onPhoneViewHolderClicked=listener;

    }

    void setPhoneCursor(Cursor cursor){
        phoneListCursor= cursor;
        notifyDataSetChanged();
        Log.v(DEBUG,"notifyDataset");

    }

    public interface OnPhoneViewHolderClicked{

        public void onPhoneItemClicked(String phoneNumberToCall);


    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_item, parent, false);
        Log.v(DEBUG,"onCreateViewHolder");

        return new PhonesRecyclerViewAdapter.ViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (phoneListCursor != null && phoneListCursor.getCount() != 0) {

            if (phoneListCursor.moveToPosition(position)) {

                String phoneNumber=phoneListCursor.getString(COLUMN_PHONE_NUMBER);
                String phoneType=phoneListCursor.getString(COLUMN_PHONE_TYPE);
                holder.mPhoneNumberTextView.setText(phoneNumber);
                holder.mPhoneTypeTextView.setText(phoneType);

            }



        }


    }

    @Override
    public int getItemCount() {
        if(phoneListCursor==null){
            return 0;
        }else return phoneListCursor.getCount();


    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mPhoneNumberTextView;
        public TextView mPhoneTypeTextView;


        public ViewHolder(View view) {
            super(view);

            mPhoneNumberTextView = (TextView) view.findViewById(R.id.phoneNumberTextView);
            mPhoneTypeTextView=(TextView)view.findViewById(R.id.phoneTypeTextView);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();

           onPhoneViewHolderClicked.onPhoneItemClicked(mPhoneNumberTextView.getText().toString());
            //prosledi u DetailFragment sta je odabrano


        }
    }




}
