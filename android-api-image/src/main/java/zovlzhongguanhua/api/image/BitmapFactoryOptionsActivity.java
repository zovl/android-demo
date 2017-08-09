package zovlzhongguanhua.api.image;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class BitmapFactoryOptionsActivity extends BaseActivity {

    private ImageView imageView;
    private TextView textView;
    private SeekBar inSampleSizeView;
    private Switch inScaledView;
    private SeekBar inDensityView;
    private TextView inPreferredConfigView;

    private Context context;
    private Resources resources;
    private DisplayMetrics metrics;
    private int densityTimes = 3;
    private InputStream in;
    private Bitmap bitmap;
    private BitmapFactory.Options options;
    private String inPreferredConfig;
    private int inSampleSize;
    private boolean inScaled;
    private int inDensity;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmapfactoryoptions);

        context = this;
        resources = getResources();
        options = new BitmapFactory.Options();

        Window window = getWindow();
        WindowManager manager = window.getWindowManager();
        Display display = manager.getDefaultDisplay();
        metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        inPreferredConfig = Bitmap.Config.ARGB_8888.toString();
        inSampleSize = 1;
        inScaled = true;
        inDensity = metrics.densityDpi;

        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        inSampleSizeView = (SeekBar) findViewById(R.id.inSampleSizeView);
        inScaledView = (Switch) findViewById(R.id.inScaledView);
        inDensityView = (SeekBar) findViewById(R.id.inDensityView);
        inPreferredConfigView = (TextView) findViewById(R.id.inPreferredConfigView);

        inSampleSizeView.setMax((int) Math.ceil(metrics.density) * densityTimes);
        inSampleSizeView.setProgress(inSampleSize);
        inSampleSizeView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                inSampleSize = seekBar.getProgress();
                if (inSampleSize == 0) {
                    inSampleSize = 1;
                }
                setBitmap();
            }
        });

        inScaledView.setChecked(inScaled);
        inScaledView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                inScaled = isChecked;
                setBitmap();
            }
        });

        inDensityView.setMax(metrics.densityDpi);
        inDensityView.setProgress(inDensity);
        inDensityView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                inDensity = seekBar.getProgress();
                if (inDensity == 0) {
                    inDensity = 1;
                }
                setBitmap();
            }
        });

        inPreferredConfigView.setText(inPreferredConfig);
        inPreferredConfigView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion = 0;
                final Bitmap.Config[] configs = Bitmap.Config.values();
                final CharSequence[] items = new CharSequence[configs.length];
                for (int i = 0; i < configs.length; i++) {
                    items[i] = configs[i].toString();
                    if (inPreferredConfig.equals(configs[i].toString())) {
                        postion = i;
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Please select inPreferredConfig!");
                builder.setSingleChoiceItems(items, postion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        inPreferredConfig = items[which].toString();
                        inPreferredConfigView.setText(inPreferredConfig);
                        setBitmap();
                    }
                });
                builder.show();
            }
        });

        setBitmap();
    }

    private void setBitmap() {
        options.inSampleSize = inSampleSize;
        options.inScaled = inScaled;
        options.inDensity = inDensity;
        options.inPreferredConfig = Bitmap.Config.valueOf(inPreferredConfig);


        in = resources.openRawResource(R.raw.image_01);
        bitmap = BitmapFactory.decodeStream(in, null, options);
        imageView.setImageBitmap(bitmap);
        /*
        BitmapDrawable drawable = new BitmapDrawable(resources, bitmap);
        imageView.setImageDrawable(drawable);*/

        showParams();
    }

    private void showParams() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("<font color=\'#00b4ff\'>Metrics: </font>");
        buffer.append("<br>" + "density: " + metrics.density);
        buffer.append("<br>" + "densityDpi: " + metrics.densityDpi);
        buffer.append("<br>" + "widthPixels * heightPixels: " + metrics.widthPixels + "*" + metrics.heightPixels);

        buffer.append("<br>");
        buffer.append("<br>" + "<font color=\'#00b4ff\'>Bitmap.Options: </font>");
        buffer.append("<br>" + "inSampleSize: " + options.inSampleSize);
        buffer.append("<br>" + "inPreferredConfig: " + options.inPreferredConfig);
        buffer.append("<br>" + "inDensity: " + options.inDensity);
        buffer.append("<br>" + "outMimeType: " + options.outMimeType);
        buffer.append("<br>" + "inJustDecodeBounds: " + options.inJustDecodeBounds);
        buffer.append("<br>" + "inMutable: " + options.inMutable);
        buffer.append("<br>" + "inPremultiplied: " + options.inPremultiplied);
        buffer.append("<br>" + "inScaled: " + options.inScaled);
        buffer.append("<br>" + "inScreenDensity: " + options.inScreenDensity);
        buffer.append("<br>" + "inTargetDensity: " + options.inTargetDensity);
        buffer.append("<br>" + "outWidth * outHeight: " + options.outWidth + "*" + options.outHeight);

        buffer.append("<br>");
        buffer.append("<br>" + "<font color=\'#00b4ff\'>Bitmap: </font>");
        buffer.append("<br>" + "byteCount: " + bitmap.getByteCount());
        buffer.append("<br>" + "allocationByteCount: " + bitmap.getAllocationByteCount());
        buffer.append("<br>" + "config: " + bitmap.getConfig());
        buffer.append("<br>" + "density: " + bitmap.getDensity());
        buffer.append("<br>" + "rowBytes: " + bitmap.getRowBytes());
        buffer.append("<br>" + "width * height: " + bitmap.getWidth() + "*" + bitmap.getHeight());

        buffer.append("<br>");
        buffer.append("<br>" + "<font color=\'#00b4ff\'>ImageView: </font>");
        buffer.append("<br>" + "width * height: " + imageView.getDrawable().getBounds().width() + "*" +
                imageView.getDrawable().getBounds().height());

        textView.setText(Html.fromHtml(buffer.toString()));
    }
}
