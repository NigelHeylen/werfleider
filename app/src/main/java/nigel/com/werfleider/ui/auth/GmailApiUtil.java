package nigel.com.werfleider.ui.auth;

import android.content.Context;

import java.io.IOException;

/**
 * Created by nigel on 28/12/14.
 */
public class GmailApiUtil {

    // Check https://developers.google.com/gmail/api/auth/scopes for all available scopes
    private static final String SCOPE = "https://www.googleapis.com/auth/gmail.readonly";
    private static final String APP_NAME = "ASAP";
    // Path to the client_secret.json file downloaded from the Developer Console
    private static final String CLIENT_SECRET_PATH = "gmail/client_secret.json";

//    final HttpTransport httpTransport;
//    final JsonFactory jsonFactory;
//    final GoogleAuthorizationCodeFlow flow;

    public GmailApiUtil(final Context context) throws IOException {
//        httpTransport = new NetHttpTransport();
//        jsonFactory = new JacksonFactory();
//
//        final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
//                jsonFactory,
//                new InputStreamReader(context.getAssets().open(CLIENT_SECRET_PATH)));
//
//        flow = new GoogleAuthorizationCodeFlow.Builder(
//                httpTransport, jsonFactory, clientSecrets, Arrays.asList(SCOPE))
//                .setAccessType("online")
//                .setApprovalPrompt("auto").build();
    }

    public void getGmailService(String code) throws IOException {
//
//        // Generate Credential using retrieved code.
//
//        GoogleTokenResponse response = flow.newTokenRequest(code)
//                                           .setRedirectUri(GoogleOAuthConstants.OOB_REDIRECT_URI).execute();
//
//        GoogleCredential credential = new GoogleCredential()
//                .setFromTokenResponse(response);
//
//        // Create a new authorized Gmail API client
//
//        return new Gmail.Builder(httpTransport, jsonFactory, credential)
//                .setApplicationName(APP_NAME).build();
    }

    public void getAuthorizationUrl() {

        // Allow user to authorize via url.

//        return flow.newAuthorizationUrl().setRedirectUri(GoogleOAuthConstants.OOB_REDIRECT_URI)
//                   .build();
    }


}
