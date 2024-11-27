package tests;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestsExamples {

	@BeforeTest
	public void Setup() {
		RestAssured.baseURI = "https://reqres.in/api";
	}

	@Test
	public void getEndpoint() {

//		Response response = get("/users?page=2");
		Response response = given().header("Content-Type", "application/json").when().get("/users?page=2");

		System.out.println("Response:" + response);
		System.out.println("Response Body:" + response.body().asString());
		System.out.println("Response Header:" + response.getHeaders());
		System.out.println("response code " + response.getStatusCode() + " Respone time: " + response.getTime());

		int statusCode = response.statusCode();

		Assert.assertEquals(statusCode, 200);

		// Parse the JSON response body
		JsonPath jsonPath = response.jsonPath();

		// Assertions for top-level fields
		Assert.assertEquals(jsonPath.getInt("page"), 2, "Page should be 2");
		Assert.assertEquals(jsonPath.getInt("per_page"), 6, "Per page should be 6");
		Assert.assertEquals(jsonPath.getInt("total"), 12, "Total should be 12");
		Assert.assertEquals(jsonPath.getInt("total_pages"), 2, "Total pages should be 2");

		// Assertions for the data array (list of user objects)
		List<Map<String, Object>> dataList = jsonPath.getList("data");

		// Assert the size of the data array
		Assert.assertEquals(dataList.size(), 6, "Data list size should be 6");

		// Loop through each user object in the data array and assert individual fields
		for (int i = 0; i < dataList.size(); i++) {

			Map<String, Object> user = dataList.get(i);
			Assert.assertNotNull(user.get("id"), "User ID should not be null");
			Assert.assertNotNull(user.get("email"), "User email should not be null");
			Assert.assertNotNull(user.get("first_name"), "User first name should not be null");
			Assert.assertNotNull(user.get("last_name"), "User last name should not be null");
			Assert.assertNotNull(user.get("avatar"), "User avatar should not be null");
		}
	}

	@Test
	public void postEndpoint() {

		HashMap<String, String> requestBody = new HashMap<String, String>();
		requestBody.put("name", "John");
		requestBody.put("job", "Developer");

		JSONObject jsonRequestBody = new JSONObject(requestBody);

		// Make the POST request
		Response response = given().header("Content-Type", "application/json").body(jsonRequestBody.toJSONString())
				.when().post("/users");

		// Print response details
		System.out.println("Response: " + response);
		System.out.println("Response Body: " + response.getBody().asString());
		System.out.println("Response Headers: " + response.getHeaders());
		System.out.println("Response Code: " + response.getStatusCode() + ", Response Time: " + response.getTime());

		// Assert the status code
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 201); // Expecting 201 for successful creation

		JsonPath jsonPath = response.jsonPath();

		String name = jsonPath.getString("name");
		String job = jsonPath.getString("job");
		String id = jsonPath.getString("id");
		String createdAt = jsonPath.getString("createdAt");

		// Assertions for each property
		Assert.assertEquals(name, "John", "Name should match the input value");
		Assert.assertEquals(job, "Developer", "Job should match the input value");
		Assert.assertNotNull(id, "ID should not be null");
		Assert.assertNotNull(createdAt, "createdAt should not be null");

		// Optional: Print specific fields to verify
		System.out.println("Name: " + name);
		System.out.println("Job: " + job);
		System.out.println("ID: " + id);
		System.out.println("Created At: " + createdAt);
	}

}
