package org.yetiz.lib.acd;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;

/**
 * Created by yeti on 2015/4/13.
 */
public class Utils {
	public static JsonObject convertBodyToJson(Response response) {
		return ((JsonObject) JsonParser.parseString(response.getResponseBody(StandardCharsets.UTF_8)));
	}

	public static String urlEncode(String str) {
		String rtn = "";
		try {
			rtn = URLEncoder.encode(str, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return rtn;
	}

	public static String getResponseBody(Response response) {
		return response.getResponseBody(Utils.getCharset());
	}

	public static Charset getCharset() {
		return StandardCharsets.UTF_8;
	}


	public static Gson getGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new DateAdapter());
		return builder.create();
	}

	public static String stringAppender(String... strings) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : strings) {
			stringBuilder.append(string);
		}
		return stringBuilder.toString();
	}

	public static String stringFormatter(String format, Object... arguments) {
		String[] formatSegments = format.split("\\{\\}");
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < formatSegments.length; i++) {
			stringBuilder.append(formatSegments[i]);
			if (i < arguments.length)
				stringBuilder.append(arguments[i]);
		}
		return stringBuilder.toString();
	}

	public static String getCurrentMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	public static String getCurrentClassName() {
		return Thread.currentThread().getStackTrace()[2].getClassName();
	}

	public static void print(Object object) {
		System.out.println(ObjectFieldsToString(object));
	}

	public static String ObjectFieldsToString(Object object) {
		return ReflectionToStringBuilder.toString(object);
	}

	public static RequestBuilder newFollowRedirectRequestBuilder() {
		return new RequestBuilder().setFollowRedirect(true);
	}
}
