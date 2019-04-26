package com.github.mjakubowski84.parquet4s;

import org.apache.parquet.io.InputFile;
import org.apache.parquet.hadoop.api.ReadSupport;

public class InputFileBuilder<T> extends org.apache.parquet.hadoop.ParquetReader.Builder<T> {
    private final ReadSupport<T> readSupport;

     InputFileBuilder(InputFile file, ReadSupport<T> readerSupport) {
       super(file);
        this.readSupport = readerSupport;
    }

    @Override
    protected ReadSupport<T> getReadSupport() {
        return readSupport;
    }
}
