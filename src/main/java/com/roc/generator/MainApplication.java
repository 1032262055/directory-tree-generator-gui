package com.roc.generator;

import com.roc.generator.controller.MainApplicationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;

/**
 * Created by Roc on 2018-10-05
 */
public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("fxml/MainApplication.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("目录树生成工具");
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        MainApplicationController controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);
    }

    public static void main(String[] args) {
        String version = System.getProperty("java.version");
        if (Integer.parseInt(version.substring(2, 3)) >= 8 && Integer.parseInt(version.substring(6)) >= 60) {
            launch(args);
        } else {
            JFrame jFrame = new JFrame("版本错误");
            jFrame.setSize(500, 100);
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel jPanel = new JPanel();
            JLabel jLabel = new JLabel("JDK的版本不能低于1.8.0.60，请升级至最近的JDK 1.8再运行此软件");
            jPanel.add(jLabel);
            jFrame.add(jPanel);
            jFrame.setLocationRelativeTo(null);
            jFrame.setVisible(true);

        }
    }
}
