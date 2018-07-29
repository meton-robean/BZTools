package com.yeah.bztools.net;

public class ResultEntity {
	private String code;
	private String appid;
	private String secret;
	private String userid;
    private String expired_in;
    private String token;
    private String roomnu;
    
	public String getRoomnu() {
		return roomnu;
	}
	public void setRoomnu(String roomnu) {
		this.roomnu = roomnu;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getExpired_in() {
		return expired_in;
	}
	public void setExpired_in(String expired_in) {
		this.expired_in = expired_in;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
    
}
