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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.esmc.client.settings.EnrollmentDataModel;
import com.esmc.client.settings.EnrollmentSettings;
import com.esmc.client.settings.InfoField;
import com.esmc.client.utils.Utils;

import com.esmc.client.utils.Utilities;

public final class EditInfoDialog extends JDialog implements ActionListener {

	// ==============================================
	// Private classes
	// ==============================================

	private final class DataTableModel extends DefaultTableModel {

		// ==============================================
		// Private static fields
		// ==============================================

		private static final long serialVersionUID = 1L;

		// ==============================================
		// Private fields
		// ==============================================

		private final String[] columnNames = {"Key"};

		// ==============================================
		// Private methods
		// ==============================================

		private void clearAllData() {
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
			return columnNames.length;
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
			return true;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return super.getColumnClass(columnIndex);
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			super.setValueAt(aValue, row, column);
			if (column == 0 && aValue != null && !aValue.equals("")) {
				dataTableCellEdited();
				newRowIndex = -1;
			}
		}

	}

	// ==============================================
	// Private static fields
	// ==============================================

	private static final long serialVersionUID = 1L;

	// ==============================================
	// Private GUI controls
	// ==============================================

	private JButton btnUp;
	private JButton btnDown;
	private JButton btnAdd;
	private JButton btnDelete;
	private JTable dataTable;
	private DataTableModel dataTableModel;
	private JLabel lblThumbnail;
	private JComboBox cmbThumbnail;
	private JButton btnOk;
	private JButton btnCancel;

	// ==============================================
	// Private fields
	// ==============================================

	private boolean isDialogResultOk;
	private int newRowIndex = -1;
	private GridBagUtils gridBagUtils;

	private final EnrollmentDataModel dataModel = EnrollmentDataModel.getInstance();

	// ==============================================
	// Public constructor
	// ==============================================

