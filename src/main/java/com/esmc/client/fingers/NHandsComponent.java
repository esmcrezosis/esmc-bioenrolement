package com.esmc.client.fingers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.neurotec.biometrics.NFPosition;
import com.esmc.client.fingers.HandComponent.Position;

public class NHandsComponent extends JPanel {

	// ===========================================================
	// Nested classes
	// ===========================================================
	private static final long serialVersionUID = 1L;

	private enum VisualisationMode {
		DEFAULT, FINGERPRINT, FINGER, MARKER
	};

	// ===========================================================
	// Private fields
	// ===========================================================

	private HandComponent leftHand;
	private HandComponent rightHand;
	private JPanel handsPanel;
	private List<PositionMap> leftHandPositionMappings;
	private List<PositionMap> rightHandPositionMappings;
	private List<List<Position>> positionRelations;

	private Color missingFillColor;
	private Color highlightedFillColor;
	private Color selectedFillColor;
	private Color hooverFillColor;

	private Color missingStrokeColor;
	private Color highlightedStrokeColor;
	private Color selectedStrokeColor;
	private Color hooverStrokeColor;

	private Stroke missingStroke = new BasicStroke(3);
	private Stroke highlightedStroke = new BasicStroke(3);
	private Stroke selectedStroke = new BasicStroke(3);
	private Stroke hooverStroke = new BasicStroke(3);

	// ===========================================================
	// Public constructor
	// ===========================================================

