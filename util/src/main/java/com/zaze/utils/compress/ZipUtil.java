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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Description : ZIP压缩工具类，提供压缩、解压功能
 *
 * @author : ZAZE
 * @version : 2018-09-18 - 20:17
 */
public class ZipUtil {
    /**
     * 获取压缩包中的文件列表（支持选择是否包含文件夹和文件）
     *
     * @param zipFileString 压缩包路径
     * @param containFolder 是否包含文件夹
     * @param containFile   是否包含文件
     * @return 压缩包中的文件列表
     */
    public static List<File> getFileList(String zipFileString, boolean containFolder, boolean containFile) {
        ZLog.d(ZTag.TAG_COMPRESS, "getFileList: " + zipFileString);
        List<File> fileList = new ArrayList<>();
        // 使用 try-with-resources 自动关闭流，避免资源泄漏
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFileString))) {
            ZipEntry zipEntry;
            String szName = "";
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
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
            }
        } catch (IOException e) {
            ZLog.e(ZTag.TAG_COMPRESS, "getFileList error", e);
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
     * 解压压缩包到指定目录
     *
     * @param zipFileString 压缩包路径
     * @param outPathString 解压目标目录
     */
    public static void unCompressToFolder(String zipFileString, String outPathString) {
        ZLog.d(ZTag.TAG_COMPRESS, ZStringUtil.format("unZipToFolder : %s >> %s ", zipFileString, outPathString));
        // 使用 try-with-resources 自动关闭 ZipInputStream
        try (ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString))) {
            ZipEntry zipEntry;
            String szName = "";
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    szName = szName.substring(0, szName.length() - 1);
                    FileUtil.createDirNotExists(new File(outPathString + File.separator + szName));
                } else {
                    String filePath = outPathString + File.separator + szName;
                    File file = new File(filePath);
                    FileUtil.createFileNotExists(filePath);
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        int len;
                        byte[] buffer = new byte[1024];
                        while ((len = inZip.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                            out.flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            ZLog.e(ZTag.TAG_COMPRESS, "unCompressToFolder error", e);
        }
    }


    /**
     * 压缩文件,文件夹
     *
     * @param srcFilePath 要压缩的文件/文件夹名字
     * @param outFilePath 指定压缩的目的和名字
     */
    public static void compressFile(String srcFilePath, String outFilePath) {
        compressFiles( new File[]{new File(srcFilePath)}, outFilePath);
    }

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
        try (ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            addFilesToZip(outputStream, sourceFiles, File.separator);
            outputStream.flush();
        } catch (Exception e) {
            ZLog.e(ZTag.TAG_COMPRESS, "compressFiles error", e);
        }
    }


    /**
     * @param zipOutputSteam zip 输出流
     * @param sourceFiles    需要压缩的文件列表
     * @param rootDir        将 sourceFiles 输出 到zip包内部的根目录
     * @throws Exception
     */
    public static void addFilesToZip(@NonNull ZipOutputStream zipOutputSteam, @Nullable File[] sourceFiles, @NonNull String rootDir) {
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
        File[] fileList = sourceFile.listFiles();
        if (fileList == null || fileList.length <= 0) {
            ZipEntry zipEntry = new ZipEntry(rootDir + sourceFile.getName());
            try {
                zipOutputSteam.putNextEntry(zipEntry);
            } catch (IOException e) {
                ZLog.e(ZTag.TAG_COMPRESS, "compressDir error", e);
            }
        } else {
            rootDir = rootDir + sourceFile.getName() + File.separator;
            addFilesToZip(zipOutputSteam, fileList, rootDir);
        }
    }

    private static void compressFile(@NonNull ZipOutputStream zipOutputSteam, @NonNull File sourceFile, String rootDir) {
        if (!sourceFile.isFile()) {
            compressDir(zipOutputSteam, sourceFile, rootDir);
            return;
        }
        ZLog.v(ZTag.TAG_COMPRESS, "压缩文件: " + rootDir + sourceFile.getName());
        try {
            compress(zipOutputSteam, rootDir, new FileInputStream(sourceFile), sourceFile.getName());
        } catch (Throwable e) {
            ZLog.e(ZTag.TAG_COMPRESS, "compressFile error", e);
        }
    }

    public static void compress(@NonNull ZipOutputStream zipOutputSteam, String rootDir, @NonNull InputStream sourceInputStream, String fileName) {
        ZLog.v(ZTag.TAG_COMPRESS, "压缩数据流: " + rootDir + fileName);
        try (InputStream inputStream = sourceInputStream) {
            ZipEntry zipEntry = new ZipEntry(rootDir + fileName);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
        } catch (Throwable e) {
            ZLog.e(ZTag.TAG_COMPRESS, "compress error", e);
        }
    }
}
