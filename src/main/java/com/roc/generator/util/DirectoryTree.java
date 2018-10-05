package com.roc.generator.util;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.function.Function;

/**
 * 目录树生成工具.
 * <p>
 * {@linkplain #setDeep(int) 设置查询目录深度}
 * {@linkplain #setFileFilter(FileFilter)}  设置查询文件筛选过滤器}
 * {@linkplain #showLength()} 显示文件大小内容}
 * {@linkplain #showModify() 显示文件修改时间内容}
 * {@linkplain #showPermission() 显示文件权限内容}
 * {@linkplain #addAppendContent(Function) 自定义显示文件内容信息}
 *
 * @author wei.Li
 * @version JDK1.8
 * @since 1
 */
public class DirectoryTree {

    /* 换行符*/
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    /* 空字符串*/
    private static final String EMPTY = "";
    /* 文件连接符*/
    private static final String VERTICAL = "│ ", INTERMEDIATE = "├─", END = "└─";
    /* 目录间距*/
    private static final String SPACING = "\t";
    /* 结果集收集*/
    private final StringBuilder r = new StringBuilder();
    /* 默认查询文件目录深度,默认为Integer.MAX_VALUE */
    private int deep = Integer.MAX_VALUE;
    /* 文件筛选过滤器*/
    private FileFilter fileFilter = pathname -> true;
    /* 写出文件名及其他信息，默认只输出文件名称*/
    private AppendTo displayContent = new AppendTo(file -> " " + file.getName());

    private DirectoryTree() {
    }

    private DirectoryTree(DirectoryTree DirectoryTree) {
        this.fileFilter = DirectoryTree.fileFilter;
    }

    /**
     * Create tree tree.
     *
     * @param generateFile the generate file
     * @return the tree
     */
    public static DirectoryTree create(File generateFile) {
        return new DirectoryTree();
    }

    /**
     * 目录读取子文件.
     *
     * @param file the file
     * @return the list
     */
    List<File> fetchFiles(File file) {
        final File[] files = file.listFiles(this.fileFilter);
        return files == null ? Collections.emptyList() : Arrays.asList(files);
    }

    /**
     * 自定义文件筛选过滤器.
     *
     * @param customFilter the custom file filter
     * @return the file filter
     */
    public DirectoryTree setFileFilter(FileFilter customFilter) {
        this.fileFilter = customFilter;
        return this;
    }

    /**
     * 设置显示文件目录深度.
     *
     * @param deep the deep
     * @return the deep
     */
    public DirectoryTree setDeep(int deep) {
        this.deep = deep;
        return this;
    }

    /**
     * 自定义排序显示结果 tree.
     *
     * @param comparable the comparable
     * @return the tree
     */
    public DirectoryTree sort(final Comparator<File> comparable) {
        return new DirectoryTree(this) {
            @Override
            List<File> fetchFiles(File file) {
                final List<File> files = super.fetchFiles(file);
                files.sort(comparable);
                return files;
            }
        };
    }

    /**
     * 显示文件大小.
     *
     * @return the tree
     */
    public DirectoryTree showLength() {
        this.displayContent.add(file -> ("[" + file.length() + "B" + "]"));
        return this;
    }

    /**
     * 显示文件修改时间.
     *
     * @return the tree
     */
    public DirectoryTree showModify() {
        this.displayContent.add(file -> ("[" + new Date(file.lastModified()) + "]"));
        return this;
    }

    /**
     * 显示文件权限.
     *
     * @return the tree
     */
    public DirectoryTree showPermission() {
        this.displayContent.add(
                file -> ("["
                        + (file.canRead() ? "r-" : "--")
                        + (file.canWrite() ? "w-" : "--")
                        + (file.canExecute() ? "x-" : "--")
                        + "]")
        );
        return this;
    }

    /**
     * 自定义添加读取 file 解析内容到输出内容.
     *
     * @param appendContent the append content
     * @return the tree
     */
    public DirectoryTree addAppendContent(Function<File, String> appendContent) {
        this.displayContent.add(appendContent);
        return this;
    }

    /**
     * 生成文件.
     *
     * @return 结果内容
     */
    public final String generate(File generateFile) {
        if (generateFile.exists()) {
            this.generateHandle(generateFile, EMPTY, 0);
        } else {
            System.err.println(generateFile.getPath() + " not found!");
        }

        return this.r.toString();
    }

    private void generateHandle(File file, String prefix, int deep) {
        final List<File> files = this.fetchFiles(file);
        if (files.isEmpty()) {
            return;
        }
        deep++;
        final int length = files.size();
        for (int i = 0; i < length; i++) {
            final File f = files.get(i);

            final boolean isLast = (i >= length - 1);
            this.r.append(prefix).append(isLast ? END : INTERMEDIATE);
            this.appendDisplayContent(f);
            this.r.append(LINE_SEPARATOR);

            if (f.isDirectory() && deep <= this.deep) {
                this.generateHandle(f, prefix + (!(length <= 1 || isLast) ? VERTICAL : EMPTY) + SPACING, deep);
            }
        }
    }

    /**
     * 处理定义文件内容
     *
     * @param f f
     */
    private void appendDisplayContent(File f) {
        final List<Function<File, String>> appendContents = displayContent.appendContents;
        for (Function<File, String> to : appendContents) {
            this.r.append(to.apply(f));
        }
    }


    /**
     * 可累积显示 tree 中具体文件属性内容
     */
    private static class AppendTo {

        private final List<Function<File, String>> appendContents = new ArrayList<>();

        AppendTo(Function<File, String> appendTo) {
            if (appendTo != null) {
                this.appendContents.add(appendTo);
            }
        }

        void add(Function<File, String> to) {
            if (to != null) {
                this.appendContents.add(0, to);
            }
        }
    }
}

/**
 * The type Main Test.
 */
/*
class MainTest {

    public static void main(String[] args) {
        final File generateFile = new File("/lw/workfile/intellij_work/analyzer/src/main/java/com/fusionskye/ezsonar");
        final String generate = DirectoryTree.create(generateFile)
                .setDeep(20)
                .setFileFilter(pathname -> (!(pathname.isHidden() || pathname.getName().contains("java"))))
                .generate(generateFile);
        System.out.println(generate);

    }

}*/