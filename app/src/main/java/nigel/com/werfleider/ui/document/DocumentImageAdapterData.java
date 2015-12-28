package nigel.com.werfleider.ui.document;

import java.util.List;
import nigel.com.werfleider.model.ParseDocumentImage;
import rx.subjects.PublishSubject;

/**
 * Created by nigel on 26/12/15.
 */
public interface DocumentImageAdapterData {

  boolean add(ParseDocumentImage documentImage);
  boolean remove(Object documentImage);

  boolean isEmpty();

  ParseDocumentImage get(int position);

  boolean contains(Object image);

  List<ParseDocumentImage> getList();

  PublishSubject<DocumentImageAction> getActionBus();

  void clear();

  void addAll(List<ParseDocumentImage> list);

  int size();
}
