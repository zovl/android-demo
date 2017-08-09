package zovlzhongguanhua.api.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    protected void startActivity(Class cls) {
        startActivity(new Intent(this, cls));
    }

    protected void showToast(Object msg) {
        Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show();
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
