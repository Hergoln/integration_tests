package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class BlogPostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";
    private static final String BASE = "/blog";

    private static final Long[] CONFIRMED_USERS = {1L, 2L, 3L};
    private static final Long[] NEW_USERS = {4L, 5L, 6L};
    private static final Long[] REMOVED_USERS = {7L, 8L, 9L};

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
                .post(String.format("%s/%d/post", USER_API, CONFIRMED_USERS[0]));
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
                .post(String.format("%s/%d/post", USER_API, NEW_USERS[0]));
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
                .post(String.format("%s/%d/post", USER_API, REMOVED_USERS[0]));
    }
}
