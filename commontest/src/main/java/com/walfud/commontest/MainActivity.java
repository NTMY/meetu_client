package com.walfud.commontest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private ImageView mIv;
    private ImageView mIv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mIv = (ImageView) findViewById(R.id.iv);
//        mIv2 = (ImageView) findViewById(R.id.iv2);
//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pure);
//        mIv.setImageBitmap(bitmap);
//        mIv2.setImageBitmap(new BitmapTransformer(bitmap).circle().get());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
