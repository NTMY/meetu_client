package com.walfud.meetu.view;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.walfud.common.widget.JumpBar;
import com.walfud.common.widget.SelectView;
import com.walfud.meetu.R;
import com.walfud.meetu.database.User;
import com.walfud.meetu.manager.UserManager;
import com.walfud.meetu.presenter.MainActivityPresenter;
import com.walfud.meetu.util.Transformer;

import org.meetu.client.handler.FriendHandler;
import org.meetu.client.listener.FriendGetMyFriendListListener;
import org.meetu.util.ListBean;

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
        mSv = (SelectView) view.findViewById(R.id.sv_list);

        //
        mActivity = (MainActivity) getActivity();
        mFriendDataList = new ArrayList<>();
        // Get friend list
        new AsyncTask<User, Void, List<FriendData>>() {

            private List<FriendFragment.FriendData> mFriendList;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                // TODO: show loading...
            }

            @Override
            protected List<FriendFragment.FriendData> doInBackground(com.walfud.meetu.database.User[] params) {
                final com.walfud.meetu.database.User user = params[0];
                new FriendHandler().onGetMyFriendList(new FriendGetMyFriendListListener() {
                    @Override
                    public void getMyFriendList(ListBean listBean) {
                        List<org.meetu.model.User> userList = (List<org.meetu.model.User>) listBean.getList();
                        mFriendList = Transformer.userList2FriendDataList(userList);

                    }
                }, Transformer.user2User(user));

                return mFriendList;
            }

            @Override
            protected void onPostExecute(List<FriendFragment.FriendData> friendList) {
                super.onPostExecute(friendList);

                setFriendList(friendList);
            }
        }.execute(UserManager.getInstance().getCurrentUser());
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
                SimpleDraweeView portrait = (SimpleDraweeView) itemRoot.findViewById(R.id.portrait);
                TextView nick = (TextView) itemRoot.findViewById(R.id.nick);
                TextView mood = (TextView) itemRoot.findViewById(R.id.mood);

                //
                if (friendData.portraitUri != null) {
                    portrait.setImageURI(Uri.parse(friendData.portraitUri));
                }
                nick.setText(friendData.nick);
                mood.setText(friendData.mood);
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
