package com.walfud.meetu.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.walfud.common.widget.RoundedImageView;
import com.walfud.common.widget.SelectView;
import com.walfud.meetu.R;
import com.walfud.meetu.database.User;
import com.walfud.meetu.presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walfud on 2015/9/24.
 */
public class FriendFragment extends Fragment {

    public static final String TAG = "FriendFragment";

    private LoginActivity mActivity;
    private MainActivityPresenter mPresenter;
    // Header
    private ImageView mPortrait;
    private TextView mNick;
    private TextView mMood;
    // List
    private SelectView mList;
    private List<User> mUserList;

    // Event
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        mPortrait = (ImageView) view.findViewById(R.id.portrait);
        mNick = (TextView) view.findViewById(R.id.nick);
        mMood = (TextView) view.findViewById(R.id.mood);
        mList = (SelectView) view.findViewById(R.id.rv_list);

        //
        mActivity = (LoginActivity) getActivity();
        mUserList = new ArrayList<>();
        mList.setLayoutManager(new LinearLayoutManager(mActivity));
        mList.setAdapter(new SelectView.Adapter<SelectView.ViewHolder>() {
            @Override
            public SelectView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(mActivity).inflate(R.layout.item_friend_list, viewGroup, false);
                return new SelectView.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(SelectView.ViewHolder viewHolder, final int i) {
                User user = mUserList.get(i);

                LinearLayout itemRoot = (LinearLayout) viewHolder.itemView;
                RoundedImageView portrait = (RoundedImageView) itemRoot.findViewById(R.id.portrait);
                TextView nick = (TextView) itemRoot.findViewById(R.id.nick);

                //
                portrait.setImageResource(R.drawable.portrait);
                nick.setText(user.getNick());
            }

            @Override
            public int getItemCount() {
                return mUserList.size();
            }
        });
        mList.setOnSelectListener(new SelectView.OnSelectListener() {
            @Override
            public void onSelect(View view, int position) {
                updateHeader(mUserList.get(position));
            }
        });

        return view;
    }

    // Function
    public void updateHeader(final User user) {
        final int duration = 200;
        // Fly out
        mPortrait.animate().translationX(-mPortrait.getWidth()).setStartDelay(0).setDuration(duration).setInterpolator(new AnticipateInterpolator());
        mNick.animate().translationX(-mNick.getWidth()).setStartDelay(50).setDuration(duration).setInterpolator(new AnticipateInterpolator());
        mMood.animate().translationX(-mMood.getWidth()).setStartDelay(100).setDuration(duration).setInterpolator(new AnticipateInterpolator());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Change value
                mPortrait.setImageResource(R.drawable.portrait);
                mNick.setText(user.getNick());
                mMood.setText(user.getMood());

                // Fly in
                mPortrait.animate().translationX(0).setStartDelay(0).setDuration(duration).setInterpolator(new DecelerateInterpolator());
                mNick.animate().translationX(0).setStartDelay(50).setDuration(duration).setInterpolator(new DecelerateInterpolator());
                mMood.animate().translationX(0).setStartDelay(100).setDuration(duration).setInterpolator(new DecelerateInterpolator());
            }
        }, 700);
    }
}
