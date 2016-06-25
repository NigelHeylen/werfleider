package nigel.com.werfleider.ui.document;

import android.Manifest;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.gc.materialdesign.views.ProgressBarDeterminate;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.mrengineer13.snackbar.SnackBar;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.io.IOException;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 02/01/16.
 */
public class LocationDetailAudioView extends ScrollView {

  private static final String LOG_TAG = "AUDIO_VIEW";

  @Inject LocationDetailAudioPresenter presenter;

  @Bind(R.id.location_detail_play_audio) FloatingActionButton playButton;
  @Bind(R.id.location_detail_play_seconds) TextView playSeconds;
  @Bind(R.id.location_detail_play_seconds_max) TextView playSecondsMax;
  @Bind(R.id.location_detail_play_container) ViewGroup playContainer;
  @Bind(R.id.location_detail_play_progress) ProgressBarDeterminate playProgress;

  @Bind(R.id.location_detail_record_audio) FloatingActionButton recordButton;
  @Bind(R.id.location_detail_record_seconds) TextView recordSeconds;
  @Bind(R.id.location_detail_record_container) ViewGroup recordContainer;
  @Bind(R.id.location_detail_record_progress) ProgressBarDeterminate recordProgress;

  @BindDrawable(R.drawable.ic_pause) Drawable pause;
  @BindDrawable(R.drawable.ic_stop) Drawable stop;
  @BindDrawable(R.drawable.ic_play) Drawable play;
  @BindDrawable(R.drawable.ic_record_audio) Drawable record;

  private MediaRecorder mediaRecorder;

  private MediaPlayer savedMediaPlayer;

  private String fileName = null;

  private boolean startRecording = true;
  private boolean startPlaying = true;

  public LocationDetailAudioView(final Context context, final AttributeSet attrs) {

    super(context, attrs);
    if (!isInEditMode()) {
      Mortar.inject(context, this);
      fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
      fileName += "/audiorecord.3gp";
    }
  }

  @Override protected void onFinishInflate() {

    super.onFinishInflate();

    if (!isInEditMode()) {
      ButterKnife.bind(this);
      presenter.takeView(this);
    }
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if(!isInEditMode()){
      ButterKnife.bind(this);
      presenter.takeView(this);
    }
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    reset();
    ButterKnife.unbind(this);
  }

  @OnClick(R.id.location_detail_record_audio) public void record() {

    RxPermissions.getInstance(getContext())
        .request(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .subscribe(granted -> {
          if (granted) {

            onRecord(startRecording);
            if (startRecording) {

              recordButton.setIconDrawable(stop);
            } else {

              recordButton.setIconDrawable(record);
            }
            Log.i(VIEW_LOG_TAG, "record:" + startRecording);
            startRecording = !startRecording;
          } else {

            Toast.makeText(getContext(),
                "Zonder de toestemming om audio op te nemen kan Werfleider geen audio opslaan.",
                Toast.LENGTH_LONG).show();
          }
        });
  }

  @OnClick(R.id.location_detail_play_audio) public void play() {

    onPlay(startPlaying);
    if (startPlaying) {

      playButton.setIconDrawable(stop);
    } else {
      playButton.setIconDrawable(play);
    }
    Log.i(VIEW_LOG_TAG, "play:" + startPlaying);
    startPlaying = !startPlaying;
  }

  private void onRecord(boolean start) {
    if (start) {
      startRecording();
    } else {
      stopRecording();
    }
  }

  private void onPlay(boolean start) {
    if (start) {
      startPlaying();
    } else {
      stopPlaying();
    }
  }

  private void startPlaying() {
    prepareMediaPlayer();
    savedMediaPlayer.start();
    presenter.startPlaying(savedMediaPlayer.getDuration());
  }

  protected void prepareMediaPlayer() {
    try {
      savedMediaPlayer = new MediaPlayer();
      savedMediaPlayer.setDataSource(fileName);
      savedMediaPlayer.prepare();
      savedMediaPlayer.setOnCompletionListener(mp -> {
        play();
      });
      playSecondsMax.setText(
          String.format("/%s", String.valueOf(savedMediaPlayer.getDuration() / 1000)));
      playProgress.setMax(savedMediaPlayer.getDuration() / 1000);
    } catch (IOException e) {
      Log.e(LOG_TAG, "prepare() failed");
    }
  }

  private void stopPlaying() {
    savedMediaPlayer.release();
    savedMediaPlayer = null;
    presenter.stopPlaying();
  }

  private void startRecording() {

    mediaRecorder = new MediaRecorder();
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    mediaRecorder.setOutputFile(fileName);
    mediaRecorder.setMaxFileSize(120 * 1024);
    mediaRecorder.setMaxDuration(30 * 1000);
    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    mediaRecorder.setOnInfoListener((mr, what, extra) -> {

      recordButton.callOnClick();
    });

    try {
      mediaRecorder.prepare();
    } catch (IOException e) {
      Log.e(LOG_TAG, "prepare() failed");
    }

    mediaRecorder.start();
    presenter.startRecording();
  }

  private void stopRecording() {
    mediaRecorder.stop();
    mediaRecorder.release();
    mediaRecorder = null;
    presenter.stopRecording();
  }

  public void setRecordingSeconds(Integer seconds) {

    recordSeconds.setText(String.valueOf(seconds));
    recordProgress.setProgress(seconds);
  }

  public String getFileName() {
    return fileName;
  }

  public void showPlayAudio(boolean show) {

    if (show) {

      playButton.setVisibility(VISIBLE);
      playContainer.setVisibility(VISIBLE);
    } else {

      playButton.setVisibility(GONE);
      playContainer.setVisibility(GONE);
    }
  }

  public void reset() {

    startRecording = true;
    startPlaying = true;

    playButton.setIconDrawable(play);
    recordButton.setIconDrawable(record);

    playSeconds.setText("0");
    playProgress.setProgress(0);
    recordSeconds.setText("0");
    recordProgress.setProgress(0);
    if (mediaRecorder != null) {
      mediaRecorder.release();
      mediaRecorder = null;
    }

    if (savedMediaPlayer != null) {
      savedMediaPlayer.release();
      savedMediaPlayer = null;
    }
  }

  public void setPlaySeconds(Integer seconds) {

    playSeconds.setText(String.valueOf(seconds));
    playProgress.setProgress(seconds);
  }

  public void hideRecordViews() {
    recordButton.setVisibility(GONE);
    recordProgress.setVisibility(GONE);
    recordSeconds.setVisibility(GONE);
    recordContainer.setVisibility(GONE);
  }

  public void askToSaveAudio() {

    new SnackBar.Builder(getContext(), (View) getParent().getParent().getParent()).withMessage(
        "Wilt u de audio opslaan?")
        .withActionMessage("Opslaan")
        .withTextColorId(R.color.blue)
        .withOnClickListener(token -> {
          presenter.saveAudio();
          prepareMediaPlayer();
          showPlayAudio(true);
        })
        .show();
  }
}
