package hw18.micronault.security;

import io.micronaut.security.authentication.*;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {
    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        String login = authenticationRequest.getIdentity().toString();
        String password = authenticationRequest.getSecret().toString();
        if (login.equals("sherlock") && password.equals("password")) {
            return Flowable.just(new UserDetails((String) authenticationRequest.getIdentity(), new ArrayList<>()));
        }
        return Flowable.just(new AuthenticationFailed());
    }
}