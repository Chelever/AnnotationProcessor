package com.renan.main;

import org.json.JSONObject;

@CustomAnnotation(value = "MSG_TEST")
public class CustomClass {

	public CustomClass() {
	}
	
	@Function(value="method")
	public String methodTest(JSONObject data) {
		System.out.println("Teste: "+data.toString());
		
		JSONObject answer = new JSONObject();
		answer.put("status", 200);
		return answer.toString();
	}
}
