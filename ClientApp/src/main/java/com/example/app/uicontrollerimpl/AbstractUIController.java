package com.example.app.uicontrollerimpl;

import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractUIController {
	
	protected static final String HEADER = "Content-Type";
	protected static final String VALUE = "application/json; charset=utf-8";
	protected static final String INTERNAL_ERROR = "Error del sistema, vuelvalo a intentar";
	protected static final String SERVER_ERROR = "Error with server communication, please try again";
	private final String MALFORMED_REQUEST = "set data and callbacks to handle request";
	private final String SUCCESS = "operation successful!";
	
	@Autowired
	protected ObjectMapper mapper;
	
	protected Object data;
	protected Consumer<String> onSuccess;
	protected Consumer<String> onError;
	private CloseableHttpAsyncClient client;
	
	protected void post(String url, Class<?> type, Consumer<Object> incomingData) throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		if(this.isMalformed(data, onSuccess, onError)) {
			throw new MalformedRequestException(MALFORMED_REQUEST);
		} else {
			client = post(url, mapper.writeValueAsString(data), this.getDefaultOnResponse(incomingData, type), this.getDefaultOnError());
		}
	}
	
	protected void put(String url, Class<?> type, Consumer<Object> incomingData) throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		if(this.isMalformed(data, onSuccess, onError)) {
			throw new MalformedRequestException(MALFORMED_REQUEST);
		} else {
			client = put(url, mapper.writeValueAsString(data), this.getDefaultOnResponse(incomingData, type), this.getDefaultOnError());
		}
	}

	protected void delete(String url, Class<?> type) throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		if(this.isMalformed(data, onSuccess, onError)) {
			throw new MalformedRequestException(MALFORMED_REQUEST);
		} else {
			client = delete(url, this.getDefaultOnResponse(null, type), this.getDefaultOnError());
		}
	}
	
	protected void get(String url, Class<?> type, Consumer<Object> incomingData) throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		if(this.isMalformed(data, onSuccess, onError)) {
			throw new MalformedRequestException(MALFORMED_REQUEST);
		} else {
			client = get(url, this.getDefaultOnResponse(incomingData, type), this.getDefaultOnError());
		}
	}

	private boolean isMalformed(Object data, Consumer<String> onSuccess, Consumer<String> onError) {
		return data == null || onSuccess == null || onError == null;
	}
	
	private void close(CloseableHttpAsyncClient client) {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Consumer<HttpResponse> getDefaultOnResponse(Consumer<Object> incomingData, Class<?> type) {
		return 
				response -> {
					int status = response.getStatusLine().getStatusCode();
					if(status == 200) {
						try {
							if(incomingData != null) {
								incomingData.accept(mapper.readValue(response.getEntity().getContent(), Transaction.class));
							}
						} catch (UnsupportedOperationException | IOException e) {
							e.printStackTrace();
							onSuccess.accept(INTERNAL_ERROR);
						}
						onSuccess.accept(SUCCESS);
					} else {
						try {
							onError.accept(mapper.readValue(response.getEntity().getContent(), String.class));
						} catch (UnsupportedOperationException | IOException e) {
							onError.accept(INTERNAL_ERROR);
							e.printStackTrace();
						}
					}
					close(client);
				};
	}
	
	private Consumer<Exception> getDefaultOnError() {
		return error ->{
			this.onError.accept(SERVER_ERROR);
			close(client);
		};
	}
	

	private CloseableHttpAsyncClient post(String url, String data, Consumer<HttpResponse> onResponse, Consumer<Exception> onError) throws UnsupportedEncodingException  {
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
	
	private CloseableHttpAsyncClient put(String url, String data, Consumer<HttpResponse> onResponse, Consumer<Exception> onError) throws UnsupportedEncodingException  {
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
	
	private CloseableHttpAsyncClient delete(String url, Consumer<HttpResponse> onResponse, Consumer<Exception> onError)  {
		CloseableHttpAsyncClient httpclient;
		HttpDelete delete;
        
        httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        delete = new HttpDelete(url);
        delete.addHeader(HEADER, VALUE);
        httpclient.execute(delete, getCallback(onResponse, onError));
        return httpclient;
	}
	
	private CloseableHttpAsyncClient get(String url, Consumer<HttpResponse> onResponse, Consumer<Exception> onError) {
		CloseableHttpAsyncClient httpclient;
		HttpGet get;
        
        httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        get = new HttpGet(url);
        get.addHeader(HEADER, VALUE);
        httpclient.execute(get, getCallback(onResponse, onError));
        return httpclient;
	}
	
	private FutureCallback<HttpResponse> getCallback(Consumer<HttpResponse> onResponse, Consumer<Exception> onError) {
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
