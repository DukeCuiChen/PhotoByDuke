package com.infrastructure.net;

public class URLData {
	private String key;
	private long expires;
	private String netType;
	private String url;
	private String mockClass;
	private boolean isRefresh;

	public URLData() {
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMockClass() {
		return mockClass;
	}

	public void setMockClass(String mockClass) {
		this.mockClass = mockClass;
	}

	public boolean getIsRefresh(){
		return isRefresh;
	}

	public void setIsRefresh(boolean isRefresh){
		this.isRefresh = isRefresh;
	}
}
