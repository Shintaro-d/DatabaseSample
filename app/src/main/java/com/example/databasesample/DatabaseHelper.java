package com.example.databasesample;

import android.os.Handler;

import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *   データベースヘルパー
 */
public class DatabaseHelper extends AccountUtilities{
    private AppDatabase mDb;                            //DB
    private AccountDao mDao;                            //DAO
    private AccountData mAd;                            //家計簿データ
    private List<AccountData> mLad = null;                           //家計簿データリスト
    private final String DB_NAME = "account-database";  //DB名
    final Handler mHandler = new Handler();             //ハンドラー

    /**
     *  コンストラクタ
     */
    public DatabaseHelper() {
        //Roomインスタンスを取得
        mDb = Room.databaseBuilder(getMainActivity(), AppDatabase.class, DB_NAME).build();
        //DAOを取得
        mDao = mDb.accountDao();
    }

    /**
     *  DBからデータ取得&表示
     */
    public void getData() {
        //ExecutorServiceを取得
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //ExecutorServiceでタスクを実行（非同期処理）
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    long startDate = getMainActivity().getDisplayStartDate();  //「表示開始日」の取得
                    long lastDate = getMainActivity().getDisplayLastDate();    //「表示終了日」の取得

                    mLad = mDao.getData(startDate, lastDate);  //DBからデータを取得
                } catch (Exception e) {
                    //データ取得エラー時の処理
                    displayMessage(AccountUtilities.getStr(R.string.canNotGetData));
                }

                //データ取得成功時の処理
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //リサイクラービューのデータを更新
                        getMainActivity().updateDataView(mLad);
                        //合計金額の表示
                        getMainActivity().displaySumPrice(mLad);
                    }
                });
            }
        });
    }

    /**
     * DBへデータ追加
     * @param ad 追加する家計簿データ
     */
    public void insertData(AccountData ad){
        mAd = ad;  //家計簿データ

        //ExecutorServiceを取得
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //ExecutorServiceでタスクを実行（非同期処理）
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //会計簿データをDBへ追加
                    mDao.insert(mAd);
                } catch (Exception e) {
                    //データ追加エラー時の処理
                    displayMessage(AccountUtilities.getStr(R.string.canNotAddData));
                }

                //データ追加成功時の処理
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        displayMessage(AccountUtilities.getStr(R.string.successRegistData));
                        getData();                                //DBからデータを取得
                        getMainActivity().displaySumPrice(mLad);  //合計金額の表示
                    }
                });
            }
        });
    }

    /**
     * DBのデータを更新
     * @param position  更新データの位置
     * @param content   内容
     * @param price     価格
     * @param date      日付
     */
    public void updateData(int position, String content, int price, long date){
        //ExecutorServiceの処理に渡す値の定数
        final String pContent = content;  //内容
        final int pPrice = price;         //価格
        final long pDate = date;          //日付
        final int pPosition = position;   //データの位置

        //ExecutorServiceを取得
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //ExecutorServiceでタスクを実行（非同期処理）
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AccountData updateAd = null;  //更新する家計簿データ
                    try {
                        //アダプター内の家計簿データを更新＆更新データの取得
                        updateAd = getAdapter().getAccountData(pPosition).update(pContent, pPrice, pDate);
                    } catch (Exception e){
                        //データ更新エラー時の処理
                        displayMessage(AccountUtilities.getStr(R.string.canNotUpdateData));
                    }
                    //DBのデータを更新
                    mDao.update(updateAd);
                } catch (Exception e) {
                    //データ更新エラー時の処理
                    displayMessage(AccountUtilities.getStr(R.string.canNotUpdateData));
                }

                //データ更新成功時の処理
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        displayMessage(AccountUtilities.getStr(R.string.successUpdateData));
                        getMainActivity().displaySumPrice(mLad);  //合計金額の表示
                    }
                });
            }
        });
    }

    /**
     * DBのデータを削除
     * @param ad        削除する家計簿データ
     * @param position  データの位置
     */
    public void deleteData(AccountData ad, final int position){
        //ExecutorServiceの処理に渡す値の定数
        mAd = ad;

        //ExecutorServiceを取得
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //ExecutorServiceでタスクを実行（非同期処理）
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //DBのデータを削除
                    mDao.delete(mAd);
                    //アダプター内の家計簿データを削除
                    getAdapter().deleteAccountDataList(position);
                } catch (Exception e) {
                    //データ削除エラー時の処理
                    displayMessage(AccountUtilities.getStr(R.string.canNotDeleteData));
                }

                //データ削除成功時の処理
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        displayMessage(AccountUtilities.getStr(R.string.successDeleteData));
                        getMainActivity().displaySumPrice(mLad);  //合計金額の表示
                    }
                });
            }
        });
    }
}