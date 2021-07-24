package com.esmc.client.swing.controls;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.esmc.client.settings.*;
import com.esmc.client.swing.*;
import com.esmc.client.swing.utils.GridBagUtils;
import com.esmc.client.swing.utils.PictureCapturingDialog;
import com.esmc.client.utils.*;
import com.neurotec.biometrics.NFace;
import com.neurotec.biometrics.swing.NFaceView;
import com.neurotec.images.NImage;
import com.neurotec.images.NImages;
import com.neurotec.licensing.NLicense;



public final class InfoPanel extends JPanel {

	// ==============================================
	// Private classes
	// ==============================================

	private class PropertyTableModel extends DefaultTableModel {

		// ==============================================
		// Private static fields
		// ==============================================

		private static final long serialVersionUID = 1L;

		// ==============================================
		// Private fields
		// ==============================================

		private String[] columnNames = {"key", "value"};
		private List<InfoField> values;

		// ==============================================
		// Private methods
		// ==============================================

		public void clearAllData() {
			if (getRowCount() > 0) {
				for (int i = getRowCount() - 1; i > -1; i--) {
					removeRow(i);
				}
			}
		}

		// ==============================================
		// Public overridden methods
		// ==============================================

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int column) {
			try {
				return columnNames[column];
			} catch (Exception e) {
				return super.getColumnName(column);
			}
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return column == 1;
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if (values != null) {
				values.get(row).setValue(aValue);
			}
			super.setValueAt(aValue, row, column);
		}

		public void setValues(List<InfoField> values) {
			clearAllData();
			this.values = values;
			for (InfoField info : values) {
				addRow(new Object[] {info.getKey(), info.getValue()});
			}
		}
	}

	// ==============================================
	// Private static fields
	// ==============================================

	private static final long serialVersionUID = 1L;

	// ==============================================
	// Private fields
	// ==============================================

	private NFaceView thumbnailImageView;
	private PropertyTableModel tableModel;

	private final Frame owner;

	// ==============================================
	// Public constructor
	// ==============================================

	public InfoPanel(Frame owner) {
		this.owner = owner;
		initializeComponents();
		onModelChanged();
	}

	// ==============================================
	// Private methods
	// ==============================================

	private void initializeComponents() {
		setLayout(new BorderLayout());

		JPanel thumbnailPanel = new JPanel();
		GridBagLayout thumbnailPanelLayout = new GridBagLayout();
		thumbnailPanelLayout.columnWidths = new int[] {90, 92, 95, 85, 5};
		thumbnailPanelLayout.rowHeights = new int[] {30, 1, 195, 5};
		thumbnailPanel.setLayout(thumbnailPanelLayout);

		JButton btnCapture = new JButton("Capture");
		btnCapture.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnCaptureClick();
			}
		});
		try {
			btnCapture.setEnabled(NLicense.isComponentActivated("Devices.Cameras"));
		} catch (IOException e) {
			btnCapture.setEnabled(false);
			e.printStackTrace();
		}

		JButton btnOpenImage = new JButton("Open image");
		btnOpenImage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnOpenClick();
			}
		});

		JLabel lblThumbnailKey = new JLabel("Thumbnail");
		thumbnailImageView = new NFaceView();
		thumbnailImageView.setAutofit(true);
		thumbnailImageView.setFace(EnrollmentDataModel.getInstance().getThumbFace());

		tableModel = new PropertyTableModel();
		JTable propertyTable = new JTable(tableModel);
		propertyTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		GridBagUtils leftPanelGridBagUtils = new GridBagUtils(GridBagConstraints.BOTH);
		leftPanelGridBagUtils.setInsets(new Insets(2, 2, 2, 2));
		leftPanelGridBagUtils.addToGridBagLayout(0, 0, 1, 1, 20, 0, thumbnailPanel, lblThumbnailKey);
		leftPanelGridBagUtils.addToGridBagLayout(2, 0, 1, 1, 1, 0, thumbnailPanel, btnCapture);
		leftPanelGridBagUtils.addToGridBagLayout(3, 0, 1, 1, 1, 0, thumbnailPanel, btnOpenImage);
		leftPanelGridBagUtils.addToGridBagLayout(0, 1, thumbnailPanel, new JLabel());
		leftPanelGridBagUtils.addToGridBagLayout(0, 2, 4, 1, 1, 1, thumbnailPanel, new JScrollPane(thumbnailImageView));

		JSplitPane infoSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, thumbnailPanel, propertyTable);
		infoSplitPane.setDividerLocation(365);
		infoSplitPane.setResizeWeight(.385);
		infoSplitPane.setEnabled(false);
		add(infoSplitPane);
	}

	private void btnOpenClick() {
		JFileChooser openFileDialog = new JFileChooser();
		openFileDialog.addChoosableFileFilter(new Utils.ImageFileFilter(NImages.getOpenFileFilterString(true, false)));
		if (openFileDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				NImage image = NImage.fromFile(openFileDialog.getSelectedFile().getPath());
				NFace face = new NFace();
				face.setImage(image);
				EnrollmentDataModel.getInstance().setThumbFace(face);
				thumbnailImageView.setFace(face);
			} catch (Exception e) {
				Utilities.showError(this, e);
			}
		}
	}

	private void btnCaptureClick() {
		if (EnrollmentDataModel.getInstance().getBiometricClient().getFaceCaptureDevice() == null) {
			Utilities.showInformation(this, "No cameras connected. Please connect camera and try again");
			return;
		}

		PictureCapturingDialog form = new PictureCapturingDialog(owner);
		form.setVisible(true);
		thumbnailImageView.setFace(EnrollmentDataModel.getInstance().getThumbFace());
	}

	// ==============================================
	// Public methods
	// ==============================================

	public void onModelChanged() {
		List<InfoField> editableInfo = new ArrayList<InfoField>();
		for (InfoField i : EnrollmentDataModel.getInstance().getInfo()) {
			if (i.isShowAsThumbnail()) {
				continue;
			}
			editableInfo.add(i);
		}
		thumbnailImageView.setFace(EnrollmentDataModel.getInstance().getThumbFace());
		tableModel.setValues(editableInfo);

	}

}
