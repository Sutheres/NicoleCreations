package com.example.sutheres.nicolecreations;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sutheres.nicolecreations.R;
import com.example.sutheres.nicolecreations.data.ProductContract.ProductEntry;
import com.example.sutheres.nicolecreations.data.ProductDBHelper;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PRODUCT_LOADER = 1;

    private static final int READ_REQUEST_CODE = 1;


    private EditText mNameEditText;
    private EditText mPriceEditText;
    private TextView mQuantityTextView;
    private EditText mSupplierEditText;
    private ImageView mImage;
    private Button mOrderButton;
    private Button mImageButton;
    private Button mPlusButton;
    private Button mMinusButton;
    private int quantityCount;
    private Uri photoUri;
    private Uri currentProductUri;
    private boolean mProductHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        currentProductUri = intent.getData();

        if (currentProductUri == null) {
            this.setTitle("Add a Product");
            invalidateOptionsMenu();
        } else {
            this.setTitle("Edit a Product");


        }


        mNameEditText = (EditText) findViewById(R.id.edit_text_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_text_price);
        mQuantityTextView = (TextView) findViewById(R.id.text_view_quantity);
        mSupplierEditText = (EditText) findViewById(R.id.text_view_supplier);
        mPlusButton = (Button) findViewById(R.id.action_quantity_plus_one);
        mMinusButton = (Button) findViewById(R.id.action_quantity_minus_one);
        mImage = (ImageView) findViewById(R.id.image);
        mImageButton = (Button) findViewById(R.id.action_add_image);
        mOrderButton = (Button) findViewById(R.id.action_order_button);

        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityTextView.setOnTouchListener(mTouchListener);
        mSupplierEditText.setOnTouchListener(mTouchListener);
        mPlusButton.setOnTouchListener(mTouchListener);
        mMinusButton.setOnTouchListener(mTouchListener);
        mImage.setOnTouchListener(mTouchListener);

        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType("*/*");
                mailIntent.putExtra(Intent.EXTRA_EMAIL, mSupplierEditText.getText().toString());
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Re-stock");
                mailIntent.putExtra(Intent.EXTRA_TEXT, "I would like to order more " + mNameEditText.getText().toString() + "s.");
                if (mailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mailIntent);
                }
            }
        });

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                imageIntent.addCategory(Intent.CATEGORY_OPENABLE);
                imageIntent.setType("*/*");
                startActivityForResult(imageIntent, READ_REQUEST_CODE);

            }
        });

        getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new product, hide the "Delete" menu item.
        if (currentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_product);
            menuItem.setVisible(false);
        }
        return true;
    }


    private void savePet() {


        String currentName = mNameEditText.getText().toString().trim();
        String currentPrice = mPriceEditText.getText().toString().trim();
        String currentQuantity = mQuantityTextView.getText().toString().trim();
        String currentSupplier = mSupplierEditText.getText().toString().trim();


        if(TextUtils.isEmpty(currentName) || TextUtils.isEmpty(currentPrice) || TextUtils.isEmpty(currentQuantity) || TextUtils.isEmpty(currentSupplier) || photoUri == null){


            Toast.makeText(this, "Please fill all the entries.", Toast.LENGTH_SHORT).show();

            return;
        }
        Uri mUri;

        int price = Integer.parseInt(currentPrice);
        int quantity = Integer.parseInt(currentQuantity);

        ContentValues values = new ContentValues();

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, currentName);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, currentSupplier);
        if (photoUri != null) {
            values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, photoUri.toString());
        }

        if (currentProductUri == null) {

            mUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

            if (mUri == null) {
                Toast.makeText(this, "Product not saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsUpdated = getContentResolver().update(
                    currentProductUri,
                    values,
                    null,
                    null);

            if (rowsUpdated != 0) {
                Toast.makeText(this, "Product updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No products updated", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_save_product:
                savePet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_product:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void incrementUp(View view) {
        quantityCount = quantityCount + 1;
        mQuantityTextView.setText(Integer.toString(quantityCount));
    }

    public void incrementDown(View view) {
        if (quantityCount == 0) {
            quantityCount = 0;
        } else {
            quantityCount = quantityCount - 1;
        }
        mQuantityTextView.setText(Integer.toString(quantityCount));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {


        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            photoUri = resultData.getData();
            mImage.setImageURI(photoUri);
        } else {
            Toast.makeText(this, "Photo could not be added", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mProductHasChanged) {
            Toast.makeText(this, "not changed", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
            return;
        }


        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER,
                ProductEntry.COLUMN_PRODUCT_IMAGE
        };

        if (currentProductUri != null) {
            return new CursorLoader(
                    this,
                    currentProductUri,
                    projection,
                    null,
                    null,
                    null
            );

        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (currentProductUri != null) {

            if (cursor.moveToFirst()) {

                String name = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
                int price = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
                quantityCount = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
                String supplier = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER));
                String image = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE));
                Uri imageUri = Uri.parse(image);

                mNameEditText.setText(name);
                mPriceEditText.setText(Integer.toString(price));
                mQuantityTextView.setText(Integer.toString(quantityCount));
                mSupplierEditText.setText(supplier);
                mImage.setImageURI(imageUri);
                Toast.makeText(this, "image uri: " + image.toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mNameEditText.getText().clear();
        mPriceEditText.getText().clear();
        quantityCount = 0;
        mSupplierEditText.getText().clear();
        photoUri = null;
        mImage.setImageURI(null);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePet() {

        if (currentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(
                    currentProductUri,
                    null,
                    null
            );

            // Show a toast message depending on whether or not the delete was successful
            if (rowsDeleted != 0) {
                Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Unable to delete product", Toast.LENGTH_SHORT).show();
            }
        }
        finish();

    }



}
