package com.aomygod.retrofit.eventbus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 保存工具类
 */
class FileUtil {

    /**
     * 取出文件
     */
    public static InputStream getFile(String path) {
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null)
            return null;
        if (!file.exists())
            return null;
        if (!file.isFile())
            return null;
        return restoreStream(file.getPath());
    }

    /**
     * 保存文件
     */
    public static boolean saveFile(String path, InputStream is) {
        if (is == null)
            return false;
        File file = new File(path);
        if (file == null)
            return false;
        if (file.exists())
            try {
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return saveStream(path, is);
    }

    /**
     * 保存输入流到本地
     */
    public static boolean saveStream(String path, InputStream is) {
        if (is == null)
            return false;
        File f = null;
        try {
            f = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (f == null)
            return false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b)) != -1) {
                fos.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 从本地取出流
     */
    public static InputStream restoreStream(String path) {
        File f = null;
        try {
            f = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (f == null && !f.exists()) {
            return null;
        }
        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
            return fis;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存对象到本地
     */
    public static boolean saveObject(String path, Object o) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        File f = new File(path);
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 从本地取出对象
     */
    public static Object restoreObject(String path) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object object = null;
        File f = new File(path);
        if (!f.exists()) {
            return null;
        }
        try {
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();
            return object;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }
}
