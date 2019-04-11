package io.choerodon.foundation.infra.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author shinan.chen
 * @date 2018/10/24
 */
public class EnumUtil {

    private EnumUtil() {
    }

    private static final Logger logger = LoggerFactory.getLogger(EnumUtil.class);

    /**
     * 枚举类通用校验
     *
     * @param cls
     * @param statusType
     * @return
     */
    public static Boolean contain(Class cls, String statusType) {
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                String type = String.valueOf(field.get(cls));
                if (type.equals(statusType)) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return false;
    }
}
