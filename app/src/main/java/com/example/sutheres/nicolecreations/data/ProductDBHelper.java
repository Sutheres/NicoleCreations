package com.example.sutheres.nicolecreations.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.sutheres.nicolecreations.data.ProductContract.ProductEntry;

/**
 * Created by Sutheres on 1/29/2017.
 */

public class ProductDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "inventory.db";

    public ProductDBHelper(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * CREATE TABLE pets (_id INTEGER PRIMARY KEY AUTOINCREMENT,
         * name TEXT NOT NULL,
         * price INTEGER NOT NULL DEFAULT 0,
         * quantity INTEGER NOT NULL DEFAULT 0,
         * supplier TEXT NOT NULL,
         * image TEXT NOT NULL);
          */
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME  + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_SUPPLIER + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_IMAGE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Red Rose Lapel Pin");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, "5");
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "6");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, "orders@walmart.com");
        Uri imageURI = Uri.parse("android.resource://com.example.sutheres.nicolecreations/drawable/red_rose");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imageURI.toString());
        db.insert(ProductEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Kente Cloth Lapel Pin");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, "5");
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "8");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, "orders@walmart.com");
        imageURI = Uri.parse("android.resource://com.example.sutheres.nicolecreations/drawable/kente");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imageURI.toString());
        db.insert(ProductEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Heartbreak Lapel Pin");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, "5");
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "14");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, "orders@walmart.com");
        imageURI = Uri.parse("android.resource://com.example.sutheres.nicolecreations/drawable/heartbreak");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imageURI.toString());
        db.insert(ProductEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Denim Bowtie Lapel Pin");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, "5");
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "4");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, "orders@walmart.com");
        imageURI = Uri.parse("android.resource://com.example.sutheres.nicolecreations/drawable/bowtie");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imageURI.toString());
        db.insert(ProductEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Black & Gold Lapel Pin");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, "5");
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "7");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, "orders@walmart.com");
        imageURI = Uri.parse("android.resource://com.example.sutheres.nicolecreations/drawable/alpha");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imageURI.toString());
        db.insert(ProductEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "St. Patricks Day Lapel Pin");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, "5");
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "10");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, "orders@walmart.com");
        imageURI = Uri.parse("android.resource://com.example.sutheres.nicolecreations/drawable/st_patricks_day");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imageURI.toString());
        db.insert(ProductEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Do The Right Thing Lapel Pin");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, "5");
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "1");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, "orders@walmart.com");
        imageURI = Uri.parse("android.resource://com.example.sutheres.nicolecreations/drawable/spike_lee");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imageURI.toString());
        db.insert(ProductEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Pink & Green Lapel Pin");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, "5");
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "5");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, "orders@walmart.com");
        imageURI = Uri.parse("android.resource://com.example.sutheres.nicolecreations/drawable/bracelet");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imageURI.toString());
        db.insert(ProductEntry.TABLE_NAME, null, values);
        values.clear();




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
