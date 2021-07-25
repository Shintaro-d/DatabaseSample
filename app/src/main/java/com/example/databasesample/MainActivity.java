package com.example.databasesample;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databasesample.AccountData;
import com.example.databasesample.AccountItemDecoration;
import com.example.databasesample.AccountRecyclerAdapter;
import com.example.databasesample.AccountUtilities;
import com.example.databasesample.DatabaseHelper;
import com.example.databasesample.DatePickerFragment;
import com.example.databasesample.R;
import com.example.databasesample.RegistAccountDataActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *  メインアクティビティ
 */
public class MainActivity extends AccountUtilities {
    private RecyclerView mAccountRecyclerView;                  //リサイクラービュー
    private RecyclerView.Adapter mRecyclerAdapter;              //アダプター
    private RecyclerView.LayoutManager mRecyclerLayoutManager;  //レイアウトマネージャー
    private long mDisplayStartDate = 0;                         //表示開始時刻（タイムスタンプ)
    private long mDisplayLastDate = 0;                          //表示終了時刻（タイムスタンプ)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AccountUtilitiesクラスへインスタンスを設定
        setContext(getApplicationContext());      //コンテキストを設定
        setMainActivity(this);                    //メインアクティビティを設定
        setDatabaseHelper(new DatabaseHelper());  //データベースヘルパーを設定

        //リサイクラービュー関連
        mAccountRecyclerView = (RecyclerView) findViewById(R.id.MaAccountRecyclerView);   //リサイクラービューの取得
        //アダプターがリサイクラービューのサイズに影響を与えない場合は true を設定
        mAccountRecyclerView.setHasFixedSize(true);
        mAccountRecyclerView.addItemDecoration(new AccountItemDecoration(this));  //AccountItemDecoration（表示内容の装飾）を設定
        mRecyclerLayoutManager = new LinearLayoutManager(this);                   //レイアウトマネージャーの生成
        mAccountRecyclerView.setLayoutManager(mRecyclerLayoutManager);                    //リサイクラービューにレイアウトマネージャーを設定
        setInitDisplayDate();                                                             //初回表示時の表示期間を設定
        displayPeriod();                                                                  //「表示開始日」と「表示終了日」を表示
        getDB().getData();                                                                // DBからデータを取得

