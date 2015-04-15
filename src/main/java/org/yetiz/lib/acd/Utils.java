package org.yetiz.lib.acd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ning.http.client.Response;
import org.yetiz.lib.acd.exception.BadContentException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by yeti on 2015/4/13.
 */
public class Utils {
	public static JsonObject convertBodyToJson(Response response) {
		try {
			return ((JsonObject) new JsonParser().parse(response.getResponseBody("utf-8")));
		} catch (IOException e) {
			throw new BadContentException();
		}
	}

	public static String urlEncode(String str) {
		String rtn = "";
		try {
			rtn = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return rtn;
	}

	public static String getResponseBody(Response response){
		try {
			return response.getResponseBody(Utils.getCharset());
		} catch (IOException e) {
			throw new BadContentException();
		}
	}

	public static String getCharset() {
		return "UTF-8";
	}

	public static String getContentType() {
		return "application/x-www-form-urlencoded";
	}

	public static Gson getGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new DateAdapter());
		return builder.create();
	}
}
