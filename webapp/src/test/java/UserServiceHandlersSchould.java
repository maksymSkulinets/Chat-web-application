import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

import static com.teamdev.javaclasses.constant.Parameters.NICKNAME;
import static com.teamdev.javaclasses.constant.Parameters.USER_ID;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserServiceHandlersSchould {
    private final String nickname = "King_Artur";
    private final String password = "knight007";

    @Test
    public void SignUpUser() throws IOException {

        final HttpResponse postResponse = TestUtils.sendRegistrationRequest(nickname, password, password);
        final JSONObject json = TestUtils.getResponseContent(postResponse);

        assertEquals("Unexpected response status.", SC_OK, getStatus(postResponse));
        assertEquals("Post request failed.Unexpected nickname.", nickname, json.optString(NICKNAME));
        assertNotNull("Post request failed.Id was not received.", json.optString(USER_ID));
    }

    private int getStatus(HttpResponse postResponse) {
        return postResponse.getStatusLine().getStatusCode();
    }
}
