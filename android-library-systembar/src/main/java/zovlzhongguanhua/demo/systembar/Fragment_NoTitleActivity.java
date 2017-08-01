package zovlzhongguanhua.demo.systembar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class Fragment_NoTitleActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        SystemBarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
    }
}
