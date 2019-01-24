package com.xht97.whulibraryseat.util;

import android.content.Context;

import java.io.File;
import java.math.BigDecimal;

public class DataCleanManager {

    private static String APP_BASE_DIRECTORY = "/data/data/com.xht97.whulibraryseat";

    /**
     * 清除本应用的内部缓存(/data/data/package.dir/cache)
     *
     * @param context 上下文
     */
    public static void cleanInternalCache(Context context){
        deleteFilesByDirectory(context.getCacheDir());
    }


    /**
     * 清除本应用的本地文件(/data/data/package.dir/files)
     *
     * @param context 上下文
     */
    public static void cleanInternalFiles(Context context){
        deleteFilesByDirectory(context.getFilesDir());
    }


    /**
     * 清除本应用的所有数据库(/data/data/package.dir/databases)
     *
     * @param context 上下文
     */
    public static void cleanInternalDatabase(Context context){
        deleteFilesByDirectory(new File(APP_BASE_DIRECTORY + "/databases"));
    }


    /**
     * 根据数据库名称清除数据库
     *
     * @param context 上下文
     * @param name 数据库名称
     */
    public static void cleanDatabaseByName(Context context, String name){
        context.deleteDatabase(name);
    }


    /**
     * 清除本应用的SharedPreference(/data/data/package.dir/sharedPrefs)
     *
     * @param context 上下文
     */
    public static void cleanInternalSharedPreference(Context context){
        deleteFilesByDirectory(new File(APP_BASE_DIRECTORY + "/shared_prefs"));
    }


    /**
     * 清除本应用的本地文件(/data/data/package.dir/files)
     *
     * @param context 上下文
     */
    public static void cleanApplicationData(Context context){
        cleanInternalCache(context);
        cleanInternalFiles(context);
        cleanInternalDatabase(context);
        cleanInternalSharedPreference(context);
    }


    /**
     * 获取应用文件夹大小
     * @param file 指定文件的名字
     * @return long 文件大小
     * @throws Exception 抛出异常
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    /**
     * 获取格式化单位
     *
     * @param size 文件大小
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


    /**
     * 获取应用缓存大小
     * @param file 指定文件的名字
     * @return String 格式化大小的字符串
     * @throws Exception 抛出异常
     */
    public static String getCacheSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }


    private static void deleteFilesByDirectory(File directory){
        if(directory != null && directory.exists() && directory.isDirectory()){
            for(File item: directory.listFiles()){
                if(item.isDirectory()){
                    deleteFilesByDirectory(item);
                } else {
                    item.delete();
                }
            }
        }
    }


}

