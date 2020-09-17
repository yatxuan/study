package com.yat.netty.wrap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 响应对象 </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/17 11:29
 */
public class HttpResponseWrapper {
	private Map<String, String> headers = new HashMap<String, String>();
	private int statusCode = 200;
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	public Map<String, String> headers() {
		return headers;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void write(byte[] bytes) throws IOException {
		outputStream.write(bytes);
	}

	public void write(InputStream is) throws IOException {
		//IoKit.copy(is, outputStream);
	}

	/**
	 * @return the outputStream
	 */
	public final OutputStream getOutputStream() {
		return outputStream;
	}

	public byte[] content() {
		return outputStream.toByteArray();
	}
}
