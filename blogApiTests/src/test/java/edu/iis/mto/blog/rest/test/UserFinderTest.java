package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UserFinderTest extends FunctionalTests {

    public static final Long REMOVED_USER = 7L;
    private static final String USER_API = "/blog/user";

    @Test
    public void whenAskedForUserWithIDApiShouldReturnBadRequest() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .get(String.format("%s/%d", USER_API, REMOVED_USER));
    }

    @Test
    public void whenSearchingForUsersByFirstNameOrLastNameOrEmailApiShouldNotReturnUsersWithStatusRemoved() {
        String searchString = "@demotywatory.com";

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .body("", Matchers.hasSize(0))
                .when()
                .get(String.format("%s/find?searchString=%s", USER_API, searchString));
    }

    @Test
    public void shouldReturnUsersSearchedByFirstNameOrLastNameOrEmailWithStatusConfirmed() {
        String searchString = "@kwejk.com";

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .body("", Matchers.hasSize(3))
                .when()
                .get(String.format("%s/find?searchString=%s", USER_API, searchString));
    }

    @Test
    public void shouldReturnUsersSearchedByFirstNameOrLastNameOrEmailWithStatusNew() {
        String searchString = "@jbzd.com";

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .body("", Matchers.hasSize(3))
                .when()
                .get(String.format("%s/find?searchString=%s", USER_API, searchString));
    }
}
