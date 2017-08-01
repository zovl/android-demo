package zovlzhongguanhua.demo.systembar;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
    }

    public void AppCompact_Title(View view) {
        startActivity(AppCompact_TitleActivity.class);
    }

    public void AppCompact_NoTitle(View view) {
        startActivity(AppCompact_NoTitleActivity.class);
    }

    public void Fragment_Title(View view) {
        startActivity(Fragment_TitleActivity.class);
    }

    public void Fragment_NoTitle(View view) {
        startActivity(Fragment_NoTitleActivity.class);
    }
}
