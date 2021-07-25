package com.example.databasesample;

import android.os.Bundle;
import android.view.View;
import java.util.Calendar;

/**
 *  「データ登録」画面のアクティビティ
 */
public class RegistAccountDataActivity extends AccountDataUtilities {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_account_data);

        setCurrentTime(R.id.RaDisplayDateTv);             //現在時刻をフィールドに保持

        final Calendar calendar = getCalendar();        //カレンダーを取得
        int year = calendar.get(Calendar.YEAR);         //「年」を取得
        int month = calendar.get(Calendar.MONTH);       //「月」を取得
        int day = calendar.get(Calendar.DAY_OF_MONTH);  //「日」を取得

        //「日付設定ボタン」のイベントリスナーを設定
        setDateEventListener(this, R.id.RaSetDateBtn, R.id.RaDisplayDateTv, R.id.RaErrorInputDateTv, year, month, day);

        //「データ登録ボタン」のイベントリスナーを設定
        getBtn(R.id.RaInputScrRegistDataBtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //エラー表示をクリア
                        clearError(R.id.RaErrorInputContentTv, R.id.RaErrorInputPriceTv, R.id.RaErrorInputDateTv);

                        //現在のタイムスタンプを取得
                        long date = getCurrentTimestamp();

                        //入力された「内容」「価格」のチェック
                        boolean resultCheckContent = checkInputContent(R.id.RaInputContentEt,R.id.RaErrorInputContentTv);
                        boolean resultCheckPrice = checkInputPrice(R.id.RaInputPriceEt,R.id.RaErrorInputPriceTv);

                        if( resultCheckContent && resultCheckPrice ){
                            //「内容」の取得
                            String content = getEt(R.id.RaInputContentEt).getText().toString();

                            //「価格」の取得
                            int price = Integer.parseInt(getEt(R.id.RaInputPriceEt).getText().toString());

                            //追加データの生成
                            AccountData ad = new AccountData(content, price, date);

                            //DBへデータを追加
                            mDbh.insertData(ad);

                            //「データ登録画面」を非表示
                            finish();
                        }
                    }
                }
        );
    }
}
