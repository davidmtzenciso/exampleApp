package com.example.app.util;

import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

public interface HttpCommunication {
	
	public static final String HEADER = "Content-Type";
	public static final String VALUE = "application/json; charset=utf-8";
	

	default CloseableHttpAsyncClient post(String url, String data, Consumer<HttpResponse> onResponse, Consumer<Exception> onError) throws UnsupportedEncodingException  {
		CloseableHttpAsyncClient httpclient;
		HttpPost post;
        
        httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        post = new HttpPost(url);
        post.addHeader(HEADER, VALUE);
        post.setEntity(new StringEntity(data));
        httpclient.execute(post, getCallback(onResponse, onError));
        return httpclient; 
	}
	
	default CloseableHttpAsyncClient put(String url, String data, Consumer<HttpResponse> onResponse, Consumer<Exception> onError) throws UnsupportedEncodingException  {
		CloseableHttpAsyncClient httpclient;
		HttpPut put;
        
        httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        put = new HttpPut(url);
        put.addHeader(HEADER, VALUE);
        put.setEntity(new StringEntity(data));
        httpclient.execute(put, getCallback(onResponse, onError));
        return httpclient;
	}
	
	default CloseableHttpAsyncClient delete(String url, Consumer<HttpResponse> onResponse, Consumer<Exception> onError)  {
		CloseableHttpAsyncClient httpclient;
		HttpDelete delete;
        
        httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        delete = new HttpDelete(url);
        delete.addHeader(HEADER, VALUE);
        httpclient.execute(delete, getCallback(onResponse, onError));
        return httpclient;
	}
	
	default CloseableHttpAsyncClient get(String url, Consumer<HttpResponse> onResponse, Consumer<Exception> onError) {
		CloseableHttpAsyncClient httpclient;
		HttpGet get;
        
        httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        get = new HttpGet(url);
        get.addHeader(HEADER, VALUE);
        httpclient.execute(get, getCallback(onResponse, onError));
        return httpclient;
	}
	
	default FutureCallback<HttpResponse> getCallback(Consumer<HttpResponse> onResponse, Consumer<Exception> onError) {
		return new FutureCallback<HttpResponse>() {

			@Override
			public void failed(Exception ex) {	
				onError.accept(ex);
			}

			@Override
			public void cancelled() {}

			@Override
			public void completed(HttpResponse result) {
				onResponse.accept(result);
			}
		};
	}
}
