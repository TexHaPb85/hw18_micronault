package hw18.micronault;

import hw18.micronault.entity.Info;
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
public class InfoControllerTest {

    @Inject
    private EmbeddedServer embeddedServer;

    @Test
    public void testIndex() throws Exception {

        String userName = "sherlock";
        String password = "password";
        Info responseInfo = new Info(777L, "this is secret info", 2.4214);

        try (RxHttpClient client = embeddedServer.getApplicationContext().createBean(RxHttpClient.class, embeddedServer.getURL())) {

            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, password);

            HttpRequest<Object> loginRequest = HttpRequest.POST("/login", creds);

            HttpResponse<BearerAccessRefreshToken> loginResponse = client
                    .toBlocking()
                    .exchange(loginRequest, BearerAccessRefreshToken.class);

            String refreshToken = loginResponse.body().getRefreshToken();
            assertEquals(HttpStatus.OK, loginResponse.getStatus());

            HttpResponse<AccessRefreshToken> refreshResponse = client
                    .toBlocking()
                    .exchange(HttpRequest
                                    .POST("/oauth/access_token",
                                            new TokenRefreshRequest("refresh_token", refreshToken)),
                            AccessRefreshToken.class);

            assertEquals(HttpStatus.OK, refreshResponse.status());
            String refreshedAccessToken = refreshResponse.body().getAccessToken();
            MutableHttpRequest<Object> infoRequest = HttpRequest.GET("/information").bearerAuth(refreshedAccessToken);
            HttpResponse<String> infoResponse = client.toBlocking().exchange(infoRequest, String.class);
            assertEquals(HttpStatus.OK, infoResponse.getStatus());
            assertEquals(responseInfo.toString(), infoResponse.body());
        }
    }
}