	public NHandsComponent() {
		initGUI();

		leftHandPositionMappings = new ArrayList<PositionMap>();
		rightHandPositionMappings = new ArrayList<PositionMap>();
		positionRelations = new ArrayList<List<Position>>();

		leftHandPositionMappings.add(new PositionMap(NFPosition.PLAIN_THUMBS, Arrays.asList(Position.THUMB)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_THUMB, Arrays.asList(Position.THUMB)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_INDEX_FINGER, Arrays.asList(Position.INDEX_FINGER)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_MIDDLE_FINGER, Arrays.asList(Position.MIDDLE_FINGER)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_RING_FINGER, Arrays.asList(Position.RING_FINGER)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_LITTLE_FINGER, Arrays.asList(Position.LITTLE_FINGER)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.PLAIN_LEFT_THUMB, Arrays.asList(Position.THUMB)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.PLAIN_LEFT_FOUR_FINGERS, Arrays.asList(Position.LITTLE_FINGER, Position.RING_FINGER, Position.MIDDLE_FINGER, Position.INDEX_FINGER)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_FULL_PALM, Arrays.asList(Position.PALM)));
		//leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_WRITERS_PALM, null));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_LOWER_PALM, Arrays.asList(Position.LOWER_PALM)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_UPPER_PALM, Arrays.asList(Position.UPPER_PALM)));
		//leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_OTHER, Arrays.asList(Position.INDEX_FINGER_PRINT))); //other parts of palm
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_INTERDIGITAL, Arrays.asList(Position.INTERDIGITAL)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_THENAR, Arrays.asList(Position.THENAR)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_HYPOTHENAR, Arrays.asList(Position.HYPOTHENAR)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_INDEX_MIDDLE_FINGERS, Arrays.asList(Position.INDEX_FINGER, Position.MIDDLE_FINGER)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_MIDDLE_RING_FINGERS, Arrays.asList(Position.MIDDLE_FINGER, Position.RING_FINGER)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_RING_LITTLE_FINGERS, Arrays.asList(Position.RING_FINGER, Position.LITTLE_FINGER)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_INDEX_MIDDLE_RING_FINGERS, Arrays.asList(Position.INDEX_FINGER, Position.MIDDLE_FINGER, Position.RING_FINGER)));
		leftHandPositionMappings.add(new PositionMap(NFPosition.LEFT_MIDDLE_RING_LITTLE_FINGERS, Arrays.asList(Position.MIDDLE_FINGER, Position.RING_FINGER, Position.LITTLE_FINGER)));

		rightHandPositionMappings.add(new PositionMap(NFPosition.PLAIN_THUMBS, Arrays.asList(Position.THUMB)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_THUMB, Arrays.asList(Position.THUMB)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_INDEX_FINGER, Arrays.asList(Position.INDEX_FINGER)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_MIDDLE_FINGER, Arrays.asList(Position.MIDDLE_FINGER)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_RING_FINGER, Arrays.asList(Position.RING_FINGER)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_LITTLE_FINGER, Arrays.asList(Position.LITTLE_FINGER)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.PLAIN_RIGHT_THUMB, Arrays.asList(Position.THUMB)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.PLAIN_RIGHT_FOUR_FINGERS, Arrays.asList(Position.LITTLE_FINGER, Position.RING_FINGER, Position.MIDDLE_FINGER, Position.INDEX_FINGER)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_FULL_PALM, Arrays.asList(Position.PALM)));
		//rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_WRITERS_PALM, null));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_LOWER_PALM, Arrays.asList(Position.LOWER_PALM)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_UPPER_PALM, Arrays.asList(Position.UPPER_PALM)));
		//rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_OTHER, Arrays.asList(Position.INDEX_FINGER_PRINT))); //other parts of palm
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_INTERDIGITAL, Arrays.asList(Position.INTERDIGITAL)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_THENAR, Arrays.asList(Position.THENAR)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_HYPOTHENAR, Arrays.asList(Position.HYPOTHENAR)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_INDEX_MIDDLE_FINGERS, Arrays.asList(Position.INDEX_FINGER, Position.MIDDLE_FINGER)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_MIDDLE_RING_FINGERS, Arrays.asList(Position.MIDDLE_FINGER, Position.RING_FINGER)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_RING_LITTLE_FINGERS, Arrays.asList(Position.RING_FINGER, Position.LITTLE_FINGER)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_INDEX_MIDDLE_RING_FINGERS, Arrays.asList(Position.INDEX_FINGER, Position.MIDDLE_FINGER, Position.RING_FINGER)));
		rightHandPositionMappings.add(new PositionMap(NFPosition.RIGHT_MIDDLE_RING_LITTLE_FINGERS, Arrays.asList(Position.MIDDLE_FINGER, Position.RING_FINGER, Position.LITTLE_FINGER)));

		positionRelations.add(Arrays.asList(Position.LITTLE_FINGER, Position.LITTLE_FINGER_PRINT, Position.LITTLE_FINGER_PRINT_MARKER));
		positionRelations.add(Arrays.asList(Position.RING_FINGER, Position.RING_FINGER_PRINT, Position.RING_FINGER_PRINT_MARKER));
		positionRelations.add(Arrays.asList(Position.MIDDLE_FINGER, Position.MIDDLE_FINGER_PRINT, Position.MIDDLE_FINGER_PRINT_MARKER));
		positionRelations.add(Arrays.asList(Position.INDEX_FINGER, Position.INDEX_FINGER_PRINT, Position.INDEX_FINGER_PRINT_MARKER));
		positionRelations.add(Arrays.asList(Position.THUMB, Position.THUMB_PRINT, Position.THUMB_PRINT_MARKER));

		setSelectedFillColor(new Color(0, 100, 0)); // Selected fill color: Green
		setMissingFillColor(new Color(255, 0, 0)); // Missing fill color: Red(255,0,0)
		setHooverFillColor(new Color(139, 0, 0)); // Hover fill color: Dark red(139,0,0)
		setHighlightedStrokeColor(Color.BLACK);
		setHighlightedStroke(new BasicStroke(1, 0, 0, 4.0f, null, 0.0f));
		setHooverStrokeColor(Color.BLACK);
		setHooverStroke(new BasicStroke(1, 0, 0, 4.0f, null, 0.0f));
	}

	// ===========================================================
	// Private methods
	// ===========================================================

	private void initGUI() {
		try {
			this.setLayout(null);
			this.setOpaque(false);
			this.setPreferredSize(new java.awt.Dimension(300, 250));
			this.setLayout(new BorderLayout());
			handsPanel = new JPanel();
			this.add(handsPanel, BorderLayout.CENTER);
			handsPanel.setOpaque(false);
			handsPanel.setLayout(new BoxLayout(handsPanel, BoxLayout.X_AXIS));

			leftHand = new HandComponent();
			leftHand.setOpaque(false);
			handsPanel.add(leftHand);

			rightHand = new HandComponent();
			handsPanel.add(rightHand);
			rightHand.setFlipped(true);
			rightHand.setOpaque(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setPositionRotate(NFPosition position, boolean isRotate, VisualisationMode mode) {
		for (Position p : getMappedPositions(position)) {
			Position convertedP = convertPosition(p, mode);
			if (isLeftHand(position)) {
				leftHand.setPositionRotate(convertedP, isRotate);
			}
			if (isRightHand(position)) {
				rightHand.setPositionRotate(convertedP, isRotate);
			}
		}
	}

	private void setPositionFillColor(NFPosition position, Color fillColor, VisualisationMode mode) {
		for (Position p : getMappedPositions(position)) {
			Position convertedP = convertPosition(p, mode);
			if (isLeftHand(position)) {
				leftHand.setPositionFillColor(convertedP, fillColor);
			}
			if (isRightHand(position)) {
				rightHand.setPositionFillColor(convertedP, fillColor);
			}
		}
	}

	private void setPositionStroke(NFPosition position, Stroke stroke, VisualisationMode mode) {
		for (Position p : getMappedPositions(position)) {
			Position convertedP = convertPosition(p, mode);
			if (isLeftHand(position)) {
				leftHand.setPositionStroke(convertedP, stroke);
			}
			if (isRightHand(position)) {
				rightHand.setPositionStroke(convertedP, stroke);
			}
		}
	}

	private void setPositionStrokeColor(NFPosition position, Color strokeColor, VisualisationMode mode) {
		for (Position p : getMappedPositions(position)) {
			Position convertedP = convertPosition(p, mode);
			if (isLeftHand(position)) {
				leftHand.setPositionStrokeColor(convertedP, strokeColor);
			}
			if (isRightHand(position)) {
				rightHand.setPositionStrokeColor(convertedP, strokeColor);
			}
		}
	}

	private List<Position> getPositionRelations(Position position) {
		for (List<Position> relations : positionRelations) {
			if (relations.contains(position)) {
				return relations;
			}
		}
		return null;
	}

	private Position convertPosition(Position position, VisualisationMode mode) {
		List<Position> relations = getPositionRelations(position);
		if (relations != null) {
			if (mode.equals(VisualisationMode.FINGER)) {
				return relations.get(0);
			} else if (mode.equals(VisualisationMode.FINGERPRINT)) {
				return relations.get(1);
			} else if (mode.equals(VisualisationMode.MARKER)) {
				return relations.get(2);
			}
		}

		return position;
	}

	private boolean isLeftHand(NFPosition position) {
		for (PositionMap map : leftHandPositionMappings) {
			if (map.getNfPosition().equals(position)) {
				return true;
			}
		}
		return false;
	}

	private boolean isRightHand(NFPosition position) {
		for (PositionMap map : rightHandPositionMappings) {
			if (map.getNfPosition().equals(position)) {
				return true;
			}
		}
		return false;
	}

	private List<Position> getMappedPositions(NFPosition position) {
		List<Position> positions = new ArrayList<Position>();

		for (PositionMap map : rightHandPositionMappings) {
			if (map.getNfPosition().equals(position)) {
				positions.addAll(map.getPositions());
			}
		}

		for (PositionMap map : leftHandPositionMappings) {
			if (map.getNfPosition().equals(position)) {
				positions.addAll(map.getPositions());
			}
		}

		return positions;
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	public final List<NFPosition> getAffectedPositions(MouseEvent e) {
		if (!(e.getSource() instanceof NHandsComponent)) {
			throw new UnsupportedOperationException("Only MouseEents provided by HandComponent are supported.");
		}

		List<NFPosition> affectedPositions = new ArrayList<NFPosition>();
		List<Position> leftHandPositions = null;
		List<Position> rightHandPositions = null;

		leftHandPositions = leftHand.getAffectedPositions(new MouseEvent(leftHand, e.getID(), e.getWhen(), e.getModifiers(), e.getX() - leftHand.getX(), e.getY() - leftHand.getY(), e.getClickCount(), e.isPopupTrigger()));

		rightHandPositions = rightHand.getAffectedPositions(new MouseEvent(rightHand, e.getID(), e.getWhen(), e.getModifiers(), e.getX() - rightHand.getX(), e.getY() - rightHand.getY(), e.getClickCount(), e.isPopupTrigger()));

		for (PositionMap map : leftHandPositionMappings) {
			for (Position position : leftHandPositions) {
				if (map.getPositions().contains(position)) {
					affectedPositions.add(map.getNfPosition());
				}
			}
		}

		for (PositionMap map : rightHandPositionMappings) {
			for (Position position : rightHandPositions) {
				if (map.getPositions().contains(position)) {
					affectedPositions.add(map.getNfPosition());
				}
			}
		}

		return affectedPositions;
	}

	public final void setPositionRotated(NFPosition position, boolean value) {
		setPositionRotate(position, value, VisualisationMode.FINGER);
		repaint();
	}

	public final void setPositionSelected(NFPosition position, boolean value) {
		if (selectedFillColor != null) {
			Color color = null;
			if (value) {
				color = selectedFillColor;
			}
			setPositionFillColor(position, color, VisualisationMode.MARKER);
		}
		if (selectedStrokeColor != null) {
			Color strokeColor = null;
			if (value) {
				strokeColor = selectedStrokeColor;
			}
			setPositionStrokeColor(position, strokeColor, VisualisationMode.MARKER);
		}
		if (selectedStroke != null) {
			Stroke stroke = null;
			if (value) {
				stroke = selectedStroke;
			}
			setPositionStroke(position, stroke, VisualisationMode.MARKER);
		}
		repaint();
	}

	public final void setPositionHighlighted(NFPosition position, boolean value) {
		if (highlightedFillColor != null) {
			Color color = null;
			if (value) {
				color = highlightedFillColor;
			}
			setPositionFillColor(position, color, VisualisationMode.MARKER);
		}
		if (highlightedStrokeColor != null) {
			Color strokeColor = null;
			if (value) {
				strokeColor = highlightedStrokeColor;
			}
			setPositionStrokeColor(position, strokeColor, VisualisationMode.MARKER);
		}
		if (highlightedStroke != null) {
			Stroke stroke = null;
			if (value) {
				stroke = highlightedStroke;
			}
			setPositionStroke(position, stroke, VisualisationMode.MARKER);
		}
		repaint();
	}

	public final void setPositionHoover(NFPosition position, boolean value) {
		if (hooverFillColor != null) {
			Color color = null;
			if (value) {
				color = hooverFillColor;
			}
			setPositionFillColor(position, color, VisualisationMode.MARKER);
		}

		if (hooverStrokeColor != null) {
			Color strokeColor = null;
			if (value) {
				strokeColor = hooverStrokeColor;
			}
			setPositionStrokeColor(position, strokeColor, VisualisationMode.MARKER);
		}
		if (hooverStroke != null) {
			Stroke stroke = null;
			if (value) {
				stroke = hooverStroke;
			}
			setPositionStroke(position, stroke, VisualisationMode.MARKER);
		}
		repaint();
	}

	public final void setPositionMissing(NFPosition position, boolean value) {
		if (missingFillColor != null) {
			Color color = null;
			if (value) {
				color = missingFillColor;
			}
			setPositionFillColor(position, color, VisualisationMode.FINGER);
		}
		if (missingStrokeColor != null) {
			Color strokeColor = null;
			if (value) {
				strokeColor = missingStrokeColor;
			}
			setPositionStrokeColor(position, strokeColor, VisualisationMode.FINGER);
		}
		if (missingStroke != null) {
			Stroke stroke = null;
			if (value) {
				stroke = missingStroke;
			}
			setPositionStroke(position, stroke, VisualisationMode.FINGER);
		}
		repaint();
	}

	public final void clearPosition(NFPosition position) {
		setPositionHighlighted(position, false);
		setPositionSelected(position, false);
		setPositionMissing(position, false);
		setPositionHoover(position, false);
		setPositionRotated(position, false);
	}

	/**
	 * @return the hooverFillColor
	 */
	public final Color getHooverFillColor() {
		return hooverFillColor;
	}

	/**
	 * @param hooverFillColor the hooverFillColor to set
	 */
	public final void setHooverFillColor(Color hooverFillColor) {
		this.hooverFillColor = hooverFillColor;
	}

	/**
	 * @return the hooverStrokeColor
	 */
	public final Color getHooverStrokeColor() {
		return hooverStrokeColor;
	}

	/**
	 * @param hooverStrokeColor the hooverStrokeColor to set
	 */
	public final void setHooverStrokeColor(Color hooverStrokeColor) {
		this.hooverStrokeColor = hooverStrokeColor;
	}

	/**
	 * @return the hooverStroke
	 */
	public final Stroke getHooverStroke() {
		return hooverStroke;
	}

	/**
	 * @param hooverStroke the hooverStroke to set
	 */
	public final void setHooverStroke(Stroke hooverStroke) {
		this.hooverStroke = hooverStroke;
	}

	/**
	 * @return the missingFillColor
	 */
	public final Color getMissingFillColor() {
		return missingFillColor;
	}

	/**
	 * @param missingFillColor the missingFillColor to set
	 */
	public final void setMissingFillColor(Color missingFillColor) {
		this.missingFillColor = missingFillColor;
	}

	/**
	 * @return the highlightedFillColor
	 */
	public final Color getHighlightedFillColor() {
		return highlightedFillColor;
	}

	/**
	 * @param highlightedFillColor the highlightedFillColor to set
	 */
	public final void setHighlightedFillColor(Color highlightedFillColor) {
		this.highlightedFillColor = highlightedFillColor;
	}

	/**
	 * @return the selectedFillColor
	 */
	public final Color getSelectedFillColor() {
		return selectedFillColor;
	}

	/**
	 * @param selectedFillColor the selectedFillColor to set
	 */
	public final void setSelectedFillColor(Color selectedFillColor) {
		this.selectedFillColor = selectedFillColor;
	}

	/**
	 * @return the missingStrokeColor
	 */
	public final Color getMissingStrokeColor() {
		return missingStrokeColor;
	}

	/**
	 * @param missingStrokeColor the missingStrokeColor to set
	 */
	public final void setMissingStrokeColor(Color missingStrokeColor) {
		this.missingStrokeColor = missingStrokeColor;
	}

	/**
	 * @return the highlightedStrokeColor
	 */
	public final Color getHighlightedStrokeColor() {
		return highlightedStrokeColor;
	}

	/**
	 * @param highlightedStrokeColor the highlightedStrokeColor to set
	 */
	public final void setHighlightedStrokeColor(Color highlightedStrokeColor) {
		this.highlightedStrokeColor = highlightedStrokeColor;
	}

	/**
	 * @return the selectedStrokeColor
	 */
	public final Color getSelectedStrokeColor() {
		return selectedStrokeColor;
	}

	/**
	 * @param selectedStrokeColor the selectedStrokeColor to set
	 */
	public final void setSelectedStrokeColor(Color selectedStrokeColor) {
		this.selectedStrokeColor = selectedStrokeColor;
	}

	/**
	 * @return the missingStroke
	 */
	public final Stroke getMissingStroke() {
		return missingStroke;
	}

	/**
	 * @param missingStroke the missingStroke to set
	 */
	public final void setMissingStroke(Stroke missingStroke) {
		this.missingStroke = missingStroke;
	}

	/**
	 * @return the highlightedStroke
	 */
	public final Stroke getHighlightedStroke() {
		return highlightedStroke;
	}

	/**
	 * @param highlightedStroke the highlightedStroke to set
	 */
	public final void setHighlightedStroke(Stroke highlightedStroke) {
		this.highlightedStroke = highlightedStroke;
	}

	/**
	 * @return the selectedStroke
	 */
	public final Stroke getSelectedStroke() {
		return selectedStroke;
	}

	/**
	 * @param selectedStroke the selectedStroke to set
	 */
	public final void setSelectedStroke(Stroke selectedStroke) {
		this.selectedStroke = selectedStroke;
	}

	public final void setLeftHandVisible(boolean value) {
		leftHand.setVisible(value);
	}

	public final void setRightHandVisible(boolean value) {
		rightHand.setVisible(value);
	}
}

class PositionMap {
	private NFPosition nfPosition;
	private List<Position> positions;

	public PositionMap(NFPosition nfPosition, List<Position> positions) {
		this.nfPosition = nfPosition;
		this.positions = positions;
	}

	/**
	 * @return the nfPosition
	 */
	public NFPosition getNfPosition() {
		return nfPosition;
	}

	/**
	 * @return the positions
	 */
	public List<Position> getPositions() {
		return positions;
	}
}
