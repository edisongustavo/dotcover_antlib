package com.egmas.ant.tasks.dotcover.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileUtils {
	public static void write(File file, String text) throws IOException {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}

		Writer out = new OutputStreamWriter(new FileOutputStream(file, false));
		try {
			out.write(text);
		} finally {
			out.close();
		}
	}

	public static String read(File file) throws IOException {
		if (!file.exists())
			return null;

		byte[] buffer = new byte[(int) file.length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(file));
			f.read(buffer);
		} finally {
			if (f != null)
				try {
					f.close();
				} catch (IOException ignored) {
				}
		}
		return new String(buffer);
	}
}
