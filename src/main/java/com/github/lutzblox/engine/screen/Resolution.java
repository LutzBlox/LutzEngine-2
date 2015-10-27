package com.github.lutzblox.engine.screen;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.Set;
import java.util.TreeSet;

public class Resolution implements Comparable<Resolution> {

	private int width;
	private int height;

	private Resolution(int width, int height) {

		this.width = width;
		this.height = height;
	}

	public int getWidth() {

		return width;
	}

	public int getHeight() {

		return height;
	}

	@Override
	public String toString() {

		return this.getClass().getName() + "[width=" + width + ",height=" + height + "]";
	}

	@Override
	public int compareTo(Resolution o) {

		return new Integer(this.getWidth()).compareTo(o.getWidth());
	}

	public static Resolution[] getSupportedResolutions() {

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		Set<Resolution> res = new TreeSet<Resolution>();

		for (DisplayMode dm : gd.getDisplayModes()) {

			res.add(new Resolution(dm.getWidth(), dm.getHeight()));
		}

		return res.toArray(new Resolution[res.size()]);
	}

	public static Resolution[] getSupportedWindowedResolutions() {

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		Rectangle maxWindowSize = Screen.getMaximumWindowSize();
		int maxWidth = maxWindowSize.width;
		int maxHeight = maxWindowSize.height;

		Set<Resolution> res = new TreeSet<Resolution>();

		for (DisplayMode dm : gd.getDisplayModes()) {

			if (dm.getWidth() > maxWidth || dm.getHeight() > maxHeight) {

				res.add(new Resolution(maxWidth, maxHeight));

			} else {

				res.add(new Resolution(dm.getWidth(), dm.getHeight()));
			}
		}

		return res.toArray(new Resolution[res.size()]);
	}
}
