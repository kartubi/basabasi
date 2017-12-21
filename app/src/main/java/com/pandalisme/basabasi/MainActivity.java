package com.pandalisme.basabasi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pandalisme.basabasi.ui.chat.ChatActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etName)
    EditText etName;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btNext.setOnClickListener(view -> validation());

    }

    @OnClick(R.id.btNext)
    private void validation(){
        name = etName.getText().toString();
        if (!name.isEmpty()){
            setName();
        }else {
            Toast.makeText(getApplicationContext(), "DI ISI DONG GANTENG", Toast.LENGTH_SHORT).show();
        }
    }

    private void setName(){
        SharedPreferences sP = getSharedPreferences("profile", Context.MODE_PRIVATE);
        sP.edit().putString("name", name).apply();
        Intent i = new Intent(getApplicationContext(), ChatActivity.class);
        startActivity(i);
        finish();
    }


}
