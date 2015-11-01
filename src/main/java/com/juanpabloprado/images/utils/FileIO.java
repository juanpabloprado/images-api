package com.juanpabloprado.images.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileIO {
  // save uploaded file to new location
  public static void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
    int read;
    final int BUFFER_LENGTH = 1024;
    final byte[] buffer = new byte[BUFFER_LENGTH];
    OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
    while ((read = uploadedInputStream.read(buffer)) != -1) {
      out.write(buffer, 0, read);
    }
    out.flush();
    out.close();
  }

  /** Open a BufferedReader from a named file. */
  public static BufferedReader openFile(String fileName)
      throws IOException {
    return new BufferedReader(new FileReader(fileName));
  }
}
