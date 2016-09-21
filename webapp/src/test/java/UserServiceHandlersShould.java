import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static com.teamdev.javaclasses.constant.Parameters.*;
import static com.teamdev.javaclasses.service.UserServiceFailCases.*;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserServiceHandlersShould {

    @Test
    public void signUpUser() throws IOException {
        final String nickname = "User_" + UUID.randomUUID();
        final String password = "correct_password";

        final HttpResponse response = TestUtils.sendRegistrationRequest(nickname, password, password);
        final JSONObject result = TestUtils.getResponseContent(response);

        assertEquals("Unexpected response status.", SC_OK, getStatus(response));
        assertEquals("Request failed.Unexpected nickname.", nickname, result.optString(NICKNAME));
        assertNotNull("Request failed.Id was not received.", result.optString(USER_ID));

    }

    @Test
    public void duplicateUserSignUpFail() throws IOException {
        final String nickname = "User_" + UUID.randomUUID();
        final String password = "correct_password";

        final HttpResponse firstResponse = TestUtils.sendRegistrationRequest(nickname, password, password);
        TestUtils.getResponseContent(firstResponse);

        final HttpResponse secondResponse = TestUtils.sendRegistrationRequest(nickname, password, password);
        final JSONObject result = TestUtils.getResponseContent(secondResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(secondResponse));
        assertEquals("Unexpected message.", EXIST_USER.getMessage(), result.optString(WARNING_MESSAGE));
    }


    @Test
    public void passwordsNonMatchingSignUpFail() throws IOException {
        final String nickname = "User_" + UUID.randomUUID();
        final String password = "correct_password";

        final HttpResponse response = TestUtils.sendRegistrationRequest(nickname, password, "NOT_MATCH_PASS");
        final JSONObject result = TestUtils.getResponseContent(response);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(response));
        assertEquals("Unexpected message.", PASSWORDS_NOT_MATCH.getMessage(), result.optString(WARNING_MESSAGE));
    }

    @Test
    public void emptyInputSignUpFail() throws IOException {
        final String nickname = "User_" + UUID.randomUUID();
        final String password = "";

        final HttpResponse response = TestUtils.sendRegistrationRequest(nickname, password, password);
        final JSONObject result = TestUtils.getResponseContent(response);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(response));
        assertEquals("Unexpected message.", EMPTY_INPUT.getMessage(), result.optString(WARNING_MESSAGE));
    }

    @Test
    public void loginUser() throws IOException {
        final String nickname = "User5_" + UUID.randomUUID();
        final String password = "correct_password";

        final HttpResponse registrationResponse = TestUtils.sendRegistrationRequest(nickname, password, password);
        final JSONObject registrationResult = TestUtils.getResponseContent(registrationResponse);

        final HttpResponse loginResponse = TestUtils.sendLoginRequest(nickname, password);
        final JSONObject loginResult = TestUtils.getResponseContent(loginResponse);

        assertEquals("Unexpected response status.", SC_OK, getStatus(loginResponse));
        assertEquals("Request failed.Unexpected user id.",
                registrationResult.optString(USER_ID), loginResult.optString(USER_ID));
        assertNotNull("Request failed.Token was not received.", loginResult.optString(TOKEN_ID));
    }

    @Test
    public void emptyInputLoginFail() throws IOException {
        final String nickname = "User_" + UUID.randomUUID();
        final String password = "";

        TestUtils.sendRegistrationRequest(nickname, password, password);

        final HttpResponse loginResponse = TestUtils.sendLoginRequest(nickname, password);
        final JSONObject loginResult = TestUtils.getResponseContent(loginResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(loginResponse));
        assertEquals("Unexpected message.", EMPTY_INPUT.getMessage(), loginResult.optString(WARNING_MESSAGE));
    }

    @Test
    public void nonSignUpUserLoginFail() throws IOException {
        final String nickname = "User_" + UUID.randomUUID();
        final String password = "correct_password";

        final HttpResponse loginResponse = TestUtils.sendLoginRequest(nickname, password);
        final JSONObject loginResult = TestUtils.getResponseContent(loginResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(loginResponse));
        assertEquals("Unexpected message.", NON_SIGN_UP_USER.getMessage(), loginResult.optString(WARNING_MESSAGE));
    }


    @Test
    public void loginWithWrongPasswordFail() throws IOException {
        final String nickname = "User_" + UUID.randomUUID();
        final String password = "correct_password";

        final HttpResponse registrationRequest = TestUtils.sendRegistrationRequest(nickname, password, password);
        TestUtils.getResponseContent(registrationRequest);

        final HttpResponse loginResponse = TestUtils.sendLoginRequest(nickname, "WRONG_PASSWORD");
        final JSONObject loginResult = TestUtils.getResponseContent(loginResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(loginResponse));
        assertEquals("Unexpected message.", NON_SIGN_UP_USER.getMessage(), loginResult.optString(WARNING_MESSAGE));
    }


    private int getStatus(HttpResponse postResponse) {
        return postResponse.getStatusLine().getStatusCode();
    }
}
