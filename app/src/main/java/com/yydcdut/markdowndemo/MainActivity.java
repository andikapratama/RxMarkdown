package com.yydcdut.markdowndemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yydcdut.rxmarkdown.EditActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.btn_edit_show).setOnClickListener(this);
        findViewById(R.id.btn_compare).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_compare:
                startActivity(new Intent(this, CompareActivity.class));
                break;
            case R.id.btn_edit_show:
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(EditActivity.TITLE, "Judul");
                intent.putExtra(EditActivity.TEXT, "Deskripsi Barang");
                intent.putExtra(EditActivity.PLACEHOLDER, "wahaha");
                startActivityForResult(intent, 123);
                break;
        }
    }
}
