package api.tdd.go_rest;

import api.pojo_classes.go_rest.CreateGoRestUser;
import api.pojo_classes.go_rest.UpdateGoRestUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.ConfigReader;
import org.hamcrest.Matchers;

import static org.hamcrest.core.IsEqual.equalTo;

public class GoRestWithoutLombok {

    Response response;
    /**
     * ObjectMapper is a class coming form fasterxml to convert Java object to Json
     */
    ObjectMapper objectMapper = new ObjectMapper();

    Faker faker = new Faker();

    int expectedGoRestId;
    String expectedGoRestName;
    String expectedGoRestEmail;
    String expectedGoRestGender;
    String expectedGoRestStatus;

    @BeforeTest
    public void beforeTest() {
        System.out.println("Starting the API test");
        // By having RestAssured URI set implicitly in to rest assured
        // we just add path to the post call
        RestAssured.baseURI = ConfigReader.getProperty("GoRestBaseURI");
    }

    @Test
    public void goRestCRUD() throws JsonProcessingException {
        // Creating a POJO (Bean) object
        CreateGoRestUser createGoRestUser = new CreateGoRestUser();

        // assigned the values to the attributes
        createGoRestUser.setName("Tech Global");
        createGoRestUser.setGender("male");
        createGoRestUser.setEmail(faker.internet().emailAddress());
        createGoRestUser.setStatus("active");

        System.out.println("-----Creating the user with POST request-----\n");
        response = RestAssured
                .given().log().all()
//                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createGoRestUser))
//                .when().post("https://gorest.co.in/public/v2/users")
                .when().post("/public/v2/users")
                .then().log().all()
                //validating the status code with rest assured
                .and().assertThat().statusCode(201)
                //validating the response time is less than the specified one
                .time(Matchers.lessThan(10000L))
                //validating the value from the body with hamcrest
                .body("name", equalTo("Tech Global"))
                //validating the response content type
                .contentType(ContentType.JSON)
                .extract().response();

        System.out.println("\n-----Fetching the user with GET request-----\n");
        expectedGoRestId = response.jsonPath().getInt("id");

        response = RestAssured
                .given().log().all()
//                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .when().get("/public/v2/users/"+ expectedGoRestId)
                .then().log().all()
                //validating the status code with rest assured
                .and().assertThat().statusCode(200)
                //validating the response time is less than the specified one
                .time(Matchers.lessThan(10000L))
                //validating the value from the body with hamcrest
                .body("name", equalTo("Tech Global"))
                //validating the response content type
                .contentType(ContentType.JSON)
                .extract().response();

        System.out.println("\n-----Updating the user with PUT request-----\n");
        UpdateGoRestUser updateGoRestUser = new UpdateGoRestUser();

        updateGoRestUser.setName("Jane Doe");
        updateGoRestUser.setEmail(faker.internet().emailAddress());

        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(updateGoRestUser))
                .when().put("/public/v2/users/"+ expectedGoRestId)
                .then().log().all()
                .and().assertThat().statusCode(200)
                .time(Matchers.lessThan(10000L))
                .body("name", equalTo("Jane Doe"))
                .contentType(ContentType.JSON)
                .extract().response();

        expectedGoRestName = updateGoRestUser.getName();
        expectedGoRestEmail = updateGoRestUser.getEmail();
        expectedGoRestGender = createGoRestUser.getGender();
        expectedGoRestStatus = createGoRestUser.getStatus();

        // "id" in the getInt is the name of the attribute in the response body
        int actualGoRestId = response.jsonPath().getInt("id");
        String actualGoRestName = response.jsonPath().get("name");
        String actualGoRestEmail = response.jsonPath().get("email");
        String actualGoRestGender = response.jsonPath().get("gender");
        String actualGoRestStatus = response.jsonPath().get("status");

        Assert.assertEquals(actualGoRestId, expectedGoRestId);
        Assert.assertEquals(actualGoRestName, expectedGoRestName);
        Assert.assertEquals(actualGoRestEmail, expectedGoRestEmail);
        Assert.assertEquals(actualGoRestGender, expectedGoRestGender);
        Assert.assertEquals(actualGoRestStatus, expectedGoRestStatus);

        System.out.println("\n-----Deleting the user with DELETE Request------\n");
        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .when().delete("/public/v2/users/"+ expectedGoRestId)
                .then().log().all()
                .and().assertThat().statusCode(204)
                .time(Matchers.lessThan(2000L))
                .extract().response();
    }
}