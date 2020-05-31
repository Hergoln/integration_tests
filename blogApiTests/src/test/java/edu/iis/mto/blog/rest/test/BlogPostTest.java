package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class BlogPostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";

    private static final Long CONFIRMED_USER = 1L;
    private static final Long NEW_USER = 4L;
    private static final Long REMOVED_USER = 7L;

    @Test
    public void postRequestByUserWithConfirmedStatusShouldReturnCreated() {
        JSONObject jsonObj = new JSONObject()
                .put("entry", "this post should be put in database");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post(String.format("%s/%d/post", USER_API, CONFIRMED_USER));
    }

    @Test
    public void postRequestByUserWithNewStatusShouldReturnBadRequest() {
        JSONObject jsonObj = new JSONObject()
                .put("entry", "this entry should NOT be put into database");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(String.format("%s/%d/post", USER_API, NEW_USER));
    }

    @Test
    public void postRequestByUserWithRemovedStatusShouldReturnBadRequest() {
        JSONObject jsonObj = new JSONObject()
                .put("entry", "this entry should NOT be put into database");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(String.format("%s/%d/post", USER_API, REMOVED_USER));
    }
}
