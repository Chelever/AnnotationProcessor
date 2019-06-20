package com.renan.main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.json.JSONObject;

import javassist.NotFoundException;

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		System.out.println("Hello world");

		JSONObject data = new JSONObject();
		data.put("user", "Ataliba");
		data.put("token", "jsid910udj92u8d912");
		JSONObject request = new JSONObject();
		request.put("type", "MSG_TEST");
		request.put("function", "method");
		request.put("data", data);
		
		JSONObject data2 = new JSONObject();
		data2.put("user", "Benegundez");
		data2.put("token", "e9012u9jdsaodkojd");
		JSONObject request2 = new JSONObject();
		request2.put("type", "MSG_TEST2");
		request2.put("function", "method2");
		request2.put("data", data2);
		

			try {
				Processor.getInstance().process(request);
				Processor.getInstance().process(request);
				System.out.println("\n");
				Processor.getInstance().process(request2);
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		
	}


}
