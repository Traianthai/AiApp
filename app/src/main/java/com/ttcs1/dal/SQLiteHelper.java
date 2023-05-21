package com.ttcs1.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


import com.ttcs1.model.Leaf;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Plant123.db";
    private static int DATABASE_VERSION = 1;
    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE leaf (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT, inf TEXT, img TEXT, date TEXT, imgbitmap BLOB)";
        db.execSQL(sql);
        String createTrigger = "CREATE TRIGGER IF NOT EXISTS task_delete_trigger " +
                "AFTER DELETE ON leaf " +
                "BEGIN " +
                "   UPDATE leaf " +
                "   SET id = id - 1 " +
                "   WHERE id > OLD.id; " +
                "END;";
        db.execSQL(createTrigger);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "leaf");
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
    // get all order by date
    public List<Leaf> getAll(){
        List<Leaf> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String order = "id ";
        Cursor rs = sqLiteDatabase.query("leaf",null,null,null,null,null,order);
        while (rs != null && rs.moveToNext()){
            int id = rs.getInt(0);
            String n = rs.getString(1);
            String i = rs.getString(2);
            String img = rs.getString(3);
            String d = rs.getString(4);
            byte[] bi = rs.getBlob(5);
            list.add(new Leaf(id,n,i,img,d,bi));
        }
        rs.close();
        return list;
    }
    public long addItem(Leaf i){
        ContentValues values = new ContentValues();
        values.put("id",i.getId());
        values.put("name",i.getName());
        values.put("inf",i.getInf());
        values.put("img",i.getImg());
        values.put("date",i.getDate());
        values.put("imgbitmap",i.getImgBitmap());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("leaf",null,values);
    }
    public List<Leaf> getByName(String key){
        List<Leaf> list = new ArrayList<>();
        String whereClause = "name like ?";
        String[] whereArgs = {"%"+key+"%"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor rs = db.query("leaf",null,whereClause,whereArgs,null,null,null);
        while(rs != null && rs.moveToNext()){
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String inf = rs.getString(2);
            String img = rs.getString(3);
            String date = rs.getString(4);
            byte[] imgbitmap = rs.getBlob(5);
            list.add(new Leaf(id,name,inf,img,date,imgbitmap));
        }
        return list;
    }


    public int deleteItem(int id) {
        String whereClause = "id = ?";
        String[] whereArg = {Integer.toString(id)};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("leaf",whereClause,whereArg);
    }

    public int getRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM leaf", null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        if (cursor != null) {
            cursor.close();
        }
        return count;
    }

}
