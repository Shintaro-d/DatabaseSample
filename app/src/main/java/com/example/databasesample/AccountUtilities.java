package com.example.databasesample;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * ユーティリティクラス
 */
public class AccountUtilities extends AppCompatActivity {

    protected int mYear = 0;  //年
    protected int mMonth = 0; //月
    protected int mDay = 0;   //日

    private static Context mContext = null;                 //コンテキスト
    private static MainActivity mMainActivity = null;       //メインアクティビティ
    private static DatabaseHelper mDbh = null;              //データベースヘルパー
    private static com.example.databasesample.AccountRecyclerAdapter mAdapter = null;  //アダプター
    private static String TIME_ZONE = "Asia/Tokyo";         //カレンダーのタイムゾーン

    /**
     * 日付選択の種別
     */
    protected enum CheckDateType{
        MAIN＿START,   //スタート画面（メインアクティビティ）の「表示開始日」選択
        MAIN_END,      //スタート画面（メインアクティビティ）の「表示終了日」選択
        INPUT_EDIT     //データ追加・編集画面の「登録日付」選択
    }
    /**
     * 「テキストビュー」の取得
     * @param id テキストビューID
     * @return テキストビュー
     */
    protected TextView getTv(int id){
        return ((TextView)findViewById(id));
    }

    /**
     * 「エディットテキスト」の取得
     * @param id エディットテキストID
     * @return エディットテキスト
     */
    protected EditText getEt(int id){
        return ((EditText)findViewById(id));
    }

    /**
     * 「ボタン」の取得
     * @param id ボタンID
     * @return ボタン
     */
    protected Button getBtn(int id) { return ((Button)findViewById(id)); }

    /**
     * 追加・編集画面の「日付設定ボタン」タップ時の処理
     * @param year              年
     * @param month             月
     * @param day               日
     * @param inputDateId       「日付選択」ボタンのID
     * @param errorInputDateId  エラー表示用テキストビューのID
     * @param checkDateType     選択ボタンの種別
     */
    protected void setDate(int year, int month, int day, int inputDateId, int errorInputDateId, CheckDateType checkDateType ) {
        boolean result = false;  //チェック結果
        //現在の日付より未来の日付が設定されていないか？
        if (isSelectedDateInTheFuture(year, month, day, errorInputDateId)) {
            if (checkDateType == CheckDateType.INPUT_EDIT) {  //データ追加・編集画面の「登録日付」選択
                result = true;
            } else {
                getTv(errorInputDateId).setText(""); //エラー表示をクリア
                if (checkDateType == CheckDateType.MAIN＿START) {                     //「表示開始日」ボタンを選択
                    Calendar calendar = getCalendar();                                //カレンダーの取得
                    calendar.set(year, month, day,0,0,1);  //選択した「年・月・日」をカレンダーに設定
                    long startTs = calendar.getTimeInMillis();                        //「表示開始日」のタイムスタンプを取得
                    //表示開始日が表示終了日より未来の日付に設定されていないか？
                    if( isStartDateGreaterThanEndDate(startTs) ){
                        getTv(errorInputDateId).setText(R.string.selectFutureDate);
                    } else {
                        getMainActivity().setDisplayStartDate(startTs);  //「表示開始日」のタイムスタンプをメインアクティビティに設定
                        getDB().getData();                               //DBからデータを取得&表示
                        result = true;
                    }

                } else if(checkDateType == CheckDateType.MAIN_END) {                        //「表示終了日」ボタンを選択
                    Calendar calendar = getCalendar();                                      //カレンダーの取得
                    calendar.set(year, month, day, 12, 59, 59);  //選択した「年・月・日」をカレンダーに設定
                    long endTs = calendar.getTimeInMillis();                                //「表示終了日」のタイムスタンプを取得
                    //表示終了日が表示開始日より過去の日付に設定されていないか？
                    if (isEndDateLessThanStartDate(endTs)) {
                        getTv(errorInputDateId).setText(R.string.selectPastDate);
                    } else {
                        getMainActivity().setDisplayLastDate(endTs);     //「表示終了日」のタイムスタンプをメインアクティビティに設定
                        getDB().getData();                               //DBからデータを取得&表示
                        result = true;
                    }
                }
            }
        }

        if( result ) {       //「日付」が正しい場合
            mYear = year;    //「年」を設定
            mMonth = month;  //「月」を設定
            mDay = day;      //「日」を設定
            //メインアクティビティの「表示開始日」または「表示終了日」を表示
            getTv(inputDateId).setText(mYear + AccountUtilities.getStr(R.string.year) + (mMonth + 1) + AccountUtilities.getStr(R.string.month) + mDay + AccountUtilities.getStr(R.string.day));
        }
    }

