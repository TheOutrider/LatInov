package com.brialchidimaar.latticeinovationassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.brialchidimaar.latticeinovationassignment.Database.DBHelper;

public class ChatActivity extends AppCompatActivity {

    private TextView textView;
    private EditText messageEdittext;
    private ImageView sendButton;

    DBHelper myDb;
    StringBuffer orderBuffer;
    TextView chatTextview;
    private SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        textView = findViewById(R.id.chatname_txt);
        messageEdittext = findViewById(R.id.message_edt);
        sendButton = findViewById(R.id.send_img);
        chatTextview = findViewById(R.id.textview_chat);

        myDb = new DBHelper(getApplicationContext());
        database = myDb.getWritableDatabase();

        final String name = getIntent().getExtras().getString("name");
        textView.setText(name);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.insertData(name, messageEdittext.getText().toString());
//                adapter.swapCount(getAllData());
                messageEdittext.setText("");
                displayOrdersData();
            }
        });

        displayOrdersData();

    }

    public void displayOrdersData() {

        Cursor res = myDb.getAllData();

        orderBuffer = new StringBuffer();
        while (res.moveToNext()) {
            orderBuffer.append(res.getString(1)+ ":");
            orderBuffer.append(res.getString(2)+ "\n\n");

        }

        showMessage("My Orders", orderBuffer.toString());
    }

    public void showMessage(String name, String message) {
        chatTextview.setText(message + "\n");
    }


}
