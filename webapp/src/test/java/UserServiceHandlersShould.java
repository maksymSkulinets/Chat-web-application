import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static com.teamdev.javaclasses.constant.Parameters.*;
import static com.teamdev.javaclasses.service.UserServiceFailCases.EMPTY_INPUT;
import static com.teamdev.javaclasses.service.UserServiceFailCases.EXIST_USER;
import static com.teamdev.javaclasses.service.UserServiceFailCases.PASSWORDS_NOT_MATCH;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserServiceHandlersShould {

    @Test
    public void signUpUser() throws IOException {
        final String nickname = "User_" + UUID.randomUUID();
        final String password = "correct_password";

        final HttpResponse postResponse = TestUtils.sendRegistrationRequest(nickname, password, password);
        final JSONObject json = TestUtils.getResponseContent(postResponse);

        assertEquals("Unexpected response status.", SC_OK, getStatus(postResponse));
        assertEquals("Post request failed.Unexpected nickname.", nickname, json.optString(NICKNAME));
        assertNotNull("Post request failed.Id was not received.", json.optString(USER_ID));

    }

    @Test
    public void duplicateUserSignUpFail() throws IOException {
        final String nickname = "User_" + UUID.randomUUID();
        final String password = "correct_password";

        TestUtils.sendRegistrationRequest(nickname, password, password);
        final HttpResponse postResponse = TestUtils.sendRegistrationRequest(nickname, password, password);
        final JSONObject json = TestUtils.getResponseContent(postResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(postResponse));
        assertEquals("Unexpected message.", EXIST_USER.getMessage(), json.optString(WARNING_MESSAGE));
    }


    @Test
    public void passwordsNonMatchingSignUpFail() throws IOException {
        final String nickname = "User_" + UUID.randomUUID();
        final String password = "correct_password";

        final HttpResponse postResponse = TestUtils.sendRegistrationRequest(nickname, password, "NOT_MATCH_PASS");
        final JSONObject json = TestUtils.getResponseContent(postResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(postResponse));
        assertEquals("Unexpected message.", PASSWORDS_NOT_MATCH.getMessage(), json.optString(WARNING_MESSAGE));
    }

    @Test
    public void emptyInputSignUpFail() throws IOException {
        final String nickname = "User_" + UUID.randomUUID();
        final String password = "";

        final HttpResponse postResponse = TestUtils.sendRegistrationRequest(nickname, password, password);
        final JSONObject json = TestUtils.getResponseContent(postResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(postResponse));
        assertEquals("Unexpected message.", EMPTY_INPUT.getMessage(), json.optString(WARNING_MESSAGE));
    }

    private int getStatus(HttpResponse postResponse) {
        return postResponse.getStatusLine().getStatusCode();
    }
}
