package io.quarkiverse.roq;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

@QuarkusTest
public class RoqTest {

    @Test
    public void testMdPost() {
        RestAssured.when().get("/posts/k8s-post").then().statusCode(200).log().ifValidationFails()
                .body("html.head.title", equalTo("Markdown Post"))
                .body("html.body.article.h1", equalTo("A post made with markdown"))
                .body("html.body.article.blockquote.p", equalTo("bar"))
                .body("html.body.div.h2", equalTo("Markdown Post"))
                .body("html.body.div.p", equalTo("bar bar bar"));
    }
}
