package com.example.databasesample;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databasesample.R;

/**
 * RecyclerViewのViewHolder
 */
public class AccountViewHolder extends RecyclerView.ViewHolder {
    public TextView mContent;  //「内容」
    public TextView mPrice;    //「価格」
    public TextView mDate;     //「日付」

    //「内容」を取得（getter）
    public TextView getContent() {
        return mContent;
    }

    //「価格」を取得（getter）
    public TextView getPrice() {
        return mPrice;
    }

    //「日付」を取得（getter）
    public TextView getDate() {
        return mDate;
    }

    /**
     * constructor
     * @param itemView　表示項目のView
     */
    public AccountViewHolder(@NonNull View itemView) {
        super(itemView);
        mContent = (TextView)itemView.findViewById(R.id.tvContent);  //「内容」のウィジェットを取得
        mPrice = (TextView)itemView.findViewById(R.id.tvPrice);      //「価格」のウィジェットを取得
        mDate = (TextView)itemView.findViewById(R.id.tvDate);        //「日付」のウィジェットを取得
    }
}