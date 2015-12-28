package nigel.com.werfleider.ui.document;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import nigel.com.werfleider.model.ParseDocumentImage;
import rx.subjects.PublishSubject;

import static nigel.com.werfleider.commons.recyclerview.Action.INSERT;
import static nigel.com.werfleider.commons.recyclerview.Action.REMOVE;

/**
 * Created by nigel on 26/12/15.
 */
public class DocumentImageAdapterDataBean extends ArrayList<ParseDocumentImage>
    implements DocumentImageAdapterData {

  final PublishSubject<DocumentImageAction> documentImageActionPublishSubject =
      PublishSubject.create();

  @Inject
  public DocumentImageAdapterDataBean() {
  }

  @Override public boolean add(ParseDocumentImage object) {

    final boolean add = super.add(object);

    documentImageActionPublishSubject.onNext(new DocumentImageAction(INSERT, object));
    return add;
  }

  @Override public boolean remove(Object object) {
    final boolean remove = super.remove(object);

    documentImageActionPublishSubject.onNext(
        new DocumentImageAction(REMOVE, (ParseDocumentImage) object));
    return remove;
  }

  @Override public List<ParseDocumentImage> getList() {
    return this;
  }

  @Override public PublishSubject<DocumentImageAction> getActionBus() {
    return documentImageActionPublishSubject;
  }

  @Override public void addAll(List<ParseDocumentImage> list) {

    super.addAll(list);
  }
}