    /**
     * 設定した「日付」が設定時より未来の日付かどうかをチェック
     * @param year  年
     * @param month 月
     * @param day   日
     * @param errorInputDateId エラー表示用テキストビューID
     * @return チェック結果 true:正、false:誤
     */
    protected boolean isSelectedDateInTheFuture(int year, int month, int day, int errorInputDateId){

        getTv(errorInputDateId).setText("");  //エラー表示をクリア
        Calendar cl = getCalendar();          //カレンダーを取得
        cl.set(year, month, day);             //指定した日付を設定

        Calendar nowCl = getCalendar();      //現在の日付を設定

        //未来の日付に設定されていないか？
        if (nowCl.getTimeInMillis() < cl.getTimeInMillis()) {
            getTv(errorInputDateId).setText(R.string.selectFutureDate);
            return false;
        }
        return true;
    }

    /**
     * 表示開始日が表示終了日より未来の日付に設定されているかをチェック
     * @param  startTs 表示開始日のタイムスタンプ
     * @return チェック結果 true:正、false:誤
     */
    private boolean isStartDateGreaterThanEndDate(long startTs){
        //表示開始日が表示終了日より未来の日付に設定されていないか？
        if( startTs > getMainActivity().getDisplayLastDate()){
            return true;
        }
        return false;
    }

    /**
     * //表示終了日が表示開始日より過去の日付に設定されているかをチェック
     * @param endTs 表示終了日のライムスタンプ
     * @return チェック結果 true:正、false:誤
     */
    private boolean isEndDateLessThanStartDate(long endTs){
        //表示終了日が表示開始日より過去の日付に設定されていないか？
        if( endTs < getMainActivity().getDisplayStartDate()){
            return true;
        }
        return false;
    }

    /**
     * コンテキストを設定（Setter）
     * @param context コンテキスト
     */
    public static void setContext(Context context){
        if ( mContext == null ) {
            mContext = context;
        }
    }

    /**
     * メインアクティビティのインスタンスを取得(Getter）
     * @return
     */
    public static MainActivity getMainActivity(){
        return mMainActivity;
    }

    /**
     * メインアクティビティのインスタンスを設定（Setter）
     * @param mainActivity メインアクティビティ
     */
    public static void setMainActivity(MainActivity mainActivity){
        if( mMainActivity == null) {
            mMainActivity = mainActivity;
        }
    }

    /**
     * データベースヘルパーを取得（Getter）
     * @return データベースヘルパーのインスタンス
     */
    public static DatabaseHelper getDB(){
        return mDbh;
    }

    /**
     * データベースヘルパーを設定（Setter）
     * @param dbh
     */
    public static void setDatabaseHelper(DatabaseHelper dbh){
        if ( mDbh == null ) {
            mDbh = dbh;
        }
    }

    /**
     * リサイクラーアダプターの取得（Getter）
     * @return リサイクラーアダプター
     */
    public static com.example.databasesample.AccountRecyclerAdapter getAdapter() { return mAdapter; }

    /**
     * リサイクルアダプターを設定（Setter）
     * @param adapter
     */
    public static void setAccountRecyclerAdapter(com.example.databasesample.AccountRecyclerAdapter adapter){
        mAdapter = adapter;
    }

    /**
     * トーストにメッセージを表示
     * @param message
     */
    public static void displayMessage(String message){
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();  //トーストを表示
    }

    /**
     * タイムゾーンを設定したカレンダークラスのインスタンスを取得
     * @return
     */
    public static Calendar getCalendar(){
        Calendar calendar = Calendar.getInstance();             //カレンダーの取得
        calendar.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));  //タイムゾーンを設定
        return calendar;
    }

    /**
     * 指定した「文字列ID」の文字列を取得
     * @param id 文字列ID
     * @return 「文字列ID」の文字列
     */
    public static String getStr(int id){
        return mContext.getResources().getString(id);
    }
}
