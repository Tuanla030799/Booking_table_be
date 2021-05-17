package com.nuce.duantp.sunshine.config.database;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogCodeSql {
    private static final String FILENAME = "codeSql.txt";

    public static void writeCodeSql(String query) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME, true))) {
            bw.write(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
