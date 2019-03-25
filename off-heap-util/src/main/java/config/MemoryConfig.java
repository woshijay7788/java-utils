package config;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

/**
 * @description mapdb 配置
 * @author chibei
 */
public class MemoryConfig {

    @Getter
    private DB onDisk;
    @Getter
    private DB inMemory;
    @Getter
    private ConcurrentMap<String, String> diskMap;
    @Getter
    private ConcurrentMap<String, String> memoryMap;

    @Getter
    private StorageType type;

    /**
     * 设置仅内存存储
     * @param mapName 生成的map的名称
     * @param maxSize map最大的存储空间大小
     * @return
     */
    public static MemoryConfig memoryOnly(String mapName, Long maxSize) {
        return new MemoryConfig(mapName, null, maxSize, StorageType.MEMORY);
    }

    /**
     * 设置仅磁盘存储
     * @param mapName 生成的map的名称
     * @param dbFilePath mapDB 存储文件的物理位置
     * @return
     */
    public static MemoryConfig diskOnly(String mapName, String dbFilePath) {

        return new MemoryConfig(mapName, dbFilePath, null, StorageType.DISK);
    }

    /**
     * 同时使用磁盘和内存进行存储
     * @param mapName 生成的map的名称
     * @param dbFilePath mapDB 存储文件的物理位置
     * @param maxSize map最大的存储空间大小
     * @return
     */
    public static MemoryConfig diskAndMemory(String mapName, String dbFilePath, Long maxSize) {

        return new MemoryConfig(mapName, dbFilePath, maxSize, StorageType.MIX);
    }


    /**
     * @param mapName 生成的map的名称
     * @param dbFilePath mapDB 存储文件的物理位置
     * @param maxSize map最大的存储空间大小
     */
    private MemoryConfig(String mapName, String dbFilePath, Long maxSize, StorageType type) {
        super();
        this.type = type;
        onDisk = DBMaker.fileDB(dbFilePath).fileMmapEnable().checksumHeaderBypass().make();
        inMemory = DBMaker.memoryDB().make();
        initMap(mapName, maxSize);
    }

    /**
     * 初始化MapDB
     * @param mapName
     * @param maxSize
     */
    private void initMap(String mapName, Long maxSize) {
        diskMap = onDisk.hashMap(mapName, Serializer.STRING, Serializer.STRING)
                .createOrOpen();
        memoryMap = inMemory.hashMap(mapName, Serializer.STRING, Serializer.STRING)
                .expireAfterGet(1, TimeUnit.SECONDS)
                .expireAfterCreate(1, TimeUnit.SECONDS)
                // this registers overflow to `onDisk`
                .expireOverflow(diskMap)
                // good idea is to enable background expiration TODO
//                .expireExecutor(Executors.newScheduledThreadPool(2))
                .expireStoreSize(maxSize)
                .createOrOpen();
    }

    public enum StorageType {

        MEMORY,
        DISK,
        MIX
    }
}
