package walfud.meetu;

import android.app.Service;
import android.os.Binder;

/**
 * Created by song on 2015/6/24.
 */
public class ServiceBinder<T extends Service> extends Binder {
    private final T mService;

    public ServiceBinder(T service) {
        this.mService = service;
    }

    public T getService() {
        return mService;
    }
}