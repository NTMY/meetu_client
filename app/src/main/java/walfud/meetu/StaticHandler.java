package walfud.meetu;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by song on 2015/7/26.
 */
public class StaticHandler<T extends StaticHandler.OnHandleMessage> extends Handler {

    public interface OnHandleMessage {
        void handleMessage(Message msg);
    }

    private WeakReference mOuterRef;
    public StaticHandler(T outerRef) {
        mOuterRef = new WeakReference(outerRef);
    }

    @Override
    public void handleMessage(Message msg) {
        T outerRef = (T) mOuterRef.get();

        if (outerRef != null) {
            outerRef.handleMessage(msg);
        }
    }
}
