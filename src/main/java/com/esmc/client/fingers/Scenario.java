package com.esmc.client.fingers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.neurotec.biometrics.NFImpressionType;
import com.neurotec.biometrics.NFPosition;
import com.neurotec.devices.NFScanner;
import com.esmc.client.fingers.HandSegmentSelector.SelectionMode;

public final class Scenario {

	// ===========================================================
	// Public static fields
	// ===========================================================

	public static final List<NFPosition> FINGERS = Arrays.asList(NFPosition.LEFT_LITTLE_FINGER, NFPosition.LEFT_RING_FINGER, NFPosition.LEFT_MIDDLE_FINGER, NFPosition.LEFT_INDEX_FINGER, NFPosition.LEFT_THUMB, NFPosition.RIGHT_THUMB, NFPosition.RIGHT_INDEX_FINGER, NFPosition.RIGHT_MIDDLE_FINGER, NFPosition.RIGHT_RING_FINGER, NFPosition.RIGHT_LITTLE_FINGER);

	public static final List<NFPosition> SLAP_AND_TWO_THUMBS = Arrays.asList(NFPosition.PLAIN_LEFT_FOUR_FINGERS, NFPosition.PLAIN_RIGHT_FOUR_FINGERS, NFPosition.PLAIN_THUMBS);

	public static final List<NFPosition> SLAP_AND_SEPARATE_THUMBS = Arrays.asList(NFPosition.PLAIN_LEFT_FOUR_FINGERS, NFPosition.PLAIN_RIGHT_FOUR_FINGERS, NFPosition.LEFT_THUMB, NFPosition.RIGHT_THUMB);
	public static final List<NFPosition> PALMS = Arrays.asList(NFPosition.LEFT_FULL_PALM, NFPosition.LEFT_HYPOTHENAR, NFPosition.LEFT_INTERDIGITAL, NFPosition.LEFT_LOWER_PALM, NFPosition.LEFT_THENAR, NFPosition.LEFT_UPPER_PALM/*, NFPosition.LEFT_WRITERS_PALM*/, NFPosition.RIGHT_FULL_PALM, NFPosition.RIGHT_HYPOTHENAR, NFPosition.RIGHT_INTERDIGITAL, NFPosition.RIGHT_LOWER_PALM, NFPosition.RIGHT_THENAR, NFPosition.RIGHT_UPPER_PALM/*, NFPosition.RIGHT_WRITERS_PALM*/);

	public static final Scenario PLAIN_FINGER = new Scenario("Single plain finger", SelectionMode.NONE);
	public static final Scenario ROLLED_FINGER = new Scenario("Single rolled finger", SelectionMode.NONE, true, false);
	public static final Scenario ALL_PLAIN_FINGERS = new Scenario("All plain fingers", SelectionMode.FINGER, FINGERS);
	public static final Scenario ALL_ROLLED_FINGERS = new Scenario("All Rolled fingers", SelectionMode.FINGER, FINGERS, true, false);
	public static final Scenario SLAP_AND_THUMB = new Scenario("4-4-2", SelectionMode.SLAPS_AND_TWO_THUMBS, SLAP_AND_TWO_THUMBS, false, true);
	public static final Scenario SLAPS_2_THUMBS = new Scenario("4-4-1-1", SelectionMode.SLAPS_AND_SEPARATE_THUMBS, SLAP_AND_SEPARATE_THUMBS, false, true);
	//public static final Scenario ROLLED_PLUS_SLAPS = new Scenario("Rolled fingers + 4-4-2", SelectionMode.SLAPS_AND_SEPARATE_THUMBS, Position.SLAP_AND_THUMB);
	//public static final Scenario ROLLED_PLUS_SLAPS_2_TUMBS;
	public static final Scenario ALL_PALMS = new Scenario("All palms", SelectionMode.PALMS, PALMS);
	public static final Scenario NONE = new Scenario("None", SelectionMode.NONE);

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static Scenario[] getAllFingerScenarios() {
		return new Scenario[] { Scenario.PLAIN_FINGER, Scenario.ROLLED_FINGER, Scenario.ALL_PLAIN_FINGERS, Scenario.ALL_ROLLED_FINGERS, Scenario.SLAP_AND_THUMB, Scenario.SLAPS_2_THUMBS,
		/* Scenario.ROLLED_PLUS_SLAPS */
		};
	}

	public static boolean isSelectAll(Scenario scenario) {
		return scenario.equals(ALL_PLAIN_FINGERS) || scenario.equals(ALL_ROLLED_FINGERS);
	}

	public static boolean isSlapsAndThumbs(Scenario scenario) {
		return scenario.equals(SLAP_AND_THUMB) || scenario.equals(SLAPS_2_THUMBS);
	}

	// ===========================================================
	// Private fields
	// ===========================================================

	private String message;
	private SelectionMode selectionMode;
	private List<NFPosition> allowedPositions = new ArrayList<NFPosition>();
	private boolean isRolled = false;
	private boolean isSlap = false;

	// ===========================================================
	// Public constructors
	// ===========================================================

	public Scenario(String message, SelectionMode selectionMode) {
		this(message, selectionMode, Collections.<NFPosition>emptyList(), false, false);
	}

	public Scenario(String message, SelectionMode selectionMode, boolean isRolled, boolean isSlap) {
		this(message, selectionMode, Collections.<NFPosition>emptyList(), isRolled, isSlap);
	}

	public Scenario(String message, SelectionMode selectionMode, List<NFPosition> allowedPositions) {
		this(message, selectionMode, allowedPositions, false, false);
	}

	public Scenario(String message, SelectionMode selectionMode, List<NFPosition> allowedPositions, boolean isRolled, boolean isSlap) {
		this.message = message;
		this.selectionMode = selectionMode;
		this.allowedPositions = allowedPositions;
		this.isRolled = isRolled;
		this.isSlap = isSlap;
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	public boolean isRolled() {
		return this.isRolled;
	}

	public boolean isSlap() {
		return this.isSlap;
	}

	public List<NFPosition> getAllowedPositions() {
		return allowedPositions;
	}

	public void setAllowedPositions(List<NFPosition> positions) {
		allowedPositions = positions;
	}

	public SelectionMode getSelectionMode() {
		return selectionMode;
	}

	public boolean isSelectionNeeded() {
		return !selectionMode.equals(SelectionMode.NONE) && allowedPositions != null;
	}

	public static Scenario[] getSupportedScenarios(NFScanner scanner) {
		ArrayList<Scenario> set = new ArrayList<Scenario>();
		Scenario[] result;
		set.add(Scenario.PLAIN_FINGER);
		boolean hasSlaps = false;
		boolean hasRolled = false;

		for (NFPosition position : scanner.getSupportedPositions()) {
			if (position.isFourFingers()) {
				hasSlaps = true;
				break;
			}
		}
		for (NFImpressionType impresion : scanner.getSupportedImpressionTypes()) {
			if (impresion.isRolled()) {
				hasRolled = true;
				break;
			}
		}
		set.add(Scenario.ALL_PLAIN_FINGERS);
		if (hasRolled) {
			set.add(Scenario.ROLLED_FINGER);
			set.add(Scenario.ALL_ROLLED_FINGERS);
		}
		if (hasSlaps) {
			set.add(Scenario.SLAPS_2_THUMBS);
			set.add(Scenario.SLAP_AND_THUMB);
//			if (hasRolled)
//				set.add(Scenario.ROLLED_PLUS_SLAPS);
		}

		result = new Scenario[set.size()];
		return set.toArray(result);
	}

	@Override
	public String toString() {
		return message;
	}
}
