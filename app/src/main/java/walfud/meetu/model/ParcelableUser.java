package walfud.meetu.model;

import org.meetu.model.User;

/**
 * Created by walfud on 2015/8/7.
 */
@com.baoyz.pg.Parcelable
public class ParcelableUser extends User {
    public ParcelableUser() {
    }

    public ParcelableUser(User user) {
        super();
        super.copyFrom(user);
    }
}
