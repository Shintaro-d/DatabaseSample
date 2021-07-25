package com.example.databasesample;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 家計簿データクラス（Room）
 */
@Entity(tableName = "account_data") //テーブル名を定義
public class AccountData {
    @PrimaryKey(autoGenerate = true)
    public int id;           //「id」カラムを定義
    public String content;   //「内容」カラムを定義
    public int price;        //「金額」カラムを定義
    public long date;        //「日付」カラムを定義

    /**
     * コンストラクタ
     * @param content 内容
     * @param price   金額
     * @param date    日付
     */
    public AccountData(String content, int price, long date) {
        this.content = content;  //「内容」を設定
        this.price = price;      //「金額」を設定
        this.date = date;        //「日付」を設定
    }

    /**
     * 「id」を取得（Getter）
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * 「内容」を取得（Getter）
     * @return 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 「価格」を取得（Getter）
     * @return
     */
    public int getPrice() {
        return price;
    }

    /**
     *　「日付」を取得（Getter）
     * @return
     */
    public long getDate() {
        return date;
    }

    /**
     * 「家計簿データ」を更新
     * @param content 更新する「内容」
     * @param price   更新する「金額」
     * @param date    更新する「日付」
     * @return 更新した「家計簿データ」
     */
    public AccountData update(String content, int price, long date){
        this.content = content;  //「内容」を設定
        this.price = price;      //「金額」を設定
        this.date = date;        //「日付」を設定
        return this;
    }
}