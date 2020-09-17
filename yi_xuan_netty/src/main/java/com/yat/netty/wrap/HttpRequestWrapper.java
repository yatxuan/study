package com.yat.netty.wrap;

import lombok.Data;

import java.util.Map;

/**
 * <p>Description: 请求参数 </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/17 11:29
 */
@Data
public class HttpRequestWrapper  {
	private String method;
	private Map<String, String> headers;
	private String uri;
	private byte[] content;
	private String params;

	public HttpRequestWrapper(String method, String uri, Map<String, String> headers, byte[] content,String params) {
		this.method = method;
		this.uri = uri;
		this.headers = headers;
		this.content = content;
		this.params = params;

	}

	public Map<String, String> headers() {
		return headers;
	}

	public String method() {
		return method;
	}

	public String uri() {
		return uri;
	}

	public byte[] content() {
		return content;
	}
}
