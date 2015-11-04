package com.myapplication.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.Assert;

public class PropertyUtils {

	private static Properties p = new Properties();
	private static Logger log = Logger.getLogger(Logger.class.getName());

	static {
		String workingDir = System.getProperty("user.dir");

		try {
			loadPropertyFile(workingDir + File.separator + "properties.properties");

		} catch (FileNotFoundException realCause) {

			Assert.fail("Unable to load file!", realCause);

		} catch (IOException realCause) {

			Assert.fail("Unable to load file!", realCause);
		}
	}

	public static void loadPropertyFile(String propertyFileName)
			throws FileNotFoundException, IOException {
		InputStream is;

		is = new FileInputStream(new File(propertyFileName));
		DataInputStream in = new DataInputStream(is);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		p.load(br);
		log.info(propertyFileName + " is loaded.");
		log.info(propertyFileName + " is loaded.");
		in.close();
		br.close();
	}

	public static String getProperty(String propertyKey) {
		String propertyValue = p.getProperty(propertyKey.trim());

		if (propertyValue == null || propertyValue.trim().length() == 0)

		{

			log.info("The property key: " + propertyKey + " is missing!");
			Assert.fail("The property key: " + propertyKey + " is missing!");

		}

		return propertyValue.trim();
	}

	public static void setProperty(String propertyKey, String value)
			throws FileNotFoundException, IOException {
		p.setProperty(propertyKey, value);
	}

	public static void listProperties() throws FileNotFoundException, IOException {

		for (Enumeration<?> e = p.propertyNames(); e.hasMoreElements();)

			while (e.hasMoreElements()) {
				String propertyKey = (String) e.nextElement();
				log.info(propertyKey + " -- " + p.getProperty(propertyKey));

			}
	}
}
