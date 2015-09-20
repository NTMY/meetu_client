package walfud.meetu.manager;

import android.content.Context;

import walfud.meetu.MeetUApplication;
import walfud.meetu.database.User;

/**
 * Created by walfud on 2015/9/20.
 */
public class UserManager {

    public static final String TAG = "UserManager";
    private static UserManager sInstance;

    private Context mContext;
    private PrefsManager mPrefsManager;
    private DbManager mDbManager;

    private UserManager(Context context) {
        mContext = context;
        mPrefsManager = PrefsManager.getInstance();
        mDbManager = DbManager.getInstance();
    }

    // Function
    public User getCurrentUser() {
        long userId = PrefsManager.getInstance().getCurrentUserId();
        return DbManager.getInstance().getUser(userId);
    }

    public void setCurrentUser(User user) {
        mPrefsManager.setCurrentUserId(user.getUserId());
        mDbManager.insertOrUpdate(user);
    }

    // Helper
    public static UserManager getInstance() {
        if (sInstance == null) {
            sInstance = new UserManager(MeetUApplication.getContext());
        }

        return sInstance;
    }
}
