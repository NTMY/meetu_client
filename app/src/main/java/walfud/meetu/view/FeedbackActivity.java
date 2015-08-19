package walfud.meetu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.meetu.dto.BaseDto;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import walfud.meetu.R;
import walfud.meetu.presenter.FeedbackPresenter;

/**
 * Created by walfud on 2015/8/19.
 */
public class FeedbackActivity extends RoboActivity {

    public static final String TAG = "FeedbackActivity";

    private FeedbackPresenter mPreseneter;

    @InjectView(R.id.content)
    private EditText mFeedbackContent;
    @InjectView(R.id.success)
    private WebView mSuccess;
    @InjectView(R.id.send)
    private Button mSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);

        mPreseneter = new FeedbackPresenter(this);

        mSend.setOnClickListener(mPreseneter);
    }

    // View Function
    public String getFeedbackContent() {
        return mFeedbackContent.getText().toString();
    }

    public void onSendResult(BaseDto result) {
        if (true) {
            // Feedback Success
            mSuccess.loadUrl("file:///android_asset/feedback_thanks.html");
        } else {
            // Feedback Fail
            Toast.makeText(this,
                    String.format("Feedback failed. Code(%d), Message(%s)", result.getErrCode(), result.getErrMsg()),
                    Toast.LENGTH_LONG).show();
        }
    }

    //
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);

        context.startActivity(intent);
    }
}
