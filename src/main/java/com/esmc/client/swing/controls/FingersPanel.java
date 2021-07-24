package com.esmc.client.swing.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import com.neurotec.biometrics.NFPosition;
import com.neurotec.biometrics.NFrictionRidge;
import com.neurotec.biometrics.swing.NFingerView;
import com.neurotec.biometrics.swing.NFingerViewBase.ShownImage;
import com.neurotec.images.NImage;
import com.esmc.client.settings.EnrollmentSettings;
import com.esmc.client.events.*;
import com.esmc.client.events.NFingerViewMouseMotionListener;
import com.esmc.client.swing.utils.*;

public final class FingersPanel extends JPanel implements ActionListener {

	// ==============================================
	// Private static fields
	// ==============================================

	private static final long serialVersionUID = 1L;

	// ==============================================
	// Private fields
	// ==============================================

	private final String[] fingerNames = new String[] {"Thumb", "Index", "Middle", "Ring", "Little"};

	private JCheckBox chkShowOriginal;
	private final NFingerView[] nfViews = new NFingerView[10];

	private final FingersViewToolBar toolBar;
	private FingersPanelPropertyChangedListner listener;

	// ==============================================
	// Public constructor
	// ==============================================

	public FingersPanel(FingersViewToolBar toolBar) {
		this.toolBar = toolBar;
		initializeComponents();
	}

	// ==============================================
	// Private methods
	// ==============================================

	private void initializeComponents() {

		GridBagLayout fingersPanelLayout = new GridBagLayout();
		fingersPanelLayout.rowHeights = new int[] {20, 20, 160, 20, 160};
		fingersPanelLayout.columnWidths = new int[] {190, 190, 190, 190, 190};
		setLayout(fingersPanelLayout);

		chkShowOriginal = new JCheckBox("Show original images");
		chkShowOriginal.addActionListener(this);

		GridBagUtils gridBagUtis = new GridBagUtils(GridBagConstraints.BOTH);
		gridBagUtis.setInsets(new Insets(1, 1, 1, 1));
		gridBagUtis.addToGridBagLayout(0, 0, 1, 1, 0.2, 0, this, chkShowOriginal);

		for (int i = 0; i < 5; i++) {
			gridBagUtis.addToGridBagLayout(i, 1, 1, 1, 0.2, 0, this, new JLabel(String.format("Left %s", fingerNames[i])));
		}

		for (int i = 0; i < 5; i++) {
			NFingerView nfView = new NFingerView();
			JPanel nfViewPanel = new JPanel();
			OverlayLayout overlay = new OverlayLayout(nfViewPanel);
			nfViewPanel.setLayout(overlay);
			nfViewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			nfView.setAlignmentX(0);
			nfView.setAlignmentY(0);
			nfView.addMouseListener(new NFingerViewMouseMotionListener(toolBar));
			nfViewPanel.add(nfView);
			nfViewPanel.setPreferredSize(nfViewPanel.getPreferredSize());
			gridBagUtis.addToGridBagLayout(i, 2, 1, 1, 0.2, 0.5, this, nfViewPanel);
			nfViews[i] = nfView;
		}

		for (int i = 0; i < 5; i++) {
			gridBagUtis.addToGridBagLayout(i, 3, 1, 1, 0.2, 0, this, new JLabel(String.format("Right %s", fingerNames[i])));
		}

		for (int i = 5; i < 10; i++) {
			NFingerView nfView = new NFingerView();
			JPanel nfViewPanel = new JPanel();
			OverlayLayout overlay = new OverlayLayout(nfViewPanel);
			nfViewPanel.setLayout(overlay);
			nfViewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			nfView.setAlignmentX(0);
			nfView.setAlignmentY(0);
			nfView.addMouseListener(new NFingerViewMouseMotionListener(toolBar));
			nfViewPanel.add(nfView);
			nfViewPanel.setPreferredSize(nfViewPanel.getPreferredSize());
			gridBagUtis.addToGridBagLayout(i - 5, 4, 1, 1, 0.2, 0.5, this, nfViewPanel);
			nfViews[i] = nfView;
		}
		showOriginal(EnrollmentSettings.getInstance().isShowOriginal());
	}

