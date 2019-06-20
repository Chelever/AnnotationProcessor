package com.renan.main;

import org.json.JSONObject;

@CustomAnnotation(value = "MSG_TEST2")
public class CustomClass2 {

	public CustomClass2() {
	}
	
	@Function(value="method")
	public String methodTest(JSONObject data) {
		System.out.println("Teste 2: "+data.toString());
		
		JSONObject answer = new JSONObject();
		answer.put("status", 200);
		return answer.toString();
	}
	
	@Function(value="method2")
	public String methodTest2(JSONObject data) {
		System.out.println("Another test: "+data.toString());
		
		JSONObject answer = new JSONObject();
		answer.put("status", 200);
		return answer.toString();
	}
}
