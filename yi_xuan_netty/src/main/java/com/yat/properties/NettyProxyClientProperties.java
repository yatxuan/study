package com.yat.properties;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix="netty.proxy-client")
public class NettyProxyClientProperties {

	private String proxyServer;

	private String proxyKeys;

	public String getProxyHost(){
		if(StringUtils.isNotEmpty(proxyServer)){
			return proxyServer.split(":")[0];
		}
		return "";
	}

	public Integer getProxyPort(){
		if(StringUtils.isNotEmpty(proxyServer)){
			return Integer.parseInt(proxyServer.split(":")[1]);
		}
		return null;
	}



}




