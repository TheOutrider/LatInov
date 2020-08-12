package com.brialchidimaar.latticeinovationassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.brialchidimaar.latticeinovationassignment.SharedPrefs.SharedPrefs;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    TextView nameTextView;
    CheckBox enableChk, visibleChk;
    ListView listView;

    private BluetoothAdapter ba;
    private Set<BluetoothDevice> pairedDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Integer.parseInt(String.valueOf(Color.WHITE)));

        listView = findViewById(R.id.listview);
        enableChk = findViewById(R.id.enable_checkbox);
        visibleChk = findViewById(R.id.visible_checkbox);
        nameTextView = findViewById(R.id.name_txt);

        nameTextView.setText(getLocalBluetoothName());
        ba = BluetoothAdapter.getDefaultAdapter();

        if(ba == null) {
            Toast.makeText(this, "Bluetooth Connection Failed", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (ba.isEnabled()) {
            enableChk.setChecked(true);
        }

        enableChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    ba.disable();
                    Toast.makeText(MainActivity.this, "Turned off", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, 0);
                    Toast.makeText(MainActivity.this, "Turned on", Toast.LENGTH_SHORT).show();
                }
            }
        });

        visibleChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getList();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedName = (String) listView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("name", clickedName);
                startActivity(intent);
            }
        });

    }

    private void getList() {

        pairedDevices = ba.getBondedDevices();
        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
        }
        Toast.makeText(MainActivity.this, "Showing Devices", Toast.LENGTH_SHORT).show();
        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);

    }

    public String getLocalBluetoothName() {
        if (ba == null) {
            ba = BluetoothAdapter.getDefaultAdapter();
        }
        String name = ba.getName();
        if (name == null ) {
            name = ba.getAddress();
        }
        return name;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                openDialog();
                return true;

            case R.id.connect_scan:
                startActivity(new Intent(getApplicationContext(), ScanActivity.class));
                break;
        }
        return  false;
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.logout_dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button yesbtn = dialog.findViewById(R.id.yesbtn);
        Button nobtn = dialog.findViewById(R.id.nobtn);

        nobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dialog.getContext(), LoginActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                SharedPrefs.setLogin("login", " ", dialog.getContext());
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
