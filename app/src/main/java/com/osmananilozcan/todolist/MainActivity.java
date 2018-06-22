package com.osmananilozcan.todolist;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*Veritabanı nesnesi tanımlayalım*/
    DbHelper dbHelper;
    ArrayAdapter<String> mAdapter;
        ListView gorevListesi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        gorevListesi = (ListView)findViewById(R.id.lstTask);

        loadTaskList();
    }

    private void loadTaskList() {
        ArrayList<String> taskList = dbHelper.tumGorevleriGetir();
        if(mAdapter == null){
            mAdapter = new ArrayAdapter<String>(this,R.layout.row,R.id.gorev_basligi,taskList);
            gorevListesi.setAdapter(mAdapter);
        }
        else{
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        //Change menu icon color
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.yeni_gorev_ekle:
                final EditText gorevEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Yeni Görev Ekle")
                        .setMessage("Ne yapmak istiyorsun?")
                        .setView(gorevEditText)
                        .setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(gorevEditText.getText());
                                dbHelper.yeniGorevEkle(task);
                                loadTaskList();
                            }
                        })
                        .setNegativeButton("Vazgeç",null)
                        .create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gorevSil(View view){
        View parent = (View)view.getParent();
        TextView gorevEditText = (TextView)parent.findViewById(R.id.gorev_basligi);
        String gorev = String.valueOf(gorevEditText.getText());
        dbHelper.gorevSil(gorev);
        loadTaskList();
    }
}
