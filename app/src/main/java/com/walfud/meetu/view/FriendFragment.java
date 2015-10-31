package com.walfud.meetu.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.walfud.common.DensityTransformer;
import com.walfud.common.collection.CollectionUtil;
import com.walfud.common.widget.JumpBar;
import com.walfud.common.widget.SelectView;
import com.walfud.meetu.MeetUApplication;
import com.walfud.meetu.R;
import com.walfud.meetu.database.User;
import com.walfud.meetu.manager.UserManager;
import com.walfud.meetu.presenter.MainActivityPresenter;
import com.walfud.meetu.util.Transformer;

import org.meetu.client.handler.FriendHandler;
import org.meetu.client.handler.PortraitHandler;
import org.meetu.client.handler.UserHandler;
import org.meetu.client.listener.FriendGetMyFriendListListener;
import org.meetu.client.listener.PortraitUploadListener;
import org.meetu.client.listener.UserUpdateListener;
import org.meetu.dto.BaseDto;
import org.meetu.model.PortraitUploadModel;
import org.meetu.util.ListBean;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by walfud on 2015/9/24.
 */
public class FriendFragment extends Fragment {

    public static final String TAG = "FriendFragment";

    private MainActivity mActivity;
    private MainActivityPresenter mPresenter;
    private UserManager mUserManager;
    // Header
    private ProfileCardView mPcv;
    private ProfileCardView.OnEventListener mProfileCardEventListener;
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
        mUserManager = UserManager.getInstance();
        mFriendDataList = new ArrayList<>();
        // Get friend list
        new AsyncTask<User, Void, List<org.meetu.model.User>>() {

            private List<org.meetu.model.User> mServerUserList = new ArrayList<>();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                // TODO: show loading...
            }

            @Override
            protected List<org.meetu.model.User> doInBackground(User[] params) {
                final User user = params[0];
                new FriendHandler().onGetMyFriendList(new FriendGetMyFriendListListener() {
                    @Override
                    public void getMyFriendList(ListBean listBean) {
                        List<org.meetu.model.User> serverUserList = (List<org.meetu.model.User>) listBean.getList();

                        // Add friend
                        mServerUserList = serverUserList;

                    }
                }, Transformer.user2ServerUser(user));

                return mServerUserList;
            }

