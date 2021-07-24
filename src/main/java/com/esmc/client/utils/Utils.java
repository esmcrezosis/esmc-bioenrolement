package com.esmc.client.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.neurotec.lang.NThrowable;
import com.neurotec.util.concurrent.AggregateExecutionException;

public final class Utils {

	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String PATH_SEPARATOR = System.getProperty("path.separator");
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public static final String VERSION = "10.0.0.0";
	public static final String COPYRIGHT = "Copyright © 2011-2018 Neurotechnology";

	private static ImageIcon createImageIcon(String path) {
		URL imgURL = Utils.class.getResource("/images/" + path);
		if (imgURL == null) {
			System.err.println("Couldn't find file: " + path);
			return null;
		} else {
			return new ImageIcon(imgURL);
		}
	}

	private static int handleNThrowable(NThrowable th) {
		int errorCode = -1;
		if (th instanceof AggregateExecutionException) {
			List<Throwable> causes = ((AggregateExecutionException) th).getCauses();
			for (Throwable cause : causes) {
				if (cause instanceof NThrowable) {
					if (cause.getCause() instanceof NThrowable) {
						errorCode = handleNThrowable((NThrowable) cause.getCause());
					} else {
						errorCode = ((NThrowable) cause).getCode();
					}
					break;
				}
			}
		} else {
			errorCode = ((NThrowable) th).getCode();
		}
		return errorCode;
	}

	public static void printTutorialHeader(String description, String name, String[] args) {
		printTutorialHeader(description, name, VERSION, COPYRIGHT, args);
	}

	public static void printTutorialHeader(String description, String name, String version, String[] args) {
		printTutorialHeader(description, name, version, COPYRIGHT, args);
	}

	public static void printTutorialHeader(String description, String name, String version, String copyright,
			String[] args) {
		System.out.println(name);
		System.out.println();
		System.out.format("%s (Version: %s)%n", description, version);
		System.out.println(copyright.replace("©", "(C)"));
		System.out.println();
		if (args != null && args.length > 0) {
			System.out.println("Arguments:");
			for (int i = 0; i < args.length; i++) {
				System.out.format("\t%s%n", args[i]);
			}
			System.out.println();
		}
	}

	public static void writeText(String pathname, String text) throws IOException {
		if (text == null)
			throw new NullPointerException("text");
		File file = new File(pathname);
		if (file.isAbsolute() && (file.getParentFile() != null)) {
			file.getParentFile().mkdirs();
		} else if (!file.exists() || !file.isFile()) {
			throw new IllegalArgumentException("No such file: " + file.getAbsolutePath());
		}
		Writer writer = new FileWriter(file);
		Closeable resource = writer;
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(writer);
			resource = bw;
			bw.write(text);
		} finally {
			if (bw != null) {
				bw.close();
			}
			resource.close();
		}
	}

	public static String readText(String file) throws IOException {
		Reader reader = new FileReader(file);
		Closeable resource = reader;
		BufferedReader br = null;
		try {
			br = new BufferedReader(reader);
			resource = br;
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			if (line == null) {
				return "";
			} else {
				for (;;) {
					sb.append(line);
					line = br.readLine();
					if (line == null) {
						return sb.toString();
					}
					sb.append(System.getProperty("line.separator"));
				}
			}
		} finally {
			if (br != null) {
				br.close();
			}
			resource.close();
		}
	}

	public static void handleError(Throwable th) {
		if (th == null)
			throw new NullPointerException("th");
		int errorCode = -1;
		if (th instanceof NThrowable) {
			errorCode = handleNThrowable((NThrowable) th);
		} else if (th.getCause() instanceof NThrowable) {
			errorCode = handleNThrowable((NThrowable) th.getCause());
		}
		th.printStackTrace();
		System.exit(errorCode);
	}

	/**
	 * Gets user working directory.
	 */
	public static String getWorkingDirectory() {
		System.out.println("System.getProperty(\"user.dir\")= " + System.getProperty("user.dir"));
		return System.getProperty("user.dir");
	}
	
	public static final class ImageFileFilter extends javax.swing.filechooser.FileFilter {

        private final List<String> extensions;
        private final String description;

        public ImageFileFilter(String extentionsString) {
            this(extentionsString, null);
        }

        public ImageFileFilter(String extentionsString, String description) {
            super();
            extensions = new ArrayList<String>();
            StringTokenizer tokenizer = new StringTokenizer(extentionsString, ";");
            StringBuilder sb;
            if (description == null) {
                sb = new StringBuilder(64);
            } else {
                sb = new StringBuilder(description).append(" (");
            }
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                sb.append(token);
                sb.append(", ");
                extensions.add(token.replaceAll("\\*", "").replaceAll("\\.", ""));
            }
            sb.delete(sb.length() - 2, sb.length());
            if (description != null) {
                sb.append(')');
            }
            this.description = sb.toString();
        }

        @Override
        public boolean accept(File f) {
            for (String extension : extensions) {
                if (f.isDirectory() || f.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            return description;
        }

        public List<String> getExtensions() {
            return new ArrayList<String>(extensions);
        }

    }

	/**
	 * Gets user home directory.
	 */
	public static String getHomeDirectory() {
		return System.getProperty("user.home");
	}

	public static String combinePath(String part1, String part2) {
		return String.format("%s%s%s", part1, FILE_SEPARATOR, part2);
	}

	public static Icon createIcon(String path) {
		return createImageIcon(path);
	}

	public static boolean isNullOrEmpty(String value) {
		return value == null || "".equals(value);
	}

	public static String[] getDirectoryFilesList(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});

		String[] string = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			string[i] = files[i].getAbsolutePath();
		}
		return string;
	}

	private Utils() {
		// Suppress default constructor for noninstantiability.
	}

}
