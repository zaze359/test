package com.zaze.utils.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-09-18 - 20:17
 */
public class GzipUtil {


    public static void compressFile(String source, String target) {
        try {
            compressFile(new FileInputStream(source), new FileOutputStream(target));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void compressFile(InputStream source, OutputStream target) {
        GZIPOutputStream gzout = null;
        try {
            gzout = new GZIPOutputStream(target);
            byte[] buf = new byte[1024];
            int num;
            while ((num = source.read(buf)) != -1) {
                gzout.write(buf, 0, num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (gzout != null) {
                    gzout.close();
                }
                if (target != null) {
                    target.close();
                }
                if (source != null) {
                    source.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @param bytes
     * @return
     * @throws IOException
     */
    public static byte[] compress(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return bytes;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(bytes);
        gzip.close();
        return out.toByteArray();
    }


    /**
     * 解压缩
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public static byte[] uncompress(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return bytes;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
}
