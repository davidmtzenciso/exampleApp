package com.example.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.RequestError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractUIController extends HttpCommunication {
	
	protected static final String HEADER = "Content-Type";
	protected static final String VALUE = "application/json; charset=utf-8";
	protected static final String INTERNAL_ERROR = "Error del sistema, vuelvalo a intentar";
	protected static final String SERVER_ERROR = "Error with server communication, please try again";
	private final String MALFORMED_REQUEST = "set data and callbacks to handle request";
	
	@Autowired
	protected ObjectMapper mapper;
	
	protected Object data;
	protected Consumer<Object> onSuccess;
	protected Consumer<RequestError> onError;
	private CloseableHttpAsyncClient client;
	
	protected void post(String url, Class<?> type) throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		if(this.isMalformed(data, onSuccess, onError)) {
			throw new MalformedRequestException(MALFORMED_REQUEST);
		} else {
			client = post(url, mapper.writeValueAsString(data), this.getDefaultOnResponse(type), this.getDefaultOnError());
		}
	}
	
	protected void put(String url, Class<?> type) throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		if(this.isMalformed(data, onSuccess, onError)) {
			throw new MalformedRequestException(MALFORMED_REQUEST);
		} else {
			client = put(url, mapper.writeValueAsString(data), this.getDefaultOnResponse(type), this.getDefaultOnError());
		}
	}

	protected void delete(String url, Class<?> type) throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		if(this.isMalformed(data, onSuccess, onError)) {
			throw new MalformedRequestException(MALFORMED_REQUEST);
		} else {
			client = delete(url, this.getDefaultOnResponse(type), this.getDefaultOnError());
		}
	}
	
	protected void get(String url, Class<?> type) throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		if(this.isMalformed(data, onSuccess, onError)) {
			throw new MalformedRequestException(MALFORMED_REQUEST);
		} else {
			client = get(url, this.getDefaultOnResponse(type), this.getDefaultOnError());
		}
	}

	private boolean isMalformed(Object data, Consumer<Object> onSuccess, Consumer<RequestError> onError) {
		return data == null || onSuccess == null || onError == null;
	}
	
	private void close(CloseableHttpAsyncClient client) {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String readResponse(HttpResponse response) throws UnsupportedOperationException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuilder builder = new StringBuilder();
		reader.lines().forEach(line -> builder.append(line));
		return builder.toString();
	}
	
	private synchronized Consumer<HttpResponse> getDefaultOnResponse(Class<?> type) {
		return 
				response -> {
					int status = response.getStatusLine().getStatusCode();
					if(status == 200) {
						try {
							if(type.getSimpleName().equals(String.class.getSimpleName())) {
								onSuccess.accept(this.readResponse(response));
							} else {
								onSuccess.accept(mapper.readValue(response.getEntity().getContent(), type));
							}
						} catch (UnsupportedOperationException | IOException e) {
							onSuccess.accept(INTERNAL_ERROR + ", JSON error");
						}
					} else {
						try {
							onError.accept(mapper.readValue(response.getEntity().getContent(), RequestError.class));
						} catch (UnsupportedOperationException | IOException e) {
							onError.accept(null);
						}
					}
					close(client);
				};
	}
	
	private synchronized Consumer<Exception> getDefaultOnError() {
		return error -> {
			this.onError.accept(new RequestError(INTERNAL_ERROR));
			close(client);
		};
	}
	
}
