package com.esmc.client.swing.utils;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EnumSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.neurotec.biometrics.NBiometricCaptureOption;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NFace;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.biometrics.swing.NFaceView;
import com.neurotec.devices.NCamera;
import com.neurotec.devices.NDevice;
import com.neurotec.devices.NDeviceManager;
import com.neurotec.media.NMediaFormat;
import com.esmc.client.utils.*;
import com.esmc.client.settings.*;
import com.neurotec.util.concurrent.CompletionHandler;

public final class PictureCapturingDialog extends JDialog {

	// ==============================================
	// Private classes
	// ==============================================

	// ==============================================
	// Private static fields
	// ==============================================

	private static final long serialVersionUID = 1L;

	// ==============================================
	// Private fields
	// ==============================================

	private JComboBox cmbCameras;
	private JComboBox cmbFormats;
	private JButton btnCapture;

	private NFaceView faceView;
	private NSubject subject;

	private JButton btnOk;
	private JButton btnCancel;

	private boolean isCaptured;

	private final EnrollmentDataModel model = EnrollmentDataModel.getInstance();

	// ==============================================
	// Public constructor
	// ==============================================

	public PictureCapturingDialog(Frame owner) {
		super(owner, "Capture Picture", true);
		setPreferredSize(new Dimension(590, 520));
		setResizable(false);
		initializeComponents();
		setLocationRelativeTo(owner);
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				zoom();
			}
		});

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				pictureCaptureFormLoad();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				pictureCaptureFormFormClosing();
			}
		});
	}

	// ==============================================
	// Private methods
	// ==============================================

	private void initializeComponents() {
		JPanel mainPanel = new JPanel();
		GridBagLayout mainPanelLayout = new GridBagLayout();
		mainPanelLayout.columnWidths = new int[] {50, 270, 85, 85, 85};
		mainPanelLayout.rowHeights = new int[] {25, 430, 25};
		mainPanel.setLayout(mainPanelLayout);

		faceView = new NFaceView();

		cmbCameras = new JComboBox();
		cmbCameras.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					changeCamera();
				}
			}
		});

		cmbFormats = new JComboBox();
		cmbFormats.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					changeFormat();
				}
			}
		});

		btnCapture = new JButton("Capture");
		btnCapture.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				capture();
			}
		});

		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				accept();
			}
		});

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		GridBagUtils gridBagUtils = new GridBagUtils(GridBagConstraints.BOTH);
		gridBagUtils.setInsets(new Insets(2, 2, 2, 2));
		gridBagUtils.addToGridBagLayout(0, 0, mainPanel, new JLabel("Cameras:"));
		gridBagUtils.addToGridBagLayout(1, 0, mainPanel, cmbCameras);
		gridBagUtils.addToGridBagLayout(2, 0, 2, 1, mainPanel, cmbFormats);
		gridBagUtils.addToGridBagLayout(4, 0, 1, 1, mainPanel, btnCapture);
		gridBagUtils.addToGridBagLayout(0, 1, 5, 1, mainPanel, new JScrollPane(faceView));
		gridBagUtils.addToGridBagLayout(3, 2, 1, 1, mainPanel, btnOk);
		gridBagUtils.addToGridBagLayout(4, 2, 1, 1, mainPanel, btnCancel);

		getContentPane().add(mainPanel);
		pack();

	}

	private void pictureCaptureFormLoad() {

		NDeviceManager deviceManager = model.getDeviceManager();
		for (NDevice device : deviceManager.getDevices()) {
			if (device instanceof NCamera) {
				cmbCameras.addItem(device);
			}
		}
	}

	private void pictureCaptureFormFormClosing() {
		cancelCapture();
	}

	private void startCapture() {
		cancelCapture();

		isCaptured = false;
		NCamera camera = model.getBiometricClient().getFaceCaptureDevice();
		if (camera != null) {
			NMediaFormat currentFormat = (NMediaFormat) cmbFormats.getSelectedItem();
			if (currentFormat != null) {
				camera.setCurrentFormat(currentFormat);
			}
			NBiometricClient client = model.getBiometricClient();
			subject = new NSubject();
			NFace face = new NFace();
			face.setCaptureOptions(EnumSet.of(NBiometricCaptureOption.STREAM, NBiometricCaptureOption.MANUAL));
			subject.getFaces().add(face);
			faceView.setFace(face);
			client.capture(subject, subject, new CompletionHandler<NBiometricStatus, NSubject>() {

				@Override
				public void completed(final NBiometricStatus arg0, final NSubject arg1) {

					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							if (arg0 == NBiometricStatus.OK) {
								EnrollmentDataModel.getInstance().setThumbFace(subject.getFaces().get(0));
							} else {
								if (arg1.getError() != null) {
									arg1.getError().printStackTrace();
								}
							}
						}
					});
				}

				@Override
				public void failed(Throwable arg0, NSubject arg1) {
				}
			});
		}

	}

	private void cancelCapture() {

		NBiometricClient client = null;
		if (model != null) {
			client = model.getBiometricClient();
		}
		if (client != null) {
			client.cancel();
		}
	}

	private void capture() {

		model.getBiometricClient().forceStart();
		isCaptured = true;
	}

	private void accept() {
		if (!isCaptured) {
			Utilities.showWarning(this, "Image not captured");
		} else {
			if (subject != null && subject.getFaces().size() > 0) {
				model.setThumbFace(subject.getFaces().get(0));
			}
			dispose();
		}
	}

	private synchronized void changeCamera() {

		if (cmbCameras.getItemCount() > 0) {
			model.getBiometricClient().setFaceCaptureDevice((NCamera) cmbCameras.getSelectedItem());
		}

		NCamera camera = model.getBiometricClient().getFaceCaptureDevice();
		if (camera == null) {
			return;
		}
		cmbFormats.removeAllItems();
		for (NMediaFormat item : camera.getFormats()) {
			cmbFormats.addItem(item);
		}

		NMediaFormat current = camera.getCurrentFormat();
		if (current != null) {
			cmbFormats.setSelectedItem(current);
		}
	}

	private void changeFormat() {
		startCapture();
	}

	private void zoom() {
		if (faceView.getFace() != null) {
			Dimension imageSize = new Dimension(faceView.getFace().getImage().getWidth(), faceView.getFace().getImage().getHeight());
			Dimension clientSize = faceView.getSize();

			float zoom = Math.min((float) clientSize.width / imageSize.width, (float) clientSize.height / imageSize.height);
			faceView.setScale(zoom);
		}
	}

}
