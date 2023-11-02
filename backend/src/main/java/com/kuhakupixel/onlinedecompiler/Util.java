package com.kuhakupixel.onlinedecompiler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {

    public String md5OfFile(File file) {
        String md5 = "";

        try (InputStream is = Files.newInputStream(Paths.get("file.zip"))) {
            md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return md5;

    }
}
