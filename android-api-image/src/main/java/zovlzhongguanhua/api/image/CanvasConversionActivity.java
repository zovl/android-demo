package zovlzhongguanhua.api.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@TargetApi(17)
public class CanvasConversionActivity extends BaseActivity {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private TextView textView;
    private SeekBar translateXView, translateYView;

    private Context context;
    private Resources resources;
    private DisplayMetrics metrics;
    private Bitmap bitmap;
    private Bitmap.Config config;
    private AtomicInteger translateX = new AtomicInteger(0);
    private AtomicInteger translateY = new AtomicInteger(0);

    private AtomicBoolean flag = new AtomicBoolean(true);
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (flag.get()) {
                drawBitmap();
            }
        }
    });
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag.set(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvasconversion);

        context = this;
        resources = getResources();

        Window window = getWindow();
        WindowManager manager = window.getWindowManager();
        Display display = manager.getDefaultDisplay();
        metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        config = Bitmap.Config.ARGB_8888;
        InputStream in = resources.openRawResource(R.raw.image_02);
        bitmap = BitmapFactory.decodeStream(in);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        textView = (TextView) findViewById(R.id.textView);
        translateXView = (SeekBar) findViewById(R.id.translateXView);
        translateYView = (SeekBar) findViewById(R.id.translateYView);

        surfaceView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

            }
        });

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                surfaceHolder = holder;
                resetController();
                thread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                surfaceHolder = holder;
                resetController();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                surfaceHolder = holder;
            }
        });
    }

    private void resetController() {
        int w0 = surfaceView.getWidth();
        int h0 = surfaceView.getHeight();
        int w1 = bitmap.getWidth();
        int h1 = bitmap.getHeight();
        translateXView.setMax(w0 - w1);
        translateXView.setProgress(translateX.get() + (w0 - w1)/2);
        translateYView.setMax(h0 - h1);
        translateXView.setProgress(translateY.get() + (h0 - h1)/2);

        translateXView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int w0 = surfaceView.getWidth();
                int w1 = bitmap.getWidth();
                translateX.set(progress - (w0 - w1)/2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        translateYView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int h0 = surfaceView.getHeight();
                int h1 = bitmap.getHeight();
                translateY.set(progress - (h0 - h1)/2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void drawBitmap() {
        if (surfaceHolder == null) return;
        int w0 = surfaceView.getWidth();
        int h0 = surfaceView.getHeight();
        int w1 = bitmap.getWidth();
        int h1 = bitmap.getHeight();
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas == null) return;
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.translate(translateX.get() + (w0 - w1)/2, translateY.get() + (h0 - h1)/2);
        canvas.drawBitmap(bitmap, 0, 0, null);
        if (surfaceHolder != null && canvas != null) {
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                showParams();
            }
        });
    }

    private void showParams() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("<font color=\'#00b4ff\'>Canvas: </font>");
        buffer.append("<br>" + "translateX: " + translateX);
        buffer.append("<br>" + "translateY: " + translateY);

        textView.setText(Html.fromHtml(buffer.toString()));
    }
}
