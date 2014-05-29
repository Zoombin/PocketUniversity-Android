package com.xyhui.types;

import android.text.TextUtils;

public class Token {
	public String oauth_token;
	public String oauth_token_secret;
	public String uid;
	public String message;
	public String uname;
	public String sid1;
	public String sid1Name;

	public boolean isEmpty() {
		if (TextUtils.isEmpty(oauth_token) || TextUtils.isEmpty(oauth_token_secret)) {
			return true;
		}
		return false;
	}
}
