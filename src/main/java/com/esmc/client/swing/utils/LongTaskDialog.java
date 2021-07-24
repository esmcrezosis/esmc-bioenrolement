package com.esmc.client.swing.utils;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import com.esmc.client.events.LongTaskListener;

public final class LongTaskDialog extends JDialog {

	// =================================================================
	// Inner class extending SwingWorker which displays progress
	// =================================================================

	private final class BackgroundWorker extends SwingWorker<Object, Object> {

		@Override
		protected Object doInBackground() {
			if (listener != null) {
				listener.processLongTask();
			}
			return null;
		}

		@Override
		protected void done() {
			dispose();
		}

	}

	// ==============================================
	// Private static fields
	// ==============================================

	private static final long serialVersionUID = 1L;

	// ==============================================
	// Public static methods
	// ==============================================

	public static Object runLongTask(Frame owner, LongTaskListener listener, String title) throws InterruptedException, ExecutionException {
		LongTaskDialog frmLongTask = new LongTaskDialog(owner, listener, title);
		frmLongTask.setLocationRelativeTo(owner);
		frmLongTask.setVisible(true);
		return frmLongTask.backGroundWorker.get();
	}

	// ==============================================
	// Private fields
	// ==============================================

	private final LongTaskListener listener;
	private BackgroundWorker backGroundWorker = new BackgroundWorker();

	// ==============================================
	// Private GUI controls
	// ==============================================

	private JLabel lblTitle;
	private JProgressBar progressBar;

	// ==============================================
	// Private constructor
	// ==============================================

	private LongTaskDialog(Frame owner, LongTaskListener listener, String text) {
		super(owner, "Working", true);
		setPreferredSize(new Dimension(375, 100));
		setResizable(false);
		this.listener = listener;
		initializeComponents();
		setLocationRelativeTo(owner);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		lblTitle.setText(text);

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {
				backGroundWorker.execute();
			}

		});
	}

	// ==============================================
	// Private methods
	// ==============================================

	private void initializeComponents() {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

		lblTitle = new JLabel("Working...");
		lblTitle.setAlignmentX(LEFT_ALIGNMENT);

		progressBar = new JProgressBar(0, 100);
		progressBar.setPreferredSize(new Dimension(345, 25));
		progressBar.setMinimumSize(new Dimension(345, 25));
		progressBar.setAlignmentX(LEFT_ALIGNMENT);
		progressBar.setIndeterminate(true);

		mainPanel.add(Box.createVerticalStrut(8));
		mainPanel.add(lblTitle);
		mainPanel.add(progressBar);
		mainPanel.add(Box.createVerticalGlue());

		getContentPane().add(mainPanel);
		pack();
	}

}
