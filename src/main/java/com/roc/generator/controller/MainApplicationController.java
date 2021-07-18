package com.roc.generator.controller;

import com.roc.generator.util.AlertUtil;
import com.roc.generator.util.DirectoryTree;
import com.roc.generator.util.FileFilterImpl;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileFilter;
import java.util.Objects;

/**
 * Created by Roc on 2018-10-05
 */
public class MainApplicationController {

    @FXML
    private TextField projectFolderField;
    @FXML
    private TextField filterTextField;
    @FXML
    private TextArea treeText;

    private Stage primaryStage;

    File selectedFolder = null;

    private String[] keyArr;
    /**
     * 保存上次文件名
     */
    private String lastFileName;

    @FXML
    public void chooseProjectFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedFolder = directoryChooser.showDialog(getPrimaryStage());
        if (selectedFolder != null) {
            projectFolderField.setText(selectedFolder.getAbsolutePath());
        }
    }

    @FXML
    public void generateCode() {
        String result = validateConfig();
        if (result != null) {
            AlertUtil.showErrorAlert(result);
            return;
        }
        //重新获取过滤文件内容，应对首次生成情况
        String fieldStr = filterTextField.getText();
        if (StringUtils.isNotBlank(fieldStr)) {
            keyArr = fieldStr.contains("，") ? fieldStr.split("，") : fieldStr.split(",");
        }
        this.doGenerate(new FileFilterImpl(keyArr));
    }

    /**
     * 生成逻辑
     *
     * @param fileFilter
     */
    private void doGenerate(FileFilter fileFilter) {
        String text = projectFolderField.getText();
        if (Objects.isNull(selectedFolder) && StringUtils.isBlank(text)) {
            AlertUtil.showErrorAlert("请选择目录！");
            return;
        }
        String path = Objects.isNull(selectedFolder) ? text : selectedFolder.getPath();
        //将路径中的\替换成/
        String filePath = path.replaceAll("\\\\", "/");
        final File generateFile = new File(filePath);
        final String generate = DirectoryTree.create(generateFile)
                .setDeep(20)
                .setFileFilter(fileFilter)
                .generate(generateFile);
        treeText.setText(generate);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private String validateConfig() {
        String projectFolder = projectFolderField.getText();
        if (StringUtils.isEmpty(projectFolder)) {
            return "请选择文件目录!";
        }
        return null;
    }

    @FXML
    public void copyText() {
        String text = treeText.getText();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(text);
        clipboard.setContents(transferable, null);
        //复制成功提示
        AlertUtil.showInfoAlert("");
    }

    /**
     * 获取过滤字段
     */
    @FXML
    public void filterField() {
        String fieldStr = filterTextField.getText();
        if (StringUtils.isNotBlank(fieldStr)) {
            keyArr = fieldStr.contains("，") ? fieldStr.split("，") : fieldStr.split(",");
            this.generateCode();
        }
    }
}