            @Override
            protected void onPostExecute(List<org.meetu.model.User> serverUserList) {
                super.onPostExecute(serverUserList);

                // Transform
                List<User> friendList = Transformer.serverUserList2UserList(serverUserList);

                // Save friend list
                mUserManager.setFriendList(friendList);

                // Add self on top and construct data for UI
                List<FriendData> friendDataList = new ArrayList<>();
                friendDataList.add(Transformer.user2FriendData(mUserManager.getCurrentUser()));
                friendDataList.addAll(Transformer.userList2FriendDataList(friendList));

                setFriendList(friendDataList);
            }
        }.execute(mUserManager.getCurrentUser());
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
                ImageView portrait = (ImageView) itemRoot.findViewById(R.id.portrait);
                TextView nick = (TextView) itemRoot.findViewById(R.id.nick);
                TextView mood = (TextView) itemRoot.findViewById(R.id.mood);

                //
                Uri portraitUri = friendData.portraitUri;
                Picasso.with(FriendFragment.this.mActivity).load(portraitUri).fit().centerCrop().error(R.drawable.ic_account_circle_light_gray_48dp).into(portrait);
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
                mPcv.update(Transformer.friendData2ProfileData(friendData));

                if (position == 0) {
                    // Current user, editable
                    mPcv.setOnEventListener(mProfileCardEventListener);
                } else {
                    mPcv.setOnEventListener(null);
                }
            }
        });
        mProfileCardEventListener = new ProfileCardView.OnEventListener() {
            @Override
            public void onPortraitChanged(Uri portraitUri) {
                new AsyncTask<Uri, Void, org.meetu.model.User>() {

                    private boolean mFailImgTooLarge = false;
                    private org.meetu.model.User mServerUser;

                    @Override
                    protected org.meetu.model.User doInBackground(Uri... params) {
                        Uri portraitUri = params[0];

                        // Get portrait file name & content
                        String portraitFileName = null;
                        byte[] portraitContent = null;
                        try {
                            // File name & size
                            long sizeInByte = 0;
                            Cursor cursor = MeetUApplication.getContext().getContentResolver().query(portraitUri, null, null, null, null);
                            if (cursor.moveToFirst()) {
                                int columnDisplayName = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
                                int columnSizeInByte = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE);
                                if (columnDisplayName != -1) {
                                    portraitFileName = cursor.getString(columnDisplayName);
                                }
                                if (columnSizeInByte != -1) {
                                    sizeInByte = Long.valueOf(cursor.getString(columnSizeInByte));
                                }
                            }
                            cursor.close();

                            // Content
                            byte[] tmpPortrait;
                            {
                                InputStream inputStream = MeetUApplication.getContext().getContentResolver().openInputStream(portraitUri);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                {
                                    byte[] buf = new byte[100 * 1024];
                                    int readLen;
                                    while ((readLen = inputStream.read(buf)) != -1) {
                                        byteArrayOutputStream.write(buf, 0, readLen);
                                    }
                                }
                                tmpPortrait = byteArrayOutputStream.toByteArray();
                                byteArrayOutputStream.close();
                                inputStream.close();
                            }

                            final long MAX_SIZE = 100 * 1024;
                            if (tmpPortrait.length < MAX_SIZE) {
                                // File be small enough
                                portraitContent = tmpPortrait;
                            } else {
                                // If file too large, compress it
                                BitmapFactory.Options boundOptions = new BitmapFactory.Options();
                                boundOptions.inJustDecodeBounds = true;
                                BitmapFactory.decodeByteArray(tmpPortrait, 0, tmpPortrait.length, boundOptions);
                                double scale = Math.max(boundOptions.outWidth, boundOptions.outHeight) / DensityTransformer.dp2px(MeetUApplication.getContext(), 56);
                                boundOptions.inJustDecodeBounds = false;
                                boundOptions.inSampleSize = (int) scale;
                                Bitmap bitmap = BitmapFactory.decodeByteArray(tmpPortrait, 0, tmpPortrait.length, boundOptions);
                                ByteArrayOutputStream portraitOutputStream = new ByteArrayOutputStream();
                                for (int i = 100; i > 0; i -= 20) {
                                    portraitOutputStream.reset();
                                    bitmap.compress(Bitmap.CompressFormat.WEBP, i, portraitOutputStream);

                                    if (portraitOutputStream.size() < MAX_SIZE) {
                                        portraitFileName = portraitFileName.replaceFirst("\\..*$", ".webp");
                                        portraitContent = portraitOutputStream.toByteArray();
                                        break;
                                    }
                                }
                                portraitOutputStream.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // If something wrong, stop upload procedure
                        if (TextUtils.isEmpty(portraitFileName) || portraitContent == null) {
                            mFailImgTooLarge = true;
                            return null;
                        }

                        // Transform to upload data
                        PortraitUploadModel portraitUploadModel = new PortraitUploadModel();
                        portraitUploadModel.setUserId(String.valueOf(mUserManager.getCurrentUser().getUserId()));
                        portraitUploadModel.setFileName(portraitFileName);
                        portraitUploadModel.setFileBytes(portraitContent);

                        new PortraitHandler().onUpload(new PortraitUploadListener() {
                            @Override
                            public void upload(org.meetu.model.User user) {
                                mServerUser = user;
                            }
                        }, portraitUploadModel);

                        return mServerUser;
                    }

                    @Override
                    protected void onPostExecute(org.meetu.model.User serverUser) {
                        super.onPostExecute(serverUser);

                        boolean suc = false;

                        if (mFailImgTooLarge) {
                            // Client error
                        } else {
                            if (TextUtils.isEmpty(serverUser.getErrCode())) {
                                // Update user info
                                String portraitUrl = serverUser.getImgUrlReal();

                                mUserManager.getCurrentUser().setPortraitUrl(portraitUrl);

                                suc = true;

                                // Invalid cache
                                Picasso.with(FriendFragment.this.mActivity).invalidate(portraitUrl);
                            }
                        }

                        FriendFragment.this.showUpdateResult(suc, "Portrait");
                    }
                }.execute(portraitUri);
            }

            @Override
            public void onNickChanged(String newNick) {
                mUserManager.getCurrentUser().setNick(newNick);

                updateServerUserInfo(mUserManager.getCurrentUser(), "Nick");
            }

            @Override
            public void onMoodChanged(String newMood) {
                mUserManager.getCurrentUser().setMood(newMood);

                updateServerUserInfo(mUserManager.getCurrentUser(), "Mood");
            }
        };
        mPcv.setStartActivityForResultHost(this);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mPcv.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    // Function
    public void setFriendList(List<FriendData> friendList) {
        if (CollectionUtil.isEmpty(friendList)) {
            mSv.removeAllViews();
            return;
        }

        // Set default profile card
        mPcv.set(friendList.get(0));
        mPcv.setOnEventListener(mProfileCardEventListener);

        // Set list
        mFriendDataList = friendList;
        mSv.getAdapter().notifyDataSetChanged();
    }

    public void checkAndShowGuide() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPcv.checkAndShowGuide();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkAndShowGuide();
                    }
                }, 1800);
            }
        }, 2000);
    }

    //
    public static class FriendData extends ProfileCardView.ProfileData {

    }

    // Internal

    /**
     * @param user
     * @param field the field which is modified
     */
    private void updateServerUserInfo(User user, final String field) {
        new AsyncTask<org.meetu.model.User, Void, BaseDto>() {
            private BaseDto mResult;

            @Override
            protected BaseDto doInBackground(org.meetu.model.User... params) {
                org.meetu.model.User serverUser = params[0];

                new UserHandler().onUpdate(new UserUpdateListener() {
                    @Override
                    public void update(BaseDto baseDto) {
                        mResult = baseDto;
                    }
                }, serverUser);

                return mResult;
            }

            @Override
            protected void onPostExecute(BaseDto baseDto) {
                super.onPostExecute(baseDto);

                boolean suc = false;
                if (TextUtils.isEmpty(baseDto.getErrCode())) {
                    suc = true;
                }

                FriendFragment.this.showUpdateResult(suc, field);
            }
        }.execute(Transformer.user2ServerUser(user));
    }

    private void showUpdateResult(boolean suc, String field) {
        // Update ProfileCard
        mPcv.set(Transformer.user2ProfileData(mActivity, mUserManager.getCurrentUser()));

        // Update list
        mFriendDataList.set(0, Transformer.user2FriendData(mUserManager.getCurrentUser()));
        mSv.getAdapter().notifyDataSetChanged();

        // Show tip
        String tip;
        if (suc) {
            tip = String.format("%s upload success", field);
        } else {
            tip = String.format("%s upload fail", field);
        }
        Snackbar.make(mPcv, tip, Snackbar.LENGTH_SHORT).show();
    }
}
