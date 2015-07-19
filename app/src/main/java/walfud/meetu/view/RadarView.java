package walfud.meetu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import walfud.meetu.R;

/**
 * Created by song on 2015/7/18.
 */
public class RadarView extends View {

    public static final String TAG = "RadarView";

    private Context mContext;

    private Bitmap mBitmap;
    private Paint mPaint;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.radar);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw radar image
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

        // Driver
        if (isRunning) {
            postInvalidateDelayed(20);
        }
    }

    private boolean isRunning = false;
    public void start() {
        isRunning = true;
    }
    public void stop() {
        isRunning = false;
    }
}
