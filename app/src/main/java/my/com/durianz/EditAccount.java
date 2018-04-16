package my.com.durianz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static my.com.durianz.FeedReaderContract.FeedEntry.TABLE_NAME;


public class EditAccount extends AppCompatActivity {

    Button edit_account_search_Button, edit_account_save_Button, edit_account_delete_Button;
    EditText customer_id, name, ic_no, email, mobile_no;
    List itemIds = new ArrayList<>();

    ListView customer_listview;
    UserListAdapter userListAdapter;

    List<Users> UserList = new ArrayList<>();


    SQLiteDatabase db;
    String[] projection;

    Cursor cursor;
    boolean items_selected = false;
    String ic_selected = "null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_edit_account);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Account");


//        edit_account_search_Button = findViewById(R.id.edit_account_page_search_button);
        edit_account_save_Button = findViewById(R.id.edit_account_page_save_button);
        edit_account_delete_Button = findViewById(R.id.edit_account_page_delete_button);

        customer_listview = findViewById(R.id.customer_listview);


//        customer_id = (EditText) findViewById(R.id.edit_account_customer_id);
        name = (EditText) findViewById(R.id.edit_account_customer_name);
        ic_no = (EditText) findViewById(R.id.edit_account_customer_ic_no);
        email = (EditText) findViewById(R.id.edit_account_customer_email);
        mobile_no = (EditText) findViewById(R.id.edit_account_customer_mobile_no);

        FeedReaderContract.FeedReaderDbHelper mDbHelper = new FeedReaderContract.FeedReaderDbHelper(EditAccount.this);
        db = mDbHelper.getWritableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        projection = new String[]{

                FeedReaderContract.FeedEntry.COLUMN_CONTACT_NAME,
                FeedReaderContract.FeedEntry.COLUMN_CONTACT_IC,
                FeedReaderContract.FeedEntry.COLUMN_CONTACT_EMAIL,
                FeedReaderContract.FeedEntry.COLUMN_CONTACT_MOBILE_NO,
                FeedReaderContract.FeedEntry.COLUMN_CONTACT_POINTS

        };

        // Filter results WHERE "title" = 'My Title'

        String non_null_arg = "name IS NOT NULL OR name != ''";


        //Log.d("dbz", "total size" + DatabaseUtils.queryNumEntries(db, TABLE_NAME,non_null_arg));
        Log.d("dbz", "total size" + DatabaseUtils.queryNumEntries(db, TABLE_NAME));

        //Log.d("dbz", "total size get count" + cursor.getCount());

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor2 = db.rawQuery(selectQuery, null);
        try {
            cursor2.moveToLast();

            Log.d("dbz", "cursor to is moving to last " + cursor2.getString(0));


            for (int i = 0; i < Integer.valueOf(cursor2.getString(0)); i++) {

                final String selection = FeedReaderContract.FeedEntry._ID + " = ?";

                cursor = db.query(
                        FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        new String[]{String.valueOf(i + 1)},                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );

                Log.d("dbz", "abc1");

                if (cursor != null) {
                    cursor.moveToFirst();

                    try {

                        Log.d("dbz", "abc2");


                        Users user = new Users(cursor.getString(0), cursor.getString(1),
                                cursor.getString(2), cursor.getString(3), cursor.getString(4));

//                    if(cursor.getString(0)!=null || !cursor.getString(0).equals("null")) {

                        UserList.add(user);

                        Log.d("dbz", "add user " + user.getName());


//                    }
                        Log.d("dbz", UserList.get(i).getName() + " " +
                                UserList.get(i).getIC() + " " + UserList.get(i).getEmail() + " " + UserList.get(i).getPoints());

                        Log.d("dbz", cursor.getString(0) + "" + cursor.getString(1) + "" + cursor.getString(2) +
                                "" + cursor.getString(3) + "" + cursor.getString(4));


//                                Toast.makeText(EditAccount.this, cursor.getString(0)+" "+
//                                        cursor.getString(1)+" "+
//                                        cursor.getString(2)+" "+
//                                        cursor.getString(3)+" "+
//                                        cursor.getString(4),Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EditAccount.this, "No such entry found", Toast.LENGTH_SHORT).show();

                                Log.d("dbz", "null many times");


                            }
                        });
                    }

                }

            }

        }

        catch (Exception e) {

        }


        userListAdapter = new UserListAdapter(EditAccount.this, UserList);
        customer_listview.setAdapter(userListAdapter);


        customer_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String selection = FeedReaderContract.FeedEntry.COLUMN_CONTACT_IC + " = ?";

                items_selected = true;

                TextView tv = (TextView) view.findViewById(R.id.userlist_ic_textview);
                ic_selected = String.valueOf(tv.getText());


                        hideKeyboard();

                Log.d("dbz","iteam ic"+ic_selected+"");

                cursor = db.query(
                        TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        new String[]{String.valueOf(tv.getText())},                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );

                if (cursor != null) {

                    cursor.moveToFirst();

                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                name.setText(cursor.getString(0));
                                ic_no.setText(cursor.getString(1));
                                email.setText(cursor.getString(2));
                                mobile_no.setText(cursor.getString(3));

//                                Toast.makeText(EditAccount.this, cursor.getString(0)+" "+
//                                        cursor.getString(1)+" "+
//                                        cursor.getString(2)+" "+
//                                        cursor.getString(3)+" "+
//                                        cursor.getString(4),Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (Exception e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(EditAccount.this, "No such entry found", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                }
            }
        });