	public EditInfoDialog(Frame owner) {
		super(owner, "Edit Info", true);
		setPreferredSize(new Dimension(620, 436));
		setResizable(true);
		initializeComponents();
		setLocationRelativeTo(owner);

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {
				editInfoFormLoad();
			}

		});
	}

	// ==============================================
	// Private methods
	// ==============================================

	private void initializeComponents() {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 3));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);

		btnOk = new JButton("OK");
		btnOk.setPreferredSize(btnCancel.getPreferredSize());
		btnOk.addActionListener(this);

		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(btnOk);
		buttonPanel.add(Box.createHorizontalStrut(4));
		buttonPanel.add(btnCancel);

		gridBagUtils = new GridBagUtils(GridBagConstraints.BOTH);

		mainPanel.add(createTopPanel());
		mainPanel.add(buttonPanel);

		getContentPane().add(mainPanel);
		pack();
	}

	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		GridBagLayout topPanelLayout = new GridBagLayout();
		topPanelLayout.columnWidths = new int[] {30, 110, 450};
		topPanelLayout.rowHeights = new int[] {32, 32, 32, 32, 20, 5, 25};
		topPanel.setLayout(topPanelLayout);

		btnUp = new JButton(Utils.createIcon("images/ArrowUp.png"));
		btnUp.addActionListener(this);

		btnDown = new JButton(Utils.createIcon("images/ArrowDown.png"));
		btnDown.addActionListener(this);

		btnAdd = new JButton(Utils.createIcon("images/Add.png"));
		btnAdd.addActionListener(this);

		btnDelete = new JButton(Utils.createIcon("images/Bad.png"));
		btnDelete.addActionListener(this);

		dataTableModel = new DataTableModel();
		dataTable = new JTable(dataTableModel);
		dataTable.getTableHeader().setReorderingAllowed(false);
		dataTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dataTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		JScrollPane tableScrollPane = new JScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		lblThumbnail = new JLabel("Subject image field:");
		cmbThumbnail = new JComboBox();

		gridBagUtils.setInsets(new Insets(3, 3, 3, 3));
		gridBagUtils.addToGridBagLayout(0, 0, topPanel, btnUp);
		gridBagUtils.addToGridBagLayout(0, 1, topPanel, btnDown);
		gridBagUtils.addToGridBagLayout(0, 2, topPanel, btnAdd);
		gridBagUtils.addToGridBagLayout(0, 3, topPanel, btnDelete);
		gridBagUtils.addToGridBagLayout(0, 4, 1, 1, 0, 1, topPanel, new JLabel());
		gridBagUtils.addToGridBagLayout(1, 0, 2, 5, 1, 0, topPanel, tableScrollPane);
		gridBagUtils.addToGridBagLayout(1, 5, 1, 1, topPanel, lblThumbnail);
		gridBagUtils.addToGridBagLayout(2, 5, 1, 1, topPanel, cmbThumbnail);
		gridBagUtils.clearGridBagConstraints();

		return topPanel;
	}

	private void editInfoFormLoad() {
		dataTableModel.clearAllData();
		for (InfoField item : dataModel.getInfo()) {
			Object[] values = new Object[] {item.getKey(), item.isEnrollToServer()};
			dataTableModel.addRow(values);
		}
		updateComboBox(cmbThumbnail);

		EnrollmentSettings settings = EnrollmentSettings.getInstance();
		cmbThumbnail.setSelectedItem(settings.getThumbnailField());
	}

	private void moveUp() {
		int[] rows = dataTable.getSelectedRows();
		if (rows.length > 0) {
			int row = rows[0];
			if (row != 0) {
				Object temp = dataTableModel.getValueAt(row - 1, 0);
				dataTableModel.setValueAt(dataTableModel.getValueAt(row, 0), row - 1, 0);
				dataTableModel.setValueAt(temp, row, 0);

				dataTable.clearSelection();
				dataTable.getSelectionModel().setSelectionInterval(row - 1, row - 1);
			}
		}
		updateComboBox(cmbThumbnail);
	}

	private void moveDown() {
		int[] rows = dataTable.getSelectedRows();
		if (rows.length > 0) {
			int row = rows[0];
			if (row != dataTable.getRowCount() - 1) {
				Object temp = dataTableModel.getValueAt(row + 1, 0);
				dataTableModel.setValueAt(dataTableModel.getValueAt(row, 0), row + 1, 0);
				dataTableModel.setValueAt(temp, row, 0);

				dataTable.clearSelection();
				dataTable.getSelectionModel().setSelectionInterval(row + 1, row + 1);
			}
		}
		updateComboBox(cmbThumbnail);
	}

	private void dataTableCellEdited() {
		int row = dataTable.getSelectedRow();
		int thumbnail = cmbThumbnail.getSelectedIndex();
		updateComboBox(cmbThumbnail);

		if (thumbnail == row) {
			cmbThumbnail.setSelectedIndex(thumbnail);
		}
	}

	private void add() {
		if (newRowIndex == -1) {
			dataTableModel.addRow(new Object[] {""});
			newRowIndex = dataTable.getRowCount() - 1;
			dataTable.editCellAt(dataTable.getRowCount() - 1, 0);
		}
	}

	private void delete() {
		int[] rows = dataTable.getSelectedRows();
		if (rows.length > 0) {
			dataTableModel.removeRow(rows[0]);
		}
		updateComboBox(cmbThumbnail);
	}

	private void accept() {
		for (int i = 0; i < dataTable.getRowCount(); i++) {
			if (i != newRowIndex && dataTableModel.getValueAt(i, 0) == null || ((String) dataTableModel.getValueAt(i, 0)).equals("")) {
				Utilities.showError(this, "Key value is invalid");
				return;
			}
		}

		if (dataTable.getRowCount() <= 1) {
			Utilities.showError(this, "Create at least one row of information description");
			return;
		}

		EnrollmentSettings settings = EnrollmentSettings.getInstance();
		settings.setThumbnailField((String) cmbThumbnail.getSelectedItem());

		List<InfoField> fields = new ArrayList<InfoField>();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < dataTable.getRowCount(); i++) {
			if (i == newRowIndex) {
				continue;
			}
			InfoField inf = new InfoField();
			inf.setKey((String) dataTableModel.getValueAt(i, 0));
			if (settings.getThumbnailField() != null) {
				inf.setShowAsThumbnail(settings.getThumbnailField().equals(inf.getKey()));
			}
			inf.setEditable(!inf.isShowAsThumbnail());
			if (inf.getKey() != null) {
				inf.setKey(inf.getKey().trim());
			}

			fields.add(inf);
			builder.append(String.format("%s;", inf));
		}

		settings.setInformation(builder.toString());
		settings.save();

		dataModel.getInfo().clear();
		for (InfoField i : fields) {
			dataModel.getInfo().add(i);
		}
		isDialogResultOk = true;
		dispose();
	}

	private void updateComboBox(JComboBox combo) {
		try {
			String selected = (String) combo.getSelectedItem();
			combo.removeAllItems();
			for (int i = 0; i < dataTable.getRowCount(); i++) {
				Object value = dataTable.getModel().getValueAt(i, 0);
				if (value != null) {
					combo.addItem(value);
				}
			}
			combo.setSelectedItem(selected);
		} finally {
			combo.updateUI();
		}
	}

	// ==============================================
	// Public methods
	// ==============================================

	public boolean isDialogResultOk() {
		return isDialogResultOk;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Object source = ev.getSource();
		if (source == btnUp) {
			moveUp();
		} else if (source == btnDown) {
			moveDown();
		} else if (source == btnDelete) {
			delete();
		} else if (source == btnOk) {
			accept();
		} else if (source == btnCancel) {
			dispose();
		} else if (source == btnAdd) {
			add();
		}
	}

}
