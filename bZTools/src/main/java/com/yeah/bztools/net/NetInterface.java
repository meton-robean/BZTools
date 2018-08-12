package com.yeah.bztools.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.yeah.bztools.net.NetInterface.DataCallBack;

public class NetInterface {
	private final Context mContext;
	public final static String LOGIN_URL = "http://120.79.233.219:22347/v1/login";
	public final static String GETTOKEN_URL = "http://120.79.233.219:22347/v1/token";
	public final static String GETROOM_URL = "http://120.79.233.219:22347/v1/get-roominfo";
	public final static String OPENDOOR_URL = "http://120.79.233.219:22347/v1/open-door";
	public final static String ADDGATEWAY_URL = "http://120.79.233.219:22347/v1/add-gateway";
	public final static String BINDROOM_URL = "http://120.79.233.219:22347/v1/bind-room";
	private final RequestQueue rq;
	
	public NetInterface(Context mContext) {
		rq = Volley.newRequestQueue(mContext);
		this.mContext = mContext;
	}

	private boolean isShowDlg = false;

	public void setShowDialog(boolean isShowDialog) {
		this.isShowDlg = isShowDialog;
	}

	private ProgressDialog progressDialog;

	private class MyResponseListener implements Response.Listener<String>, Response.ErrorListener {
		private final Class cls;
		private final DataCallBack dataCallBack;
		private final Map<String, String> params;
		private final String interfaceName;

		private MyResponseListener(Class cls, DataCallBack dataCallBack,
				Map<String, String> params, String interfaceName) {
			this.cls = cls;
			this.dataCallBack = dataCallBack;
			this.params = params;
			this.interfaceName = interfaceName;
		}

		@Override
		public void onErrorResponse(VolleyError error) {
			String msg = null;
			if (error instanceof TimeoutError
					|| error instanceof NoConnectionError) {
				msg = "当前网络较差,请重试看看";
			} else {
				msg = error.getMessage();
			}

			if (TextUtils.isEmpty(msg)) {
				msg = "系统错误，请联系管理员";
			}

			if (isShowDlg)
				progressDialog.dismiss();
			dataCallBack.onFail(msg);
		}

		@Override
		public void onResponse(String response) {
			try {
				JSONObject jsonObject = new JSONObject(response);
				Result result = new Result();
				int ret = (int) jsonObject.getInt("ret");
				// Log.e("rettype", "fffff--"+ret);
				result.setRet(ret);
				result.setMsg(jsonObject.getString("msg"));
				if (result.isSuccess()) {
					Gson gson = new Gson();
					Object data = gson.fromJson(response, cls);
					dataCallBack.onData(data);
				} else {
					dataCallBack.onFail(result.getMsg());

				}
			} catch (JSONException e) {
				dataCallBack.onFail(e.getMessage());
			}
			if (isShowDlg) {
				progressDialog.dismiss();
			}
		}
	}
	
	private class MyGetResponseListener implements Response.Listener<String>, Response.ErrorListener {
        private final Class cls;
        private final DataCallBack dataCallBack;
        private final Map<String, String> params;
        private final String interfaceName;
        

        private MyGetResponseListener(Class cls, DataCallBack dataCallBack, Map<String, String> params, String interfaceName) {
            this.cls = cls;
            this.dataCallBack = dataCallBack;
            this.params = params;
            this.interfaceName = interfaceName;
        }	

        @Override
        public void onErrorResponse(VolleyError error) {
            String msg = null;
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                msg = "当前网络较差,请重试看看";
            } else {
                msg = error.getMessage();
            }

            if (TextUtils.isEmpty(msg)) {
                msg = "系统错误，请联系管理员";
            }

            if (isShowDlg)
                progressDialog.dismiss();
            	dataCallBack.onFail(msg);
        }

        @Override
        public void onResponse(String response) {
            	if (response != null) {
            		Gson gson = new Gson();
                    try {
                    	//cmt 这里cls传入的是装载json信息的类实例
                    	Object data = gson.fromJson(response, cls);
                        dataCallBack.onData(data);
					} catch (Exception e) {
						dataCallBack.onFail("数据解析失败,错误码："+Result.DATA_DEC_FAIL);
					}
            	} else {
            		dataCallBack.onFail("数据请求失败");
            	}
        
            if (isShowDlg) {
                progressDialog.dismiss();
            }
        }
    }

