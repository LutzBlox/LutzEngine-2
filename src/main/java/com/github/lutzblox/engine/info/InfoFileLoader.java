package com.github.lutzblox.engine.info;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class InfoFileLoader {

	public static void load(File file) throws IOException {

		FileInputStream stream = new FileInputStream(file);

		load(stream);

		stream.close();
	}

	public static void load(InputStream stream) {

		Scanner sc = new Scanner(stream);

		while (sc.hasNextLine()) {

			String line = sc.nextLine();

			if (!line.startsWith("#") && line.contains("=")) {

				String[] parts = line.split("=", 2);

				if (parts.length == 2) {

					System.setProperty(parts[0], parts[1]);
				}
			}
		}

		sc.close();
	}
}