        //「データ登録ボタン」のイベントリスナー
        getBtn(R.id.MaRegistDataBtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //「データ登録ボタン」がクリックされたら「データ登録」用アクティビティを表示
                        startActivity(new Intent(getApplication(), RegistAccountDataActivity.class));
                    }
                }
        );


        Calendar calendar = getCalendar();              //カレンダークラスのインスタンスを取得
        calendar.setTimeInMillis(mDisplayStartDate);    //カレンダーに「表示開始日（タイムスタンプ）」を設定
        int year = calendar.get(Calendar.YEAR);         //「年」の取得
        int month = calendar.get(Calendar.MONTH);       //「月」の取得
        int day = calendar.get(Calendar.DAY_OF_MONTH);  //「日」の取得

        //「表示開始日」設定ボタンのイベントリスナー設定
        setDateEventListener(this, R.id.MaSelectStartDateBtn, R.id.MaStartDateTv, R.id.MaErrorDateTv, year, month, day, CheckDateType.MAIN＿START);

        //「表示終了日」設定ボタンのイベントリスナー設定
        setDateEventListener(this, R.id.MaSelectEndDateBtn, R.id.MaEndDateTv, R.id.MaErrorDateTv, year, month, day, CheckDateType.MAIN_END);
    }

    /**
     * リサイクラービューを更新
     * @param lad 家計簿データセット
     */
    public void updateDataView(List<AccountData> lad){
        ArrayList<AccountData> alad= (ArrayList<AccountData>)lad;              //ArrayListへ変換
        mRecyclerAdapter = new AccountRecyclerAdapter(alad);                   //リサイクラーアダプターにデータを設定
        setAccountRecyclerAdapter((AccountRecyclerAdapter) mRecyclerAdapter);  //AccountUtilitiesクラスへアダプターを設定
        mAccountRecyclerView.setAdapter(mRecyclerAdapter);                     //リサイクラービューにアダプターを設定
        mRecyclerAdapter.notifyDataSetChanged();                               //リサイクラービューの表示を更新
    }

    /**
     * 初回表示時の表示期間を設定
     */
    private void setInitDisplayDate(){
        Calendar calendar = getCalendar();               //カレンダーを取得
        mDisplayLastDate = calendar.getTimeInMillis();   //現在時刻のタイムスタンプを取得し、「表示終了日」に設定
        calendar.set(Calendar.DAY_OF_MONTH,1);           //「表示開始日」の「日」を設定（その月の「1日」を設定）
        calendar.set(Calendar.HOUR_OF_DAY,0);            //「表示開始日」の「時」を設定（「０時」を設定）
        calendar.set(Calendar.MINUTE,0);                 //「表示開始日」の「分」を設定（「０分」を設定）
        mDisplayStartDate = calendar.getTimeInMillis();  //「表示開始日」を設定
    }

    /**
     * 表示データの合計金額を算出
     * @param lad 家計簿データセット
     */
    public void displaySumPrice(List<AccountData> lad){
        long sum = 0;
        for( AccountData ad : lad){
            sum += ad.getPrice();
        }
        getTv(R.id.MaSumPriceTv).setText(getString(R.string.priceSum) + String.valueOf(sum) + getString(R.string.priceUnit));
    }

    /**
     * 「表示期間」の「開始日」と「終了日」を表示
     */
    private void displayPeriod(){
        displayDate(R.id.MaStartDateTv, mDisplayStartDate);
        displayDate(R.id.MaEndDateTv, mDisplayLastDate);
    }

    /**
     * 指定IDのテキストビューへ時刻を表示
     * @param id        テキストビューID
     * @param timestamp タイムスタンプ
     */
    private void displayDate(int id, long timestamp){
        Calendar calendar = getCalendar();              //カレンダーを取得
        calendar.setTimeInMillis(timestamp);            //「タイムスタンプ」をカレンダーに設定
        int year = calendar.get(Calendar.YEAR);         //「年」を取得
        int month = calendar.get(Calendar.MONTH);       //「月」を取得
        int day = calendar.get(Calendar.DAY_OF_MONTH);  //「日」を取得
        getTv(id).setText(year + getString(R.string.year) + (month + 1) + getString(R.string.month) + day + getString(R.string.day));  //テキストビューへ表示
    }

    /**
     * 「日付設定」ボタンのイベントリスナー設定
     * @param activity          リスナーを設定するアクティビティ
     * @param setDateBtnId     「日付設定」ボタンID
     * @param setDateBtnId      イベントリスナーを設定するボタンID
     * @param errorInputDateId 「日付」のエラー表示用テキストビューID
     * @param year              「年」
     * @param month             「月」
     * @param day               「日」
     * @param dateType           「設定する日付」の種類
     */
    protected void setDateEventListener(Activity activity, int setDateBtnId, int inputDateId, int errorInputDateId, int year, int month, int day, AccountUtilities.CheckDateType dateType){
        //匿名クラス（View.OnClickListener）に渡す値の定数
        final Activity pActivity = activity;
        final int pInputDateId = inputDateId;
        final int pErrorInputDateId = errorInputDateId;
        final int pYear = year;
        final int pMonth = month;
        final int pDay = day;
        final AccountUtilities.CheckDateType pDateType = dateType;

        //「日付設定ボタン」のイベントリスナー
        getBtn(setDateBtnId).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //DatePickerFragmentクラスのインスタンスを生成
                        DatePickerFragment dpFragment = new DatePickerFragment(pActivity, pInputDateId, pErrorInputDateId, pYear, pMonth, pDay, pDateType);
                        //DatePickerFragment（日付設定画面）を表示
                        dpFragment.show(getSupportFragmentManager(), "datePicker");
                    }
                }
        );
    }

    /**
     * 「表示開始日」のタイムスタンプを取得(Getter)
     * @return 「表示開始日」のタイムスタンプ
     */
    public long getDisplayStartDate() {
        return mDisplayStartDate;
    }

    /**
     * 「表示開始日」のタイムスタンプを設定(Setter)
     * @param displayStartDate
     */
    public void setDisplayStartDate(long displayStartDate) {
        mDisplayStartDate = displayStartDate;
    }

    /**
     * 「表示終了日」のタイムスタンプを取得(Getter)
     * @return 「表示終了日」のタイムスタンプ
     */
    public long getDisplayLastDate() {
        return mDisplayLastDate;
    }

    /**
     * 「表示終了日」のタイムスタンプを取得(Setter)
     * @param displayLastDate 「表示終了日」のタイムスタンプ
     */
    public void setDisplayLastDate(long displayLastDate) {
        mDisplayLastDate = displayLastDate;
    }
}