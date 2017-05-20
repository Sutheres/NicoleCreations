package com.example.sutheres.nicolecreations.data;

/**
 * Created by Sutheres on 1/28/2017.
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Nicole Creations App
 */
public final class ProductContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor
    private ProductContract() {}

    /**
     * Inner class that defines the constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    public static final class ProductEntry implements BaseColumns {

        /**
         * The "Content Authority is a name for the entire content provider, similar to the
         * relationship between a domain name and its website. A convenient string to use for the
         * content authority is the package name for the app, which is guaranteed to be unique on the
         * device.
         */
        public static final String CONTENT_AUTHORITY = "com.example.sutheres.nicolecreations";

        /**
         * Use CONTENT_AUTHORITY to create the base of all the URI's which apps will use to contact
         * the content provider
         */
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        /**
         * Possible path (appended to base content URI for possible URI's)
         * FOr instance, content://com.example.suteres.nicolecreations/products/ is a valid path for
         * looking at pet data. content://com.example.sutheres.nicolecreations/staff/ will fail,
         * as the ContentProvider hasn't been given any information on what to do with "staff".
         */
        public static final String PATH_PRODUCTS = "products";


        /** The content URI to access the pet data in the provider. */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;


        // Table name
        public static final String TABLE_NAME = "products";

        /**
         * Unique ID number for the product (only for use in the database table).
         * TYPE: INTEGER
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Name of the Product
         * TYPE: TEXT
         */
        public static final String COLUMN_PRODUCT_NAME = "name";

        /**
         * Current quantity of product
         * Type: INTEGER
         */
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";

        /**
         * Price of the Product
         * TYPE: INTEGER
         */
        public static final String COLUMN_PRODUCT_PRICE = "price";

        /**
         * Image URI of the Product
         * TYPE: TEXT
         */
        public static final String COLUMN_PRODUCT_IMAGE = "image";

        /**
         * Supplier of the Product
         * TYPE: TEXT
         */
        public static final String COLUMN_PRODUCT_SUPPLIER = "supplier";
    }
}
