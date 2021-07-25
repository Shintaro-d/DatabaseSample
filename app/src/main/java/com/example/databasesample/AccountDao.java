package com.example.databasesample;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Room(SQLite)用データベース操作インターフェース
 */
@Dao
public interface AccountDao {
    /**
     * 指定した期間のデータを取得
     * @param startDate 表示開始日
     * @param lastDate  表示終了日
     * @return 家計簿データリスト
     */
    @Query("SELECT * FROM account_data WHERE date > :startDate AND date < :lastDate ORDER BY date ASC")
    List<AccountData> getData(long startDate, long lastDate);

    /**
     * データを追加
     * @param ad 追加データ
     */
    @Insert
    void insert(AccountData ad);

    /**
     * データを更新
     * @param ad 更新データ
     */
    @Update
    void update(AccountData ad);

    /**
     * データを削除
     * @param ad 削除データ
     */
    @Delete
    void delete(AccountData ad);
}