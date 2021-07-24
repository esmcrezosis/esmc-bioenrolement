package com.esmc.client.swing.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.neurotec.biometrics.NFPosition;
import com.neurotec.biometrics.swing.NFingerView;
import com.esmc.client.swing.utils.GridBagUtils;

public final class SlapsPanel extends JPanel {

	// ==============================================
	// Private static fields
	// ==============================================

	private static final long serialVersionUID = 1L;

	// ==============================================
	// Private fields
	// ==============================================
	private NFingerView nfvLeftFour;
	private NFingerView nfvRightFour;
	private NFingerView nfvThumbs;

	// ==============================================
	// Public constructor
	// ==============================================

	public SlapsPanel() {
		initializeComponents();
	}

	// ==============================================
	// Private methods
	// ==============================================

	private void initializeComponents() {
		GridBagLayout slapsPanelLayout = new GridBagLayout();
		slapsPanelLayout.rowHeights = new int[] {20, 365};
		setLayout(slapsPanelLayout);

		nfvLeftFour = new NFingerView();
		nfvLeftFour.setAutofit(true);
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		leftPanel.add(nfvLeftFour, BorderLayout.CENTER);
		leftPanel.setPreferredSize(leftPanel.getPreferredSize());
		JScrollPane leftScrollPane = new JScrollPane(leftPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		nfvThumbs = new NFingerView();
		nfvThumbs.setAutofit(true);
		JPanel thumbsPanel = new JPanel(new BorderLayout());
		thumbsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		thumbsPanel.add(nfvThumbs, BorderLayout.CENTER);
		thumbsPanel.setPreferredSize(thumbsPanel.getPreferredSize());
		JScrollPane thumbScrollPane = new JScrollPane(thumbsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		nfvRightFour = new NFingerView();
		nfvRightFour.setAutofit(true);
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		rightPanel.add(nfvRightFour, BorderLayout.CENTER);
		rightPanel.setPreferredSize(rightPanel.getPreferredSize());
		JScrollPane rightScrollPane = new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		GridBagUtils gridBagUtils = new GridBagUtils(GridBagConstraints.BOTH);
		gridBagUtils.setInsets(new Insets(1, 1, 1, 1));

		gridBagUtils.addToGridBagLayout(0, 0, 1, 1, 0.3, 0, this, new JLabel("Left four fingers"));
		gridBagUtils.addToGridBagLayout(1, 0, this, new JLabel("Thumbs"));
		gridBagUtils.addToGridBagLayout(2, 0, this, new JLabel("Right four fingers"));
		gridBagUtils.addToGridBagLayout(0, 1, 1, 1, 0.3, 1, this, leftScrollPane);
		gridBagUtils.addToGridBagLayout(1, 1, this, thumbScrollPane);
		gridBagUtils.addToGridBagLayout(2, 1, this, rightScrollPane);

	}

	// ==============================================
	// Public methods
	// ==============================================

	public NFingerView getView(NFPosition position) {
		if (position == NFPosition.PLAIN_LEFT_FOUR_FINGERS) {
			return nfvLeftFour;
		} else if (position == NFPosition.PLAIN_RIGHT_FOUR_FINGERS) {
			return nfvRightFour;
		} else if (position == NFPosition.PLAIN_THUMBS) {
			return nfvThumbs;
		} else {
			return null;
		}
	}

}
