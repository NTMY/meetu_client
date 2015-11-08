package com.walfud.meetu.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.walfud.common.DensityTransformer;
import com.walfud.common.widget.SelectView;
import com.walfud.meetu.R;
import com.walfud.meetu.presenter.MainFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walfud on 2015/11/6.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "MainFragment";

    private MainActivity mHostActivity;
    private MainFragmentPresenter mPresenter;

    private SelectView mSvFriendList;
    private FloatingActionButton mFabClean;
    private FloatingActionButton mFabSearch;

    private List<NearbyFriendData> mNearbyFriendDataList = new ArrayList<>();

    /**
     * Whether need searching animation
     */
    private boolean mIsSearchingAnimate = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        RelativeLayout root = (RelativeLayout) inflater.inflate(R.layout.fragment_main, container, false);
        mSvFriendList = (SelectView) root.findViewById(R.id.sv_friend_list);
        mFabClean = (FloatingActionButton) root.findViewById(R.id.fab_clean);
        mFabSearch = (FloatingActionButton) root.findViewById(R.id.fab_search);

        //
        mSvFriendList.setLayoutManager(new LinearLayoutManager(mHostActivity));
        mSvFriendList.setAdapter(new SelectView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(mHostActivity).inflate(R.layout.item_friend_list, parent, false);
                return new SelectView.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                LinearLayout itemRoot = (LinearLayout) holder.itemView;
                ImageView portrait = (ImageView) itemRoot.findViewById(R.id.portrait);
                TextView nick = (TextView) itemRoot.findViewById(R.id.nick);
                TextView mood = (TextView) itemRoot.findViewById(R.id.mood);

                NearbyFriendData friendData = mNearbyFriendDataList.get(position);

                //
                Uri portraitUri = friendData.portraitUri;
                Picasso.with(null).load(portraitUri).fit().centerCrop().error(R.drawable.ic_account_circle_light_gray_48dp).into(portrait);
                nick.setText(friendData.nick);
                mood.setText(friendData.mood);
            }

            @Override
            public int getItemCount() {
                return mNearbyFriendDataList.size();
            }
        });
        mFabClean.setOnClickListener(this);
        mFabSearch.setOnClickListener(this);
        mFabSearch.animate().setListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mFabSearch.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (mIsSearchingAnimate) {
                    mFabSearch.animate().rotationYBy(180).setInterpolator(new OvershootInterpolator()).setStartDelay(500).setDuration(1000);
                } else {
                    mFabSearch.setEnabled(true);
                }
            }
        });

        mHostActivity = (MainActivity) getActivity();
        mPresenter = new MainFragmentPresenter(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_search:
                mPresenter.onSearch();
                break;

            case R.id.fab_clean:
                mPresenter.onCleanNearbyFriendList();
                break;

            default:
                break;
        }
    }

    // Function
    public void showSearching(boolean start) {
        mIsSearchingAnimate = start;
        if (!start) {
            return;
        }

        mFabSearch.animate().rotationYBy(180).setInterpolator(new OvershootInterpolator()).setDuration(1000);
    }

    public void setNearbyFriend(List<NearbyFriendData> nearbyFriendDataList) {
        mNearbyFriendDataList = nearbyFriendDataList;
        mSvFriendList.getAdapter().notifyDataSetChanged();
    }

    public void showCleanBtn(boolean show) {
        if (show) {
            mFabClean.animate()
                    .translationX(-DensityTransformer.dp2px(mHostActivity, 100))
                    .rotation(-720)
                    .setInterpolator(new OvershootInterpolator()).setDuration(500)
                    .withStartAction(new Runnable() {
                        @Override
                        public void run() {
                            mFabClean.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            mFabClean.animate()
                    .translationX(0)
                    .rotation(0)
                    .setInterpolator(new DecelerateInterpolator()).setDuration(100)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mFabClean.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    //
    public static class NearbyFriendData extends FriendFragment.FriendData {
        public NearbyFriendData() {
        }

        public NearbyFriendData(Uri portraitUri, String nick, String mood, long userId) {
            super(portraitUri, nick, mood, userId);
        }
    }
}
