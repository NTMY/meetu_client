package walfud.meetu.manager;

import android.content.Context;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import walfud.meetu.MeetUApplication;
import walfud.meetu.database.DaoMaster;
import walfud.meetu.database.DaoSession;
import walfud.meetu.database.Location;
import walfud.meetu.database.User;
import walfud.meetu.database.UserDao;

/**
 * Created by walfud on 2015/9/20.
 */
public class DbManager {

    public static final String TAG = "DbManager";
    private static final String DB_NAME = "meetu.sqlite3";
    private static DbManager sInstance;

    private Context mContext;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DbManager(Context context) {
        mContext = context;
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    // Function
    public void insert(User user) {
        mDaoSession.getUserDao().insert(user);
    }
    public void insert(Location location) {
        mDaoSession.getLocationDao().insert(location);
    }

    public void update(User user) {
        mDaoSession.getUserDao().update(user);
    }

    /**
     * Insert or update user by 'USER_ID'
     * @param user
     */
    public void insertOrUpdate(User user) {
        User dbUser = mDaoSession.getUserDao().queryBuilder().where(UserDao.Properties.UserId.eq(user.getUserId())).unique();
        if (dbUser == null) {
            // Insert
            insert(user);
        } else {
            // Update
            user.setId(dbUser.getId());
            update(user);
        }
    }

    public User getUser(long userId) {
        return mDaoSession.getUserDao().queryBuilder().where(UserDao.Properties.UserId.eq(userId)).list().get(0);
    }
    public List<User> getUserList() {
        return mDaoSession.getUserDao().queryBuilder().list();
    }

    // Helper
    public static DbManager getInstance() {
        if (sInstance == null) {
            sInstance = new DbManager(MeetUApplication.getContext());
        }

        return sInstance;
    }
}
