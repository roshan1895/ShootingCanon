package com.example.shootingcanon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.start_btn)
    ImageButton startBtn;
    @BindView(R.id.start_txt)
    TextView startTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.start_btn)
    public void onViewClicked() {
        Log.e("start button","clicked");
        Intent intent=new Intent(MainActivity.this,StartGame.class);
        startActivity(intent);
        finish();
    }

}
