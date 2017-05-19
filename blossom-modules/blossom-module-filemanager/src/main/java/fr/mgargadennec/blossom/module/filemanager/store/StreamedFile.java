package fr.mgargadennec.blossom.module.filemanager.store;

import java.io.InputStream;

public class StreamedFile {

  private final InputStream inputStream;

  public StreamedFile(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public InputStream getInputStream() {
    return inputStream;
  }
}
