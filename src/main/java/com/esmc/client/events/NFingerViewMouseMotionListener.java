package com.esmc.client.events;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.neurotec.biometrics.NFAttributes;
import com.neurotec.biometrics.NFinger;
import com.neurotec.biometrics.swing.NFingerView;
import com.esmc.client.settings.EnrollmentDataModel;
import com.esmc.client.swing.controls.FingersViewToolBar;

public final class NFingerViewMouseMotionListener extends MouseAdapter {

	// ==============================================
	// Private fields
	// ==============================================

	private final FingersViewToolBar toolBar;
	private final EnrollmentDataModel dataModel = EnrollmentDataModel.getInstance();
	private JComponent control;

	// ==============================================
	// Public constructor
	// ==============================================

	public NFingerViewMouseMotionListener(FingersViewToolBar toolBar) {
		this.toolBar = toolBar;
	}

	// ==============================================
	// Public methods
	// ==============================================

	@Override
	public void mouseEntered(MouseEvent ev) {
		if (EnrollmentDataModel.getInstance().getSubject() == null) {
			return;
		}
		JComponent sourceComponent = (JComponent) ev.getSource();
		if (sourceComponent instanceof JPanel) {
			control = sourceComponent;
		} else if (sourceComponent instanceof NFingerView) {
			control = (JComponent) sourceComponent.getParent();
		}
		if (control == null) {
			return;
		}
		NFinger finger = (NFinger) control.getClientProperty("TAG");
		if (toolBar.getParent() != null) {
			toolBar.getParent().remove(toolBar);
		}
		if (finger != null) {
			if (finger != dataModel.getBiometricClient().getCurrentBiometric()) {
				if (finger.getImage() != null) {
					boolean canSaveRecord = false;
					if (finger.getPosition().isSingleFinger()) {
						for (NFAttributes item : finger.getObjects()) {
							if (item.getTemplate() != null) {
								canSaveRecord = true;
								break;
							}
						}
					}
					toolBar.setSaveRecordVisible(canSaveRecord);
					toolBar.putClientProperty("TAG", finger);
					JComponent childComponent = (JComponent) control.getComponent(0);
					if (childComponent != null) {
						childComponent.setAlignmentX(0);
						childComponent.setAlignmentY(0);
					}
					control.removeAll();
					toolBar.setAlignmentX(0);
					toolBar.setAlignmentY(0);
					control.add(toolBar);
					control.add(childComponent);
					toolBar.setVisible(true);
				}

			}
		}

	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (!toolBar.getBounds().contains(e.getPoint())) {
			toolBar.setVisible(false);
			toolBar.putClientProperty("TAG", null);
		}

	}

}
