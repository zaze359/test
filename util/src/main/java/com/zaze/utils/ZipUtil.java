package com.zaze.utils;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-09-18 - 20:17
 */
public class ZipUtil {
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
        ZLog.d(ZTag.TAG_COMPRESS, "getFileList: " + zipFileString);
        List<File> fileList = null;
        try {
            fileList = new ArrayList<>();
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
     * @param outPathString 指定的路径DIR
     */
    public static void unCompressToFolder(String zipFileString, String outPathString) {
        ZLog.d(ZTag.TAG_COMPRESS, ZStringUtil.format("unZipToFolder : %s >> %s ", zipFileString, outPathString));
        try {
            ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
            ZipEntry zipEntry;
            String szName = "";
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    // get the folder name of the widget
                    szName = szName.substring(0, szName.length() - 1);
                    FileUtil.createDirNotExists(new File(outPathString + File.separator + szName));
                } else {
                    String filePath = outPathString + File.separator + szName;
                    File file = new File(filePath);
                    FileUtil.createFileNotExists(filePath);
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
     * @param srcFilePath 要压缩的文件/文件夹名字
     * @param outFilePath 指定压缩的目的和名字
     */
    public static void compressFile(String srcFilePath, String outFilePath) {
        ZLog.d(ZTag.TAG_COMPRESS, ZStringUtil.format("zipFile : %s >> %s", srcFilePath, outFilePath));
        FileUtil.createDirNotExists(new File(outFilePath).getParentFile());
        FileUtil.reCreateFile(outFilePath);
        ZipOutputStream outZip = null;
        try {
            //创建Zip包
            outZip = new ZipOutputStream(new FileOutputStream(outFilePath));
            //压缩
            File srcFile = new File(srcFilePath);
            compressFiles(outZip, srcFile, srcFile.getName() + File.separator);
            outZip.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outZip != null) {
                try {
                    outZip.closeEntry();
                    outZip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 压缩文件
     *
     * @param zipOutputSteam
     * @param srcFile        需要压缩的文件
     * @param baseDir        生成的压缩包的内容的根目录
     * @throws Exception
     */
    private static void compressFiles(ZipOutputStream zipOutputSteam, File srcFile, String baseDir) throws Exception {
        ZLog.v(ZTag.TAG_COMPRESS, "压缩文件: " + baseDir + srcFile.getName());
        if (zipOutputSteam == null) {
            return;
        }
        if (srcFile.isFile()) {
            ZipEntry zipEntry = new ZipEntry(baseDir + srcFile.getName());
            FileInputStream inputStream = new FileInputStream(srcFile);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
        } else {
            //文件夹的方式,获取文件夹下的子文件
            File[] fileList = srcFile.listFiles();
            //如果没有子文件, 则添加进去即可
            if (fileList == null || fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(baseDir + srcFile.getName());
                zipOutputSteam.putNextEntry(zipEntry);
            } else {
                //如果有子文件, 遍历子文件
                for (File file : fileList) {
                    if (file == null) {
                        continue;
                    }
                    if (file.isDirectory()) {
                        compressFiles(zipOutputSteam, file, baseDir + file.getName() + File.separator);
                    } else {
                        compressFiles(zipOutputSteam, file, baseDir);
                    }
                }
            }
        }
    }
}
