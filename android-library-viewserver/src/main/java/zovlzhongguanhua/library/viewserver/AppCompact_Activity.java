package zovlzhongguanhua.library.viewserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.debug.hv.ViewServer;

public class AppCompact_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewServer.get(this).addWindow(this);

        startActivity(new Intent(this, Fragment_DarkActionBar_Activity.class));
        startActivity(new Intent(this, Fragment_NoActionBar_Activity.class));
        startActivity(new Intent(this, Fragment_Dialog_Activity.class));
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
