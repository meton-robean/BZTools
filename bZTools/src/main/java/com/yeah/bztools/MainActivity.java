package com.yeah.bztools;


import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
	public static final String LOG_TAG="MainActivity";

	private EditText code;
	private EditText token;
	private EditText deviceid;
	private EditText gwid;
	private EditText gwname;
	private EditText roomnu;
	

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
        //cmt 从登录界面获得appid secret userid
        Intent intent_tmp=getIntent();
        appid_text=intent_tmp.getStringExtra("appid_s");
		secret_text=intent_tmp.getStringExtra("secret_s");
		userid_text=intent_tmp.getStringExtra("user_s");
		Log.d(LOG_TAG,appid_text);
		Log.d(LOG_TAG,secret_text);
		Log.d(LOG_TAG,userid_text);
		getToken(appid_text,secret_text);
        initView();
    }

	private void initView() 
	{

//		password = (EditText) findViewById(R.id.et_password);
//		username = (EditText) findViewById(R.id.et_username);
//		appid = (EditText) findViewById(R.id.et_appid);
//		secret = (EditText) findViewById(R.id.et_secret);
//		userid = (EditText) findViewById(R.id.et_userid);
//		code = (EditText) findViewById(R.id.et_code);
//		token = (EditText) findViewById(R.id.et_token);
		deviceid = (EditText) findViewById(R.id.et_deviceid);
		deviceid.setEnabled(false);
		gwid = (EditText) findViewById(R.id.et_gwid);
		gwname = (EditText) findViewById(R.id.et_gwname);
		roomnu = (EditText) findViewById(R.id.et_roomnu);
		roomnu.setEnabled(false);

		llAutolayout = (LinearLayout) findViewById(R.id.ll_linear);

		Button dataClear = (Button) findViewById(R.id.btn_clear);
		dataClear.setOnClickListener(this);
		Button submit = (Button) findViewById(R.id.btn_submit);
		submit.setOnClickListener(this);

//		Button login = (Button) findViewById(R.id.btn_login);
//		login.setOnClickListener(this);
//		Button gettoken = (Button) findViewById(R.id.btn_gettoken);
//		gettoken.setOnClickListener(this);
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
					roomnu.setText("");
					deviceid.setText("");
					gwid.setText("");
					gwname.setText("");
					getToken(appid_text,secret_text);
					deviceid.setEnabled(false);
					roomnu.setEnabled(false);
					llAutolayout.removeAllViews();
				}
			});
			break;
		case R.id.btn_scan_deviceid:
			//cmt 启动扫描二维码的界面
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
			deviceid_text = deviceid.getText().toString();
			getToken(appid_text,secret_text);//cmt 获取token,保存于全局变量 token_text中
			if (!TextUtils.isEmpty(deviceid_text)&&!TextUtils.isEmpty(token_text)) {
				getRoomInfo(deviceid_text,token_text);
			} else {
				Toast.makeText(MainActivity.this, "设备id参数不能为空,或者token过期",Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_opendoor:
			roomnu_text = roomnu.getText().toString().trim();
			deviceid.setEnabled(true);
			roomnu.setEnabled(true);
			getToken(appid_text,secret_text);
			if (!TextUtils.isEmpty(appid_text)&&!TextUtils.isEmpty(token_text)) {
				openDoor(token_text,appid_text,roomnu_text);
			} else {
				Toast.makeText(MainActivity.this, "相关参数不能为空，或者token过期",Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_addgateway:
			gwid_text = gwid.getText().toString().trim();
			gwname_text = gwname.getText().toString().trim();
			deviceid.setEnabled(true);
			roomnu.setEnabled(true);
			getToken(appid_text,secret_text);
			if(!TextUtils.isEmpty(gwid_text)&&!TextUtils.isEmpty(token_text)) {
				addGateway(gwid_text,gwname_text,userid_text,token_text);
			} else {
				Toast.makeText(MainActivity.this, "(添加网关)指定参数不能为空,或者token过期",Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_bindroom:
			gwid_text = gwid.getText().toString().trim();
			deviceid_text = deviceid.getText().toString().trim();
			roomnu_text = roomnu.getText().toString().trim();
			deviceid.setEnabled(false);
			roomnu.setEnabled(false);
			getToken(appid_text,secret_text);
			if (!TextUtils.isEmpty(roomnu_text)&&!TextUtils.isEmpty(token_text)) {
				bindRoom(deviceid_text,roomnu_text,userid_text,token_text,gwid_text);
				Log.d(LOG_TAG,"in case btn_bindroom");
			} else {
				Toast.makeText(MainActivity.this, "(绑定房间)指定参数不能为空,或者token过期",Toast.LENGTH_SHORT).show();
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
                    	gwid.setText("");
                		gwid.setText(gwidStr);
                    }
                    break;
                case SCAN_DEVICE_CODE:
                	//二维码扫描的返回结果
                	if (data !=null) {
                		String deviceidStr = data.getStringExtra("result");
                		deviceid.setText("");
                		deviceid.setText(deviceidStr);
                	}
                	break;
            }
        }
    }
	


    /*
    * cmt 获取token,保存于全局变量 token_text,code_text中
    *
    * */
	private void getToken(String username_text, String password_text) {
		new NetInterface(getApplicationContext()).getToken(username_text,password_text, new NetInterface.DataCallBack<ResultEntity>() {
			@Override
			public void onData(ResultEntity data) {
				Toast.makeText(MainActivity.this, getMsg(data.getCode(),"获取Token操作"), Toast.LENGTH_SHORT).show();
				//code.setText(data.getCode());
				//token.setText(data.getToken());
				code_text=data.getCode();
				token_text=data.getToken();
				//autoViewTOLayout(data);
			}
			@Override
			public void onFail(String msg) {
				Toast.makeText(MainActivity.this, "获取Token失败,再试一次"+msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void getRoomInfo(String deviceid_text, String token_text) {
		new NetInterface(getApplicationContext()).getRoomInfo(token_text,deviceid_text, new NetInterface.DataCallBack<ResultEntity>() {
			@Override
			public void onData(ResultEntity data) {
				Toast.makeText(MainActivity.this, getMsg(data.getCode(),"获取房间信息操作"), Toast.LENGTH_LONG).show();
				roomnu.setText(data.getRoomnu());
				autoViewTOLayout(data,"获取房间信息");
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
				code_text=data.getCode();
				autoViewTOLayout(data,"开门操作");
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
				code_text=data.getCode();
				autoViewTOLayout(data,"添加网关");
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
				code_text=data.getCode();
				autoViewTOLayout(data,"绑定网关、设备、房间,");
			}
			@Override
			public void onFail(String msg) {
				Toast.makeText(MainActivity.this, "获取Token失败"+msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
	private String getMsg(String key,String operName){
		Map<String, String> resMap = new HashMap<String,String>();
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

	//cmt 在这个view中展示对应code的对应描述
	private void autoViewTOLayout(ResultEntity res,String operName) {
	    llAutolayout.removeAllViews();
	    llAutolayout.addView(autoTextView("返回码:  "+res.getCode()));
	    llAutolayout.addView(autoTextView("描述:  "+getMsg(res.getCode(), operName)));
	}
	
	 private TextView autoTextView(String str) {
	    TextView text = new TextView(this);
	    text.setTextColor(Color.BLACK);
	    text.setText(str);
	    return text;
	}
}
