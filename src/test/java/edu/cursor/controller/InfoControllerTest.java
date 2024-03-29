package edu.cursor.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.endpoints.TokenRefreshRequest;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class InfoControllerTest {

    @Inject
    EmbeddedServer embeddedServer;

    @Test
    void testIndex() throws Exception {

        String userName = "micro-user";
        String password = "password";
        String goodbyeString = userName + ", sorry the server is busy now, please, try again later, goodbye!";

        try (RxHttpClient client = embeddedServer.getApplicationContext().createBean(RxHttpClient.class, embeddedServer.getURL())) {

            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, password);
            HttpRequest<Object> loginRequest = HttpRequest.POST("/login", creds);
            HttpResponse<BearerAccessRefreshToken> loginResponse = client.toBlocking().exchange(loginRequest, BearerAccessRefreshToken.class);

            String refreshToken = loginResponse.body().getRefreshToken();
            HttpResponse<AccessRefreshToken> refreshResponse = client
                    .toBlocking()
                    .exchange(HttpRequest
                                    .POST("/oauth/access_token", new TokenRefreshRequest("refresh_token", refreshToken)),
                            AccessRefreshToken.class);

            assertEquals(HttpStatus.OK, refreshResponse.status());
            String refreshedAccessToken = refreshResponse.body().getAccessToken();
            assertEquals(HttpStatus.OK, loginResponse.getStatus());
            MutableHttpRequest<Object> goodbyeRequest = HttpRequest.GET("/goodbye").bearerAuth(refreshedAccessToken);
            HttpResponse<String> goodbyeResponse = client.toBlocking().exchange(goodbyeRequest, String.class);
            assertEquals(HttpStatus.OK, goodbyeResponse.getStatus());
            assertEquals(goodbyeString, goodbyeResponse.body());
        }
    }
}
