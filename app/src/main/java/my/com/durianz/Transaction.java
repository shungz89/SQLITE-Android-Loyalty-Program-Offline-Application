package my.com.durianz;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Transaction extends AppCompatActivity {

    EditText customer_id;
    Button transaction_customer_search_Button, transaction_customer_addpoints_Button, transaction_customer_spendpoints_Button;
    TextView id, name, credit_balance;
    Cursor cursor;
    SQLiteDatabase db;
    LinearLayout transaction_customer_detail_Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_transaction);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transaction");


        customer_id =  findViewById(R.id.transaction_customer_id);
        transaction_customer_search_Button = findViewById(R.id.transaction_customer_search_button);
        id =  findViewById(R.id.transaction_customer_detail_id);
        name = findViewById(R.id.transaction_customer_detail_name);
        credit_balance =  findViewById(R.id.transaction_customer_detail_credit);
        transaction_customer_detail_Layout = findViewById(R.id.transaction_customer_detail_layout);

        transaction_customer_addpoints_Button = findViewById(R.id.transaction_add_points_button);
        transaction_customer_spendpoints_Button = findViewById(R.id.transaction_spend_points_button);



        FeedReaderContract.FeedReaderDbHelper mDbHelper = new FeedReaderContract.FeedReaderDbHelper(Transaction.this);


        db = mDbHelper.getWritableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        final String[] projection = {

                FeedReaderContract.FeedEntry.COLUMN_CONTACT_NAME,
                FeedReaderContract.FeedEntry.COLUMN_CONTACT_IC,
                FeedReaderContract.FeedEntry.COLUMN_CONTACT_EMAIL,
                FeedReaderContract.FeedEntry.COLUMN_CONTACT_MOBILE_NO,
                FeedReaderContract.FeedEntry.COLUMN_CONTACT_POINTS

        };

// Filter results WHERE "title" = 'My Title'
        final String selection = FeedReaderContract.FeedEntry.COLUMN_CONTACT_IC+ " = ?";



        transaction_customer_search_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cursor = db.query(
                        FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        new String[] { String.valueOf(customer_id.getText()) },                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );

                hideKeyboard();

                if (cursor != null) {
                    cursor.moveToFirst();

                    try{

                        id.setText(customer_id.getText());
                        name.setText(cursor.getString(0));
                        credit_balance.setText(cursor.getString(4));

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(Transaction.this, cursor.getString(0)+" "+
//                                        cursor.getString(1)+" "+
//                                        cursor.getString(2)+" "+
//                                        cursor.getString(3)+" "+
//                                        cursor.getString(4),Toast.LENGTH_SHORT).show();
//
//                            }
//                        });

                        if(transaction_customer_detail_Layout.getVisibility()==View.INVISIBLE){
                            transaction_customer_detail_Layout.setVisibility(View.VISIBLE);
                        }

                    }
                    catch (Exception e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Transaction.this, "No such entry found",Toast.LENGTH_SHORT).show();

                                if(transaction_customer_detail_Layout.getVisibility()==View.VISIBLE){
                                    transaction_customer_detail_Layout.setVisibility(View.INVISIBLE);
                                }

                            }
                        });
                    }

                }

            }
        });


        transaction_customer_addpoints_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(Transaction.this);
                dialog.setContentView(R.layout.plus_point);
                dialog.setTitle("Key in the amount of customer spent");

                // set the custom dialog components - text, image and button

                Button dialogButton = (Button) dialog.findViewById(R.id.add_points);
                final EditText amountText = (EditText) dialog.findViewById(R.id.transaction_customer_spending) ;
                final TextView totalpointreceivedText = (TextView) dialog.findViewById(R.id.transaction_customer_detail_points_text);

                amountText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if(amountText.getText().toString().trim().equalsIgnoreCase("")){
                            totalpointreceivedText.setText("0");
                        }
                        else {
                            totalpointreceivedText.setText(String.valueOf(Double.valueOf(amountText.getText().toString()) * 1 / 10));
                        }

                    }
                });

                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(amountText.getText().toString().trim().equalsIgnoreCase("")) {
                            Toast.makeText(Transaction.this,"Please key in an amount", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            final String total_points;
                            // total_points = Double.valueOf(cursor.getString(4)) + Double.valueOf(amountText.getText().toString());

                            ContentValues cv = new ContentValues();
                            cv.put(FeedReaderContract.FeedEntry.COLUMN_CONTACT_POINTS,
                                    String.valueOf((Double.valueOf(totalpointreceivedText.getText().toString()) +
                                            Double.valueOf(credit_balance.getText().toString()))));

                            total_points = String.valueOf(Double.valueOf(totalpointreceivedText.getText().toString()) +
                                            Double.valueOf(credit_balance.getText().toString()));


                            db.update(FeedReaderContract.FeedEntry.TABLE_NAME, cv, "ic=" + customer_id.getText().toString(), null);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    credit_balance.setText(total_points);

                                }
                            });

                            dialog.dismiss();

                        }
                    }
                });

                dialog.show();
            }
        });

        transaction_customer_spendpoints_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(Transaction.this);
                dialog.setContentView(R.layout.minus_point);
                dialog.setTitle("Key in the amount of points use");

                // set the custom dialog components - text, image and button

                Button dialogButton = (Button) dialog.findViewById(R.id.redeem_points);
                final EditText amountSpentText = (EditText) dialog.findViewById(R.id.transaction_customer_redemption) ;
                final TextView totalpointredeemedText = (TextView) dialog.findViewById(R.id.transaction_customer_available_points_text);

                totalpointredeemedText.setText(credit_balance.getText().toString());

                amountSpentText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {



                    }
                });

                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(amountSpentText.getText().toString().trim().equalsIgnoreCase("")) {
                            Toast.makeText(Transaction.this,"Please key in an amount", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            final String total_points;
                            // total_points = Double.valueOf(cursor.getString(4)) + Double.valueOf(amountText.getText().toString());


                            if(Double.valueOf(credit_balance.getText().toString())-
                                    (Double.valueOf(amountSpentText.getText().toString()))>0) {
                                ContentValues cv = new ContentValues();
                                cv.put(FeedReaderContract.FeedEntry.COLUMN_CONTACT_POINTS,
                                        String.valueOf(Double.valueOf(credit_balance.getText().toString()) -
                                                (Double.valueOf(amountSpentText.getText().toString()))));

                                total_points = String.valueOf(Double.valueOf(credit_balance.getText().toString()) -
                                        (Double.valueOf(amountSpentText.getText().toString())));


                                db.update(FeedReaderContract.FeedEntry.TABLE_NAME, cv, "ic=" + customer_id.getText().toString(), null);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        credit_balance.setText(total_points);

                                    }
                                });

                                dialog.dismiss();
                            }
                            else{
                                Toast.makeText(Transaction.this,"Not enough points",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                dialog.show();
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(cursor!=null) {
            cursor.close();
        }

        if(db!=null) {
            db.close();
        }
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = Transaction.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) Transaction.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
