package nigel.com.werfleider.ui.auth;

/**
 * Created by nigel on 28/12/14.
 */
public class GmailApiMessagesUtil {
//
//    // Email address of the user, or "me" can be used to represent the currently authorized user.
//    private static final String USER = "me";
//
//    /**
//     * Get Message with given ID.
//     *
//     * @param service   Authorized Gmail API instance.
//     * @param userId    User's email address. The special value "me"
//     *                  can be used to indicate the authenticated user.
//     * @param messageId ID of Message to retrieve.
//     * @return Message Retrieved Message.
//     * @throws IOException
//     */
//    public static Message getMessage(Gmail service, String userId, String messageId)
//            throws IOException {
//        Message message = service.users().messages().get(userId, messageId).execute();
//
//        System.out.println("message.getPayload().getBody().getData() = " + message.getPayload().getBody());
//
////        System.out.println("Message snippet: " + message.getSnippet());
//
//        return message;
//    }
//
//    /**
//     * Get All Message.
//     *
//     * @param service Authorized Gmail API instance.
//     * @param userId  User's email address. The special value "me"
//     *                can be used to indicate the authenticated user.
//     * @return Message Retrieved Message.
//     * @throws IOException
//     */
//    public static List<MessageDetails> getMessages(Gmail service, String userId)
//            throws IOException {
//        return getMessages(service, userId, "");
//    }
//
//    /**
//     * List all Messages of the user's mailbox matching the query.
//     *
//     * @param service Authorized Gmail API instance.
//     * @param userId  User's email address. The special value "me"
//     *                can be used to indicate the authenticated user.
//     * @param query   String used to filter the Messages listed.
//     * @throws IOException
//     */
//    public static List<MessageDetails> getMessages(
//            Gmail service, String userId,
//            String query) throws IOException {
//        ListMessagesResponse response = service.users().messages().list(userId).setQ(query).setMaxResults(10l).execute();
//
//        List<Message> messages = new ArrayList<>();
////        while (response.getMessages() != null) {
//        messages.addAll(response.getMessages());
////            if (response.getNextPageToken() != null) {
////                String pageToken = response.getNextPageToken();
////                response = service.users().messages().list(userId).setQ(query)
////                                  .setPageToken(pageToken).execute();
////            } else {
////                break;
////            }
////        }
//
//        List<MessageDetails> mimeMessages = newArrayList();
//
//        for (Message message : messages) {
//
//            mimeMessages.add(getMessageDetails(service, message.getId()));
//        }
//
//        return mimeMessages;
//    }
//
//    public static MessageDetails getMessageDetails(Gmail service, String messageId) {
//        try {
//            Message message = service.users().messages().get("me", messageId).setFormat("raw").execute();
//
//            byte[] emailBytes = Base64.decodeBase64(message.getRaw());
//
//            Properties props = new Properties();
//            Session session = Session.getDefaultInstance(props, null);
//            MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));
//
//            return new MessageDetails(
//                    email.getSubject(),
//                    email.getFrom(),
//                    email.getSentDate() != null ? new DateTime(email.getSentDate()) : null,
//                    message.getSnippet(),
//                    message.getThreadId(),
//                    message.getId(),
//                    getText(email));
//        } catch (MessagingException | IOException ex) {
//            ex.printStackTrace();
////            Logger.getLogger(GoogleAuthHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return null;
//
//    }
//
//
//    private static boolean textIsHtml = false;
//
//    private static String getText(Part p) throws
//            MessagingException, IOException {
//        if (p.isMimeType("text/*")) {
//            String s = (String) p.getContent();
//            textIsHtml = p.isMimeType("text/html");
//            return s;
//        }
//
//        if (p.isMimeType("multipart/alternative")) {
//            // prefer html text over plain text
//            Multipart mp = (Multipart) p.getContent();
//            String text = null;
//            for (int i = 0; i < mp.getCount(); i++) {
//                Part bp = mp.getBodyPart(i);
//                if (bp.isMimeType("text/plain")) {
//                    if (text == null) {
//                        text = getText(bp);
//                    }
//                    continue;
//                } else if (bp.isMimeType("text/html")) {
//                    String s = getText(bp);
//                    if (s != null) {
//                        return s;
//                    }
//                } else {
//                    return getText(bp);
//                }
//            }
//            return text;
//        } else if (p.isMimeType("multipart/*")) {
//            Multipart mp = (Multipart) p.getContent();
//            for (int i = 0; i < mp.getCount(); i++) {
//                String s = getText(mp.getBodyPart(i));
//                if (s != null) {
//                    return s;
//                }
//            }
//        }
//
//        return null;
//    }
//
//    /**
//     * List all Messages of the user's mailbox with labelIds applied.
//     *
//     * @param service  Authorized Gmail API instance.
//     * @param userId   User's email address. The special value "me"
//     *                 can be used to indicate the authenticated user.
//     * @param labelIds Only return Messages with these labelIds applied.
//     * @throws IOException
//     */
//    public static List<Message> listMessagesWithLabels(
//            Gmail service, String userId,
//            List<String> labelIds) throws IOException {
//        ListMessagesResponse response = service.users().messages().list(userId)
//                                               .setLabelIds(labelIds).execute();
//
//        List<Message> messages = new ArrayList<>();
//        while (response.getMessages() != null) {
//            messages.addAll(response.getMessages());
//            if (response.getNextPageToken() != null) {
//                String pageToken = response.getNextPageToken();
//                response = service.users().messages().list(userId).setLabelIds(labelIds)
//                                  .setPageToken(pageToken).execute();
//            } else {
//                break;
//            }
//        }
//
//        return messages;
//    }


}
