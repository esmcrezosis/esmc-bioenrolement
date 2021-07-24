package com.esmc.client.swing.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.neurotec.biometrics.NTemplateSize;
import com.neurotec.biometrics.client.NBiometricClient;
import com.esmc.client.utils.Utilities;
import com.esmc.client.settings.EnrollmentDataModel;

public final class ExtractionOptionsDialog extends JDialog implements ActionListener {

	// ==============================================
	// Private static fields
	// ==============================================

	private static final long serialVersionUID = 1L;

	// ==============================================
	// Private fields
	// ==============================================

	private final NBiometricClient biometricClient;
	private JComboBox cmbTemplateSize;

	private JCheckBox chkFastExtraction;

	private JSpinner spinnerQualityThreshold;
	private JSpinner spinnerMaximalRotation;

	private JButton btnDefault;
	private JButton btnOk;
	private JButton btnCancel;

	private GridBagUtils gridBagUtils;

	// ==============================================
	// Public constructor
	// ==============================================

	public ExtractionOptionsDialog(Frame owner) {
		super(owner, "Extraction Options", true);
		setPreferredSize(new Dimension(280, 175));
		setResizable(false);
		initializeComponents();
		updateComboboxes();
		setLocationRelativeTo(owner);
		biometricClient = EnrollmentDataModel.getInstance().getBiometricClient();
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {
				optionsFormLoad();
			}
		});
	}

	// ==============================================
	// Private methods
	// ==============================================

	private void initializeComponents() {
		getContentPane().setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		GridBagLayout mainPanelLayout = new GridBagLayout();
		mainPanelLayout.columnWidths = new int[] {5, 90, 20, 110};
		mainPanel.setLayout(mainPanelLayout);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		cmbTemplateSize = new JComboBox();
		chkFastExtraction = new JCheckBox("Fast extraction");

		spinnerQualityThreshold = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		spinnerMaximalRotation = new JSpinner(new SpinnerNumberModel(0, 0, 180, 1));

		gridBagUtils = new GridBagUtils(GridBagConstraints.BOTH);
		gridBagUtils.setInsets(new Insets(2, 2, 2, 2));

		gridBagUtils.addToGridBagLayout(1, 0, mainPanel, new JLabel("Template size:"));
		gridBagUtils.addToGridBagLayout(1, 1, mainPanel, new JLabel("Quality threshold:"));
		gridBagUtils.addToGridBagLayout(1, 2, mainPanel, new JLabel("Maximal rotation:"));
		gridBagUtils.addToGridBagLayout(3, 2, 1, 1, mainPanel, spinnerMaximalRotation);
		gridBagUtils.addToGridBagLayout(3, 1, 1, 1, mainPanel, spinnerQualityThreshold);
		gridBagUtils.addToGridBagLayout(3, 0, 1, 1, mainPanel, cmbTemplateSize);

		JPanel chkBoxPanel = new JPanel(new GridLayout(1, 2));
		chkBoxPanel.add(new JLabel(""));
		chkBoxPanel.add(chkFastExtraction);

		btnDefault = new JButton("Default");
		btnDefault.addActionListener(this);

		btnOk = new JButton("OK");
		btnOk.addActionListener(this);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 3, 3, 3));
		buttonPanel.add(btnDefault);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(btnOk);
		buttonPanel.add(Box.createHorizontalStrut(5));
		buttonPanel.add(btnCancel);

		getContentPane().add(mainPanel, BorderLayout.NORTH);
		getContentPane().add(chkBoxPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.AFTER_LAST_LINE);

		pack();
	}

	private void updateComboboxes() {
		cmbTemplateSize.addItem(NTemplateSize.SMALL);
		cmbTemplateSize.addItem(NTemplateSize.MEDIUM);
		cmbTemplateSize.addItem(NTemplateSize.LARGE);
	}

	private boolean saveSettings() {
		try {
			biometricClient.setFingersTemplateSize((NTemplateSize) cmbTemplateSize.getSelectedItem());
			biometricClient.setFingersQualityThreshold(Byte.valueOf(spinnerQualityThreshold.getValue().toString()));
			biometricClient.setFingersMaximalRotation(Float.valueOf(spinnerMaximalRotation.getValue().toString()));
			biometricClient.setFingersFastExtraction(chkFastExtraction.isSelected());
			return true;
		} catch (Exception e) {
			Utilities.showError(this, "Failed to set value: %s", e.getMessage());
			return false;
		}
	}

	private void loadSettings() {
		cmbTemplateSize.setSelectedItem(biometricClient.getFingersTemplateSize());
		spinnerQualityThreshold.setValue(biometricClient.getFingersQualityThreshold());
		spinnerMaximalRotation.setValue(biometricClient.getFingersMaximalRotation());
		chkFastExtraction.setSelected(biometricClient.isFingersFastExtraction());
	}

	private void resetToDefault() {
		biometricClient.resetProperty("Fingers.TemplateSize");
		biometricClient.resetProperty("Fingers.QualityThreshold");
		biometricClient.resetProperty("Fingers.MaximalRotation");
		biometricClient.resetProperty("Fingers.FastExtraction");
		loadSettings();
	}

	private void optionsFormLoad() {
		loadSettings();
	}

	// ==============================================
	// Public methods
	// ==============================================

	@Override
	public void actionPerformed(ActionEvent ev) {
		Object source = ev.getSource();
		if (source == btnOk) {
			saveSettings();
			dispose();
		} else if (source == btnDefault) {
			resetToDefault();
		} else if (source == btnCancel) {
			dispose();
		}
	}

}
