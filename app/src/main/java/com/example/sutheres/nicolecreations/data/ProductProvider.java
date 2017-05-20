package com.example.sutheres.nicolecreations.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.sutheres.nicolecreations.data.ProductContract.ProductEntry;

/**
 * Created by Sutheres on 1/29/2017.
 */

public class ProductProvider extends ContentProvider{

    private ProductDBHelper mDbHelper;

    /** Tag for the log messages */
    public static final String LOG_TAG = ProductProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the products table */
    public static final int PRODUCTS = 100;

    /** URI matcher code for the content URI for a single product in the products table */
    public static final int PRODUCTS_ID = 101;


    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It;s common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        /**
         * The calls to addURI() go here, for all the content URI patterns that the provider
         * should recognize. All paths added to the UriMatcher have a corresponding code to return
         * when a match is found
         */

        sUriMatcher.addURI(ProductEntry.CONTENT_AUTHORITY, ProductEntry.PATH_PRODUCTS, PRODUCTS);
        sUriMatcher.addURI(ProductEntry.CONTENT_AUTHORITY, ProductEntry.PATH_PRODUCTS + "/#", PRODUCTS_ID);
    }


    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // Make sure the variable is a global variable, so it can be reference from other
        // ContentProvider methods.
        mDbHelper = new ProductDBHelper(getContext());
        return true;
    }

    // Perform the Query for the given URI. Use the given projection, selection, selection arguments, and sort order.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Get readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                // For the PRODUCTS code, query the products table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the products table.
                cursor = db.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCTS_ID:
                // For the PRODUCTS code, extract out the ID from the URI.
                // For an example such as "content://com.example.android.nicolecreations/products/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "=?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }

        // Set the notification URI on the Cursor,
        // so we know what content URI the cursor was created for.
        // If the data at this URi changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertPet(uri, contentValues);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given contentValues. Return the new content URI
     * for that specific row in the database
     */
    private Uri insertPet(Uri uri, ContentValues contentValues) {

        // Check that the name is not null
        String name = contentValues.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if(TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Product requires a name", Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Product requires a name");
        }

        // Check that the price is not null
        Integer price = contentValues.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
        if(price != null && price < 0) {
            Toast.makeText(getContext(), "Product requires a price", Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Product requires a price");
        }

        // Check that the quantity is not null
        Integer quantity = contentValues.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity != null && quantity < 0) {
            Toast.makeText(getContext(), "Product requires a quantity", Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Product requires a quantity");
        }

        // Check that the supplier is not null
        String supplier = contentValues.getAsString(ProductEntry.COLUMN_PRODUCT_SUPPLIER);
        if (TextUtils.isEmpty(supplier)) {
            Toast.makeText(getContext(), "Product requires a supplier", Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Product requires a supplier");
        }

        // Check that that image uri is not null
        String imageUri = contentValues.getAsString(ProductEntry.COLUMN_PRODUCT_IMAGE);
        if (TextUtils.isEmpty(imageUri)) {
            Toast.makeText(getContext(), "Product requires an image", Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Product requires an image");
        }


        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(ProductEntry.TABLE_NAME, null, contentValues);

        // If the ID is -1, then the insretion failed. Log and error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            Toast.makeText(getContext(), "Failed to insert row for " + uri, Toast.LENGTH_SHORT).show();
            return null;
        }

        // Notify all listeners that the data has been changed for the product content URI
        // uri: content://com.example.sutheres.nicolecreations/products
        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table, return the new URI
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCTS_ID:
                // Delete a row based on the ID given from the URI
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion not supported for the uri :" + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            // Notify all listeners that the data has changed for the pet content URI
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCTS_ID:
                // For the PRODUCTS_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(("Update is not supported for " + uri));
        }
    }

    /**
     * Update products in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // If the {@link ProductEntry#COLUMN_PRODUCT_NAME} key is present,
        // check that the name value is not null.
        if(values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("Product name is required");
            }
        }

        // If the {@link ProductEntry#COLUMN_PRODUCT_PRICE} key is present,
        // check that the name value is not null.
        if(values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE)) {
            Integer price = values.getAsInteger((ProductEntry.COLUMN_PRODUCT_PRICE));
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Product price is required");
            }
        }

        // If the {@link ProductEntry#COLUMN_PRODUCT_QUANTITY} key is present,
        // check that the name value is not null.
        if(values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Product quantity is required");
            }
        }

        // If the {@link ProductEntry#COLUMN_PRODUCT_SUPPLIER} key is present,
        // check that the supplier value is not null
        if(values.containsKey(ProductEntry.COLUMN_PRODUCT_SUPPLIER)) {
            String supplier = values.getAsString(ProductEntry.COLUMN_PRODUCT_SUPPLIER);
            if (TextUtils.isEmpty(supplier)) {
                throw new IllegalArgumentException("Product supplier email is required");
            }
        }

        // If the {@link ProductEntry#COLUMN_PRODUCT_IMAGE} key is present,
        // check that the image uri value is not null. No image uri means no image.
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_IMAGE)) {
            String imageUri = values.getAsString(ProductEntry.COLUMN_PRODUCT_IMAGE);
            if (TextUtils.isEmpty(imageUri)) {
                throw new IllegalArgumentException("Product image is required");
            }
        }


        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }


        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);

        // If any rows have updated at all, notify the listeners that the data has changed
        if (rowsUpdated != 0) {
            // Notify all listeners that the data has changed for the product content URI
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    /**
     * Returns the MIME type of data for the content URi
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCTS_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }
}
