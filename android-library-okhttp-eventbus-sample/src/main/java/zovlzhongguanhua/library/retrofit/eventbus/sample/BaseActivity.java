package zovlzhongguanhua.library.retrofit.eventbus.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;

class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    protected void startActivity(Class cls) {
        startActivity(new Intent(this, cls));
    }

    protected void showToast(Object event) {
        Toast.makeText(this, new Gson().toJson(event), Toast.LENGTH_LONG).show();
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
