package nigel.com.werfleider.ui.document;

import android.os.Bundle;
import com.parse.ParseUser;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import nigel.com.werfleider.commons.parse.ParseErrorHandler;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import nigel.com.werfleider.util.AudioUtils;
import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.Subscriptions;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

/**
 * Created by nigel on 02/01/16.
 */
public class LocationDetailAudioPresenter extends ReactiveViewPresenter<LocationDetailAudioView> {

  @Inject BehaviorSubject<ParseDocumentImage> documentImageBus;

  @Inject ParseErrorHandler parseErrorHandler;

  private ParseDocumentImage documentImage;

  private Subscription timerSubscription = Subscriptions.empty();

  @Override protected void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);
    if (getView() == null) return;

    subscribe(documentImageBus.subscribe(this::bindDocumentImage));
  }

  private void bindDocumentImage(final ParseDocumentImage documentImage) {

    this.documentImage = documentImage;
    if (getView() != null) {

      getView().reset();
      resetTimerSubscription();

      if(!documentImage.getAuthor().equals(ParseUser.getCurrentUser())){

        if(getView() != null){

          getView().hideRecordViews();
        }
      }

      if (documentImage.hasAudio()) {

        resetAudio();
      } else {

        getView().showPlayAudio(false);
      }
    }
  }

  private void resetTimerSubscription() {
    timerSubscription.unsubscribe();
  }

  protected void saveAudio() {

    documentImage.setAudio(AudioUtils.getByteArrayFromPath(getView().getFileName()));
    documentImage.saveEventually(e1 -> {
      if(e1 != null) parseErrorHandler.handleParseError(e1);
    });

    getView().showPlayAudio(true);
  }

  public void startRecording() {

    getView().setRecordingSeconds(0);

    timerSubscription = getTimerObservable(30).subscribe(seconds -> getView().setRecordingSeconds(seconds));
    subscribe(timerSubscription);
  }

  public void startPlaying(final int duration) {

    getView().setPlaySeconds(0);

    timerSubscription = getTimerObservable(duration).subscribe(seconds -> getView().setPlaySeconds(
        seconds));
    subscribe(timerSubscription);
  }

  private Observable<Integer> getTimerObservable(int duration) {
    return Observable.interval(1, TimeUnit.SECONDS)
        .take(duration)
        .map(aLong -> aLong + 1)
        .map(Long::intValue)
        .observeOn(mainThread());
  }

  public void stopRecording() {

    resetTimerSubscription();
    getView().askToSaveAudio();
  }

  public void stopPlaying() {

    resetTimerSubscription();
  }

  public void resetAudio() {

    AudioUtils.saveByteArrayToFile(getView().getFileName(), documentImage.getAudio());
    getView().setPlaySeconds(0);
    getView().prepareMediaPlayer();
    getView().showPlayAudio(true);
  }
}
