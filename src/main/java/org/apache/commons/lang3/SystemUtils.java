
package org.apache.commons.lang3;

import java.io.File;


public class SystemUtils {

    
    private static final String OS_NAME_WINDOWS_PREFIX = "Windows";

    // System property constants
    // -----------------------------------------------------------------------
    // These MUST be declared first. Other constants depend on this.

    
    private static final String USER_HOME_KEY = "user.home";

    
    private static final String USER_DIR_KEY = "user.dir";

    
    private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";

    
    private static final String JAVA_HOME_KEY = "java.home";

    
    public static final String AWT_TOOLKIT = getSystemProperty("awt.toolkit");

    
    public static final String FILE_ENCODING = getSystemProperty("file.encoding");

    
    @Deprecated
    public static final String FILE_SEPARATOR = getSystemProperty("file.separator");

    
    public static final String JAVA_AWT_FONTS = getSystemProperty("java.awt.fonts");

    
    public static final String JAVA_AWT_GRAPHICSENV = getSystemProperty("java.awt.graphicsenv");

    
    public static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");

    
    public static final String JAVA_AWT_PRINTERJOB = getSystemProperty("java.awt.printerjob");

    
    public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");

    
    public static final String JAVA_CLASS_VERSION = getSystemProperty("java.class.version");

    
    public static final String JAVA_COMPILER = getSystemProperty("java.compiler");

    
    public static final String JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs");

    
    public static final String JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs");

    
    public static final String JAVA_HOME = getSystemProperty(JAVA_HOME_KEY);

    
    public static final String JAVA_IO_TMPDIR = getSystemProperty(JAVA_IO_TMPDIR_KEY);

    
    public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");

    
    public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");

    
    public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");

    
    public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");

    
    public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");

    
    public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");
    private static final JavaVersion JAVA_SPECIFICATION_VERSION_AS_ENUM = JavaVersion.get(JAVA_SPECIFICATION_VERSION);

    
    public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY =
        getSystemProperty("java.util.prefs.PreferencesFactory");

    
    public static final String JAVA_VENDOR = getSystemProperty("java.vendor");

    
    public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");

    
    public static final String JAVA_VERSION = getSystemProperty("java.version");

    
    public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");

    
    public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");

    
    public static final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");

    
    public static final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");

    
    public static final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");

    
    public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");

    
    public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");

    
    @Deprecated
    public static final String LINE_SEPARATOR = getSystemProperty("line.separator");

    
    public static final String OS_ARCH = getSystemProperty("os.arch");

    
    public static final String OS_NAME = getSystemProperty("os.name");

    
    public static final String OS_VERSION = getSystemProperty("os.version");

    
    @Deprecated
    public static final String PATH_SEPARATOR = getSystemProperty("path.separator");

    
    public static final String USER_COUNTRY = getSystemProperty("user.country") == null ?
            getSystemProperty("user.region") : getSystemProperty("user.country");

    
    public static final String USER_DIR = getSystemProperty(USER_DIR_KEY);

    
    public static final String USER_HOME = getSystemProperty(USER_HOME_KEY);

    
    public static final String USER_LANGUAGE = getSystemProperty("user.language");

    
    public static final String USER_NAME = getSystemProperty("user.name");

    
    public static final String USER_TIMEZONE = getSystemProperty("user.timezone");

    // Java version checks
    // -----------------------------------------------------------------------
    // These MUST be declared after those above as they depend on the
    // values being set up

    
    public static final boolean IS_JAVA_1_1 = getJavaVersionMatches("1.1");

    
    public static final boolean IS_JAVA_1_2 = getJavaVersionMatches("1.2");

    
    public static final boolean IS_JAVA_1_3 = getJavaVersionMatches("1.3");

    
    public static final boolean IS_JAVA_1_4 = getJavaVersionMatches("1.4");

    
    public static final boolean IS_JAVA_1_5 = getJavaVersionMatches("1.5");

    
    public static final boolean IS_JAVA_1_6 = getJavaVersionMatches("1.6");

    
    public static final boolean IS_JAVA_1_7 = getJavaVersionMatches("1.7");

    
    public static final boolean IS_JAVA_1_8 = getJavaVersionMatches("1.8");

    
    @Deprecated
    public static final boolean IS_JAVA_1_9 = getJavaVersionMatches("9");

    
    public static final boolean IS_JAVA_9 = getJavaVersionMatches("9");

    
    public static final boolean IS_JAVA_10 = getJavaVersionMatches("10");

    
    public static final boolean IS_JAVA_11 = getJavaVersionMatches("11");

