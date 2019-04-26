package com.github.mjakubowski84.parquet4s;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.PositionedReadable;
import org.apache.hadoop.fs.Seekable;
import org.apache.parquet.io.DelegatingSeekableInputStream;
import org.apache.parquet.io.SeekableInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

//public class StreamSeekable extends DelegatingSeekableInputStream {
//
//    private long position = 0;
//    private FSDataInputStream stream;
//
//    public StreamSeekable(FSDataInputStream inputStream) {
//        super(inputStream);
//        this.stream = inputStream;
//    }
//
//    @Override
//    public long getPos() throws IOException {
//        return stream.getPos();
//    }
//
//    @Override
//    public void seek(long newPos) throws IOException {
//        stream.seek(newPos);
//    }
//
//    @Override
//    public void readFully(byte[] bytes) throws IOException {
//        stream.readFully(bytes, 0, bytes.length);
//    }
//
//    @Override
//    public void readFully(byte[] bytes, int start, int len) throws IOException {
//        stream.readFully(bytes);
//    }
//}

public class StreamSeekable extends DelegatingSeekableInputStream implements Seekable,
        PositionedReadable {

    private ByteArrayInputStream inputStream;
    private long pos;

    public StreamSeekable(ByteArrayInputStream in) {
        super(in);
        this.inputStream = in ;//new ByteArrayInputStream(recommendationBytes);
        pos = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void seek(long pos) throws IOException {
        inputStream.skip(pos);
        this.pos = pos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getPos() throws IOException {
        return pos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean seekToNewSource(long targetPos) throws IOException {
        throw new UnsupportedOperationException("seekToNewSource is not supported.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        int val = inputStream.read();
        if (val > 0) {
            pos++;
        }
        return val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(long position, byte[] buffer, int offset, int length) throws IOException {
        long currPos = pos;
        inputStream.skip(position);
        int bytesRead = inputStream.read(buffer, offset, length);
        inputStream.skip(currPos);
        return bytesRead;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readFully(long position, byte[] buffer, int offset, int length) throws IOException {
        long currPos = pos;
        inputStream.skip(position);
        inputStream.read(buffer, offset, length);
        inputStream.skip(currPos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readFully(long position, byte[] buffer) throws IOException {
        long currPos = pos;
        inputStream.skip(position);
        inputStream.read(buffer);
        inputStream.skip(currPos);
    }

}