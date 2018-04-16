package my.com.durianz;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class CreateAccount extends AppCompatActivity {

    EditText contact_name, contact_ic, contact_email, contact_mobile_no;
    Button register_Button;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_create_account);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Account");


        contact_name = (EditText) findViewById(R.id.create_account_full_name);
        contact_ic = (EditText) findViewById(R.id.create_account_ic_no);
        contact_email = (EditText) findViewById(R.id.create_account_email);
        contact_mobile_no = (EditText) findViewById(R.id.create_account_mobile_no);

        register_Button = (Button) findViewById(R.id.create_account_register_button);


        register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (contact_name.getText().toString().trim().equalsIgnoreCase("")) {
                    contact_name.setError("Please fill in customer's Name");
                } else if (contact_ic.getText().toString().trim().equalsIgnoreCase("")) {
                    contact_ic.setError("Please fill in customer's IC No.");

                } else if (contact_email.getText().toString().trim().equalsIgnoreCase("")) {
                    contact_email.setError("Please fill in customer's Email");

                } else if (contact_mobile_no.getText().toString().trim().equalsIgnoreCase("")) {
                    contact_mobile_no.setError("Please fill in customer's Phone No.");

                }

                else {

                    if(!contact_name.getText().toString().matches("^[a-zA-Z\\s]+$")){
                        contact_name.setError("Only Alphabets (A-Z) are allowed");
                    }
                    else if (contact_ic.getText().length()>13) {
                        contact_ic.setError("IC No. too long");

                    }
                    else if (contact_ic.getText().length()<12) {
                        contact_ic.setError("Please enter a valid IC No.");

                    }
                    else if(!contact_email.getText().toString().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")){

                        contact_email.setError("Please enter a valid Email Address");

                    }
                    else if (contact_mobile_no.getText().length()<10) {
                        contact_mobile_no.setError("Please enter a valid Mobile No.");

                    }
                    else {

                        FeedReaderContract.FeedReaderDbHelper mDbHelper = new FeedReaderContract.FeedReaderDbHelper(CreateAccount.this);

                        // Gets the data repository in write mode
                        db = mDbHelper.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        values.put(FeedReaderContract.FeedEntry.COLUMN_CONTACT_NAME, contact_name.getText().toString());
                        values.put(FeedReaderContract.FeedEntry.COLUMN_CONTACT_IC, contact_ic.getText().toString());
                        values.put(FeedReaderContract.FeedEntry.COLUMN_CONTACT_EMAIL, contact_email.getText().toString());
                        values.put(FeedReaderContract.FeedEntry.COLUMN_CONTACT_MOBILE_NO, contact_mobile_no.getText().toString());
                        values.put(FeedReaderContract.FeedEntry.COLUMN_CONTACT_POINTS, "10");

                        // Insert the new row, returning the primary key value of the new row
                        db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

                        // 1. Instantiate an AlertDialog.Builder with its constructor
                        final AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);

// 2. Chain together various setter methods to set the dialog characteristics
                        builder.setMessage("Welcome " + contact_name.getText().toString() + "! You have 10 DZP as your balance.")
                                .setTitle("Account Created")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button

                                        dialog.dismiss();

                                    }
                                });

// 3. Get the AlertDialog from create()
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                }

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

        if(db!=null) {
            db.close();
        }

    }
}
