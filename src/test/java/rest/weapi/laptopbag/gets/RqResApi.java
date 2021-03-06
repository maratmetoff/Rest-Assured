package rest.weapi.laptopbag.gets;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import rest.webapi.laptopbag.helpers.RestHelpers;
import rest.webapi.laptopbag.helpers.RestHelpers.Methods;

public class RqResApi {

	@Test
	public void JsonPathPractice() throws URISyntaxException {
		String ping = RestHelpers.buildURI(Methods.PING);
		System.out.println(given().when().get(ping).asString());
		
		/*
		 * Given accept type is Json
		 * when I perform GET request using ALL method
		 * I can see all ID's
		 */
		
		String json = given().accept(ContentType.JSON).when().get(RestHelpers.buildURI(Methods.ALL)).andReturn().body().asString();
		
		JsonPath jsonBody = new JsonPath(json);            //parsing json string into JSON object so I can easily navigate through it 
		
		List<Integer> ids = jsonBody.getList("Id");
		System.out.println(ids);
		List<String> brands = jsonBody.getList("BrandName");
		System.out.println(brands);
		List<String> features = jsonBody.getList("Features.Feature");
		System.out.println(features);
		
		//get all laptops whose ID's are less than 150
		List<String> laptopsLess150 = jsonBody.getList("findAll{it.Id<150}.LaptopName");
		System.out.println(laptopsLess150);
		
		List<String> laptopsApple = jsonBody.getList("findAll{it.BrandName=='Lenovo'}.Id");
		System.out.println(laptopsApple);
	}
	
	@Test
	public void XmlPathSamples() throws URISyntaxException {
		/*
		 * Given accept type is xml
		 * When I perform GET request to get all laptops
		 * Then status is 200
		 * And I can navigate through XML using XmlPath
		 */
		
		URI uri = new URI(RestHelpers.buildURI(Methods.ALL));
		
		Response response = given().accept(ContentType.XML).when().get(uri);        // helper method didn't work
							//given().accept(ContentType.XML).when().get(uri);
		assertEquals(200, response.getStatusCode());
		XmlPath xml = new XmlPath(response.body().asString());
		
		List<String> brandNames = xml.getList("laptopDetailss.Laptop.BrandName");
		System.out.println(brandNames);
	}
	
	
	
}
