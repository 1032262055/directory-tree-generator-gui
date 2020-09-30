package com.roc.generator.util;

import java.io.File;
import java.io.FileFilter;

/**
 * @author: chengpeng
 * @Date: 2020/9/30/0030 10:43
 * @Description: 过滤文件后缀工具类
 */
public class FilterUtil {
    /**
     * 文件名称
     *
     * @param pathname 文件全路径
     * @return
     */
    private static boolean accept(File pathname) {
        return (!(pathname.isHidden() ||
                pathname.getName().contains("Maven") ||
                pathname.getName().contains(".iml") ||
                pathname.getName().contains(".idea") ||
                pathname.getName().contains(".log") ||
                pathname.getName().contains(".prefs") ||
                pathname.getName().contains("target") ||
                pathname.getName().contains("assets") ||
                pathname.getName().contains(".class") ||
                pathname.getName().contains(".gitignore")
        ));
    }

    /**
     * 获取过滤之后满足过滤文件信息
     *
     * @return
     */
    public static FileFilter getFileFilter() {
        return FilterUtil::accept;
    }
}
