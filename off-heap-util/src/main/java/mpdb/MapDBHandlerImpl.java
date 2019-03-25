package mpdb;

import api.IMemoryHandler;
import config.MemoryConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @description mapdb 配置
 * @author chibei
 */
@Slf4j
public class MapDBHandlerImpl implements IMemoryHandler {

    private MemoryConfig memoryConfig;

    public MapDBHandlerImpl(MemoryConfig memoryConfig) {
        this.memoryConfig = memoryConfig;
    }

    @Override
    public String getValue(String key) {

        if (memoryConfig.getType() == null) {
            log.warn("getValue, the type of memoryConfig is null, key:{}", key);
            return null;
        }
        switch (memoryConfig.getType()) {
            case MEMORY :
            case MIX:
                return memoryConfig.getMemoryMap().get(key);
            case DISK:
                return memoryConfig.getDiskMap().get(key);
            default:
                log.warn("this is the default type");
        }

        return null;
    }

    @Override
    public boolean setValue(String key, String value) {

        try {
            if (memoryConfig.getType() == null) {
                log.warn("setValue, the type of memoryConfig is null, key:{}, value:{}", key, value);
                return false;
            }
            switch (memoryConfig.getType()) {
                case MEMORY :
                    memoryConfig.getMemoryMap().put(key, value);
                    memoryConfig.getInMemory().commit();
                    break;
                case MIX:
                    memoryConfig.getMemoryMap().remove(key);
                    memoryConfig.getDiskMap().remove(key);
                    memoryConfig.getDiskMap().put(key, value);
                    memoryConfig.getInMemory().commit();
                    memoryConfig.getOnDisk().commit();
                    break;
                case DISK:
                    memoryConfig.getDiskMap().remove(key);
                    memoryConfig.getDiskMap().put(key, value);
                    memoryConfig.getOnDisk().commit();
                    break;
                default:
                    log.warn("this is the default type");
            }
            return true;
        } catch (Exception e) {
            log.warn("setValue failed, errorMessage:{}", e);
            return false;
        }
    }

    @Override
    public boolean deleteKey(String key) {
        try {
            if (memoryConfig.getType() == null) {
                log.warn("deleteKey, the type of memoryConfig is null, key:{}", key);
                return false;
            }
            switch (memoryConfig.getType()) {
                case MEMORY :
                    memoryConfig.getMemoryMap().remove(key);
                    memoryConfig.getInMemory().commit();
                    break;
                case MIX:
                    memoryConfig.getMemoryMap().remove(key);
                    memoryConfig.getDiskMap().remove(key);
                    memoryConfig.getOnDisk().commit();
                    break;
                case DISK:
                    memoryConfig.getDiskMap().remove(key);
                    memoryConfig.getOnDisk().commit();
                    break;
                default:
                    log.warn("this is the default type");
            }
            return true;
        } catch (Exception e) {
            log.warn("deleteKey failed, errorMessage:{}", e);
            return false;
        }
    }
}
