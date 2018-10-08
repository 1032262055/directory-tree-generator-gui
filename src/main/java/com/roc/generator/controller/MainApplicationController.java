package com.roc.generator.controller;

import com.roc.generator.util.AlertUtil;
import com.roc.generator.util.DirectoryTree;
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

/**
 * Created by Roc on 2018-10-05
 */
public class MainApplicationController {

    @FXML
    private TextField projectFolderField;

    @FXML
    private TextArea treeText;

    private Stage primaryStage;

    File selectedFolder = null;

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
        //将路径中的\替换成/
        String path = selectedFolder.getPath().replaceAll("\\\\", "/");
        final File generateFile = new File(path);
        final String generate = DirectoryTree.create(generateFile)
                .setDeep(20)
                .setFileFilter(pathname -> (!(pathname.isHidden() ||
                        pathname.getName().contains("Maven") ||
                        pathname.getName().contains(".iml") ||
                        pathname.getName().contains(".idea") ||
                        pathname.getName().contains(".log") ||
                        pathname.getName().contains(".prefs") ||
                        pathname.getName().contains("target") ||
                        pathname.getName().contains("assets") ||
                        pathname.getName().contains(".class") ||
                        pathname.getName().contains(".gitignore")
                )))
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
            return "目录不能为空!";
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
}
