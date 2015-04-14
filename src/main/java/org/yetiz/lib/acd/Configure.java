package org.yetiz.lib.acd;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by yeti on 2015/4/14.
 */
public class Configure {
	private String name = "ACD-JAPI";
	private String path = System.getProperty("user.dir") + File.separator + name + ".conf";
	private Boolean writable = true;
	private Boolean autoRefresh = true;
	private Boolean autoConfigureUpdate = true;
	private String client_id = "";
	private String client_secret = "";
	private String token_type = "";
	private String access_token = "";
	private String refresh_token = "";
	private String redirect_uri = "http://localhost";

	public static Configure load(File configureFile) {
		Properties properties = new Properties();
		Configure configure = new Configure();
		try {
			properties.load(new FileReader(configureFile));
			configure.path = configureFile.getPath();
		} catch (IOException e) {
			return null;
		}
		configure.name = properties.getProperty("name") == null ? configure.name :
			properties.getProperty("name");
		configure.writable = properties.getProperty("writable") == null ? configure.writable :
			properties.getProperty("writable").toUpperCase().equals("TRUE");
		configure.autoRefresh = properties.getProperty("autoRefresh") == null ? configure.autoRefresh :
			properties.getProperty("autoRefresh").toUpperCase().equals("TRUE");
		configure.autoConfigureUpdate = properties.getProperty("autoConfigureUpdate") == null ? configure
			.autoConfigureUpdate :
			properties.getProperty("autoConfigureUpdate").toUpperCase().equals("TRUE");
		configure.client_id = properties.getProperty("client_id") == null ? configure.client_id :
			properties.getProperty("client_id");
		configure.client_secret = properties.getProperty("client_secret") == null ? configure.client_secret :
			properties.getProperty("client_secret");
		configure.token_type = properties.getProperty("token_type") == null ? configure.token_type :
			properties.getProperty("token_type");
		configure.access_token = properties.getProperty("access_token") == null ? configure.access_token :
			properties.getProperty("access_token");
		configure.refresh_token = properties.getProperty("refresh_token") == null ? configure.refresh_token :
			properties.getProperty("refresh_token");
		configure.redirect_uri = properties.getProperty("redirect_uri") == null ? configure.redirect_uri :
			properties.getProperty("redirect_uri");
		return configure;
	}

	public void update() {
		Properties properties = new Properties();
		properties.setProperty("name", name);
		properties.setProperty("writable", writable ? "TRUE" : "FALSE");
		properties.setProperty("autoRefresh", autoRefresh ? "TRUE" : "FALSE");
		properties.setProperty("autoConfigureUpdate", autoConfigureUpdate ? "TRUE" : "FALSE");
		properties.setProperty("client_id", client_id);
		properties.setProperty("client_secret", client_secret);
		properties.setProperty("token_type", token_type);
		properties.setProperty("access_token", access_token);
		properties.setProperty("refresh_token", refresh_token);
		properties.setProperty("redirect_uri", redirect_uri);
		try {
			properties.store(new FileWriter(new File(path)), "Amazon Cloud Drive Java API Configure File.");
		} catch (IOException e) {
		}
	}

	public String getName() {
		return name;
	}

	public String getClientId() {
		return client_id;
	}

	public String getClientSecret() {
		return client_secret;
	}

	public String getTokenType() {
		return token_type;
	}

	public String getAccessToken() {
		return access_token;
	}

	public String getRefreshToken() {
		return refresh_token;
	}

	public String getRedirectUri() {
		return redirect_uri;
	}

	public Boolean isWritable() {
		return writable;
	}

	public Boolean isAutoRefresh() {
		return autoRefresh;
	}

	public Boolean isAutoConfigureUpdate() {
		return autoConfigureUpdate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setClientId(String client_id) {
		this.client_id = client_id;
	}

	public void setClientSecret(String client_secret) {
		this.client_secret = client_secret;
	}

	public void setTokenType(String token_type) {
		this.token_type = token_type;
	}

	public void setAccessToken(String access_token) {
		this.access_token = access_token;
	}

	public void setRefreshToken(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public void setRedirectUri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public void setWritable(Boolean writable) {
		this.writable = writable;
	}

	public void setAutoRefresh(Boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
	}

	public void setAutoConfigureUpdate(Boolean autoConfigureUpdate) {
		this.autoConfigureUpdate = autoConfigureUpdate;
	}
}
