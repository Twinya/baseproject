package com.appengine.frame.utils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
	private static final OkHttpClient mOkHttpClient = new OkHttpClient();

	static {
		mOkHttpClient.setConnectTimeout(120, TimeUnit.SECONDS);
		mOkHttpClient.setReadTimeout(120, TimeUnit.SECONDS);
		mOkHttpClient.setConnectTimeout(120, TimeUnit.SECONDS);
	}

	/**
	 * 该不会开启异步线程。
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static Response execute(Request request) throws IOException {
		return mOkHttpClient.newCall(request).execute();
	}

	/**
	 * 开启异步线程访问网络
	 *
	 * @param request
	 * @param responseCallback
	 */
	public static void enqueue(Request request, Callback responseCallback) {
		mOkHttpClient.newCall(request).enqueue(responseCallback);
	}

	/**
	 * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
	 *
	 * @param request
	 */
	public static void enqueue(Request request) {
		mOkHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Response response) throws IOException {

			}

			@Override
			public void onFailure(Request request, IOException e) {

			}
		});
	}

	public static String getStringFromServer(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Response response = execute(request);
		if (response.isSuccessful()) {
			String responseUrl = response.body().string();
			return responseUrl;
		} else {
			throw new IOException("Unexpected code " + response);
		}
	}

	/**
	 * 为HttpGet 的 url 方便的添加1个name value 参数。
	 *
	 * @param url
	 * @param name
	 * @param value
	 * @return
	 */
	public static String attachHttpGetParam(String url, String name, String value) {
		return url + "?" + name + "=" + value;
	}
}
