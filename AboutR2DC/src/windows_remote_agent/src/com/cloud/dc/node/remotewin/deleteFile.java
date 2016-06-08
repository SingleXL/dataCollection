package com.cloud.dc.node.remotewin;

import java.io.*;


public class deleteFile {

        private File file;
        private boolean flag;

        public boolean deleteFile(String sPath) {
            flag =false;
            file = new File(sPath);
            if (file.isFile() && file.exists()) {
                file.delete();
                flag = true;
            }
            return flag;
        }
        public boolean deleteDirectory(String sPath) {

            if (!sPath.endsWith(File.separator)) {
                sPath = sPath + File.separator;
            }
            File dirFile = new File(sPath);

            if (!dirFile.exists() || !dirFile.isDirectory()) {
                return false;
            }
            boolean flag = true;
            File[] files = dirFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    if (!flag) break;
                }
                else {
                    flag = deleteDirectory(files[i].getAbsolutePath());
                    if (!flag) break;
                }
            }
            if (!flag) return false;
            if (dirFile.delete()) {
                return true;
            } else {
                return false;
            }
        }

//    public static void main(String[] args) {
//
//        String sPath="C:\\MLP5.0.zip";
//        deleteFile test=new deleteFile();
//        test.deleteDirectory(sPath);
//
//
//    }
}