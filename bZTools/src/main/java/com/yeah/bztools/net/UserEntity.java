package com.yeah.bztools.net;

import java.util.ArrayList;


public class UserEntity {
	private String ret;
    private String           msg;
    private ArrayList<DataEntity> data;

    public void setRet(String ret) {
        this.ret = ret;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(ArrayList<DataEntity> data) {
        this.data = data;
    }

    public String getRet() {
        return ret;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<DataEntity> getData() {
        return data;
    }

 public static class DataEntity{
        
     private String username;
     private String userpass;
     private String usersta;
     private String orgcode;
     private String orgname;
     private String postname;


		public String getUserpass() {
			return userpass;
		}

		public void setUserpass(String userpass) {
			this.userpass = userpass;
		}

     public String getUsername() {
         return username;
     }

     public void setUsername(String username) {
         this.username = username;
     }

     public String getUsersta() {
         return usersta;
     }

     public void setUsersta(String usersta) {
         this.usersta = usersta;
     }

     public String getOrgcode() {
         return orgcode;
     }

     public void setOrgcode(String orgcode) {
         this.orgcode = orgcode;
     }

     public String getOrgname() {
         return orgname;
     }

     public void setOrgname(String orgname) {
         this.orgname = orgname;
     }

     public String getPostname() {
         return postname;
     }

     public void setPostname(String postname) {
         this.postname = postname;
     }
 }
}
