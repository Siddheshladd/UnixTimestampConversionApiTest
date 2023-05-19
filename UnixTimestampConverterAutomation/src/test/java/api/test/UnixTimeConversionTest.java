package api.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;  
import java.util.Calendar;  

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

	@Test(priority = 1, description = "Verify response, When a valid date is passed it returns the correct unix timestamp in response.")
	public void testConvertFromDateToUnixTimeStamp() throws UnsupportedEncodingException{
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
	
	

	@Test(priority = 2, description = "Verify response, When a valid Timestamp is passed it returns the correct Date String in response.")

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
	
	

	@Test(priority = 3, description = "Verify response, When a invalid date/Timestamp is passed it returns the Error Message")

	
	public void testInvalidDateString() {
		String invalidDateTSString = "ASDH 2016-01-01%202:3:22";
		String expectedinvalidTSDateString = "Invalid Timestamp / Date";
		
		Response response = RestAssured.given().queryParam("cached").queryParam("s", invalidDateTSString)
				.accept(ContentType.JSON).get();

		int statusCode = response.getStatusCode();
		String actualInvalidDateTSString = response.getBody().asString();
		System.out.println("Print Response for testInvalidDateString : " + actualInvalidDateTSString);	
		
		Assert.assertEquals(statusCode, 400);
		Assert.assertEquals(actualInvalidDateTSString, expectedinvalidTSDateString);
	}
	
	
	
	
	@Test(priority = 4, description = "Verify response, When an empty date parameter is passed it returns the current time")

	public void testEmptyDateString() {
		String emptyDateString = "";


        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
        String strDate = dateFormat.format(date);  
		
		String expectedDateString = strDate;

		Response response = RestAssured.given().queryParam("cached").queryParam("s", emptyDateString)
				.accept(ContentType.JSON).get();

		int statusCode = response.getStatusCode();
		String actualDateString = response.getBody().asString();
		System.out.println("Print Response for testEmptyDateString : " + actualDateString);

		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(actualDateString, expectedDateString);
	}
	
	@Test(priority = 5, description = "Verify response, When an empty Timestamp parameter is passed it returns the Current date Timestamp")

	public void testEmptyTimeStampString() {
		String emptyTimestampString = "";
		Instant expectedunixTimestamp = Instant.now();
	
		
		Response response = RestAssured.given().queryParam("cached").queryParam("s", emptyTimestampString)
				.accept(ContentType.JSON).get();

		int statusCode = response.getStatusCode();
		String actualDateString = response.getBody().asString();
		System.out.println("Print Response for testEmptyTimeStampString : " + actualDateString);

		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(actualDateString, expectedunixTimestamp);
	}
	
	
	
	
}
