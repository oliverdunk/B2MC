package com.oliverdunk.b2mc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private static void addAllFiles(List<File> fileList, File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            fileList.add(file);
            if (file.isDirectory()) addAllFiles(fileList, file);
        }
    }

    public static boolean createZipFromDirectory(File directoryToZip, File backupFile) {
        try {
            FileOutputStream fos = new FileOutputStream(backupFile, false);
            ZipOutputStream zos = new ZipOutputStream(fos);

            ArrayList<File> files = new ArrayList<>();
            addAllFiles(files, directoryToZip);

            for (File file : files) {
                if (!file.isDirectory()) { // we only zip files, not directories
                    addToZip(directoryToZip, file, zos);
                }
            }

            zos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws IOException {
        FileInputStream fis = new FileInputStream(file);

        String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1, file.getCanonicalPath().length());
        ZipEntry zipEntry = new ZipEntry(zipFilePath);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }

}
