package com.file.upload.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileStorageCache {

    public static final Map<String, Object> cache = new ConcurrentHashMap<>();
}
