package com.plmt.boommall.ui.activity;

import com.plmt.boommall.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


public class AboutActivity extends Activity implements OnClickListener {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        findViewById();
        initView();
    }


    protected void findViewById() {
        mImageView = (ImageView) findViewById(R.id.about_back_iv);
    }


    protected void initView() {
        mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_back_iv: {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            }
            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        return super.onKeyDown(keyCode, event);
    }

}
