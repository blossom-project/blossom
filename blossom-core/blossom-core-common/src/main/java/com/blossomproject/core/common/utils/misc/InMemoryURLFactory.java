package com.blossomproject.core.common.utils.misc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Map;
import java.util.WeakHashMap;

public class InMemoryURLFactory {
    private final Map<URL, byte[]> contents = new WeakHashMap<>();
    private final URLStreamHandler handler = new InMemoryStreamHandler();

    private static InMemoryURLFactory instance = null;

    public static synchronized InMemoryURLFactory getInstance() {
      if (instance == null) {
        instance = new InMemoryURLFactory();
      }
        return instance;
    }

    private InMemoryURLFactory() {

    }

    public URL build(String path, String data) {
        try {
            return build(path, data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public URL build(String path, byte[] data) {
        try {
            URL url = new URL("memory", "", -1, path, handler);
            contents.put(url, data);
            return url;
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private class InMemoryStreamHandler extends URLStreamHandler {

        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            if(!u.getProtocol().equals("memory")) {
                throw new IOException("Cannot handle protocol: " + u.getProtocol());
            }
            return new URLConnection(u) {

                private byte[] data = null;

                @Override
                public void connect() throws IOException {
                    initDataIfNeeded();
                    checkDataAvailability();
                    // Protected field from superclass
                    connected = true;
                }

                @Override
                public long getContentLengthLong() {
                    initDataIfNeeded();
                  if (data == null) {
                    return 0;
                  }
                    return data.length;
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    initDataIfNeeded();
                    checkDataAvailability();
                    return new ByteArrayInputStream(data);
                }

                private void initDataIfNeeded() {
                  if (data == null) {
                    data = contents.get(u);
                  }
                }

                private void checkDataAvailability() throws IOException {
                  if (data == null) {
                    throw new IOException("In-memory data cannot be found for: " + u.getPath());
                  }
                }

            };
        }

    }
}
