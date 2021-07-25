package com.example.databasesample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * リサイクラーアダプタークラス
 */
public class AccountRecyclerAdapter extends RecyclerView.Adapter<AccountViewHolder>{
    private ArrayList<AccountData> mLad = null;                           //家計簿データリスト
    private Calendar mCalendar = AccountUtilities.getCalendar();          //カレンダー
    private MainActivity mActivity = AccountUtilities.getMainActivity();  //メインアクティビティ

    /**
     * コンストラクタ
     * @param lad 家計簿データリスト
     */
    public AccountRecyclerAdapter(ArrayList<AccountData> lad) {
        mLad = lad;
    }

    /**
     * ビューホルダー作成時に実行する処理
     * @param parent　　ビューホルダーのビューグループ
     * @param viewType  getItemViewTypeメソッドの返り値
     * @return ビューホルダー
     */
    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //家計簿データ表示用のViewを取得
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_item, parent,false);
        //ビューホルダーの取得
        final AccountViewHolder viewHolder = new AccountViewHolder(inflater);

        //「家計簿データ」タッチ時の処理を設定
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //タッチされた「家計簿データ」の位置を取得
                final int position = viewHolder.getAdapterPosition();
                //アラートダイアログの生成
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AccountUtilities.getMainActivity());
                alertDialog.setTitle(R.string.selectTask);                   //アラートダイアログのタイトル文字列を設定
                alertDialog.setMessage(R.string.askingTaskType);  //アラートダイアログの表示メッセージを設定

                //「編集ボタン」タッチ時の処理
                alertDialog.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        //インテントの生成
                        Intent intent = new Intent(AccountUtilities.getMainActivity().getApplicationContext(), EditAccountDataActivity.class);

                        intent.putExtra("id", mLad.get(position).getId());  //インテントにIDを設定
                        intent.putExtra("position", position);              //インテントにタッチされたアイムのポジションを設定
                        AccountUtilities.getMainActivity().startActivity(intent);  //「編集画面」を表示
                    }});

                //「削除ボタン」タッチ時の処理
                alertDialog.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        //DBのデータを削除
                        AccountUtilities.getDB().deleteData(mLad.get(position),position);
                    }});
                alertDialog.show();
            }
        });
        return viewHolder;
    }

    /**
     * 表示データをビューホルダーに設定
     * @param holder    アカウントビューホルダー
     * @param position  位置
     */
    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        holder.mContent.setText(mLad.get(position).getContent());                   //「内容」を設定
        holder.mPrice.setText(String.valueOf(mLad.get(position).getPrice())+mActivity.getString(R.string.priceUnit));  //「金額」を設定
        mCalendar.setTimeInMillis(mLad.get(position).getDate());                    //カレンダーにタイムスタンプを設定
        //「日付」を設定
        holder.mDate.setText(mCalendar.get(Calendar.YEAR) + mActivity.getString(R.string.year) + (mCalendar.get(Calendar.MONTH) + 1) + mActivity.getString(R.string.month) + mCalendar.get(Calendar.DATE) + mActivity.getString(R.string.day));
    }

    /**
     * データ数の取得
     * @return データ数
     */
    @Override
    public int getItemCount() {
        return mLad.size();
    }

    /**
     * 家計簿データリストから指定位置のデータを削除
     * @param index 削除するデータの位置
     */
    public void deleteAccountDataList(int index){
        mLad.remove(index);       //家計簿データリストから指定位置のデータを削除
        notifyItemRemoved(index); //「削除」後の家計簿データを表示に反映
    }

    /**
     * 家計簿データの取得（Getter）
     * @param position 取得データの位置
     * @return　家計簿データ
     */
    public AccountData getAccountData(int position){
        return mLad.get(position);
    }

    /**
     * 家計簿データの更新
     * @param position 更新データの位置
     * @param content  更新する「内容」
     * @param price    更新する「金額」
     * @param date     更新する「日付（タイムスタンプ）」
     */
    public void updateAccountData(int position,String content, int price, long date ){
        AccountData ad = mLad.get(position);  //更新するデータを取得
        ad.update(content, price, date);      //データを更新
        notifyItemChanged(position, ad);      //「更新」後の内容を表示に反映
    }
}