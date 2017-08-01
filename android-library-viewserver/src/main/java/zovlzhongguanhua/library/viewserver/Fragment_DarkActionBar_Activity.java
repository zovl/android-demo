package zovlzhongguanhua.library.viewserver;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.debug.hv.ViewServer;

public class Fragment_DarkActionBar_Activity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewServer.get(this).addWindow(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }
}
