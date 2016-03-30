package com.infrastructure.net;

import java.io.InputStream;

public interface RequestCallback
{
	public void onSuccess(String content);
	
	public void onSuccess(InputStream in);

	public void onFail(String errorMessage);

	public void onCookieExpired();
}
