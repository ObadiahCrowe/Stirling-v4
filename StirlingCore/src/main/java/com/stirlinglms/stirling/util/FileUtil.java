package com.stirlinglms.stirling.util;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.concurrent.ThreadSafe;
import java.io.File;
import java.io.IOException;

@ThreadSafe
public final class FileUtil {

    public static File fromMultipart(MultipartFile file) {
        File nFile = new File(file.getName());

        try {
            file.transferTo(nFile);
        } catch (IOException e) {
            return new File(file.getName());
        }

        return nFile;
    }
}
