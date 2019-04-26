package com.github.mjakubowski84.parquet4s;

import org.apache.parquet.io.DelegatingPositionOutputStream;
import org.apache.parquet.io.PositionOutputStream;

import java.io.IOException;

public class OutputStream extends DelegatingPositionOutputStream {
    private long position = 0;
    private java.io.OutputStream outputStream;

    public OutputStream(java.io.OutputStream outputStream) {
        super(outputStream);
        this.outputStream = outputStream;
    }

    @Override
    public long getPos() throws IOException {
        return position;
    }

    @Override
    public void write(int b) throws IOException {
        position++;
        outputStream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
        position += len;
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
