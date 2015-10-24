package com.github.lutzblox.engine.screen;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.lutzblox.engine.LutzEngine;

public class Screen {

	private static int mode = 0;

	private static JFrame frame = null;

	private static JPanel drawPane = null;

	private static boolean fullscreen = false;

	private static GraphicsEnvironment env = null;
	private static GraphicsDevice dev = null;

	private static Rectangle maxWindowSize = new Rectangle(0, 0);

	public static void performOSSetup() {

		maxWindowSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	}

	public static void setup(int screenMode) {

		frame = new JFrame();
		frame.setTitle(LutzEngine.getGameName());

		drawPane = new JPanel();
		drawPane.setPreferredSize(new Dimension(200, 100));
		frame.add(drawPane);
		frame.setResizable(false);
		frame.pack();

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {

				LutzEngine.shutdownEngine();
			}
		});
		frame.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowGainedFocus(WindowEvent e) {
			}

			@Override
			public void windowLostFocus(WindowEvent e) {

				// TODO Focus-loss controls
			}
		});
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {

				if (arg0.getKeyCode() == KeyEvent.VK_0) {

					int next = mode + 1;

					if (next > 2) {

						next = 0;
					}

					changeScreenMode(next);
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {

				if (arg0.getKeyCode() == KeyEvent.VK_0) {

					int next = mode + 1;

					if (next > 2) {

						next = 0;
					}

					changeScreenMode(next);
				}
			}
		});

		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		dev = env.getDefaultScreenDevice();

		changeScreenMode(screenMode);
	}

	public static void refreshTitle() {

		if (frame != null) {

			frame.setTitle(LutzEngine.getGameName());
		}
	}

	public static void changeScreenMode(int screenMode) {

		mode = screenMode;

		switch (mode) {

		case 0:

			// Window

			disposeFullscreen();
			frame.dispose();
			frame.setUndecorated(false);
			frame.pack();
			frame.setVisible(true);

			break;

		case 1:

			// Borderless window

			disposeFullscreen();
			frame.dispose();
			frame.setUndecorated(true);
			frame.pack();
			frame.setVisible(true);

			break;

		case 2:

			// Fullscreen

			frame.dispose();
			makeFullscreen();

			break;
		}
	}

	private static void disposeFullscreen() {

		if (fullscreen) {

			fullscreen = false;

			dev.setFullScreenWindow(null);
		}
	}

	private static void makeFullscreen() {

		if (!fullscreen) {

			fullscreen = true;

			frame.setUndecorated(true);

			try {

				dev.setFullScreenWindow(frame);

			} catch (Exception e) {

				dev.setFullScreenWindow(null);
			}
		}
	}

	public static Rectangle getMaximumWindowSize() {

		return maxWindowSize;
	}
}
