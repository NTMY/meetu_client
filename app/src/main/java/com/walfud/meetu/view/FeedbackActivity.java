package com.walfud.meetu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.walfud.meetu.BaseActivity;
import com.walfud.meetu.R;
import com.walfud.meetu.presenter.FeedbackPresenter;

import org.meetu.constant.Constant;
import org.meetu.dto.BaseDto;

/**
 * Created by walfud on 2015/8/19.
 */
public class FeedbackActivity extends BaseActivity {

    public static final String TAG = "FeedbackActivity";

    private FeedbackPresenter mPresenter;

    private EditText mContent;
    private WebView mSuccess;
    private Button mSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        mContent = $(R.id.content);
        mSuccess = $(R.id.success);
        mSend = $(R.id.send);

        mPresenter = new FeedbackPresenter(this);

        mSend.setOnClickListener(mPresenter);
    }

    // View Function
    public String getFeedbackContent() {
        return mContent.getText().toString();
    }

    public void onSendResult(BaseDto result) {
        if (result.getErrCode() == null) {
            // Feedback Success
            mSuccess.loadUrl(Constant.FEEDBACK_URL);
            mSuccess.setVisibility(View.VISIBLE);
            mContent.setVisibility(View.GONE);
            mSend.setVisibility(View.GONE);
        } else {
            // Feedback Fail
            Toast.makeText(this,
                    String.format("Feedback failed. Code(%s), Message(%s)", result.getErrCode(), result.getErrMsg()),
                    Toast.LENGTH_LONG).show();
        }
    }

    //
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }
}
