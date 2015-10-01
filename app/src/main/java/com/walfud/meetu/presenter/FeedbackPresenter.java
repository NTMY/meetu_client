package com.walfud.meetu.presenter;

import android.os.AsyncTask;
import android.view.View;

import com.walfud.meetu.R;
import com.walfud.meetu.manager.UserManager;
import com.walfud.meetu.view.FeedbackActivity;

import org.meetu.client.handler.FeedbackHandler;
import org.meetu.client.listener.FeedbackListener;
import org.meetu.dto.BaseDto;
import org.meetu.model.Feedback;

/**
 * Created by walfud on 2015/8/19.
 */
public class FeedbackPresenter implements View.OnClickListener {

    public static final String TAG = "FeedbackPresenter";

    private FeedbackActivity mView;

    public FeedbackPresenter(FeedbackActivity view) {
        mView = view;
    }

    // View Event
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                onClickSend();
                break;

            default:
                break;
        }
    }

    // Presenter Function

    //
    private void onClickSend() {
        Feedback feedback = new Feedback();
        feedback.setUserId((int) (long) UserManager.getInstance().getUserId());
        feedback.setContent(mView.getFeedbackContent());

        new AsyncTask<Feedback, Integer, BaseDto>() {

            private BaseDto mResult;

            @Override
            protected BaseDto doInBackground(Feedback... params) {
                Feedback feedback = params[0];

                new FeedbackHandler().onFeedback(new FeedbackListener() {
                    @Override
                    public void feedback(BaseDto baseDto) {
                        mResult = baseDto;
                    }
                }, feedback);

                return mResult;
            }

            @Override
            protected void onPostExecute(BaseDto baseDto) {
                super.onPostExecute(baseDto);

                mView.onSendResult(baseDto);
            }
        }.execute(feedback);

    }
}
