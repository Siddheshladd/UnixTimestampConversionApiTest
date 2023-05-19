package api.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.UnsupportedEncodingException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import api.endpoints.Routes;

public class UnixTimeConversionTest {

	
	
	@BeforeClass
	public static void setup() {
		Routes url = new Routes();

		RestAssured.baseURI = url.BASE_URL;
	}

	@Test
	public void testConvertFromDateToUnixTimeStamp(){
		String dateString = "2016-01-01T02:03:22";
		long expectedUnixTimeStamp = 1451613802;


		Response response = RestAssured.given().urlEncodingEnabled(true).queryParam("cached").queryParam("s", dateString)
				.accept(ContentType.JSON)
//				.log().all() // Print the request details
				.get();

		
		int statusCode = response.getStatusCode();
		Object responseBody;
		if (response.getContentType().startsWith("application/json")) {
			responseBody = response.getBody().as(Long.class);
		} else {
			responseBody = response.getBody().asString();
		}
		System.out.println("Print Response for testConvertFromDateToUnixTimeStamp : " + responseBody);
		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(responseBody, expectedUnixTimeStamp);

		

	}
	
	
	

	@Test
	public void testConvertFromUnixTimeStampToDate() {
		long unixTimeStamp = 1451613802;
		String expectedDateString = "2016-01-01 02:03:22";

		Response response = RestAssured.given().queryParam("cached").queryParam("s", unixTimeStamp)
				.accept(ContentType.JSON)
//				.log().all()
				.get();

		int statusCode = response.getStatusCode();
		String actualDateString = response.getBody().asString().replace("\"", "");
		System.out.println("Print Response for testConvertFromUnixTimeStampToDate : " + actualDateString);

		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(actualDateString, expectedDateString);


	}

	@Test
	public void testInvalidDateString() {
		String invalidDateString = "asdfasd";

		Response response = RestAssured.given().queryParam("cached").queryParam("s", invalidDateString)
				.accept(ContentType.JSON).get();

		int statusCode = response.getStatusCode();
		boolean isInvalidDateString = response.getBody().as(Boolean.class);
		System.out.println("Print Response for testInvalidDateString : " + isInvalidDateString);

		Assert.assertEquals(statusCode, 200);
		Assert.assertFalse(isInvalidDateString);
	}
}
