package com.esmc.client.swing.utils;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

public final class GridBagUtils {

	// ==============================================
	// Private fields
	// ==============================================

	private GridBagConstraints c;

	// ==============================================
	// Public methods
	// ==============================================

	public GridBagUtils(int fill) {
		c = new GridBagConstraints();
		c.fill = fill;
	}

	public GridBagUtils(int fill, Insets insets) {
		c = new GridBagConstraints();
		c.fill = fill;
		c.insets = insets;
	}

	public void setInsets(Insets insets) {
		c.insets = insets;
	}

	public void addToGridBagLayout(int x, int y, JPanel parent, JComponent component) {
		c.gridx = x;
		c.gridy = y;
		parent.add(component, c);
	}

	public void addToGridBagLayout(int x, int y, int width, int height, JPanel parent, JComponent component) {
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.gridheight = height;
		parent.add(component, c);
	}

	public void addToGridBagLayout(int x, int y, int width, int height, double weightX, double weightY, JPanel parent, JComponent component) {
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.gridheight = height;
		c.weightx = weightX;
		c.weighty = weightY;
		parent.add(component, c);
	}

	public void clearGridBagConstraints() {
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
	}

}
