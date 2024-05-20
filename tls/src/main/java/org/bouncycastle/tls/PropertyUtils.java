package org.bouncycastle.tls;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;

class PropertyUtils {
    private static final Logger LOG = Logger.getLogger(PropertyUtils.class.getName());

    static String getSystemProperty(final String propertyName) {
        try {
            return AccessController.doPrivileged((PrivilegedAction<String>) () -> {
                return System.getProperty(propertyName);
            });
        } catch (RuntimeException e) {
            LOG.log(Level.WARNING, "Failed to get system property [" + propertyName + "]: "  + e);
            return null;
        }
    }

    public static boolean getBooleanSystemProperty(String propertyName, boolean defaultValue) {
        String propertyValue = getSystemProperty(propertyName);
        if (null != propertyValue) {
            if ("true".equalsIgnoreCase(propertyValue)) {
                LOG.finest("Found boolean system property [" + propertyName + "]: " + true);
                return true;
            }
            if ("false".equalsIgnoreCase(propertyValue)) {
                LOG.finest("Found boolean system property [" + propertyName + "]: " + false);
                return false;
            }
            LOG.warning("Unrecognized value for boolean system property [" + propertyName + "]: " + propertyValue);
        }
        LOG.fine("Boolean system property [" + propertyName + "] defaulted to: " + defaultValue);
        return defaultValue;
    }
}