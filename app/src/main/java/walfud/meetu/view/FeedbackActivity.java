package walfud.meetu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.meetu.constant.Constant;
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
    private EditText mContent;
    @InjectView(R.id.success)
    private WebView mSuccess;
    @InjectView(R.id.send)
    private Button mSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);

        mPreseneter = new FeedbackPresenter(this);

        mSuccess.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mSend.setOnClickListener(mPreseneter);
    }

    // View Function
    public String getFeedbackContent() {
        return mContent.getText().toString();
    }

    public void onSendResult(BaseDto result) {
        if (result.getErrCode() == null) {
            // Feedback Success
            mSuccess.loadUrl(Constant.DOMAIN);
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
