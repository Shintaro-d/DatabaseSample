package com.example.databasesample;

import android.app.Activity;
import android.view.View;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * データ追加・編集画面用ユーティリティクラス
 */
public class AccountDataUtilities extends AccountUtilities {
    protected DatabaseHelper mDbh = new DatabaseHelper(); //データベースヘルパー

    /**
     * コンストラクタ
     */
    public AccountDataUtilities() {
        mDbh = AccountUtilities.getDB();  //データベースヘルパーを取得
    }

    /**
     * 「内容」のチェック
     * @param inputContentId      「内容」のエディットテキストID
     * @param errorInputContentId 「内容」のエラー表示用テキストビューID
     * @return チェック結果
     */
    protected boolean checkInputContent(int inputContentId, int errorInputContentId){
        //「内容」をチェック
        if( getEt(inputContentId).getText().toString().length() == 0 ){
            getTv(errorInputContentId).setText(R.string.noticeInputName);
            return false;
        }
        return true;
    }

    /**
     * 「価格」のチェック
     * @param inputPriceId      「価格」のエディットテキストID
     * @param errorInputPriceId 「価格」のエラー表示用テキストビューID
     * @return チェック結果
     */
    protected boolean checkInputPrice(int inputPriceId, int errorInputPriceId){
        String price = getEt(inputPriceId).getText().toString();    //「金額」を取得
        Pattern pattern = Pattern.compile("^[0-9]+$");              //「金額」の正規表現パターンを生成
        Matcher matcher = pattern.matcher(price);                   //マッチャーの生成
        String errorMessage = getString(R.string.inputOnlyNumber);       //エラーメッセージの設定
        boolean result = matcher.matches();                         //「金額」のチェック
        try{
            //入力値を整数型（Integer）に変換
            Integer.parseInt(price);
        }catch (NumberFormatException e){
            result = false;
            errorMessage = getString(R.string.InputMaxPrice);  //エラーメッセージの設定
        } catch(Exception e){
            result = false;
        }

        if ( !result ){
            getTv(errorInputPriceId).setText(errorMessage);   //エラーメッセージを表示
        }
        return result;
    }

    /**
     * 「現在の日付」をテキストビューに表示
     * @param tvDisplayDate 「日付」表示用テキストビューID
     */
    protected void setCurrentTime(int tvDisplayDate){
        Calendar cl = getCalendar();      //カレンダーを取得
        mYear = cl.get(Calendar.YEAR);    //「年」を取得
        mMonth = cl.get(Calendar.MONTH);  //「月」を取得
        mDay = cl.get(Calendar.DATE);     //「日」を取得

        //「現在の日付」をテキストビューに表示
        getTv(tvDisplayDate).setText(mYear + getString(R.string.year) + (mMonth + 1)
                + getString(R.string.month) + mDay + getString(R.string.day));
    }

    /**
     * 現在のタイムスタンプを取得
     * @return 現在のタイムスタンプ
     */
    protected long getCurrentTimestamp(){
        Calendar cl = getCalendar();  //カレンダーの取得
        cl.set(mYear, mMonth, mDay);  //カレンダーに「年・月・日」を設定
        return cl.getTimeInMillis();
    }

    /**
     * エラーメッセージをクリア
     * @param errorInputContentTvId 「内容」のエラー表示用テキストビューID
     * @param errorInputPriceTvId   「価格」のエラー表示用テキストビューID
     * @param errorInputDateTvId    「日付」のエラー表示用テキストビューID
     */
    protected void clearError(int errorInputContentTvId, int errorInputPriceTvId, int errorInputDateTvId){
        getTv(errorInputContentTvId).setText("");  //「内容」のエラー表示をクリア
        getTv(errorInputPriceTvId).setText("");    //「価格」のエラー表示をクリア
        getTv(errorInputDateTvId).setText("");     //「日付」のエラー表示をクリア
    }

    /**
     * 「日付設定ボタン」のイベントリスナー設定
     * @param activity          リスナーを設定するアクティビティ
     * @param setDateBtnId     「日付設定」ボタンID
     * @param errorInputDateId 「日付」のエラー表示用テキストビューID
     */
    protected void setDateEventListener(Activity activity, int setDateBtnId, int inputDateId, int errorInputDateId, int year, int month, int day){
        //「日付設定ボタン」のイベントリスナーに渡す値の定数
        final Activity pActivity = activity;             //リスナーを設定するアクティビティ
        final int pInputDateId = inputDateId;            //「日付」表示用テキストビューID
        final int pErrorInputDateId = errorInputDateId;  //「日付」のエラー表示用テキストビューID
        final int pYear = year;                          //「年」
        final int pMonth = month;                        //「月」
        final int pDay = day;                            //「日」

        //「日付設定ボタン」のイベントリスナー
        getBtn(setDateBtnId).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //DatePickerFragmentを生成
                        DatePickerFragment dpFragment = new DatePickerFragment(pActivity, pInputDateId, pErrorInputDateId, pYear, pMonth, pDay, CheckDateType.INPUT_EDIT);
                        //「日付設定」ダイアログの表示
                        dpFragment.show(getSupportFragmentManager(), "datePicker");
                    }
                }
        );
    }
}