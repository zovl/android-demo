package zovlzhongguanhua.api.application;

import android.os.Bundle;
import android.util.Log;

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Log.d(TAG, "onCreate: " + getApplication());
        Log.d(TAG, "onCreate: " + getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: " + getApplication());
        Log.d(TAG, "onResume: " + getApplicationContext());
    }
}
