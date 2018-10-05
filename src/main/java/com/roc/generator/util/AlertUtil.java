package com.roc.generator.util;

import javafx.scene.control.Alert;

/**
 * Created by Roc on 2018-10-05
 */
public class AlertUtil {

    /**
     * 展示错误信息
     * @param message
     */
    public static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
