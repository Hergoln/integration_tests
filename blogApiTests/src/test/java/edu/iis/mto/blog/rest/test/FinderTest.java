package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class FinderTest extends FunctionalTests {
    private static final String USER_API = "/blog/user";

    private static final Long REMOVED_USER = 7L;
    private static final Long POST_ID = 1L;

    @Test
    public void searchForPostsMadeByUserWithStatusRemovedShouldReturnNotFound() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .get(String.format("%s/%d/post", USER_API, REMOVED_USER));
    }

    @Test
    public void likePostsShouldReturnCorrectAmountOfLikes() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .body("likesCount", equalTo(1))
                .when()
                .get(String.format("/blog/post/%d", POST_ID));
    }
}
