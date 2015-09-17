package walfud.meetu;

import android.app.Application;
import android.content.Context;

import com.bugtags.library.Bugtags;

/**
 * Created by song on 2015/6/22.
 */
public class MeetUApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Bugtags.start("b27243a172339c9df358ab036868ec05", this, Bugtags.BTGInvocationEventBubble);

        sApplicationContext = this;
    }

    private static Context sApplicationContext;
    public static Context getContext() {
        return sApplicationContext;
    }
}
