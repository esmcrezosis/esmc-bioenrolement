package com.esmc.client.utils;

import java.awt.Container;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import com.neurotec.biometrics.NFPosition;

public final class Utilities {

	// ==============================================
	// Private static fields
	// ==============================================

	private static final String ERROR_TITLE = "Error";
	private static final String INFORMATION_TITLE = "Information";
	private static final String QUESTION_TITLE = "Question";
	private static final String WARNING_TITLE = "Warning";

	// ==============================================
	// Public static methods
	// ==============================================

	public static String getCurrentApplicationName() {
		return "EnrollementMcnp";
	}

	public static void showError(Container owner, String message) {
		JOptionPane.showMessageDialog(owner, message, String.format("%s: %s", getCurrentApplicationName(), ERROR_TITLE), JOptionPane.ERROR_MESSAGE);
	}

	public static void showError(Container owner, Exception ex) {
		showError(owner, ex.toString());
	}

	public static void showError(Container owner, String format, Object... args) {
		String str = String.format(format, args);
		showError(owner, str);
	}

	public static void showInformation(Container owner, String message) {
		JOptionPane.showMessageDialog(owner, message, String.format("%s: %s", getCurrentApplicationName(), INFORMATION_TITLE), JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showInformation(Container owner, String format, Object... args) {
		String str = String.format(format, args);
		showInformation(owner, str);
	}

	public static boolean showQuestion(Container owner, String message) {
		return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(owner, message, String.format("%s: %s", getCurrentApplicationName(), QUESTION_TITLE), JOptionPane.YES_NO_OPTION);
	}

	public static boolean showQuestion(Container owner, String format, Object... args) {
		String str = String.format(format, args);
		return showQuestion(owner, str);
	}

	public static void showWarning(Container owner, String message, Object... args) {
		showWarning(owner, String.format(message, args));
	}

	public static void showWarning(Container owner, String message) {
		JOptionPane.showMessageDialog(owner, message, getCurrentApplicationName() + ": " + WARNING_TITLE, JOptionPane.WARNING_MESSAGE);
	}

	public static String convertNFPositionNameToCamelCase(NFPosition position) {
		String name = position.name();
		if (name.toUpperCase().contains("LITTLE") || name.toUpperCase().contains("THUMB")) {
			name = name.replace("FINGER", "");
		}
		StringTokenizer tokenizer = new StringTokenizer(name, "_");
		StringBuilder buf = new StringBuilder();
		while (tokenizer.hasMoreElements()) {
			String tokenWord = (String) tokenizer.nextElement();
			tokenWord = tokenWord.toLowerCase();
			char firstChar = Character.toUpperCase(tokenWord.charAt(0));
			String firstLowerChar = Character.toString(tokenWord.charAt(0));
			String firstUpperChar = Character.toString(firstChar);
			tokenWord = tokenWord.replaceFirst(firstLowerChar, firstUpperChar);
			buf.append(tokenWord);
		}
		return buf.toString();
	}

	// ==============================================
	// Private constructor
	// ==============================================

	private Utilities() {
	}
}
