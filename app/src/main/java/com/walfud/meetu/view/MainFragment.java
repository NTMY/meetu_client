package com.walfud.meetu.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;

import com.walfud.common.widget.SelectView;
import com.walfud.meetu.R;
import com.walfud.meetu.presenter.MainFragmentPresenter;

import org.meetu.model.LocationCurr;

import java.util.List;

/**
 * Created by walfud on 2015/11/6.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "MainFragment";

    private MainActivity mHostActivity;
    private MainFragmentPresenter mPresenter;

    private SelectView mSvFriendList;
    private FloatingActionButton mFabSearch;

    /**
     * Whether need searching animation
     */
    private boolean mIsSearchingAnimate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        RelativeLayout root = (RelativeLayout) inflater.inflate(R.layout.fragment_main, container, false);
        mSvFriendList = (SelectView) root.findViewById(R.id.sv_friend_list);
        mFabSearch = (FloatingActionButton) root.findViewById(R.id.fab_search);

        //
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
        mIsSearchingAnimate = false;

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

    public void showSearchResult(List<LocationCurr> locationList) {
    }
}