//	public void doPostRequest(final Map<String, String> params,
//			final String interfaceName, final Class cls,
//			final DataCallBack dataCallBack) {
//		String url = BASE_URL.concat(interfaceName);
//		url = url.replaceAll(" ", "%20");
//		MyResponseListener myResponseListener = new MyResponseListener(cls,
//				dataCallBack, params, interfaceName);
//		doPostRequest(new StringRequest(Request.Method.POST, url,
//				myResponseListener, myResponseListener) {
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				return params;
//			}
//		});
//	}

	public void doGetRequest( Map<String, String> params,
			 String urlname,  Class cls,
			 DataCallBack dataCallBack) {
		String url = urlname;
		//cmt 拼接请求接口url
		String mapToString = getMapToString(params);

		if (!TextUtils.isEmpty(mapToString)){
			url = url+"?"+mapToString;
		}
		url = url.replaceAll(" ", "%20");
		MyGetResponseListener myGetResponseListener = new MyGetResponseListener(cls,
				dataCallBack, params, urlname);

		//cmt StringRequest(int method, String url, Listener<String> listener, ErrorListener errorListener)
		doPostRequest( new StringRequest(Request.Method.GET, url,
				myGetResponseListener, myGetResponseListener) );
	}


	private String message = "loading...";

	public void setDialogMessage(String message) {
		this.message = message;
	}

	private static final int OUT_TIME = 8000;
	private static final int TIMES_OF_RETRY = 1;

	private void doPostRequest(Request request) {
		if (isShowDlg) {
			progressDialog = ProgressDialog.show(mContext, null, message, true);
			progressDialog.setCancelable(true);
		}
		// 给每个请求重设超时时间、重试次数
		request.setRetryPolicy(new DefaultRetryPolicy(OUT_TIME, TIMES_OF_RETRY,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		//cmt 加入请求队列
		rq.add(request);
	}


	//cmt 接口
	public interface DataCallBack<T> {
		public void onData(T data);

		public void onFail(String msg);
	}


	private String getMapToString(Map<String, String> params) {
    	String mapString = "";
    	if (params != null && params.size()>0) {
			Set<Entry<String, String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String key = entry.getKey();
				String value = entry.getValue();
				mapString =mapString + key+"="+value+"&";
			}
		}
    	if (!TextUtils.isEmpty(mapString)) {
    		mapString = mapString.substring(0, mapString.lastIndexOf("&"));
    	}
    	return mapString;
    }
	
	public void login(String username, String password,
			DataCallBack<ResultEntity> dataCallBack) 
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
    	params.put("pwd", MD5Util.md5Encode(password));
    	doGetRequest(params, LOGIN_URL, ResultEntity.class, dataCallBack);
	}

	public void getToken(String username_text, String password_text,
			DataCallBack<ResultEntity> dataCallBack) {
		Map<String, String> params = new HashMap<String, String>();
		//http://120.79.233.219:12347/v1/token?appid=8dddeeffbf83d067fa4f385e33f15a3f
		//&secret=ba71399e517572dbba14a1360ea86900//GET请求
		
    	params.put("appid", username_text);
    	params.put("secret", password_text);
    	doGetRequest(params, GETTOKEN_URL, ResultEntity.class, dataCallBack);
	}
	
	public void getRoomInfo(String token_text, String deviceid_text,
			DataCallBack<ResultEntity> dataCallBack) {
		Map<String, String> params = new HashMap<String, String>();
		//http://120.79.233.219:12347/v1/get-roominfo?deviceid=123456790
		//&token=247b740204dea87aa82baae09bd289a493efb8b74b9351c518cb3307456e6d43"

    	params.put("deviceid", deviceid_text);
    	params.put("token", token_text);
    	doGetRequest(params, GETROOM_URL, ResultEntity.class, dataCallBack);
	}

	public void openDoor(String token_text,String appid_text,String roomnu_text,
			DataCallBack<ResultEntity> dataCallBack) {
		Map<String, String> params = new HashMap<String, String>();
		//http://120.79.233.219:12347/v1/open-door?roomnu=1001&appid=8dddeeffbf83d067fa4f385e33f15a3f
		//&method=1&token=bd65d5b49d432e88c3109dbce09a4a89ff4ec5c11ccea2f26f4a70cfb8296fd9"

		params.put("roomnu", roomnu_text);
    	params.put("appid", appid_text);
    	params.put("method", "1");
    	params.put("token", token_text);
    	doGetRequest(params, OPENDOOR_URL, ResultEntity.class, dataCallBack);
	}

	public void addGateway(String gwid_text, String gwname_text,
			String userid_text, String token_text,
			DataCallBack<ResultEntity> dataCallBack) {
		Map<String, String> params = new HashMap<String, String>();
		//http://120.79.233.219:12347/v1/token?add-gateway?gwid =2342352&gwname=gatewayname&userid=12&token=aaaaa
		
    	params.put("gwid", gwid_text);
    	params.put("gwname", gwname_text);
    	params.put("userid", userid_text);
    	params.put("token", token_text);
    	doGetRequest(params, ADDGATEWAY_URL, ResultEntity.class, dataCallBack);
	}

	public void bindRoom(String deviceid_text, String roomnu_text,
			String userid_text, String token_text,String gwid_text,
			DataCallBack<ResultEntity> dataCallBack) {
		Map<String, String> params = new HashMap<String, String>();
		//http://120.79.233.219:12347/v1/token?deviceid=2342352&userid=111111&roomnu=room2001&token=aaaaa&gwid
		
    	params.put("gwid", gwid_text);
    	params.put("deviceid", deviceid_text);
    	params.put("roomnu", roomnu_text);
    	params.put("userid", userid_text);
    	params.put("token", token_text);
    	doGetRequest(params, BINDROOM_URL, ResultEntity.class, dataCallBack);
	}
}
