package com.vertial.veritel;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


class PhonesRecyclerViewAdapter extends RecyclerView.Adapter<PhonesRecyclerViewAdapter.ViewHolder> {

    public static final String DEBUG="PhonesRecyclerView";


    private static final int COLUMN_PHONE_NUMBER =1 ;
    private static final int COLUMN_PHONE_TYPE =2 ;


    Cursor phoneListCursor;
    private OnPhoneViewHolderClicked onPhoneViewHolderClicked;
    private ArrayList<PhoneObject> dataSet=new ArrayList<>(15);


    public PhonesRecyclerViewAdapter(Cursor cursor,OnPhoneViewHolderClicked listener) {

        phoneListCursor = cursor;
        onPhoneViewHolderClicked = listener;
        setDataSet(phoneListCursor);
    }


    void setDataSet(Cursor phoneCursor){

        if(phoneCursor!=null){

            for(int index=0;index<phoneCursor.getCount();index++){
                Log.v(DEBUG,"index="+index);
                if(phoneCursor.moveToPosition(index)&&!phoneCursor.isNull(1)){
                    // prvi red Cursora
                    if(index==0){
                        int type=0;
                        String phone=phoneCursor.getString(1);
                        if( !phoneCursor.isNull(2)) {
                             type =phoneCursor.getInt(2);
                        }

                        PhoneObject phoneObject=new PhoneObject(phone,type);
                        dataSet.add(phoneObject);
                        Log.v(DEBUG,"index=0"+index);
                    }else{

                        String currentPhone=phoneCursor.getString(1);
                        boolean hasPhoneNumber=false;

                        //provera da li je ovo dupliran telefon
                        for (int n=0;n<dataSet.size();n++){
                            Log.v(DEBUG,"dataset size je "+dataSet.size());
                            if(((dataSet.get(n)).phoneNumber).equals(currentPhone)){
                                hasPhoneNumber=true;
                            }

                        }

                        if(!hasPhoneNumber){
                            int type=0;
                            String phone=phoneCursor.getString(1);
                            if( !phoneCursor.isNull(2)) {
                                type =phoneCursor.getInt(2);
                            }

                            PhoneObject phoneObject=new PhoneObject(phone,type);
                            dataSet.add(phoneObject);

                        }

                    }

                }

            }

        }

    }

    void setPhoneCursor(Cursor cursor){
        phoneListCursor= cursor;
        setDataSet(phoneListCursor);
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

        PhoneObject currentPhoneObject=dataSet.get(position);
        holder.mPhoneNumberTextView.setText(currentPhoneObject.phoneNumber);
        holder.mPhoneTypeTextView.setText(returnType(currentPhoneObject.typeOfNumber));


        /*if (phoneListCursor != null && phoneListCursor.getCount() != 0) {

            if (phoneListCursor.moveToPosition(position)) {

                String phoneNumber=phoneListCursor.getString(COLUMN_PHONE_NUMBER);
                String phoneType=phoneListCursor.getString(COLUMN_PHONE_TYPE);
                holder.mPhoneNumberTextView.setText(phoneNumber);
                holder.mPhoneTypeTextView.setText(phoneType);

                [position]=phoneNumber;
                Log.v(DEBUG,"position " +position);
                boolean repeatedPhoneNumber=false;

                for (int i =0;i<position;i++){

                        if(phoneNumber.equals(arrayDifferentPhones[i])){
                            repeatedPhoneNumber=true;
                        }

                }
            //ako vec postoji ovaj broj izbaci ViewHolder

                 holder.mView.setVisibility(repeatedPhoneNumber ? View.GONE : View.VISIBLE);

            }

        }*/

    }

    @Override
    public int getItemCount() {
        if(dataSet==null){
            return 0;
        }else return dataSet.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mPhoneNumberTextView;
        public TextView mPhoneTypeTextView;
        public View mView;


        public ViewHolder(View view) {
            super(view);
            mView=view;
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


    private class PhoneObject {

        String phoneNumber;
        int typeOfNumber;

        PhoneObject(String s, int i){

            phoneNumber=s;
            typeOfNumber=i;
        }

        PhoneObject(String s){

            phoneNumber=s;
            typeOfNumber=10;
        }

    }

    private String returnType(int n){

        switch (n){

            case 1: {return "Home";}
            case 2: {return "Mobile";}
            case 3: {return "Work";}
            case 4: {return "Work Fax";}
            case 5: {return "Home Fax";}
            case 6: {return "Pager";}
            case 7: {return "Other";}
            case 8: {return "Callback";}
            default:return "";

        }


    }




}
