package com.zaze.utils.compress;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zaze.utils.FileUtil;
import com.zaze.utils.ZStringUtil;
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
     * @param zipFileString 压缩包名字
     * @param containFolder 是否包括 文件夹
     * @param containFile   是否包括 文件
     * @return 压缩包中内容
     */
    public static List<File> getFileList(String zipFileString, boolean containFolder, boolean containFile) {
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
                    if (containFolder) {
                        fileList.add(folder);
                    }
                } else {
                    File file = new File(szName);
                    if (containFile) {
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
        compressFiles( new File[]{new File(srcFilePath)}, outFilePath);
//
//        ZLog.d(ZTag.TAG_COMPRESS, ZStringUtil.format("zipFile : %s >> %s", srcFilePath, outFilePath));
//        FileUtil.createDirNotExists(new File(outFilePath).getParentFile());
//        FileUtil.reCreateFile(outFilePath);
//        ZipOutputStream outZip = null;
//        try {
//            //创建Zip包
//            outZip = new ZipOutputStream(new FileOutputStream(outFilePath));
//            //压缩
//            compressFiles(outZip, srcFile, srcFile.getName() + File.separator);
//            outZip.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (outZip != null) {
//                try {
//                    outZip.closeEntry();
//                    outZip.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
//
//    /**
//     * 压缩文件
//     *
//     * @param zipOutputSteam zip 输出流
//     * @param sourceFile     需要压缩的文件
//     * @param zipDir         生成的压缩包中的 内容的根目录，相对路径
//     * @throws Exception
//     */
//    private static void compressFiles(ZipOutputStream zipOutputSteam, File sourceFile, String zipDir) throws Exception {
//        ZLog.v(ZTag.TAG_COMPRESS, "压缩文件: " + zipDir + sourceFile.getName());
//        if (zipOutputSteam == null) {
//            return;
//        }
//        if (sourceFile.isFile()) {
//            ZipEntry zipEntry = new ZipEntry(zipDir + sourceFile.getName());
//            FileInputStream inputStream = new FileInputStream(sourceFile);
//            zipOutputSteam.putNextEntry(zipEntry);
//            int len;
//            byte[] buffer = new byte[4096];
//            while ((len = inputStream.read(buffer)) != -1) {
//                zipOutputSteam.write(buffer, 0, len);
//            }
//        } else {
//            //文件夹的方式,获取文件夹下的子文件
//            File[] fileList = sourceFile.listFiles();
//            //如果没有子文件, 则添加进去即可
//            if (fileList == null || fileList.length <= 0) {
//                ZipEntry zipEntry = new ZipEntry(zipDir + sourceFile.getName());
//                zipOutputSteam.putNextEntry(zipEntry);
//            } else {
//                //如果有子文件, 遍历子文件
//                for (File file : fileList) {
//                    if (file == null) {
//                        continue;
//                    }
//                    if (file.isDirectory()) {
//                        compressFiles(zipOutputSteam, file, zipDir + file.getName() + File.separator);
//                    } else {
//                        compressFiles(zipOutputSteam, file, zipDir);
//                    }
//                }
//            }
//        }
//    }
//

    /**
     * 批量压缩文件，支持文件夹
     *
     * @param sourceFiles 需要压缩的文件
     * @param zipFilePath 生成的压缩包绝对路径： /sdcard/zaze/a.zip
     */
    public static void compressFiles(File[] sourceFiles, String zipFilePath) {
        ZLog.d(ZTag.TAG_COMPRESS, "zipFile: " + zipFilePath);
        File zipFile = new File(zipFilePath);
        FileUtil.reCreateFile(zipFile);
        ZipOutputStream outputStream = null;
        try {
            // 创建 zip 输出流
            outputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            // 压缩
//            File srcFile = new File(srcFilePath);
            compressFilesInner(outputStream, sourceFiles, File.separator);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.closeEntry();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * @param zipOutputSteam zip 输出流
     * @param sourceFiles    需要压缩的文件列表
     * @param rootDir        将 sourceFiles 输出 到zip包内部的根目录
     * @throws Exception
     */
    private static void compressFilesInner(@NonNull ZipOutputStream zipOutputSteam, @Nullable File[] sourceFiles, @NonNull String rootDir) {
        if (sourceFiles == null) {
            return;
        }
        // 遍历文件
        for (File file : sourceFiles) {
            if (file == null) {
                continue;
            }
            if (file.isDirectory()) {
                compressDir(zipOutputSteam, file, rootDir);
            } else {
                compressFile(zipOutputSteam, file, rootDir);
            }
        }

    }

    private static void compressDir(@NonNull ZipOutputStream zipOutputSteam, @NonNull File sourceFile, String rootDir) {
        if (!sourceFile.isDirectory()) {
            compressFile(zipOutputSteam, sourceFile, rootDir);
            return;
        }
        ZLog.v(ZTag.TAG_COMPRESS, "压缩文件夹: " + rootDir + sourceFile.getName());
        // 文件夹的方式,获取文件夹下的子文件
        File[] fileList = sourceFile.listFiles();
        if (fileList == null || fileList.length <= 0) {
            // 如果没有子文件, 则添加进去即可
            ZipEntry zipEntry = new ZipEntry(rootDir + sourceFile.getName());
            try {
                zipOutputSteam.putNextEntry(zipEntry);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 存在子文件，新添加一层目录
            rootDir = rootDir + sourceFile.getName() + File.separator;
            compressFilesInner(zipOutputSteam, fileList, rootDir);
        }
    }

    private static void compressFile(@NonNull ZipOutputStream zipOutputSteam, @NonNull File sourceFile, String rootDir) {
        if (!sourceFile.isFile()) {
            compressDir(zipOutputSteam, sourceFile, rootDir);
            return;
        }
        ZLog.v(ZTag.TAG_COMPRESS, "压缩文件: " + rootDir + sourceFile.getName());
        try {
            ZipEntry zipEntry = new ZipEntry(rootDir + sourceFile.getName());
            FileInputStream inputStream = new FileInputStream(sourceFile);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
