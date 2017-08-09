package zovlzhongguanhua.api.application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends BaseActivity {

    private int startIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: " + getApplication());
        Log.d(TAG, "onCreate: " + getApplicationContext());

        for (int i = 0; i < 10; i++) {
            Intent intent = new Intent();
            intent.setClass(this, SecondActivity.class);
            intent.putExtra("startIndex", ++startIndex);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: " + getApplication());
        Log.d(TAG, "onResume: " + getApplicationContext());
    }
}
