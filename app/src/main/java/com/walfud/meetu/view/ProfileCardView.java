package com.walfud.meetu.view;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.walfud.meetu.R;

/**
 * Created by walfud on 2015/9/28.
 */
public class ProfileCardView extends FrameLayout implements View.OnClickListener {

    public static final String TAG = "ProfileCardView";

    protected Context mContext;
    protected OnEventListener mEventListener;
    protected RelativeLayout mRootLayout;
    protected SimpleDraweeView mPortrait;
    protected TextView mNick;
    protected TextView mMood;

    public ProfileCardView(Context context) {
        this(context, null);
    }

    public ProfileCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        mRootLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_profile_card, this, false);
        addView(mRootLayout);
        mPortrait = (SimpleDraweeView) mRootLayout.findViewById(R.id.portrait);
        mNick = (TextView) mRootLayout.findViewById(R.id.nick);
        mMood = (TextView) mRootLayout.findViewById(R.id.mood);

        //
        mPortrait.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.portrait:
                if (mEventListener != null) {
                    mEventListener.onClickPortrait();
                }
                break;

            default:
                break;
        }
    }

    // Function
    public ProfileData get() {
        return new ProfileData();
    }

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
                if (profileData.portraitUri != null) {
                    mPortrait.setImageURI(Uri.parse(profileData.portraitUri));
                }
                mNick.setText(profileData.nick);
                mMood.setText(profileData.mood);

                // Fly in
                mPortrait.animate().translationX(0).setStartDelay(0).setDuration(duration).setInterpolator(new DecelerateInterpolator());
                mNick.animate().translationX(0).setStartDelay(50).setDuration(duration).setInterpolator(new DecelerateInterpolator());
                mMood.animate().translationX(0).setStartDelay(100).setDuration(duration).setInterpolator(new DecelerateInterpolator());
            }
        }, 700);
    }

    public void setOnEventListener(OnEventListener listener) {
        mEventListener = listener;
    }

    //
    public static class ProfileData {
        public String portraitUri;
        public String nick;
        public String mood;

        public ProfileData() {
        }
        public ProfileData(String portraitUri, String nick, String mood) {
            this.portraitUri = portraitUri;
            this.nick = nick;
            this.mood = mood;
        }
    }

    //
    public interface OnEventListener {
        void onClickPortrait();
    }
}
