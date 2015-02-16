package nigel.com.werfleider.model;


import com.google.common.base.Optional;

import org.joda.time.DateTime;


/**
 * Created by nigel on 20/01/15.
 */
public class MessageDetails {
    final String subject;
    private final DateTime sentDate;
    private final String snippet;
    private final String threadId;
    private final String id;
    private final String text;
//    private final Address[] sender;

    public MessageDetails(
            final String subject,
//            final Address[] sender,
            final DateTime sentDate,
            final String snippet,
            final String threadId,
            final String id,
            final String text) {
        this.subject = subject;
//        this.sender = sender;
        this.sentDate = sentDate;
        this.snippet = snippet;
        this.threadId = threadId;
        this.id = id;
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public Optional<DateTime> getSentDate() {
        return Optional.fromNullable(sentDate);
    }

    public String getSnippet() {
        return snippet;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

//    public Optional<Address[]> getSender() {
//        return Optional.fromNullable(sender);
//    }
//
//    public String getSenderShortString(){
//        return sender[0].toString().split("<")[0];
//    }
}