    // Operating system checks
    // -----------------------------------------------------------------------
    // These MUST be declared after those above as they depend on the
    // values being set up
    // OS names from http://www.vamphq.com/os.html
    // Selected ones included - please advise dev@commons.apache.org
    // if you want another added or a mistake corrected

    
    public static final boolean IS_OS_AIX = getOsMatchesName("AIX");

    
    public static final boolean IS_OS_HP_UX = getOsMatchesName("HP-UX");

    
    public static final boolean IS_OS_400 = getOsMatchesName("OS/400");

    
    public static final boolean IS_OS_IRIX = getOsMatchesName("Irix");

    
    public static final boolean IS_OS_LINUX = getOsMatchesName("Linux") || getOsMatchesName("LINUX");

    
    public static final boolean IS_OS_MAC = getOsMatchesName("Mac");

    
    public static final boolean IS_OS_MAC_OSX = getOsMatchesName("Mac OS X");

    
    public static final boolean IS_OS_MAC_OSX_CHEETAH = getOsMatches("Mac OS X", "10.0");

    
    public static final boolean IS_OS_MAC_OSX_PUMA = getOsMatches("Mac OS X", "10.1");

    
    public static final boolean IS_OS_MAC_OSX_JAGUAR = getOsMatches("Mac OS X", "10.2");

    
    public static final boolean IS_OS_MAC_OSX_PANTHER = getOsMatches("Mac OS X", "10.3");

    
    public static final boolean IS_OS_MAC_OSX_TIGER = getOsMatches("Mac OS X", "10.4");

    
    public static final boolean IS_OS_MAC_OSX_LEOPARD = getOsMatches("Mac OS X", "10.5");

    
    public static final boolean IS_OS_MAC_OSX_SNOW_LEOPARD = getOsMatches("Mac OS X", "10.6");

    
    public static final boolean IS_OS_MAC_OSX_LION = getOsMatches("Mac OS X", "10.7");

    
    public static final boolean IS_OS_MAC_OSX_MOUNTAIN_LION = getOsMatches("Mac OS X", "10.8");

    
    public static final boolean IS_OS_MAC_OSX_MAVERICKS = getOsMatches("Mac OS X", "10.9");

    
    public static final boolean IS_OS_MAC_OSX_YOSEMITE = getOsMatches("Mac OS X", "10.10");

    
    public static final boolean IS_OS_MAC_OSX_EL_CAPITAN = getOsMatches("Mac OS X", "10.11");

    
    public static final boolean IS_OS_FREE_BSD = getOsMatchesName("FreeBSD");

    
    public static final boolean IS_OS_OPEN_BSD = getOsMatchesName("OpenBSD");

    
    public static final boolean IS_OS_NET_BSD = getOsMatchesName("NetBSD");

    
    public static final boolean IS_OS_OS2 = getOsMatchesName("OS/2");

    
    public static final boolean IS_OS_SOLARIS = getOsMatchesName("Solaris");

    
    public static final boolean IS_OS_SUN_OS = getOsMatchesName("SunOS");

    
    public static final boolean IS_OS_UNIX = IS_OS_AIX || IS_OS_HP_UX || IS_OS_IRIX || IS_OS_LINUX || IS_OS_MAC_OSX
            || IS_OS_SOLARIS || IS_OS_SUN_OS || IS_OS_FREE_BSD || IS_OS_OPEN_BSD || IS_OS_NET_BSD;

    
    public static final boolean IS_OS_WINDOWS = getOsMatchesName(OS_NAME_WINDOWS_PREFIX);

    
    public static final boolean IS_OS_WINDOWS_2000 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 2000");

    
    public static final boolean IS_OS_WINDOWS_2003 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 2003");

    
    public static final boolean IS_OS_WINDOWS_2008 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " Server 2008");

    
    public static final boolean IS_OS_WINDOWS_2012 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " Server 2012");

    
    public static final boolean IS_OS_WINDOWS_95 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 95");

    
    public static final boolean IS_OS_WINDOWS_98 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 98");

    
    public static final boolean IS_OS_WINDOWS_ME = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " Me");

    
    public static final boolean IS_OS_WINDOWS_NT = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " NT");

    
    public static final boolean IS_OS_WINDOWS_XP = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " XP");

    // -----------------------------------------------------------------------
    
    public static final boolean IS_OS_WINDOWS_VISTA = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " Vista");

    
    public static final boolean IS_OS_WINDOWS_7 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 7");

    
    public static final boolean IS_OS_WINDOWS_8 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 8");

    
    public static final boolean IS_OS_WINDOWS_10 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 10");

    
    // Values on a z/OS system I tested (Gary Gregory - 2016-03-12)
    // os.arch = s390x
    // os.encoding = ISO8859_1
    // os.name = z/OS
    // os.version = 02.02.00
    public static final boolean IS_OS_ZOS = getOsMatchesName("z/OS");

    
    public static File getJavaHome() {
        return new File(System.getProperty(JAVA_HOME_KEY));
    }

    
    public static String getHostName() {
        return SystemUtils.IS_OS_WINDOWS ? System.getenv("COMPUTERNAME") : System.getenv("HOSTNAME");
    }

    
    public static File getJavaIoTmpDir() {
        return new File(System.getProperty(JAVA_IO_TMPDIR_KEY));
    }

    
    private static boolean getJavaVersionMatches(final String versionPrefix) {
        return isJavaVersionMatch(JAVA_SPECIFICATION_VERSION, versionPrefix);
    }

    
    private static boolean getOsMatches(final String osNamePrefix, final String osVersionPrefix) {
        return isOSMatch(OS_NAME, OS_VERSION, osNamePrefix, osVersionPrefix);
    }

    
    private static boolean getOsMatchesName(final String osNamePrefix) {
        return isOSNameMatch(OS_NAME, osNamePrefix);
    }

    // -----------------------------------------------------------------------
    
    private static String getSystemProperty(final String property) {
        try {
            return System.getProperty(property);
        } catch (final SecurityException ex) {
            // we are not allowed to look at this property
            // System.err.println("Caught a SecurityException reading the system property '" + property
            // + "'; the SystemUtils property value will default to null.");
            return null;
        }
    }

    
    public static String getEnvironmentVariable(final String name, final String defaultValue) {
        try {
            final String value = System.getenv(name);
            return value == null ? defaultValue : value;
        } catch (final SecurityException ex) {
            // we are not allowed to look at this property
            // System.err.println("Caught a SecurityException reading the environment variable '" + name + "'.");
            return defaultValue;
        }
    }

    
    public static File getUserDir() {
        return new File(System.getProperty(USER_DIR_KEY));
    }

    
    public static File getUserHome() {
        return new File(System.getProperty(USER_HOME_KEY));
    }

    
    public static boolean isJavaAwtHeadless() {
        return Boolean.TRUE.toString().equals(JAVA_AWT_HEADLESS);
    }

    
    public static boolean isJavaVersionAtLeast(final JavaVersion requiredVersion) {
        return JAVA_SPECIFICATION_VERSION_AS_ENUM.atLeast(requiredVersion);
    }

    
    static boolean isJavaVersionMatch(final String version, final String versionPrefix) {
        if (version == null) {
            return false;
        }
        return version.startsWith(versionPrefix);
    }

    
    static boolean isOSMatch(final String osName, final String osVersion, final String osNamePrefix, final String osVersionPrefix) {
        if (osName == null || osVersion == null) {
            return false;
        }
        return isOSNameMatch(osName, osNamePrefix) && isOSVersionMatch(osVersion, osVersionPrefix);
    }

    
    static boolean isOSNameMatch(final String osName, final String osNamePrefix) {
        if (osName == null) {
            return false;
        }
        return osName.startsWith(osNamePrefix);
    }

    
    static boolean isOSVersionMatch(final String osVersion, final String osVersionPrefix) {
        if (StringUtils.isEmpty(osVersion)) {
            return false;
        }
        // Compare parts of the version string instead of using String.startsWith(String) because otherwise
        // osVersionPrefix 10.1 would also match osVersion 10.10
        final String[] versionPrefixParts = osVersionPrefix.split("\\.");
        final String[] versionParts = osVersion.split("\\.");
        for (int i = 0; i < Math.min(versionPrefixParts.length, versionParts.length); i++) {
            if (!versionPrefixParts[i].equals(versionParts[i])) {
                return false;
            }
        }
        return true;
    }

    // -----------------------------------------------------------------------
    
    public SystemUtils() {
        super();
    }

}
