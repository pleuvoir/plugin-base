package io.github.pleuvoir.base.kit;

import java.security.CodeSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionUtils {

	private static Logger logger = LoggerFactory.getLogger(VersionUtils.class);

	private static final String VERSION = getVersion(VersionUtils.class, "1.0.1");


	/**
	 * Gets version.
	 * @return the version
	 */
	public static String getVersion() {
		return VERSION;
	}

	/**
	 * Gets version.
	 * @param cls            the cls
	 * @param defaultVersion the default version
	 * @return the version
	 */
	public static String getVersion(final Class<?> cls, final String defaultVersion) {
		try {
			// find version info from MANIFEST.MF first
			String version = cls.getPackage().getImplementationVersion();
			if (version == null || version.length() == 0) {
				version = cls.getPackage().getSpecificationVersion();
			}
			if (version == null || version.length() == 0) {
				// guess version fro jar file name if nothing's found from MANIFEST.MF
				CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
				if (codeSource == null) {
					logger.info("No codeSource for class " + cls.getName() + " when getVersion, use default version "
							+ defaultVersion);
				} else {
					String file = codeSource.getLocation().getFile();
					if (file != null && file.length() > 0 && file.endsWith(".jar")) {
						file = file.substring(0, file.length() - 4);
						int i = file.lastIndexOf('/');
						if (i >= 0) {
							file = file.substring(i + 1);
						}
						i = file.indexOf("-");
						if (i >= 0) {
							file = file.substring(i + 1);
						}
						while (file.length() > 0 && !Character.isDigit(file.charAt(0))) {
							i = file.indexOf("-");
							if (i >= 0) {
								file = file.substring(i + 1);
							} else {
								break;
							}
						}
						version = file;
					}
				}
			}
			// return default version if no version info is found
			return version == null || version.length() == 0 ? defaultVersion : version;
		} catch (Throwable e) {
			// return default version when any exception is thrown
			logger.error("return default version, ignore exception " + e.getMessage(), e);
			return defaultVersion;
		}
	}
}