//        edit_account_search_Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        edit_account_save_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (items_selected) {

                    if (name.getText().toString().trim().equalsIgnoreCase("")) {
                        name.setError("Please fill in customer's Name");
                    } else if (ic_no.getText().toString().trim().equalsIgnoreCase("")) {
                        ic_no.setError("Please fill in customer's IC No.");

                    } else if (email.getText().toString().trim().equalsIgnoreCase("")) {
                        email.setError("Please fill in customer's Email");

                    } else if (mobile_no.getText().toString().trim().equalsIgnoreCase("")) {
                        mobile_no.setError("Please fill in customer's Phone No.");

                    }

                    else {

                        if (!name.getText().toString().matches("^[a-zA-Z\\s]+$")) {
                            name.setError("Only Alphabets (A-Z) are allowed");
                        } else if (ic_no.getText().length() > 13) {
                            ic_no.setError("IC No. too long");

                        } else if (ic_no.getText().length() < 12) {
                            ic_no.setError("Please enter a valid IC No.");

                        } else if (!email.getText().toString().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {

                            email.setError("Please enter a valid Email Address");

                        } else if (mobile_no.getText().length() < 10) {
                            mobile_no.setError("Please enter a valid Mobile No.");

                        } else {

                            ContentValues cv = new ContentValues();
                            cv.put(FeedReaderContract.FeedEntry.COLUMN_CONTACT_NAME, name.getText().toString());
                            cv.put(FeedReaderContract.FeedEntry.COLUMN_CONTACT_IC, ic_no.getText().toString());
                            cv.put(FeedReaderContract.FeedEntry.COLUMN_CONTACT_EMAIL, email.getText().toString());
                            cv.put(FeedReaderContract.FeedEntry.COLUMN_CONTACT_MOBILE_NO, mobile_no.getText().toString());

                            db.update(TABLE_NAME, cv, "ic=" + ic_selected, null);

                            Toast.makeText(EditAccount.this, "Contact details UPDATED!", Toast.LENGTH_SHORT).show();

                            clearEditText();

                            items_selected = false;
                            ic_selected = "null";

                            queryAndLoadListView();

                        }
                    }

                } else {
                    Toast.makeText(EditAccount.this, "Please Select a User Account to Edit ", Toast.LENGTH_SHORT).show();
                }


            }
        });

        edit_account_delete_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (items_selected) {

                    db.delete(TABLE_NAME, "ic="+ic_selected , null);

                    Toast.makeText(EditAccount.this, "Contact DELETED!", Toast.LENGTH_SHORT).show();

                    clearEditText();

                    items_selected = false;
                    ic_selected = "null";

                    queryAndLoadListView();

                }
                else{

                    Toast.makeText(EditAccount.this, "Please Select a User Account to Edit ", Toast.LENGTH_SHORT).show();

                }


            }
        });

        mobile_no.getText().toString().isEmpty();


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

        if (cursor != null) {
            cursor.close();
        }

        if (db != null) {
            db.close();
        }
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = EditAccount.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) EditAccount.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void clearEditText() {
//        customer_id.setText("");
        name.setText("");
        ic_no.setText("");
        email.setText("");
        mobile_no.setText("");
    }

    public void queryAndLoadListView() {
        final String selection = FeedReaderContract.FeedEntry._ID + " = ?";

        UserList.clear();

//        String non_null_arg = "name IS NOT NULL OR name != ''";


        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor2 = db.rawQuery(selectQuery, null);

        try{
            cursor2.moveToLast();

            for (int i = 0; i < Integer.valueOf(cursor2.getString(0)); i++) {
                cursor = db.query(
                        FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        new String[]{String.valueOf(i + 1)},                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );

                if (cursor != null) {
                    cursor.moveToFirst();

                    try {


                        Users user = new Users(cursor.getString(0), cursor.getString(1),
                                cursor.getString(2), cursor.getString(3), cursor.getString(4));

                        if(cursor.getString(0)!=null || !cursor.getString(0).equals("null")) {
                            UserList.add(user);
                        }



//                    Log.d("dbz", UserList.get(i).getName()+ " " +
//                            UserList.get(i).getIC()+ " " +UserList.get(i).getEmail()+ " " +UserList.get(i).getPoints());
//
//                    Log.d("dbz", cursor.getString(0)+""+cursor.getString(1)+""+cursor.getString(2)+
//                            ""+cursor.getString(3)+""+cursor.getString(4));


//                                Toast.makeText(EditAccount.this, cursor.getString(0)+" "+
//                                        cursor.getString(1)+" "+
//                                        cursor.getString(2)+" "+
//                                        cursor.getString(3)+" "+
//                                        cursor.getString(4),Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(EditAccount.this, "No such entry found", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                }

            }
        }catch (Exception e){

        }





        userListAdapter = new UserListAdapter(this, UserList);
        customer_listview.setAdapter(userListAdapter);
    }

    public long getProfilesCount() {
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }
}
