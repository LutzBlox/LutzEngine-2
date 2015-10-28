package com.github.lutzblox.engine.info;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InfoFileLoader {

	public static void load(File file) throws FileNotFoundException {

		Scanner sc = new Scanner(file);

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
