package com.esmc.client.swing.utils;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.esmc.client.settings.EnrollmentDataModel;
import com.esmc.client.utils.Utils;
import com.neurotec.biometrics.NFImpressionType;
import com.neurotec.biometrics.NFPosition;
import com.neurotec.devices.NDevice;
import com.neurotec.devices.NDeviceManager;
import com.neurotec.devices.NFScanner;

import com.esmc.client.utils.Utilities;

public final class DeviceSelectDialog extends JDialog implements ActionListener {

	// ==============================================
	// Private static fields
	// ==============================================

	private static final long serialVersionUID = 1L;

	// ==============================================
	// Private fields
	// ==============================================

	private JComboBox cmbScanner;
	//private JLabel lblCanCaptureSlaps;
	//private JLabel lblCanCaptureRolled;
	private JButton btnOk;
	private JButton btnCancel;

	private NDeviceManager deviceMan;
	private NFScanner selectedDevice;
	private boolean isDialogResultOk;

	private EnrollmentDataModel dataModel;

	// ==============================================
	// Public constructor
	// ==============================================

	public DeviceSelectDialog(Frame owner) {
		super(owner, "Select Scanner", true);
		setPreferredSize(new Dimension(365, 175));
		setResizable(false);
		initializeComponents();
		setLocationRelativeTo(owner);
		dataModel = EnrollmentDataModel.getInstance();
		setDeviceManager(dataModel.getBiometricClient().getDeviceManager());
		setSelectedDevice(dataModel.getBiometricClient().getFingerScanner());

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				scannerFormLoad();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				cancellingDeviceDialog();
			}
		});
	}

	// ==============================================
	// Private methods
	// ==============================================

	private void initializeComponents() {
		JPanel mainPanel = new JPanel();
		GridBagLayout mainPanelLayout = new GridBagLayout();
		mainPanelLayout.columnWidths = new int[] {40, 155, 75, 75};
		mainPanel.setLayout(mainPanelLayout);

		cmbScanner = new JComboBox();
		cmbScanner.addActionListener(this);

		//lblCanCaptureSlaps = new JLabel("Can capture slaps");
		//lblCanCaptureRolled = new JLabel("Can capture rolled fingers");

		btnOk = new JButton("OK");
		btnOk.addActionListener(this);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);

		GridBagUtils gridBagUtils = new GridBagUtils(GridBagConstraints.BOTH);
		gridBagUtils.setInsets(new Insets(3, 3, 3, 3));

		gridBagUtils.addToGridBagLayout(0, 0, 4, 1, mainPanel, new JLabel("Selected scanner"));
		gridBagUtils.addToGridBagLayout(0, 1, mainPanel, cmbScanner);
		//gridBagUtils.addToGridBagLayout(1, 2, 1, 1, mainPanel, lblCanCaptureSlaps);
		//gridBagUtils.addToGridBagLayout(1, 3, mainPanel, lblCanCaptureRolled);
		gridBagUtils.addToGridBagLayout(2, 4, mainPanel, btnOk);
		gridBagUtils.addToGridBagLayout(3, 4, mainPanel, btnCancel);

		getContentPane().add(mainPanel);
		pack();
	}

	private void scannerFormLoad() {
		try {
			cmbScanner.removeAllItems();
			for (NDevice item : getDeviceManager().getDevices()) {
				if (item instanceof NFScanner) {
					cmbScanner.addItem(item);
				}
			}

			selectedDevice = getSelectedDevice();
			cmbScanner.setSelectedItem(selectedDevice);

			if (cmbScanner.getSelectedItem() == null && cmbScanner.getItemCount() > 0) {
				cmbScanner.setSelectedIndex(0);
			}
		} finally {
			cmbScanner.updateUI();
		}
	}

	private void cmbScannerSelectedIndexChanged() {
		boolean canCaptureSlaps = false;
		boolean canCaptureRolled = false;
		selectedDevice = (NFScanner) cmbScanner.getSelectedItem();
		if (selectedDevice != null) {
			for (NFPosition item : selectedDevice.getSupportedPositions()) {
				if (!item.isFourFingers()) {
					continue;
				}
				canCaptureSlaps = true;
				break;
			}

			for (NFImpressionType item : selectedDevice.getSupportedImpressionTypes()) {
				if (!item.isRolled()) {
					continue;
				}
				canCaptureRolled = true;
				break;
			}
		}
	/*	if (canCaptureRolled) {
			lblCanCaptureRolled.setIcon(Utils.createIcon("images/Accept.png"));
		} else {
			lblCanCaptureRolled.setIcon(Utils.createIcon("com/esmc/client/images/Bad.png"));
		}

		if (canCaptureSlaps) {
			lblCanCaptureSlaps.setIcon(Utils.createIcon("com/esmc/client/images/Accept.png"));
		} else {
			lblCanCaptureSlaps.setIcon(Utils.createIcon("com/esmc/client/images/Bad.png"));
		}*/
	}

	private void btnOkClick() {
		if (selectedDevice != null) {
			setSelectedDevice(selectedDevice);
			isDialogResultOk = true;
			dispose();
		} else {
			Utilities.showWarning(this, "Scanner not selected");
		}
	}

	private void cancellingDeviceDialog() {
		dispose();
	}

	// ==============================================
	// Public methods
	// ==============================================

	public NDeviceManager getDeviceManager() {
		return deviceMan;
	}

	public void setDeviceManager(NDeviceManager deviceMan) {
		this.deviceMan = deviceMan;
	}

	public NFScanner getSelectedDevice() {
		return selectedDevice;
	}

	public void setSelectedDevice(NFScanner selectedDevice) {
		this.selectedDevice = selectedDevice;
	}

	public boolean isDialogResultOk() {
		return isDialogResultOk;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Object source = ev.getSource();
		if (source == btnOk) {
			btnOkClick();
		} else if (source == btnCancel) {
			cancellingDeviceDialog();
		} else if (source == cmbScanner) {
			cmbScannerSelectedIndexChanged();
		}
	}

}