	private static void zoomView(NFingerView view) {
		float zoom = 1.0f;
		NFrictionRidge finger = view.getFinger();
		if (finger != null) {
			NImage image = finger.getImage(false);
			if (image != null) {
				Dimension clientSize = view.getSize();
				int imageWidth = image.getWidth();
				int imageHeight = image.getHeight();
				zoom = Math.min((float) clientSize.width / imageWidth, (float) clientSize.height / imageHeight);
				zoom = Math.max(0.01f, zoom);
			}
		}
		view.setScale(zoom);
		view.repaint();
	}

	// ==============================================
	// Public methods
	// ==============================================

	public NFingerView getView(NFPosition position) {
		switch (position) {
		case LEFT_INDEX_FINGER:
			return nfViews[1];
		case LEFT_LITTLE_FINGER:
			return nfViews[4];
		case LEFT_MIDDLE_FINGER:
			return nfViews[2];
		case LEFT_RING_FINGER:
			return nfViews[3];
		case LEFT_THUMB:
			return nfViews[0];
		case RIGHT_INDEX_FINGER:
			return nfViews[6];
		case RIGHT_LITTLE_FINGER:
			return nfViews[9];
		case RIGHT_MIDDLE_FINGER:
			return nfViews[7];
		case RIGHT_RING_FINGER:
			return nfViews[8];
		case RIGHT_THUMB:
			return nfViews[5];
		case PLAIN_LEFT_FOUR_FINGERS:
		case PLAIN_RIGHT_FOUR_FINGERS:
		case PLAIN_THUMBS:
		case LEFT_FULL_PALM:
		case LEFT_HYPOTHENAR:
		case LEFT_INDEX_MIDDLE_FINGERS:
		case LEFT_INDEX_MIDDLE_RING_FINGERS:
		case LEFT_INTERDIGITAL:
		case LEFT_LOWER_PALM:
		case LEFT_MIDDLE_RING_FINGERS:
		case LEFT_MIDDLE_RING_LITTLE_FINGERS:
		case LEFT_OTHER:
		case LEFT_RING_LITTLE_FINGERS:
		case LEFT_THENAR:
		case LEFT_UPPER_PALM:
		case LEFT_WRITERS_PALM:
		case PLAIN_LEFT_THUMB:
		case PLAIN_RIGHT_THUMB:
		case RIGHT_FULL_PALM:
		case RIGHT_HYPOTHENAR:
		case RIGHT_INDEX_LEFT_INDEX_FINGERS:
		case RIGHT_INDEX_MIDDLE_FINGERS:
		case RIGHT_INDEX_MIDDLE_RING_FINGERS:
		case RIGHT_INTERDIGITAL:
		case RIGHT_LOWER_PALM:
		case RIGHT_MIDDLE_RING_FINGERS:
		case RIGHT_MIDDLE_RING_LITTLE_FINGERS:
		case RIGHT_OTHER:
		case RIGHT_RING_LITTLE_FINGERS:
		case RIGHT_THENAR:
		case RIGHT_UPPER_PALM:
		case RIGHT_WRITERS_PALM:
		case UNKNOWN:
		case UNKNOWN_FOUR_FINGERS:
		case UNKNOWN_PALM:
		case UNKNOWN_THREE_FINGERS:
		case UNKNOWN_TWO_FINGERS:
			return null;
		default:
			throw new AssertionError("Cannot happen");
		}
	}

	public void addFingersPanelPropertyChangedListner(FingersPanelPropertyChangedListner listener) {
		this.listener = listener;
	}

	public void zoomViews() {
		for (NFingerView view : nfViews) {
			zoomView(view);
		}
	}

	public void setShowOriginal(boolean show) {
		chkShowOriginal.setSelected(show);
		showOriginal(show);
	}

	public boolean isShowOriginal() {
		return chkShowOriginal.isSelected();
	}

	public void showOriginal(boolean selected) {
		ShownImage shown = selected ? ShownImage.ORIGINAL : ShownImage.RESULT;
		for (NFingerView view : nfViews) {
			view.setShownImage(shown);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == chkShowOriginal) {
			boolean selected = ((JCheckBox) source).isSelected();
			showOriginal(selected);
			EnrollmentSettings.getInstance().setShowOriginal(selected);
			listener.checkboxPropertyChanged();
		}
	}

}
