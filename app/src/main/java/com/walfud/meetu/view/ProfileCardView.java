package com.walfud.meetu.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.walfud.common.DensityTransformer;
import com.walfud.meetu.Constants;
import com.walfud.meetu.MeetUApplication;
import com.walfud.meetu.R;

/**
 * Created by walfud on 2015/9/28.
 */
public class ProfileCardView extends FrameLayout
        implements View.OnClickListener, View.OnFocusChangeListener, TextView.OnEditorActionListener {

    public static final String TAG = "ProfileCardView";

    protected Context mContext;
    protected OnEventListener mEventListener;
    /**
     * `startActivityForResult` via `Activity` or `Fragment`
     */
    protected Object mForResultHost;
    protected ProfileData mProfileData;

    protected RelativeLayout mRootLayout;
    protected ImageView mPortrait;
    protected EditText mNick;
    protected EditText mMood;

    protected DisplayImageOptions mPortraitOptions;

    public ProfileCardView(Context context) {
        this(context, null);
    }

    public ProfileCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mProfileData = new ProfileData();

        mRootLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_profile_card, this, false);
        addView(mRootLayout);
        mPortrait = (ImageView) mRootLayout.findViewById(R.id.portrait);
        mNick = (EditText) mRootLayout.findViewById(R.id.nick);
        mMood = (EditText) mRootLayout.findViewById(R.id.mood);

        mPortraitOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new RoundedBitmapDisplayer(DensityTransformer.dp2px(MeetUApplication.getContext(), 10)))
                .build();

        //
        mRootLayout.setOnClickListener(this);
        mPortrait.setOnClickListener(this);
        mNick.setOnClickListener(this);
        mMood.setOnClickListener(this);

        mNick.setOnFocusChangeListener(this);
        mNick.setOnEditorActionListener(this);
        mMood.setOnFocusChangeListener(this);
        mMood.setOnEditorActionListener(this);

        mNick.setTag(R.id.tag_text_fg_color, mNick.getTextColors());
        mNick.setTag(R.id.tag_text_bg_color, mNick.getBackground());
        mMood.setTag(R.id.tag_text_fg_color, mMood.getTextColors());
        mMood.setTag(R.id.tag_text_bg_color, mMood.getBackground());
    }

    @Override
    public void onClick(View v) {
        if (mEventListener == null) {
            return;
        }

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
                setFocus(mMood, false);
                break;

            case R.id.mood:
                setFocus(mNick, false);
                setFocus(mMood, true);
                break;

            default: {
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
                setFocus(mNick, false);
                setFocus(mMood, false);
            }
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
            case R.id.nick: {
                String nick = mNick.getText().toString();
                if (!TextUtils.equals(mProfileData.nick, nick)) {
                    mProfileData.nick = nick;
                    mEventListener.onNickChanged(nick);
                }
            }
                break;

            case R.id.mood: {
                String mood = mMood.getText().toString();
                if (!TextUtils.equals(mProfileData.mood, mood)) {
                    mProfileData.mood = mood;
                    mEventListener.onMoodChanged(mood);
                }
            }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);

        switch (v.getId()) {
            case R.id.nick:
                setFocus(mNick, false);
                return true;

            case R.id.mood:
                setFocus(mMood, false);
                return true;

            default:
                break;
        }

        return false;
    }

    // Function
    public ProfileData get() {
        return mProfileData;
    }

    public void set(ProfileData profileData) {
        mProfileData = profileData;

        ImageLoader.getInstance().displayImage(profileData.portraitUri.toString(), mPortrait, mPortraitOptions);
        mNick.setText(profileData.nick);
        mMood.setText(profileData.mood);
    }

    /**
     * `set` & `refresh`
     */
    public void update(final ProfileData profileData) {
        final int duration = 200;
        // Fly out
        mPortrait.animate().translationX(-mPortrait.getWidth()).setStartDelay(0).setDuration(duration).setInterpolator(new AnticipateInterpolator());
        mNick.animate().translationX(-mNick.getWidth()).setStartDelay(50).setDuration(duration).setInterpolator(new AnticipateInterpolator());
        mMood.animate().translationX(-mMood.getWidth()).setStartDelay(100).setDuration(duration).setInterpolator(new AnticipateInterpolator());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Change value
                set(profileData);

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

    /**
     * Set the caller for `startActivityForResult` via `Activity` or `Fragment`
     *
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
     *
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

                    mProfileData.portraitUri = portraitUri;
                    mEventListener.onPortraitChanged(portraitUri);
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
    private void setFocus(EditText et, boolean focus) {
        et.setFocusable(focus);
        et.setFocusableInTouchMode(focus);
        if (focus) {
            et.requestFocus();

            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(et, 0);

            et.setTextColor(Color.BLACK);
            et.setBackgroundColor(Color.WHITE);
        } else {
            et.clearFocus();
            et.setTextColor((ColorStateList) et.getTag(R.id.tag_text_fg_color));
            et.setBackground((Drawable) et.getTag(R.id.tag_text_bg_color));
        }
    }
}
