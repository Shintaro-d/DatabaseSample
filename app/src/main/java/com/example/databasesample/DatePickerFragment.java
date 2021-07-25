package com.example.databasesample;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

/**
 *  「日付選択画面」のフラグメント
 */
public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener{

    private Activity mActivity;         //アクティビティ
    private int mInputDateId = 0;       //日付設定ボタンのID
    private int mErrorInputDateId = 0;  //日付設定エラー表示用テキストビューのID
    private int mYear = 0;              //「年」
    private int mMonth = 0;             //「月」
    private int mDay = 0;               //「日」

    private AccountUtilities.CheckDateType mCheckType;  //「日付設定ボタン」の種別

    /**
     * コンストラクタ
     * @param activity           表示するアクティビティ
     * @param inputDateId        日付設定ボタンのID
     * @param errorInputDateId   エラーを表示するテキストビューのID
     * @param year               年
     * @param month              月
     * @param day                日
     * @param checkType          日付設定ボタンの種別
     */
    public DatePickerFragment(Activity activity, int inputDateId, int errorInputDateId, int year, int month, int day, AccountUtilities.CheckDateType checkType) {
        mActivity = activity;                  //アクティビティを設定
        mInputDateId = inputDateId;            //日付設定ボタンのIDを設定
        mErrorInputDateId = errorInputDateId;  //エラーを表示するテキストビューのIDを設定
        mYear = year;                          //年を設定
        mMonth = month;                        //月を設定
        mDay = day;                            //日を設定
        mCheckType = checkType;                //日付設定ボタンの種別を設定
    }

    /**
     * DatePickerFragmentのインスタンス生成時の処理
     * @param savedInstanceState Bundleインスタンス
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // DatePickerDialogのインスタンスを生成
        return new DatePickerDialog((AccountUtilities) mActivity,
                this, mYear, mMonth, mDay);
    }

    /**
     * 日付が選択された時の処理
     * @param view         //DatePickerのview
     * @param year         //年
     * @param monthOfYear  //月
     * @param dayOfMonth   //日
     */
    @Override
    public void onDateSet(android.widget.DatePicker view, int year,
                          int monthOfYear, int dayOfMonth) {
        //日付が選択された時の処理を設定
        ((AccountUtilities) mActivity).setDate(year, monthOfYear, dayOfMonth, mInputDateId, mErrorInputDateId, mCheckType);
    }
}
