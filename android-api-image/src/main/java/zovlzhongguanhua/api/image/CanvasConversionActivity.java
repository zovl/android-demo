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

@TargetApi(17)
public class CanvasConversionActivity extends BaseActivity {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private TextView textView;
    private SeekBar rotateView;
    private SeekBar translateXView, translateYView;
    private SeekBar scaleXView, scaleYView;
    private SeekBar skewXView, skewYView;

    private Context context;
    private Resources resources;
    private DisplayMetrics metrics;
    private Bitmap bitmap;
    private Bitmap.Config config;
    private int rotate = 0;
    private int translateX = 0;
    private int translateY = 0;
    private float scaleX = 1f;
    private float scaleY = 1f;
    private float scaleTimes = 2f;
    private float skewX = 0;
    private float skewY = 0;
    private float skewTimes = 10f;

    private AtomicBoolean flag = new AtomicBoolean(true);
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (flag.get()) {
                drawBitmap();
            }
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
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
        rotateView = (SeekBar) findViewById(R.id.rotateView);
        translateXView = (SeekBar) findViewById(R.id.translateXView);
        translateYView = (SeekBar) findViewById(R.id.translateYView);
        scaleXView = (SeekBar) findViewById(R.id.scaleXView);
        scaleYView = (SeekBar) findViewById(R.id.scaleYView);
        skewXView = (SeekBar) findViewById(R.id.skewXView);
        skewYView = (SeekBar) findViewById(R.id.skewYView);

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

        rotateView.setMax(360);
        rotateView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rotate = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        translateXView.setMax(w0 - w1);
        translateXView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int w0 = surfaceView.getWidth();
                int w1 = bitmap.getWidth();
                translateX = progress - (w0 - w1)/2;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        translateYView.setMax(h0 - h1);
        translateYView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int h0 = surfaceView.getHeight();
                int h1 = bitmap.getHeight();
                translateY = progress - (h0 - h1)/2;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        scaleXView.setMax((int) (100f * scaleTimes));
        scaleXView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                scaleX = ((float) progress)/100f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        scaleYView.setMax((int) (100f * scaleTimes));
        scaleYView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                scaleY = ((float) progress)/100f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        skewXView.setMax((int) (100f * skewTimes));
        skewXView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                skewX = ((float) progress)/100f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        skewYView.setMax((int) (100f * skewTimes));
        skewYView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                skewY = ((float) progress)/100f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        rotateView.setProgress(rotate);
        translateXView.setProgress(translateX + (w0 - w1)/2);
        translateYView.setProgress(translateY + (h0 - h1)/2);
        scaleXView.setProgress((int) (scaleX * 100f));
        scaleYView.setProgress((int) (scaleY * 100f));
        skewXView.setProgress((int) (skewX * 100f));
        skewYView.setProgress((int) (skewY * 100f));
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
        {
            int dx = translateX + (w0 - w1)/2;
            int dy  = translateY + (h0 - h1)/2;
            canvas.translate(dx, dy);
        }
        {
            int px = w1/2;
            int py = h1/2;
            canvas.rotate(rotate, px, py);
        }
        {
            int px = w1/2;
            int py = h1/2;
            canvas.scale(scaleX, scaleY, px, py);
        }
        {
            canvas.skew(skewX, skewY);
        }
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
        buffer.append("<br>" + "rotate: " + rotate);
        buffer.append("<br>" + "translateX: " + translateX);
        buffer.append("<br>" + "translateY: " + translateY);
        buffer.append("<br>" + "scaleX: " + scaleX);
        buffer.append("<br>" + "scaleY: " + scaleY);
        buffer.append("<br>" + "skewX: " + skewX);
        buffer.append("<br>" + "skewY: " + skewY);

        textView.setText(Html.fromHtml(buffer.toString()));
    }
}
