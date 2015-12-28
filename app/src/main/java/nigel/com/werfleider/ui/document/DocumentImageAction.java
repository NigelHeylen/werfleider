package nigel.com.werfleider.ui.document;

import nigel.com.werfleider.commons.recyclerview.Action;
import nigel.com.werfleider.model.ParseDocumentImage;

/**
 * Created by nigel on 26/12/15.
 */
public class DocumentImageAction {

  final Action action;

  final ParseDocumentImage documentImage;

  public DocumentImageAction(Action action, ParseDocumentImage documentImage) {
    this.action = action;
    this.documentImage = documentImage;
  }

  public Action getAction() {
    return action;
  }

  public ParseDocumentImage getDocumentImage() {
    return documentImage;
  }
}
