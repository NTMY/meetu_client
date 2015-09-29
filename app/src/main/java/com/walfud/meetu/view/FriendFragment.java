package com.walfud.meetu.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.walfud.common.widget.JumpBar;
import com.walfud.common.widget.RoundedImageView;
import com.walfud.common.widget.SelectView;
import com.walfud.meetu.R;
import com.walfud.meetu.presenter.MainActivityPresenter;
import com.walfud.meetu.util.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walfud on 2015/9/24.
 */
public class FriendFragment extends Fragment {

    public static final String TAG = "FriendFragment";

    private MainActivity mActivity;
    private MainActivityPresenter mPresenter;
    // Header
    private ProfileCardView mPcv;
    // List
    private SelectView mSv;
    private JumpBar mJb;
    private List<FriendData> mFriendDataList;

    // Event
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        mPcv = (ProfileCardView) view.findViewById(R.id.pcv);
        mJb = (JumpBar) view.findViewById(R.id.jb);
        mSv = (SelectView) view.findViewById(R.id.rv_list);

        //
        mActivity = (MainActivity) getActivity();
        mFriendDataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            FriendData friendData = new FriendData();
            friendData.nick = "" + i;
            mFriendDataList.add(friendData);
        }
        mJb.setOnJumpListener(new JumpBar.OnJumpListener() {
            @Override
            public boolean onJump(View view, int index, int totalIndex) {
                mSv.smoothScrollToPosition(mFriendDataList.size() * index / totalIndex);

                return false;
            }
        });
        mSv.setLayoutManager(new LinearLayoutManager(mActivity));
        mSv.setAdapter(new SelectView.Adapter<SelectView.ViewHolder>() {
            @Override
            public SelectView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(mActivity).inflate(R.layout.item_friend_list, viewGroup, false);
                return new SelectView.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(SelectView.ViewHolder viewHolder, final int i) {
                FriendData friendData = mFriendDataList.get(i);

                LinearLayout itemRoot = (LinearLayout) viewHolder.itemView;
                RoundedImageView portrait = (RoundedImageView) itemRoot.findViewById(R.id.portrait);
                TextView nick = (TextView) itemRoot.findViewById(R.id.nick);

                //
                portrait.setImageBitmap(friendData.portrait);
                nick.setText(friendData.nick);
            }

            @Override
            public int getItemCount() {
                return mFriendDataList.size();
            }
        });
        mSv.setOnSelectListener(new SelectView.OnSelectListener() {
            @Override
            public void onSelect(View view, int position) {
                FriendData friendData = mFriendDataList.get(position);
                mPcv.set(Transformer.friendData2ProfileData(friendData));
            }
        });

        return view;
    }

    // Function
    public void setFriendList(List<FriendData> friendList) {
        mFriendDataList = friendList;
        mSv.getAdapter().notifyDataSetChanged();
    }

    //
    public static class FriendData extends ProfileCardView.ProfileData {

    }
}
