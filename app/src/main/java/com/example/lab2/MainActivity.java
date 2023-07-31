package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2.provider.BookDao;
import com.example.lab2.provider.BookRepositary;
import com.example.lab2.provider.BookViewModel;
import com.example.lab2.provider.bookItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    EditText editText6;
    DrawerLayout drawer;
    private BookViewModel myBookViewModel;
    DatabaseReference myRef;
    View myFrame;
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        editText1 = findViewById(R.id.editTextTextPersonName);
        editText2 = findViewById(R.id.editTextTextPersonName2);
        editText3 = findViewById(R.id.editTextTextPersonName3);
        editText4 = findViewById(R.id.editTextTextPersonName4);
        editText5 = findViewById(R.id.editTextTextPersonName5);
        editText6 = findViewById(R.id.editTextTextPersonName6);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame1,new RecyclerViewFragment()).commit();
        myFrame = findViewById(R.id.frame_id);

        myBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        myBookViewModel.getAllBooks().observe(this, newListOfAddedBooks -> {
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},0);
        IntentFilter intentFilter = new IntentFilter(SMSReceiver.SMS_FILTER);
        registerReceiver(myReceiver, intentFilter);

        NavigationView navigationView = findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(new myNavigationListener());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Item/book");

        gestureDetector = new GestureDetector(this, new MyGestureDetector());

        myFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText2.getText().toString();
                Double price = new Double(editText6.getText().toString());
                bookItem BookItem = new bookItem(
                        editText1.getText().toString(), editText2.getText().toString(),
                        editText3.getText().toString(), editText4.getText().toString(),
                        editText5.getText().toString(), editText6.getText().toString());
                myBookViewModel.addBook(BookItem);
                myRef.push().setValue(BookItem);
                Toast.makeText(getApplicationContext(), "The book " + title + " with a price of " + price + " has been added!", Toast.LENGTH_SHORT).show();
            }
        });

        drawer = findViewById(R.id.dl);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
            RandomISBN randomISBN = new RandomISBN();
            String newISBN = randomISBN.generateNewRandomISBN(4);
            editText3.setText(newISBN);
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            editText1.setText("");
            editText2.setText("");
            editText3.setText("");
            editText4.setText("");
            editText5.setText("");
            editText6.setText("");
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            if (distanceX > 0) { // right to left decrease
                Double newPrice = new Double(editText6.getText().toString());
                Double newNewPrice = newPrice - Math.abs(distanceX);
                editText6.setText(String.format("%.2f", newNewPrice));
            } else if (distanceX < 0) { //left to right increase
                Double newPrice = new Double(editText6.getText().toString());
                Double newNewPrice = newPrice + Math.abs(distanceX);
                editText6.setText(String.format("%.2f", newNewPrice));
            }
            if (distanceY > 10 || distanceY < -10) {
                editText2.setText("Untitled");
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            if (velocityX > 600 || velocityY > 600){
                moveTaskToBack(true);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            SharedPreferences myData = getSharedPreferences("bookApp", 0);
            String text1 = myData.getString("attribute1", "");
            String text2 = myData.getString("attribute2", "");
            String text3 = myData.getString("attribute3", "");
            String text4 = myData.getString("attribute4", "");
            String text5 = myData.getString("attribute5", "");
            String text6 = myData.getString("attribute6", "");

            editText1.setText(text1);
            editText4.setText(text4);
            editText5.setText(text5);
            editText6.setText(text6);
            editText2.setText(text2);
            editText3.setText(text3);
            super.onLongPress(e);
        }
    }

    class myNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.item1) {
                String title = editText2.getText().toString();
                Double price = new Double(editText6.getText().toString());

                bookItem BookItem = new bookItem(
                        editText1.getText().toString(), editText2.getText().toString(),
                        editText3.getText().toString(), editText4.getText().toString(),
                        editText5.getText().toString(), editText6.getText().toString());
                myBookViewModel.addBook(BookItem);
                myRef.push().setValue(BookItem);
                Toast.makeText(getApplicationContext(), "The book " + title + " with a price of " + price + " has been added!", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.item2) {
                Intent j = new Intent(getApplicationContext(), BookListActivity.class);
                startActivity(j);
            } else if (id == R.id.item3){
                myBookViewModel.deleteAll();
                myRef.removeValue();
            } else {
                Intent i = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(i);
            }
            drawer.closeDrawers();
            return true;
        }
    }

    public class RandomISBN {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digits = "0123456789";
        String alphaNumeric = upper + lower + digits;

        public String generateNewRandomISBN(int length) {
            char[] buf;
            Random random = new Random();
            if (length < 1) throw new IllegalArgumentException();
            buf = new char[length];
            for (int idx = 0; idx < buf.length; ++idx)
                buf[idx] =
                        alphaNumeric.charAt(random.nextInt(alphaNumeric.length()));
            return new String(buf);
        }
    }

    public void addBook(View view){
        bookItem BookItem = new bookItem(
                editText1.getText().toString(), editText2.getText().toString(),
                editText3.getText().toString(), editText4.getText().toString(),
                editText5.getText().toString(), editText6.getText().toString());
        myBookViewModel.addBook(BookItem);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear_item) {
            editText1.setText("");
            editText2.setText("");
            editText3.setText("");
            editText4.setText("");
            editText5.setText("");
            editText6.setText("");
        } else if (id == R.id.reload_item) {
            SharedPreferences myData = getSharedPreferences("bookApp", 0);
            String text1 = myData.getString("attribute1", "");
            String text2 = myData.getString("attribute2", "");
            String text3 = myData.getString("attribute3", "");
            String text4 = myData.getString("attribute4", "");
            String text5 = myData.getString("attribute5", "");
            String text6 = myData.getString("attribute6", "");

            editText1.setText(text1);
            editText4.setText(text4);
            editText5.setText(text5);
            editText6.setText(text6);
            editText2.setText(text2);
            editText3.setText(text3);
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendBroadcast(View view)
    {
        Intent myIntent = new Intent();
        myIntent.setAction(SMSReceiver.SMS_FILTER);
        myIntent.putExtra(SMSReceiver.SMS_MSG_KEY, SMSReceiver.SMS_FILTER);
        sendBroadcast(myIntent);
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            editText1 = findViewById(R.id.editTextTextPersonName);
            editText2 = findViewById(R.id.editTextTextPersonName2);
            editText3 = findViewById(R.id.editTextTextPersonName3);
            editText4 = findViewById(R.id.editTextTextPersonName4);
            editText5 = findViewById(R.id.editTextTextPersonName5);
            editText6 = findViewById(R.id.editTextTextPersonName6);

            String test = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            StringTokenizer tokenizer = new StringTokenizer(test, "|");

            String t1 = tokenizer.nextToken();
            String t2 = tokenizer.nextToken();
            String t3 = tokenizer.nextToken();
            String t4 = tokenizer.nextToken();
            String t5 = tokenizer.nextToken();
            String t6 = tokenizer.nextToken();

            editText1.setText(t1);
            editText2.setText(t2);
            editText3.setText(t3);
            editText4.setText(t4);
            editText5.setText(t5);
            editText6.setText(t6);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        editText1 = findViewById(R.id.editTextTextPersonName);
        editText2 = findViewById(R.id.editTextTextPersonName2);
        editText3 = findViewById(R.id.editTextTextPersonName3);
        editText4 = findViewById(R.id.editTextTextPersonName4);
        editText5 = findViewById(R.id.editTextTextPersonName5);
        editText6 = findViewById(R.id.editTextTextPersonName6);

        SharedPreferences myData = getSharedPreferences("bookApp", 0);
        String text1 = myData.getString("attribute1", "");
        String text2 = myData.getString("attribute2", "");
        String text3 = myData.getString("attribute3", "");
        String text4 = myData.getString("attribute4", "");
        String text5 = myData.getString("attribute5", "");
        String text6 = myData.getString("attribute6", "");

        editText1.setText(text1);
        editText2.setText(text2);
        editText3.setText(text3);
        editText4.setText(text4);
        editText5.setText(text5);
        editText6.setText(text6);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        editText1 = findViewById(R.id.editTextTextPersonName);
        editText2 = findViewById(R.id.editTextTextPersonName2);
        editText3 = findViewById(R.id.editTextTextPersonName3);
        editText4 = findViewById(R.id.editTextTextPersonName4);
        editText5 = findViewById(R.id.editTextTextPersonName5);
        editText6 = findViewById(R.id.editTextTextPersonName6);

        SharedPreferences preferences = getSharedPreferences("bookApp", 0);
        SharedPreferences.Editor myEditor = preferences.edit();
        myEditor.putString("attribute1", editText1.getText().toString());
        myEditor.putString("attribute2", editText2.getText().toString());
        myEditor.putString("attribute3", editText3.getText().toString());
        myEditor.putString("attribute4", editText4.getText().toString());
        myEditor.putString("attribute5", editText5.getText().toString());
        myEditor.putString("attribute6", editText6.getText().toString());
        myEditor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lab3", "onStop");
        editText1 = findViewById(R.id.editTextTextPersonName);
        editText2 = findViewById(R.id.editTextTextPersonName2);
        editText3 = findViewById(R.id.editTextTextPersonName3);
        editText4 = findViewById(R.id.editTextTextPersonName4);
        editText5 = findViewById(R.id.editTextTextPersonName5);
        editText6 = findViewById(R.id.editTextTextPersonName6);

        String text1 = editText1.getText().toString();
        String text2 = editText2.getText().toString();
        String text3 = editText3.getText().toString();
        String text4 = editText4.getText().toString();
        String text5 = editText5.getText().toString();
        String text6 = editText6.getText().toString();

        SharedPreferences myData = getSharedPreferences("bookApp", 0);
        SharedPreferences.Editor myEditor = myData.edit();
        myEditor.putString("attribute1", text1);
        myEditor.putString("attribute2", text2);
        myEditor.putString("attribute3", text3);
        myEditor.putString("attribute4", text4);
        myEditor.putString("attribute5", text5);
        myEditor.putString("attribute6", text6);
        myEditor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);

        editText2 = findViewById(R.id.editTextTextPersonName2);
        editText3 = findViewById(R.id.editTextTextPersonName3);

        outState.putString("title", editText2.getText().toString());
        outState.putString("ISBN", editText3.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);
        editText1 = findViewById(R.id.editTextTextPersonName);
        editText2 = findViewById(R.id.editTextTextPersonName2);
        editText3 = findViewById(R.id.editTextTextPersonName3);
        editText4 = findViewById(R.id.editTextTextPersonName4);
        editText5 = findViewById(R.id.editTextTextPersonName5);
        editText6 = findViewById(R.id.editTextTextPersonName6);

        String newTitle = savedInstanceState.getString("title");
        String newISBN = savedInstanceState.getString("ISBN");

        editText2.setText(newTitle);
        editText3.setText(newISBN);
        editText1.setText("");
        editText4.setText("");
        editText5.setText("");
        editText6.setText("");
    }
}