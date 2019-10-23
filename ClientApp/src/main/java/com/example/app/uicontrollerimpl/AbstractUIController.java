package com.example.app.uicontrollerimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Transaction;
import com.example.app.util.HttpCommunication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractUIController extends HttpCommunication {
	
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
	
}
