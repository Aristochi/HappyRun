package com.example.happyrunning.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.happyrunning.R;

import java.util.ArrayList;
import java.util.List;

public class Friend extends AppCompatActivity {
    ArrayAdapter<String>adapter;
    List<String>contactsList=new ArrayList<>();
    ImageView iv_left;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        iv_left=findViewById(R.id.iv_left_friend);
        ListView contactView=(ListView)findViewById(R.id.contacts_view);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contactsList);
        contactView.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }
        else
        {
            readContacts();
        }
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Friend.this,MainActivity.class));
            }
        });
    }
    private void readContacts()
    {
        Cursor cursor=null;
        try
        {
            cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);

            if (cursor!=null) {
                while (cursor.moveToNext())
                {
                    String displayname=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayname+"\n"+number);

                }
                adapter.notifyDataSetChanged();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (cursor!=null)
            {
                cursor.close();
            }



        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode)
        {
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    readContacts();
                }
                else
                {
                    Toast.makeText(this,"没有读取联系人权限",Toast.LENGTH_SHORT).show();

                }
                break;
                default:
        }
    }
}
