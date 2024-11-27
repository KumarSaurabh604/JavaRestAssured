package tests;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

public class GetAndPostExamples {

	@Test
	public void testGet() {
		
		baseURI = "https://reqres.in/api";
		
		given().
			get("/users?page=2").
		then().
			statusCode(200).
			body("data[4].first_name", equalTo("George")).
			body("data.first_name", hasItems("George", "Rachel"));
	}
	
	@Test
	public void testPost() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
//		Method 1: 
		map.put("name", "morpheus");
		map.put("job", "leader");
//		
//		System.out.println(map);
//		
		JSONObject requestBody = new JSONObject(map);
////		
//		System.out.println(requestBody);
//		System.out.println(requestBody.toJSONString());

//		
//		Method 2: 
//		JSONObject requestBody = new JSONObject();
//		
//		requestBody.put("name", "Saurabh");
//		requestBody.put("job", "Engineer");
		
		
		
		
		
		baseURI = "https://reqres.in/api";
		
		given().
			header("Content-Type", "application/json").
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			body(requestBody.toJSONString()).
        when().
			post("/users").
		then().
			statusCode(201).
			body("name", equalTo("morpheus")).
			body("job", equalTo("leader")).log().body();
		
	}
}
