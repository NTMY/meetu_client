package com.walfud.meetu.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.walfud.meetu.Constants;
import com.walfud.meetu.R;

/**
 * Created by walfud on 2015/9/28.
 */
public class ProfileCardView extends FrameLayout
        implements View.OnClickListener, View.OnFocusChangeListener {

    public static final String TAG = "ProfileCardView";

    protected Context mContext;
    protected OnEventListener mEventListener;
    /**
     * `startActivityForResult` via `Activity` or `Fragment`
     */
    protected Object mForResultHost;

    protected RelativeLayout mRootLayout;
    protected SimpleDraweeView mPortrait;
    protected EditText mNick;
    protected EditText mMood;

    public ProfileCardView(Context context) {
        this(context, null);
    }

    public ProfileCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        mRootLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_profile_card, this, false);
        addView(mRootLayout);
        mPortrait = (SimpleDraweeView) mRootLayout.findViewById(R.id.portrait);
        mNick = (EditText) mRootLayout.findViewById(R.id.nick);
        mMood = (EditText) mRootLayout.findViewById(R.id.mood);

        //
        mRootLayout.setOnClickListener(this);
        mPortrait.setOnClickListener(this);
        mNick.setOnClickListener(this);
        mMood.setOnClickListener(this);

        mNick.setOnFocusChangeListener(this);
        mMood.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.portrait: {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (false) {
                } else if (mForResultHost instanceof Activity) {
                    ((Activity) (mForResultHost)).startActivityForResult(intent, Constants.REQUEST_PICK_PORTRAIT);
                } else if (mForResultHost instanceof android.support.v4.app.Fragment) {
                    ((android.support.v4.app.Fragment) (mForResultHost)).startActivityForResult(intent, Constants.REQUEST_PICK_PORTRAIT);
                } else {
                }
            }
                break;

            case R.id.nick:
                setFocus(mNick, true);
                break;

            case R.id.mood:
                setFocus(mMood, true);
                break;

            default:
                setFocus(mNick, false);
                setFocus(mMood, false);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // Don't care if obtain focus
        if (hasFocus) {
            return;
        }

        switch (v.getId()) {
            case R.id.nick:
                if (mEventListener != null) {
                    mEventListener.onNickChanged(mNick.getText().toString());
                }
                break;

            case R.id.mood:
                if (mEventListener != null) {
                    mEventListener.onMoodChanged(mMood.getText().toString());
                }
                break;

            default:
                break;
        }
    }

    // Function
    public void set(final ProfileData profileData) {
        final int duration = 200;
        // Fly out
        mPortrait.animate().translationX(-mPortrait.getWidth()).setStartDelay(0).setDuration(duration).setInterpolator(new AnticipateInterpolator());
        mNick.animate().translationX(-mNick.getWidth()).setStartDelay(50).setDuration(duration).setInterpolator(new AnticipateInterpolator());
        mMood.animate().translationX(-mMood.getWidth()).setStartDelay(100).setDuration(duration).setInterpolator(new AnticipateInterpolator());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Change value
                Uri portraitUri = profileData.portraitUri;
                mPortrait.setImageURI(portraitUri);
                mNick.setText(profileData.nick);
                mMood.setText(profileData.mood);

                // Fly in
                mPortrait.animate().translationX(0).setStartDelay(0).setDuration(duration).setInterpolator(new DecelerateInterpolator());
                mNick.animate().translationX(0).setStartDelay(50).setDuration(duration).setInterpolator(new DecelerateInterpolator());
                mMood.animate().translationX(0).setStartDelay(100).setDuration(duration).setInterpolator(new DecelerateInterpolator());
            }
        }, 700);
    }

    /**
     * Refresh UI without animation
     */
    public void refresh(ProfileData profileData) {
        mPortrait.setImageURI(profileData.portraitUri);
        mNick.setText(profileData.nick);
        mMood.setText(profileData.mood);
    }

    public void setOnEventListener(OnEventListener listener) {
        mEventListener = listener;
    }

    /**
     * Set the caller for `startActivityForResult` via `Activity` or `Fragment`
     * @param host should be either `Activity` or `Fragment`
     */
    public void setStartActivityForResultHost(Object host) {
        if (!(host instanceof Activity
                || host instanceof android.support.v4.app.Fragment)) {
            throw new IllegalArgumentException("`setStartActivityForResultHost`. Argument should be either `Activity` or `Fragment`");
        }

        mForResultHost = host;
    }

    /**
     * You should call this function in `onActivityResult` of activity/fragment
     * @param requestCode
     * @param resultCode
     * @param data
     * @return true if this function has consumed the result, in which case, you should not go
     * further in you host activity/fragment, otherwise is false
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_PICK_PORTRAIT:
                if (resultCode == Activity.RESULT_OK) {
                    Uri portraitUri = data.getData();

                    if (mEventListener != null) {
                        mEventListener.onPortraitChanged(portraitUri);
                    }
                }
                return true;

            default:
                break;
        }

        return false;
    }

    //
    public static class ProfileData {
        public Uri portraitUri;
        public String nick;
        public String mood;

        public ProfileData() {
        }
        public ProfileData(Uri portraitUri, String nick, String mood) {
            this.portraitUri = portraitUri;
            this.nick = nick;
            this.mood = mood;
        }
    }

    //
    public interface OnEventListener {
        void onPortraitChanged(Uri newPortraitUri);
        void onNickChanged(String newNick);
        void onMoodChanged(String newMood);
    }

    // Internal
    private void setFocus(View view, boolean focus) {
        view.setFocusable(focus);
        view.setFocusableInTouchMode(focus);
        if (focus) {
            view.requestFocus();
        } else {
            view.clearFocus();
        }
    }
}
