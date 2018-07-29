package com.yeah.bztools;


import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mining.app.zxing.MipcaActivityCapture;
import com.yeah.bztools.net.NetInterface;
import com.yeah.bztools.net.ResultEntity;

public class MainActivity extends Activity implements OnClickListener{
	public static final String SCAN_GATEWAY_FLAG_CODE = "1";
	public static final String SCAN_DEVICE_FLAG_CODE = "2";
	public static final int SCAN_GATEWAY_CODE = 0;
	public static final int SCAN_DEVICE_CODE = 1;
	
	private EditText password;
	private EditText appid;
	private EditText secret;
	private EditText userid;
	private EditText username;
	private EditText code;
	private EditText token;
	private EditText deviceid;
	private EditText gwid;
	private EditText gwname;
	private EditText roomnu;
	
	String username_text;
	String password_text;
	String appid_text;
	String secret_text;
	String userid_text;
	String code_text;
	String token_text;
	String deviceid_text;
	String gwid_text;
	String gwname_text;
	String roomnu_text;
	
	private LinearLayout llAutolayout ;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

	private void initView() 
	{
		password = (EditText) findViewById(R.id.et_password);
		username = (EditText) findViewById(R.id.et_username);
		appid = (EditText) findViewById(R.id.et_appid);
		secret = (EditText) findViewById(R.id.et_secret);
		userid = (EditText) findViewById(R.id.et_userid);
		code = (EditText) findViewById(R.id.et_code);
		token = (EditText) findViewById(R.id.et_token);
		deviceid = (EditText) findViewById(R.id.et_deviceid);
		gwid = (EditText) findViewById(R.id.et_gwid);
		gwname = (EditText) findViewById(R.id.et_gwname);
		roomnu = (EditText) findViewById(R.id.et_roomnu);
		
		llAutolayout = (LinearLayout) findViewById(R.id.ll_linear);
		
		Button dataClear = (Button) findViewById(R.id.btn_clear);
		dataClear.setOnClickListener(this);
		Button submit = (Button) findViewById(R.id.btn_submit);
		submit.setOnClickListener(this);
		
		Button login = (Button) findViewById(R.id.btn_login);
		login.setOnClickListener(this);
		Button gettoken = (Button) findViewById(R.id.btn_gettoken);
		gettoken.setOnClickListener(this);
		Button getroom = (Button) findViewById(R.id.btn_getroom);
		getroom.setOnClickListener(this);
		Button scangwid = (Button) findViewById(R.id.btn_scan_gwid);
		scangwid.setOnClickListener(this);
		Button scandeviceid = (Button) findViewById(R.id.btn_scan_deviceid);
		scandeviceid.setOnClickListener(this);
		Button opendoor = (Button) findViewById(R.id.btn_opendoor);
		opendoor.setOnClickListener(this);
		Button addgateway = (Button) findViewById(R.id.btn_addgateway);
		addgateway.setOnClickListener(this);
		Button bindroom = (Button) findViewById(R.id.btn_bindroom);
		bindroom.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_clear:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
//					appid.setText("");
//					secret.setText("");
					userid.setText("");
					code.setText("");
					token.setText("");
					roomnu.setText("");
					llAutolayout.removeAllViews();
				}
			});
			break;
		case R.id.btn_login:
			username_text = username.getText().toString().trim();
			password_text = password.getText().toString().trim();
			if (!TextUtils.isEmpty(username_text)&&!TextUtils.isEmpty(password_text)) {
				login(username_text,password_text);
			} else {
				Toast.makeText(MainActivity.this, "输入账号和密码登录!",Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_gettoken:
			appid_text = appid.getText().toString().trim();
			secret_text = secret.getText().toString().trim();
			if (!TextUtils.isEmpty(appid_text)&&!TextUtils.isEmpty(secret_text)) {
				getToken(appid_text,secret_text);
			} else {
				Toast.makeText(MainActivity.this, "输入appid和secret登录!",Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_scan_deviceid:
			Intent intent = new Intent(this, MipcaActivityCapture.class);
            intent.putExtra("tag",SCAN_DEVICE_FLAG_CODE);
            startActivityForResult(intent,SCAN_DEVICE_CODE);
			break;
		case R.id.btn_scan_gwid:
			Intent intent2 = new Intent(this, MipcaActivityCapture.class);
			intent2.putExtra("tag",SCAN_GATEWAY_FLAG_CODE);
			startActivityForResult(intent2,SCAN_GATEWAY_CODE);
			break;
		case R.id.btn_getroom:
			deviceid_text = deviceid.getText().toString().trim();
			token_text = token.getText().toString().trim();
			
			if (!TextUtils.isEmpty(deviceid_text)&&!TextUtils.isEmpty(token_text)) {
				getRoomInfo(deviceid_text,token_text);
			} else {
				Toast.makeText(MainActivity.this, "设备ID和token不能为空!",Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_opendoor:
			appid_text = appid.getText().toString().trim();
			token_text = token.getText().toString().trim();
			roomnu_text = roomnu.getText().toString().trim();
			if (!TextUtils.isEmpty(appid_text)&&!TextUtils.isEmpty(token_text)) {
				openDoor(token_text,appid_text,roomnu_text);
			} else {
				Toast.makeText(MainActivity.this, "appid_text和token不能为空!",Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_addgateway:
			gwid_text = gwid.getText().toString().trim();
			gwname_text = gwname.getText().toString().trim();
			userid_text = userid.getText().toString().trim();
			token_text = token.getText().toString().trim();
			if (!TextUtils.isEmpty(deviceid_text)&&!TextUtils.isEmpty(token_text)) {
				addGateway(gwid_text,gwname_text,userid_text,token_text);
			} else {
				Toast.makeText(MainActivity.this, "(添加网关)指定参数不能为空！",Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_bindroom:
			gwid_text = gwid.getText().toString().trim();
			deviceid_text = deviceid.getText().toString().trim();
			roomnu_text = roomnu.getText().toString().trim();
			userid_text = userid.getText().toString().trim();
			token_text = token.getText().toString().trim();
			if (!TextUtils.isEmpty(roomnu_text)&&!TextUtils.isEmpty(token_text)) {
				bindRoom(deviceid_text,roomnu_text,userid_text,token_text,gwid_text);
			} else {
				Toast.makeText(MainActivity.this, "(绑定房间)指定参数不能为空！",Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_submit:
			break;
		case R.id.btn_dbtype:
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
               	case SCAN_GATEWAY_CODE:
                    //二维码扫描的返回结果
                    if (data !=null) {
                    	String gwidStr = data.getStringExtra("result");
                		gwid.setText(gwidStr);
                    }
                    break;
                case SCAN_DEVICE_CODE:
                	//二维码扫描的返回结果
                	if (data !=null) {
                		String deviceidStr = data.getStringExtra("result");
                		deviceid.setText(deviceidStr);
                	}
                	break;
            }
        }
    }
	

	private void login(String username, String password) {
		new NetInterface(getApplicationContext()).login(username,password, new NetInterface.DataCallBack<ResultEntity>() {
			@Override
			public void onData(ResultEntity data) {
				if(data.getCode().equals("0")){
					code.setText(data.getCode());
					userid.setText(data.getUserid());
					appid.setText(data.getAppid());
					secret.setText(data.getSecret());
					autoViewTOLayout(data);
				}
				Toast.makeText(MainActivity.this, getMsg(data.getCode(),"登录操作"), Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onFail(String msg) {
				Toast.makeText(MainActivity.this, "登录失败,检查用户名密码是否正确"+msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void getToken(String username_text, String password_text) {
		new NetInterface(getApplicationContext()).getToken(username_text,password_text, new NetInterface.DataCallBack<ResultEntity>() {
			@Override
			public void onData(ResultEntity data) {
				Toast.makeText(MainActivity.this, getMsg(data.getCode(),"获取Token操作"), Toast.LENGTH_SHORT).show();
				code.setText(data.getCode());
				token.setText(data.getToken());
				autoViewTOLayout(data);
			}
			@Override
			public void onFail(String msg) {
				Toast.makeText(MainActivity.this, "获取Token失败"+msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void getRoomInfo(String deviceid_text, String token_text) {
		new NetInterface(getApplicationContext()).getRoomInfo(token_text,deviceid_text, new NetInterface.DataCallBack<ResultEntity>() {
			@Override
			public void onData(ResultEntity data) {
				Toast.makeText(MainActivity.this, getMsg(data.getCode(),"获取房间信息操作"), Toast.LENGTH_LONG).show();
				roomnu.setText(data.getRoomnu());
				autoViewTOLayout(data);
			}
			@Override
			public void onFail(String msg) {
				Toast.makeText(MainActivity.this, "获取房间信息失败"+msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void openDoor(String token_text,String appid_text,String roomnu_text) {
		new NetInterface(getApplicationContext()).openDoor(token_text,appid_text,roomnu_text, new NetInterface.DataCallBack<ResultEntity>() {
			@Override
			public void onData(ResultEntity data) {
				Toast.makeText(MainActivity.this, getMsg(data.getCode(),"请求开门操作"), Toast.LENGTH_LONG).show();
				code.setText(data.getCode());
				autoViewTOLayout(data);
			}
			@Override
			public void onFail(String msg) {
				Toast.makeText(MainActivity.this, "请求开门失败"+msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void addGateway(String gwid_text, String gwname_text,
			String userid_text, String token_text) {
		new NetInterface(getApplicationContext()).addGateway(gwid_text,gwname_text,userid_text,token_text, new NetInterface.DataCallBack<ResultEntity>() {
			@Override
			public void onData(ResultEntity data) {
				Toast.makeText(MainActivity.this, getMsg(data.getCode(),"添加网关操作"), Toast.LENGTH_LONG).show();
				code.setText(data.getCode());
				autoViewTOLayout(data);
			}
			@Override
			public void onFail(String msg) {
				Toast.makeText(MainActivity.this, "添加网关失败"+msg, Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	private void bindRoom(String deviceid_text, String roomnu_text,
			String userid_text, String token_text,String gwid_text) {
		new NetInterface(getApplicationContext()).bindRoom(deviceid_text,roomnu_text,userid_text,token_text,gwid_text, new NetInterface.DataCallBack<ResultEntity>() {
			@Override
			public void onData(ResultEntity data) {
				Toast.makeText(MainActivity.this, getMsg(data.getCode(),"绑定房间操作"), Toast.LENGTH_LONG).show();
				code.setText(data.getCode());
				autoViewTOLayout(data);
			}
			@Override
			public void onFail(String msg) {
				Toast.makeText(MainActivity.this, "获取Token失败"+msg, Toast.LENGTH_SHORT).show();
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

	private void autoViewTOLayout(ResultEntity res) {
	    llAutolayout.removeAllViews();
	    llAutolayout.addView(autoTextView("编码:  "+res.getCode()));
	    llAutolayout.addView(autoTextView("描述:  "+getMsg(res.getCode(), "")));
	}
	
	 private TextView autoTextView(String str) {
	    TextView text = new TextView(this);
	    text.setTextColor(Color.BLACK);
	    text.setText(str);
	    return text;
	}
}
