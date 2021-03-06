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
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.walfud.meetu.Constants;
import com.walfud.meetu.R;
import com.walfud.walle.DensityTransformer;

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
    protected boolean mEditable;

    protected RelativeLayout mRootLayout;
    protected ImageView mPortrait;
    protected EditText mNick;
    protected EditText mMood;

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
        switch (v.getId()) {
            case R.id.portrait: {
                if (mEditable) {
                    // Choose picture
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    if (false) {
                        // Stub
                    } else if (mForResultHost instanceof Activity) {
                        ((Activity) (mForResultHost)).startActivityForResult(intent, Constants.REQUEST_PICK_PORTRAIT);
                    } else if (mForResultHost instanceof android.support.v4.app.Fragment) {
                        ((android.support.v4.app.Fragment) (mForResultHost)).startActivityForResult(intent, Constants.REQUEST_PICK_PORTRAIT);
                    } else {
                        // Nothing
                    }
                } else {
                    // TODO: show HD picture
                }

            }
            break;

            case R.id.nick:
                if (mEditable) {
                    setFocus(mNick, true);
                    setFocus(mMood, false);
                }
                break;

            case R.id.mood:
                if (mEditable) {
                    setFocus(mNick, false);
                    setFocus(mMood, true);
                }
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

                    if (mEventListener != null) {
                        mEventListener.onNickChanged(nick);
                    }
                }
            }
            break;

            case R.id.mood: {
                String mood = mMood.getText().toString();
                if (!TextUtils.equals(mProfileData.mood, mood)) {
                    mProfileData.mood = mood;

                    if (mEventListener != null) {
                        mEventListener.onMoodChanged(mood);
                    }
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

        Picasso.with(null).load(profileData.portraitUri).fit().centerCrop().error(R.drawable.ic_account_box_white_48dp).into(mPortrait);
        mNick.setText(profileData.nick);
        mMood.setText(profileData.mood);
    }

    /**
     * `set` with animation
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

    public void checkAndShowGuide() {
        // If is editing someone, should not show guide at the same time
        if (mNick.isFocused() || mMood.isFocused()) {
            return;
        }

        if (false) {
            // Stub
        } else if (TextUtils.isEmpty(mProfileData.nick)) {
            showGuideTo(mNick);
        } else if (TextUtils.isEmpty(mProfileData.mood)) {
            showGuideTo(mMood);
        } else {
            // Nothing
        }
    }

    public boolean getEditable() {
        return mEditable;
    }
    public void setEditable(boolean editable) {
        mEditable = editable;

        setFocus(mNick, false);
        setFocus(mMood, false);
    }

    public void setNickHint(String nickHint) {
        mNick.setHint(nickHint);
    }

    public void setMoodHint(String moodHint) {
        mMood.setHint(moodHint);
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

    private void showGuideTo(final View view) {
        view.animate().translationX(DensityTransformer.dp2px(mContext, 24)).setInterpolator(new LinearInterpolator()).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.animate().translationX(0).setInterpolator(new BounceInterpolator()).setDuration(500);
            }
        });
    }
}
