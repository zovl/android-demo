package zovlzhongguanhua.demo.systembar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AppCompact_NoTitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        SystemBarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
    }
}
