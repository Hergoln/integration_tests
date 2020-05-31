package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class LikePostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";

    private static final Long[] CONFIRMED_USERS = {1L, 2L};
    private static final Long NEW_USER = 4L;
    private static final Long REMOVED_USER = 7L;
    private static final Long POST = 1L;

    @Test
    public void likePostByUserWithStatusRemovedShouldReturnBadRequest() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(String.format("%s/%d/like/%d", USER_API, REMOVED_USER, POST));
    }

    @Test
    public void likePostByUserWithStatusNewShouldReturnBadRequest() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(String.format("%s/%d/like/%d", USER_API, NEW_USER, POST));
    }

    @Test
    public void likePostByOwnerOfThisPostShouldReturnBadRequest() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(String.format("%s/%d/like/%d", USER_API, CONFIRMED_USERS[0], POST));
    }

    @Test
    public void likePostByUserWithStatusConfirmedShouldReturnOk() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(String.format("%s/%d/like/%d", USER_API, CONFIRMED_USERS[1], POST));
    }

    @Test
    public void relikePostShouldNotChangeItsLikeStatus() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(String.format("%s/%d/like/%d", USER_API, CONFIRMED_USERS[1], POST));

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(String.format("%s/%d/like/%d", USER_API, CONFIRMED_USERS[1], POST));
    }
}
