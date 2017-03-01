package com.github.magnusja.libaums.javafs;

import android.util.Log;

import com.github.magnusja.libaums.javafs.wrapper.DeviceWrapper;
import com.github.magnusja.libaums.javafs.wrapper.FSBlockDeviceWrapper;
import com.github.magnusja.libaums.javafs.wrapper.FileSystemWrapper;
import com.github.mjdev.libaums.driver.BlockDeviceDriver;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.FileSystemCreator;
import com.github.mjdev.libaums.partition.PartitionTableEntry;

import org.apache.log4j.Logger;
import org.jnode.fs.FileSystemException;
import org.jnode.fs.FileSystemType;
import org.jnode.fs.jfat.FatFileSystemType;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.mindpipe.android.logging.log4j.LogCatAppender;

/**
 * Created by magnusja on 3/1/17.
 */

public class FatFileSystemCreator implements FileSystemCreator {
    static {

        final Logger root = Logger.getRootLogger();
        final LogCatAppender logCatAppender = new LogCatAppender();
        root.addAppender(logCatAppender);
    }

    @Override
    public FileSystem read(PartitionTableEntry entry, BlockDeviceDriver blockDevice) throws IOException {

        try {
            FSBlockDeviceWrapper wrapper = new FSBlockDeviceWrapper(blockDevice, entry);
            FileSystemType type = new FatFileSystemType();
            ByteBuffer buffer = ByteBuffer.allocate(512);
            blockDevice.read(0, buffer);
            if(type.supports(wrapper.getPartitionTableEntry(), buffer.array() , wrapper)) {
                return new FileSystemWrapper(type.create(new DeviceWrapper(blockDevice, entry), true));
            }

            return null;

        } catch (FileSystemException e) {
            Log.e("asd", "should not happen", e);
            return null;
        }
    }
}
