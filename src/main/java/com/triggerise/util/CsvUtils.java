/**
 * Copyright (c) 2019 WIT Software <info@wit-software.com>. All rights reserved.
 * <p>
 * WIT Software Confidential and Proprietary information. It is strictly forbidden for 3rd parties to modify, decompile,
 * disassemble, defeat, disable or circumvent any protection mechanism; to sell, license, lease, rent, redistribute or
 * make accessible to any third party, whether for profit or without charge.
 */
package com.triggerise.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvUtils {
    private CsvUtils() {}

    public static List<String> readCsv(String csvFile, boolean ignoreFirstLine) {
        List<String> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(getFileFromResources(csvFile));) {
            int idx = 0;
            while (scanner.hasNextLine()) {
                if(idx++ == 0 && ignoreFirstLine) {
                    scanner.nextLine();
                    continue;
                }

                records.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return records;
    }

    private static File getFileFromResources(String fileName) {
        ClassLoader classLoader = CsvUtils.class.getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }
    }
}
