package com.example.app.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import com.example.app.exceptions.RequestFailedException;

public interface HttpClient {
	
	public static final String INTERNAL_ERROR = "Error del sistema, vuelvalo a intentar";
	public static final String REQUEST_FAILED = "request failed, response: ";
	
	default HttpEntity post(String uri, String data) throws RequestFailedException, InterruptedException, 
															ExecutionException, UnsupportedEncodingException, IOException {
		HttpResponse response;
		CloseableHttpAsyncClient httpclient;
		HttpPost post;
        
        httpclient = HttpAsyncClients.createDefault();
        try {
            httpclient.start();
            post = new HttpPost(uri);
            post.setEntity(new StringEntity(data));
            Future<HttpResponse> future = httpclient.execute(post, null);
            response =  future.get();
            if(response.getStatusLine().getStatusCode() == 200) {
            	return response.getEntity();
            } else {
            	throw new RequestFailedException(REQUEST_FAILED + response.getStatusLine().getStatusCode());
            }
        } 
        finally {
			httpclient.close();
        }
	}
}
