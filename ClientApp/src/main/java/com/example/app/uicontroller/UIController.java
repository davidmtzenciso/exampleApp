package com.example.app.uicontroller;

import java.util.function.Consumer;

public interface UIController {
	
	public UIController setData(Object data);
	
	public UIController setOnSuccess(Consumer<String> consumer);
	
	public UIController setOnError(Consumer<String> consumer);
	
	default boolean isMalformed(Object data, Consumer<String> onSuccess, Consumer<String> onError) {
		return data == null || onSuccess == null || onError == null;
	}
	
	public static final String INTERNAL_ERROR = "Error del sistema, vuelvalo a intentar";
	public static final String SERVER_ERROR = "Error with server communication, please try again";

}
