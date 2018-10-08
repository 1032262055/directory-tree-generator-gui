package com.roc.generator.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Roc on 2018-10-08
 */
public class Test {private void readIgnore() throws IOException {
/*        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(".gitignore"));
            while (true) {
                String nextline = reader.readLine();
                if (nextline == null) {
                    break;
                }
//                System.out.println(nextline);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            reader.close();
        }*/
    Files.lines(new File(".gitignore").toPath())
            .map(s -> s.trim())
            .filter(s -> s.startsWith("."))
            .forEach(s -> System.out.println(s));

}
}
