package com.example.tomato.lesson6sqlitetdgiang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.tomato.lesson6sqlitetdgiang.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    public static final String DB_NAME = "contact_list";
    private static final String TABLE_NAME = "contact";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PHONE = "phone";

    private Context context;

    public DBManager(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                PHONE + " TEXT )";
        db.execSQL(sqlQuery);
        Toast.makeText(context, "create successfylly", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertData(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, contact.getName());
        values.put(PHONE, contact.getNumberPhone());

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public List<Contact> getAllData() {
        List<Contact> list = new ArrayList<Contact>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                contact.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                contact.setNumberPhone(cursor.getString(cursor.getColumnIndex(PHONE)));

                list.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public int updateData(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME, contact.getName());
        values.put(PHONE, contact.getNumberPhone());

        return db.update(TABLE_NAME, values, ID + "=?",
                new String[]{String.valueOf(contact.getId())});
    }

    public void deleteData(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " =? ",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }

}
