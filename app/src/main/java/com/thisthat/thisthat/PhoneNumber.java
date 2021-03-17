package com.thisthat.thisthat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thisthat.thisthat.adapters.MainAdapter;
import com.thisthat.thisthat.models.ContactModel;
import com.thisthat.thisthat.models.GetUserModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneNumber extends AppCompatActivity {
    SharedPreferencesConfig sharedPreferencesConfig;
    RecyclerView recyclerView;
    ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
    MainAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        recyclerView = findViewById(R.id.recycler_view);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());

        checkPermission();

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(PhoneNumber.this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //request permission
            ActivityCompat.requestPermissions(PhoneNumber.this,
                    new String[]{Manifest.permission.READ_CONTACTS},100);
        }else {
            //permission granted
            AlertDialog.Builder al = new AlertDialog.Builder(PhoneNumber.this);
            al.setTitle("IMPORTANT!")
                    .setMessage("Before you select a friend, ensure your friend's phone country code matches the code showing on the right side of the contact.\nIf they do not match, kindly change to their country code, otherwise you'll get an error")
                    .setPositiveButton("Cool", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog alertDialog = al.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            getContactList();
        }
    }

    private void getContactList() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //sort by ascending
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";
        //initialize cursor
        Cursor cursor = getContentResolver().query(uri,null,null,null,sort);
        //check condition
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                Cursor phoneCursor = getContentResolver().query(uriPhone,null,selection,new String[]{id},null);
                if (phoneCursor.moveToNext()){
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    ContactModel contactModel = new ContactModel();
                    contactModel.setName(name);
                    contactModel.setNumber(number);
                    arrayList.add(contactModel);
                    phoneCursor.close();
                }
            }
            cursor.close();
        }
        //setLayout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            AlertDialog.Builder al = new AlertDialog.Builder(PhoneNumber.this);
            al.setTitle("IMPORTANT!")
                    .setMessage("Before you select a friend, ensure your friend's phone country code matches the code showing on the right side of the contact.\nIf they do not match, kindly change to their country code, otherwise you'll get an error")
                    .setPositiveButton("Cool", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog alertDialog = al.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            getContactList();
        }else {
            checkPermission();
        }
    }


}