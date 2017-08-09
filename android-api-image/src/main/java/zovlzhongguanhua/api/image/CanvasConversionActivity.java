package zovlzhongguanhua.api.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.InputStream;

@TargetApi(17)
public class CanvasConversionActivity extends BaseActivity {

    private ImageView imageView;
    private TextView textView;
    private SeekBar translateXView, translateYView;

    private Context context;
    private Resources resources;
    private DisplayMetrics metrics;
    private Bitmap bitmap;
    private Bitmap.Config config;
    private Canvas canvas;
    private Paint paint;
    private int translateX;
    private int translateY;

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

        translateX = 0;
        translateY = 0;

        config = Bitmap.Config.ARGB_8888;
        InputStream in = resources.openRawResource(R.raw.image_02);
        bitmap = BitmapFactory.decodeStream(in);
        canvas = new Canvas();
        paint = new Paint();

        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        translateXView = (SeekBar) findViewById(R.id.translateXView);
        translateYView = (SeekBar) findViewById(R.id.translateYView);

        translateXView.setMax(metrics.widthPixels);
        translateXView.setProgress(0);
        translateXView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                translateX = progress;
                drawBitmap();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        translateYView.setMax(metrics.heightPixels);
        translateYView.setProgress(0);
        translateYView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                translateY = progress;
                drawBitmap();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        imageView.setImageBitmap(bitmap);
        drawBitmap();
    }

    private void drawBitmap() {
        canvas.save();
        canvas.translate(translateX, translateY);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.restore();

        imageView.invalidate();
        showParams();
    }

    private void showParams() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("<font color=\'#00b4ff\'>Canvas: </font>");
        buffer.append("<br>" + "translateX: " + translateX);
        buffer.append("<br>" + "translateY: " + translateY);

        textView.setText(Html.fromHtml(buffer.toString()));
    }
}
