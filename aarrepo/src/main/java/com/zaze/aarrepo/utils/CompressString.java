package com.zaze.aarrepo.utils;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterInputStream;
import java.util.zip.InflaterInputStream;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class CompressString {
    public CompressString() {
    }

    public static String gzipUnCompressString(String uncompressedString) {
        if (uncompressedString.length() > 0) {
            try {
                byte[] e = Base64.decode(uncompressedString, 0);
                ByteArrayInputStream bais = new ByteArrayInputStream(e);
                InflaterInputStream iis = new InflaterInputStream(bais);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                boolean len = true;

                int len1;
                while ((len1 = iis.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len1);
                }

                byte[] data = outStream.toByteArray();
                outStream.close();
                iis.close();
                bais.close();
                return new String(data, "utf-8");
            } catch (Exception var8) {
                throw new IllegalStateException("GZIP compression failed: " + var8, var8);
            }
        } else {
            return "";
        }
    }

    public static String gzipCompressString(String uncompressedString) {
        if (uncompressedString.length() > 0) {
            try {
                ByteArrayInputStream e = new ByteArrayInputStream(uncompressedString.getBytes());
                DeflaterInputStream dis = new DeflaterInputStream(e);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                boolean len = true;

                int len1;
                while ((len1 = dis.read(buffer)) != -1) {
                    baos.write(buffer, 0, len1);
                }

                byte[] data = Base64.encode(baos.toByteArray(), 0);
                baos.close();
                dis.close();
                e.close();
                return new String(data, "utf-8");
            } catch (Exception var7) {
                throw new IllegalStateException("GZIP compression failed: " + var7, var7);
            }
        } else {
            return "";
        }
    }
}
