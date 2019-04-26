package com.github.mjakubowski84.parquet4s;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.parquet.io.InputFile;
import org.apache.parquet.io.SeekableInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamInputFile implements InputFile {
    private long length;
    private byte[] in;

    public StreamInputFile(byte[] in) {
        this.length = in.length;
        this.in = in;
    }

    @Override
    public long getLength() throws IOException {
        return length;
    }

    @Override
    public SeekableInputStream newStream() throws IOException {
        return new StreamSeekable(new ByteArrayInputStream(in));
    }
}
