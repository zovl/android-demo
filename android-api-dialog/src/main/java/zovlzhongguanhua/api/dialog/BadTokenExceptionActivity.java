package zovlzhongguanhua.api.dialog;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * android.view.WindowManager$BadTokenException:
 * Unable to add window -- token android.os.BinderProxy@11303740 is not valid; is your activity running?
 */
public class BadTokenExceptionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // except();
        solute();
    }

    private void except() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                new AlertDialog.Builder(mContext)
                        .setTitle("android.view.WindowManager$BadTokenException: ")
                        .setMessage("Unable to add window -- token null is not valid; is your activity running?")
                        .setCancelable(false)
                        .show();
            }
        }, 5000);

        finish();
    }

    private void solute() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(getParent())
                        .setTitle("android.view.WindowManager$BadTokenException: ")
                        .setMessage("Unable to add window -- token null is not valid; is your activity running?")
                        .setCancelable(false)
                        .show();
            }
        }, 5000);

        finish();
    }
}
