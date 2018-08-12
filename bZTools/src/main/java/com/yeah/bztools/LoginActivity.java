package com.yeah.bztools;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yeah.bztools.net.NetInterface;
import com.yeah.bztools.net.ResultEntity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    //String code;
    String userid;
    String appid;
    String secret;

    private EditText accountEdit;

    private EditText passwordEdit;

    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();
                login(account,password);
            }
        });
    }


    private void login(String username, String password) {
        new NetInterface(getApplicationContext()).login(username,password, new NetInterface.DataCallBack<ResultEntity>() {
            @Override
            public void onData(ResultEntity data) {
                if(data.getCode().equals("0")){
                    //code=data.getCode();
                    userid=data.getUserid();
                    appid=data.getAppid();
                    secret=data.getSecret();

                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                    //cmt 密码正确进入MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                    //intent.putExtra("code_s",code);
                    intent.putExtra("user_s",userid);
                    intent.putExtra("appid_s",appid);
                    intent.putExtra("secret_s",secret);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(LoginActivity.this, getMsg(data.getCode(), "登录操作"), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFail(String msg) {
                Toast.makeText(LoginActivity.this, "登录失败,检查用户名密码是否正确"+msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getMsg(String key,String operName){
        Map<String, String> resMap = new HashMap<String, String>();
        resMap.put("0", operName+",成功");
        resMap.put("10000", operName+",其他");
        resMap.put("10001", operName+",secret 不正确");
        resMap.put("10002", operName+",接口凭证过期");
        resMap.put("10003", operName+",参数出错");
        resMap.put("10004", operName+",房间号不存在");
        resMap.put("10005", operName+",设备ID不存在");
        resMap.put("10006", operName+",数据库服务异常");
        resMap.put("10007", operName+",Redis服务异常");
        resMap.put("10008", operName+",开门失败，网关不在线");
        resMap.put("10009", operName+",网关已存在");
        resMap.put("10010", operName+",用户数据不匹配");
        resMap.put("10011", operName+",设备已被绑定");
        resMap.put("10012", operName+",用户ID不存在");
        resMap.put("10013", operName+",网关不存在");
        resMap.put("10014", operName+",该用户没有登录权限");
        resMap.put("10015", operName+",房间已被绑定");

        return resMap.get(key);
    }

}
