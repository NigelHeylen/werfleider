package nigel.com.werfleider.ui.emailoverzicht;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Flow;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.MessageDetails;

/**
 * Created by nigel on 28/12/14.
 */
public class EmailOverzichtView extends LinearLayout {


    @Inject Flow flow;

    @InjectView(R.id.emailoverzicht_recyclerview) RecyclerView emailRecyclerview;

    private EmailoverzichtAdapter adapter;

    public EmailOverzichtView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context,this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        initRecyclerView();

    }

    private void initRecyclerView() {
        emailRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.reset(this);
    }

    public void loadMessages(final List<MessageDetails> messages) {
        emailRecyclerview.setAdapter(adapter = new EmailoverzichtAdapter(messages));
        Mortar.inject(getContext(), adapter);

        adapter.notifyDataSetChanged();
    }
}
