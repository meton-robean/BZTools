package com.yeah.bztools;

import com.yeah.bztools.fragment.ConstactFatherFragment;
import com.yeah.bztools.fragment.AddGatewayFragment;
import com.yeah.bztools.fragment.SettingFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;



public class MainActivity2 extends FragmentActivity {
    protected static final String TAG = "MainActivity2";
    private Button mSetting;
    private Button mConstact;
    private Button mNews;
    private LinearLayout buttomBarGroup;
    private View currentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottombar);
        initView();

    }


    private void initView(){

        buttomBarGroup=(LinearLayout) findViewById(R.id.buttom_bar_group);
        mNews=(Button) findViewById(R.id.buttom_news);
        mConstact=(Button) findViewById(R.id.buttom_constact);
        mSetting=(Button) findViewById(R.id.buttom_setting);

        mNews.setOnClickListener(newsOnClickListener);
        mConstact.setOnClickListener(constactOnClickListener);
        mSetting.setOnClickListener(settingOnClickListener);

    }

    private View.OnClickListener newsOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            AddGatewayFragment addGatewayFragment=new AddGatewayFragment();
            ft.replace(R.id.fl_content, addGatewayFragment, MainActivity2.TAG);
            ft.commit();
            setButton(v);

        }
    };

    private View.OnClickListener constactOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ConstactFatherFragment constactFatherFragment=new ConstactFatherFragment();
            ft.replace(R.id.fl_content, constactFatherFragment,MainActivity2.TAG);
            ft.commit();
            setButton(v);

        }
    };

    private View.OnClickListener settingOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            SettingFragment settingFragment=new SettingFragment();
            ft.replace(R.id.fl_content, settingFragment, MainActivity2.TAG);
            ft.commit();
            setButton(v);

        }
    };

    private void setButton(View v){
        if(currentButton!=null&&currentButton.getId()!=v.getId()){
            currentButton.setEnabled(true);
        }
        v.setEnabled(false);
        currentButton=v;
    }


}
