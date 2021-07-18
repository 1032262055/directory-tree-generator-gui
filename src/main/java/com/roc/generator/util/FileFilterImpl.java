package com.roc.generator.util;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

/**
 * @author: chengpeng
 * @Date: 2020/9/30/0030 10:43
 * @Description: 过滤文件后缀工具类
 */
public class FileFilterImpl implements FileFilter {

    /**
     * 过滤关键字数组
     */
    private String[] keyArr;

    public FileFilterImpl(String[] keyArr) {
        this.keyArr = keyArr;
    }

    /**
     * 文件名称
     *
     * @param pathName 文件全路径
     * @return
     */
    private boolean acceptType(File pathName) {
        return (!(pathName.isHidden() ||
                pathName.getName().contains("Maven") ||
                pathName.getName().contains(".iml") ||
                pathName.getName().contains(".idea") ||
                pathName.getName().contains(".log") ||
                pathName.getName().contains(".prefs") ||
                pathName.getName().contains("target") ||
                pathName.getName().contains("assets") ||
                pathName.getName().contains(".class") ||
                pathName.getName().contains(".gitignore")
        ));
    }

    /**
     * @param pathName 文件全路径
     * @return
     */
    private boolean acceptByKey(File pathName) {
        if (ArrayUtils.isNotEmpty(keyArr)) {
            List<String> list = Arrays.asList(keyArr);
            return (!(pathName.isHidden() || list.stream().anyMatch(s -> pathName.getName().contains(s))));
        }
        return acceptType(pathName);
    }

//    /**
//     * 获取过滤之后满足过滤文件信息
//     *
//     * @return
//     */
//    public static FileFilter getFileFilter() {
//        return FileFilterImpl::accept;
//    }

    /**
     * Tests whether or not the specified abstract pathname should be
     * included in a pathname list.
     *
     * @param pathname The abstract pathname to be tested
     * @return <code>true</code> if and only if <code>pathname</code>
     * should be included
     */
    @Override
    public boolean accept(File pathname) {
        return this.acceptByKey(pathname);
    }
}
