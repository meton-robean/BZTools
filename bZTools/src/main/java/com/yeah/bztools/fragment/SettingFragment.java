package com.yeah.bztools.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mining.app.zxing.MipcaActivityCapture;
import com.yeah.bztools.R;

public class SettingFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = SettingFragment.class.getName();
    private Context mContext;
    private View mBaseView;
    private EditText edittext_hotel;
    private EditText edittext_accout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView=inflater.inflate(R.layout.activity_third_bottom_menu, container,false);
        Intent intent_tmp=getActivity().getIntent();
        initView();
        return mBaseView;
    }

    private void initView(){
        Button btn_finish = (Button) mBaseView.findViewById(R.id.btn_finish_install);
        btn_finish.setOnClickListener(this);

        Button btn_exit = (Button) mBaseView.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);

        edittext_accout=(EditText) mBaseView.findViewById(R.id.current_account);
        edittext_hotel=(EditText) mBaseView.findViewById(R.id.hotel_name);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish_install:
                /* something to do */
                break;

            case R.id.btn_exit:
                Intent intent = new Intent("com.yeah.bztools.FORCE_OFFLINE");
                mContext.sendBroadcast(intent);
                break;

            default:
                break;
        }
    }

}
