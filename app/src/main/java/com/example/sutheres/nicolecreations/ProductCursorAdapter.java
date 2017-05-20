package com.example.sutheres.nicolecreations;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.IntegerRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sutheres.nicolecreations.data.ProductContract.ProductEntry;

import static java.security.AccessController.getContext;

/**
 * Created by Sutheres on 1/30/2017.
 */

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {


    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find fields to populate the inflated template
        TextView productName = (TextView)view.findViewById(R.id.list_item_name);
        TextView productPrice = (TextView)view.findViewById(R.id.list_item_price);
        TextView productQuantity = (TextView)view.findViewById(R.id.list_item_quantity);
        Button productSale = (Button)view.findViewById(R.id.list_item_sale_button);

        // extract properties from cursor
        final int id = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
        String name = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
        String price = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
        final String quantity = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));

        // populate fields with extracted properties
        productName.setText(name);
        productPrice.setText("$" + price);
        if (Integer.parseInt(quantity) == 0) {
            productQuantity.setText("None left...");
        } else {
            productQuantity.setText(quantity + " left...");
        }

        //TODO: Set on click listener for current list item and sale button
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent editIntent = new Intent(context ,EditorActivity.class);

                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);

                editIntent.setData(currentProductUri);

                context.startActivity(editIntent);


            }
        });

        productSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);

                ContentValues values = new ContentValues();

                if (Integer.parseInt(quantity) == 0) {
                    Toast.makeText(context.getApplicationContext(), "Sale declined", Toast.LENGTH_SHORT).show();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 0);
                } else {
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, Integer.parseInt(quantity) - 1);
                }

                int rowsUpdated = context.getContentResolver().update(currentProductUri, values, null, null);
            }
        });
    }

}
