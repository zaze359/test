package com.zaze.util;

import com.zaze.commons.log.LogKit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class ZtyZip {

    public ZtyZip() {
    }

    /**
     * 取得压缩包中的 文件列表(文件夹,文件自选)
     *
     * @param zipFileString  压缩包名字
     * @param bContainFolder 是否包括 文件夹
     * @param bContainFile   是否包括 文件
     * @return
     * @throws Exception
     */
    public static List<File> getFileList(String zipFileString, boolean bContainFolder, boolean bContainFile) {
        LogKit.v("XZip : getFileList(String)");

        List<File> fileList = null;
        try {
            fileList = new ArrayList<File>();
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFileString));
            ZipEntry zipEntry;
            String szName = "";

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    // get the folder name of the widget  
                    szName = szName.substring(0, szName.length() - 1);
                    File folder = new File(szName);
                    if (bContainFolder) {
                        fileList.add(folder);
                    }

                } else {
                    File file = new File(szName);
                    if (bContainFile) {
                        fileList.add(file);
                    }
                }
            }//end of while  
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileList;
    }

    /**
     * 返回压缩包中的文件InputStream
     * @param zipFileString 压缩文件的名字
     * @param fileString    解压文件的名字
     * @return InputStream
     * @throws Exception
     */
//    public static InputStream upZip(String zipFileString, String fileString) throws Exception {
//        LogKit.v("XZip : UpZip(String, String)");
//        ZipFile zipFile = new ZipFile(zipFileString);
//        ZipEntry zipEntry = zipFile.getEntry(fileString);
//        return zipFile.getInputStream(zipEntry);
//    }


    /**
     * 解压一个压缩文档 到指定位置
     *
     * @param zipFileString 压缩包的名字
     * @param outPathString 指定的路径
     * @throws Exception
     */
    public static void unZipFolder(String zipFileString, String outPathString) {
        LogKit.v("XZip : UnZipFolder(String, String)");
        try {
            ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
            ZipEntry zipEntry;
            String szName = "";

            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();

                if (zipEntry.isDirectory()) {

                    // get the folder name of the widget  
                    szName = szName.substring(0, szName.length() - 1);
                    File folder = new File(outPathString + File.separator + szName);
                    folder.mkdirs();

                } else {

                    File file = new File(outPathString + File.separator + szName);
                    file.createNewFile();
                    // get the output stream of the file  
                    FileOutputStream out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[1024];
                    // read (len) bytes into buffer  
                    while ((len = inZip.read(buffer)) != -1) {
                        // write (len) byte from buffer at the position 0  
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                }
            }//end of while  
            inZip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end of func  


    /**
     * 压缩文件,文件夹
     *
     * @param srcFileString 要压缩的文件/文件夹名字
     * @param zipFileString 指定压缩的目的和名字
     * @throws Exception
     */
    public static void zipFolder(String srcFileString, String zipFileString) {
        LogKit.v("XZip : ZipFolder(String, String)");
        try {
            //创建Zip包  
            ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
            //打开要输出的文件  
            File file = new File(srcFileString);

            //压缩  
            zipFiles(file.getParent() + File.separator, file.getName(), outZip);

            //完成,关闭  
            outZip.finish();
            outZip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end of func  

    /**
     * 压缩文件
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     */
    private static void zipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
        LogKit.v("XZip : ipFiles(String, String, ZipOutputStream)");

        if (zipOutputSteam == null)
            return;

        File file = new File(folderString + fileString);

        //判断是不是文件  
        if (file.isFile()) {

            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);

            int len;
            byte[] buffer = new byte[4096];

            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }

            zipOutputSteam.closeEntry();
        } else {

            //文件夹的方式,获取文件夹下的子文件  
            String fileList[] = file.list();

            //如果没有子文件, 则添加进去即可  
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }

            //如果有子文件, 遍历子文件  
            for (int i = 0; i < fileList.length; i++) {
                zipFiles(folderString, fileString + File.separator + fileList[i], zipOutputSteam);
            }//end of for  

        }//end of if  

    }//end of func  


    // 压缩
    public static byte[] gzipCompress(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return bytes;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(bytes);
        gzip.close();
        return out.toByteArray();
    }

    // 解压缩
    public static byte[] gzipUncompress(byte[] bytes) throws IOException {
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
