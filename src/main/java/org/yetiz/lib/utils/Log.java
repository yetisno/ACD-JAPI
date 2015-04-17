package org.yetiz.lib.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yeti on 4/15/15.
 */
public class Log {
	private static Logger getLogger() {
		return LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[3].getClassName());
	}

	private static Logger getLogger(String className) {
		return LoggerFactory.getLogger(className);
	}

	/**
	 * verbose
	 *
	 * @param description
	 */
	public static void t(String description) {
		getLogger().trace(" {}", description);
	}

	/**
	 * debug
	 *
	 * @param description
	 */
	public static void d(String description) {
		getLogger().debug(" {}", description);
	}

	/**
	 * info
	 *
	 * @param description
	 */
	public static void i(String description) {
		getLogger().info(" {}", description);
	}

	/**
	 * warning
	 *
	 * @param description
	 */
	public static void w(String description) {
		getLogger().warn(" {}", description);
	}

	/**
	 * error
	 *
	 * @param description
	 */
	public static void e(String description) {
		getLogger().error(" {}", description);
	}

	/**
	 * verbose
	 *
	 * @param name
	 * @param description
	 */
	public static void t(String name, String description) {
		getLogger().trace("{}: {}", name, description);
	}

	/**
	 * debug
	 *
	 * @param name
	 * @param description
	 */
	public static void d(String name, String description) {
		getLogger().debug("{}: {}", name, description);
	}

	/**
	 * info
	 *
	 * @param name
	 * @param description
	 */
	public static void i(String name, String description) {
		getLogger().info("{}: {}", name, description);
	}

	/**
	 * warning
	 *
	 * @param name
	 * @param description
	 */
	public static void w(String name, String description) {
		getLogger().warn("{}: {}", name, description);
	}

	/**
	 * error
	 *
	 * @param name
	 * @param description
	 */
	public static void e(String name, String description) {
		getLogger().error("{}: {}", name, description);
	}

	/**
	 * verbose
	 *
	 * @param description
	 */
	public static void t(Class clazz, String description) {
		getLogger(clazz.getName()).trace(" {}", description);
	}

	/**
	 * debug
	 *
	 * @param description
	 */
	public static void d(Class clazz, String description) {
		getLogger(clazz.getName()).debug(" {}", description);
	}

	/**
	 * info
	 *
	 * @param description
	 */
	public static void i(Class clazz, String description) {
		getLogger(clazz.getName()).info(" {}", description);
	}

	/**
	 * warning
	 *
	 * @param description
	 */
	public static void w(Class clazz, String description) {
		getLogger(clazz.getName()).warn(" {}", description);
	}

	/**
	 * error
	 *
	 * @param description
	 */
	public static void e(Class clazz, String description) {
		getLogger(clazz.getName()).error(" {}", description);
	}
}
