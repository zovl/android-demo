package zovlzhongguanhua.api.collection;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.util.SparseArray;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SparseArray_InstrumentedTest {

    private final String TAG = this.getClass().getSimpleName();

    private SparseArray<Object> sparseArray = new SparseArray<>();
    private int size = 1000 * 1000;
    private int writeSize = 1000 * 1000;

    {

        for (int i = 0; i < size; i++) {
            sparseArray.put(i, size);
        }
    }

    @Test
    public void sparseArray_read() throws Exception {

        for (int i = 0; i < 3; i++) {
            final int index = i + 1;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName("Thread-read-" + index);
                    for (int i = 0; i < sparseArray.size(); i++) {
                        sparseArray.get(i);
                        Log.d(TAG, Thread.currentThread().getName() + ": " + i);
                    }
                }
            }).start();
        }
    }

    @Test
    public void sparseArray_write() throws Exception {

        for (int i = 0; i < 3; i++) {
            final int index = i + 1;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName("Thread-write-" + index);
                    for (int i = 0; i < writeSize; i++) {
                        sparseArray.put(sparseArray.size(), sparseArray.size() + 1);
                        Log.d(TAG, Thread.currentThread().getName() + ": " + sparseArray.get(sparseArray.size() - 1));
                    }
                }
            }).start();
        }
    }

    @Test
    public void sparseArray_delete() throws Exception {

        for (int i = 0; i < 3; i++) {
            final int index = i + 1;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName("Thread-delete-" + index);
                    Log.d(TAG, Thread.currentThread().getName() + ": start...");
                    while (sparseArray.size() > 0) {
                        sparseArray.delete(0);
                    }
                    Log.d(TAG, Thread.currentThread().getName() + ": " + sparseArray.size());
                }
            }).start();
        }
    }
}
