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

	@Test(priority = 1, description = "Converting a valid date string to a Unix timestamp")
	public void testConvertFromDateToUnixTimeStamp() throws UnsupportedEncodingException {
		String dateString = "2016-01-01T02:03:22";
		long expectedUnixTimeStamp = 1451613802;

		Response response = RestAssured.given().urlEncodingEnabled(true).header("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
				.queryParam("cached").queryParam("s", dateString)
				.accept(ContentType.JSON)
//				.log().all() //This logs the request 
				.get();

		int statusCode = response.getStatusCode();
        long actualUnixTimeStamp = response.getBody().as(Long.class);
        
		System.out.println("Print Response for testConvertFromDateToUnixTimeStamp : " + actualUnixTimeStamp);
		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(actualUnixTimeStamp, expectedUnixTimeStamp);

	}

	@Test(priority = 2, description = "Converting a Unix timestamp to Date string")

	public void testConvertFromUnixTimeStampToDate() {
		long unixTimeStamp = 1451613802;
		String expectedDateString = "2016-01-01 02:03:22";

		Response response = RestAssured.given().queryParam("cached").queryParam("s", unixTimeStamp)
				.accept(ContentType.JSON)
				.get();

		int statusCode = response.getStatusCode();
		String actualDateString = response.getBody().asString().replace("\"", "");
		System.out.println("Print Response for testConvertFromUnixTimeStampToDate : " + actualDateString);

		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(actualDateString, expectedDateString);

	}

	@Test(priority = 3, description = "Converting an invalid date string format")

	public void testInvalidDateString() {
		String invalidDateTSString = "ASDH 2016-01-01%202:3:22";
		String expectedinvalidTSDateString = "Invalid Input Date";

		Response response = RestAssured.given()
				.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
				.queryParam("cached").queryParam("s", invalidDateTSString)
				.accept(ContentType.JSON)
				.get();

		int statusCode = response.getStatusCode();
		String actualInvalidDateTSString = response.getBody().asString();
		System.out.println("Print Response for testInvalidDateString : " + actualInvalidDateTSString);

		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(actualInvalidDateTSString, expectedinvalidTSDateString);
	}

	
	@Test(priority = 4, description = "Converting an empty date string")

	public void testEmptyDateString() {
		String emptyDateString = "";

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String strDate = dateFormat.format(date);

		String expectedDateString = strDate;

		Response response = RestAssured.given()
				.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
				.queryParam("cached").queryParam("s", emptyDateString)
				.accept(ContentType.JSON)
				.get();

		int statusCode = response.getStatusCode();
		String actualDateString = response.getBody().asString();
		System.out.println("Print Response for testEmptyDateString : " + actualDateString);

		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(actualDateString, expectedDateString);
	}

	@Test(priority = 5, description = "Converting a Unix timestamp with a negative value")

	public void testNegativeTS() {
		long negativeTimeStamp = -1451613802;
		String expectedResult = "Invalid Timestamp";
		
//		Instant expectedunixTimestamp = Instant.now();

		Response response = RestAssured.given()
				.header("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
				.queryParam("cached").queryParam("s", negativeTimeStamp)
				.accept(ContentType.JSON)
				.get();
		

		int statusCode = response.getStatusCode();
		String actualTimeStampString = response.getBody().asString();
		System.out.println("Print Response for testNegativeTS : " + actualTimeStampString);

		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(actualTimeStampString, expectedResult);
	}
	
	@Test(priority = 6, description = "Converting a Unix timestamp with a negative value")

	public void testLargeTSValue() {
		long largeTimeStamp = 9999999999999L;
		String expectedResult = "Invalid Timestamp/Out of range";
		

		Response response = RestAssured.given()
				.header("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
				.queryParam("cached").queryParam("s", largeTimeStamp)
				.accept(ContentType.JSON)
				.get();
		

		int statusCode = response.getStatusCode();
		String actualTimeStampString = response.getBody().asString();
		System.out.println("Print Response for testLargeTSValue : " + actualTimeStampString);

		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(actualTimeStampString, expectedResult);
	}

}
