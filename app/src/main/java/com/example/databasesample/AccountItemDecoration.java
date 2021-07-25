package com.example.databasesample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databasesample.R;

/**
 * 家計簿データの表示内容装飾用クラス
 */
public class AccountItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider; //ディバイダー

    /**
     * コンストラクタ
     * @param context コンテキスト
     */
    public AccountItemDecoration(Context context) {
        //ディバイダーの取得
        mDivider = context.getResources().getDrawable(R.drawable.under_border);;
    }

    /**
     * 装飾内容の描画
     * @param c       キャンバス
     * @param parent  リサイクラービュー
     * @param state   現在のリサイクラービューの状態
     */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        //リサイクラービューのデータ数を取得
        int childCount = parent.getChildCount();

        int left = parent.getPaddingLeft()+10;                        //表示用ビューの左の余白を算出
        int right = parent.getWidth() - parent.getPaddingRight()-10;  //表示用ビューの右の余白を算出

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);  //表示用ビューの取得

            //レイアウトパラメータの取得
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;  //表示用ビューの上の余白を算出
            int bottom = top + mDivider.getIntrinsicHeight();   //表示用ビューの下の余白を算出
            mDivider.setBounds(left,top,right,bottom);          //余白の算出値を設定
            mDivider.draw(c);                                   //描画
        }
    }
}