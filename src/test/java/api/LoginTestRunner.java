package api;

import com.intuit.karate.junit5.Karate;

public class LoginTestRunner {
    @Karate.Test
    Karate testLogin() {
        return Karate.run("login.feature").relativeTo(getClass());
    }
}
