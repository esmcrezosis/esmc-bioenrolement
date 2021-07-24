package com.esmc.client.swing.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JToolBar;

import com.esmc.client.settings.*;
import com.esmc.client.utils.*;
import com.neurotec.biometrics.NFRecord;
import com.neurotec.biometrics.NFinger;
import com.neurotec.images.NImage;
import com.neurotec.images.NImageFormat;
import com.neurotec.images.NImages;
import com.neurotec.io.NBuffer;
import com.neurotec.io.NFile;


public final class FingersViewToolBar extends JToolBar implements ActionListener {

	// ==============================================
	// Private static fields
	// ==============================================

	private static final long serialVersionUID = 1L;

	// ==============================================
	// Private fields
	// ==============================================

	private JButton btnSaveImage;
	private JButton btnSaveRecord;
	private final EnrollmentSettings settings = EnrollmentSettings.getInstance();

	// ==============================================
	// Public constructor
	// ==============================================

	public FingersViewToolBar() {
		initializeComponents();
	}

	// ==============================================
	// Private methods
	// ==============================================

	private void initializeComponents() {
		setFloatable(true);

		btnSaveImage = new JButton("Save Image");
		//btnSaveImage.setIcon(Utils.createIcon("images/Save.png"));
		btnSaveImage.addActionListener(this);

		btnSaveRecord = new JButton("Save Record");
		//btnSaveRecord.setIcon(Utils.createIcon("images/Save.png"));
		btnSaveRecord.addActionListener(this);

		add(btnSaveImage);
		add(btnSaveRecord);
	}

	private void saveImage() {
		String fileName;
		NImage image;
		NFinger finger = (NFinger) getClientProperty("TAG");
		if (finger != null) {
			boolean originalImage = EnrollmentSettings.getInstance().isShowOriginal() && finger.getPosition().isSingleFinger();
			boolean isRolled = finger.getImpressionType().isRolled();
			image = originalImage ? finger.getImage(false) : finger.getBinarizedImage(false);
			fileName = String.format("%s%s%s", Utilities.convertNFPositionNameToCamelCase(finger.getPosition()), isRolled ? "Rolled" : "", originalImage ? "" : "Binarized");

			JFileChooser saveFileDialog = new JFileChooser();
			saveFileDialog.addChoosableFileFilter(new Utils.ImageFileFilter(NImages.getSaveFileFilterString()));
			saveFileDialog.setSelectedFile(new File(fileName));
			if (settings.getLastDirectory() != null) {
				saveFileDialog.setCurrentDirectory(new File(settings.getLastDirectory()));
			}
			if (saveFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					String savePath = saveFileDialog.getSelectedFile().getPath();
					settings.setLastDirectory(saveFileDialog.getSelectedFile().getParent().toString());
					if (saveFileDialog.getSelectedFile().getName().lastIndexOf(".") == -1) {
						savePath = savePath + "." + NImageFormat.getJPEG().getDefaultFileExtension();
					}
					image.save(savePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void saveRecord() {
		NFinger finger = (NFinger) getClientProperty("TAG");
		JFileChooser saveFileDialog = new JFileChooser();
		if (finger != null) {
			boolean isRolled = finger.getImpressionType().isRolled();
			saveFileDialog.setSelectedFile(new File(String.format("%s%s", Utilities.convertNFPositionNameToCamelCase(finger.getPosition()), isRolled ? "Rolled" : "")));
			if (settings.getLastDirectory() != null) {
				saveFileDialog.setCurrentDirectory(new File(settings.getLastDirectory()));
			}
			if (saveFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				NFRecord record = finger.getObjects().get(0).getTemplate();
				NBuffer buffer = record.save();
				settings.setLastDirectory(saveFileDialog.getSelectedFile().getParent().toString());
				try {
					NFile.writeAllBytes(saveFileDialog.getSelectedFile().getPath(), buffer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ==============================================
	// Public methods
	// ==============================================

	public void setSaveRecordVisible(boolean visible) {
		btnSaveRecord.setVisible(visible);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == btnSaveImage) {
			//saveImage();
		} else if (source == btnSaveRecord) {
			//saveRecord();
		}
	}

}
