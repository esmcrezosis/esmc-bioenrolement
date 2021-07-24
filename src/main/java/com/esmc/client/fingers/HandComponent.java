package com.esmc.client.fingers;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JPanel;

public class HandComponent extends JPanel {

	// ===========================================================
	// Private classes
	// ===========================================================

	public enum Position {
		THUMB, INDEX_FINGER, MIDDLE_FINGER, RING_FINGER, LITTLE_FINGER,

		FINGERNAILS,

		THUMB_PRINT, INDEX_FINGER_PRINT, MIDDLE_FINGER_PRINT, RING_FINGER_PRINT, LITTLE_FINGER_PRINT,

		THUMB_PRINT_MARKER, INDEX_FINGER_PRINT_MARKER, MIDDLE_FINGER_PRINT_MARKER, RING_FINGER_PRINT_MARKER, LITTLE_FINGER_PRINT_MARKER,

		PALM, UPPER_PALM, LOWER_PALM, SLAP,

		THENAR, HYPOTHENAR, INTERDIGITAL,

		PALM_CREASES, PALM_CONTOUR;

		public static EnumSet<Position> FINGERPRINTS = EnumSet.of(THUMB_PRINT, INDEX_FINGER_PRINT, MIDDLE_FINGER_PRINT, RING_FINGER_PRINT, LITTLE_FINGER_PRINT);
		public static EnumSet<Position> FINGERPRINT_MARKERS = EnumSet.of(THUMB_PRINT_MARKER, INDEX_FINGER_PRINT_MARKER, MIDDLE_FINGER_PRINT_MARKER, RING_FINGER_PRINT_MARKER, LITTLE_FINGER_PRINT_MARKER);
		public static EnumSet<Position> FINGERS = EnumSet.of(THUMB, INDEX_FINGER, MIDDLE_FINGER, RING_FINGER, LITTLE_FINGER);
		public static EnumSet<Position> PALMS = EnumSet.of(PALM, UPPER_PALM, LOWER_PALM, THENAR, HYPOTHENAR, INTERDIGITAL);
		public static EnumSet<Position> SLAP_AND_THUMB = EnumSet.of(SLAP, THUMB, THUMB_PRINT, THUMB_PRINT_MARKER);
		public static EnumSet<Position> ALL = EnumSet.allOf(Position.class);

		public static Position getMarker(Position position) {
			if (position.equals(THUMB)) {
				return THUMB_PRINT_MARKER;
			} else if (position.equals(INDEX_FINGER)) {
				return INDEX_FINGER_PRINT_MARKER;
			} else if (position.equals(MIDDLE_FINGER)) {
				return MIDDLE_FINGER_PRINT_MARKER;
			} else if (position.equals(RING_FINGER)) {
				return RING_FINGER_PRINT_MARKER;
			} else if (position.equals(LITTLE_FINGER)) {
				return LITTLE_FINGER_PRINT_MARKER;
			}
			return position;
		}

		public static Position getFingerprint(Position position) {
			if (position.equals(THUMB)) {
				return THUMB_PRINT;
			} else if (position.equals(INDEX_FINGER)) {
				return INDEX_FINGER_PRINT;
			} else if (position.equals(MIDDLE_FINGER)) {
				return MIDDLE_FINGER_PRINT;
			} else if (position.equals(RING_FINGER)) {
				return RING_FINGER_PRINT;
			} else if (position.equals(LITTLE_FINGER)) {
				return LITTLE_FINGER_PRINT;
			}
			return null;
		}

		public static Position getFinger(Position position) {
			if (position.equals(THUMB) || position.equals(THUMB_PRINT) || position.equals(THUMB_PRINT_MARKER)) {
				return THUMB;
			} else if (position.equals(INDEX_FINGER) || position.equals(INDEX_FINGER_PRINT) || position.equals(INDEX_FINGER_PRINT_MARKER)) {
				return INDEX_FINGER;
			} else if (position.equals(MIDDLE_FINGER) || position.equals(MIDDLE_FINGER_PRINT) || position.equals(MIDDLE_FINGER_PRINT_MARKER)) {
				return MIDDLE_FINGER;
			} else if (position.equals(RING_FINGER) || position.equals(RING_FINGER_PRINT) || position.equals(RING_FINGER_PRINT_MARKER)) {
				return RING_FINGER;
			} else if (position.equals(LITTLE_FINGER) || position.equals(LITTLE_FINGER_PRINT) || position.equals(LITTLE_FINGER_PRINT_MARKER)) {
				return LITTLE_FINGER;
			}
			return null;
		}

		public static boolean isFinger(Position position) {
			return FINGERS.contains(position);
		}

		public static boolean isFingerprint(Position position) {
			return FINGERPRINTS.contains(position);
		}

		public static boolean isFingerMarker(Position position) {
			return FINGERPRINT_MARKERS.contains(position);
		}

		public static boolean isPalm(Position position) {
			return PALMS.contains(position);
		}

		public static boolean isSlapOrThumb(Position position) {
			return SLAP_AND_THUMB.contains(position);
		}
	}

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final long serialVersionUID = 1L;

	// ===========================================================
	// Private fields
	// ===========================================================

	private final double originalWidth = 310;
	private final double originalHeight = 296;

	private boolean flipped;

	private double angle = 0;

	private Hashtable<Position, ShapeDrawSettings> drawSettings;
	private Hashtable<Position, Shape> shapes;
	private Hashtable<Position, Shape> fingerRotationShapes;
	private Hashtable<Position, FingerShapeSettings> fingerSettings;
	private List<Position> drawOrder;

	// ===========================================================
	// Public constructor
	// ===========================================================

	public HandComponent() {
		shapes = new Hashtable<Position, Shape>();
		shapes.put(Position.PALM, getPalmShape());
		shapes.put(Position.UPPER_PALM, getUpperPalmShape());
		shapes.put(Position.LOWER_PALM, getLowerPalmShape());
		shapes.put(Position.THENAR, getThenarShape());
		shapes.put(Position.HYPOTHENAR, getHypothenarShape());
		shapes.put(Position.INTERDIGITAL, getInterdigitalShape());
		shapes.put(Position.SLAP, getSlapShape());
		shapes.put(Position.THUMB_PRINT, getThumbPrintShape());
		shapes.put(Position.INDEX_FINGER_PRINT, getIndexFingerprintShape());
		shapes.put(Position.MIDDLE_FINGER_PRINT, getMiddleFingerprintShape());
		shapes.put(Position.RING_FINGER_PRINT, getRingFingerprintShape());
		shapes.put(Position.LITTLE_FINGER_PRINT, getLittleFingerprintShape());
		shapes.put(Position.THUMB, getThumbShape());
		shapes.put(Position.INDEX_FINGER, getIndexFingerShape());
		shapes.put(Position.MIDDLE_FINGER, getMiddleFingerShape());
		shapes.put(Position.RING_FINGER, getRingFingerShape());
		shapes.put(Position.LITTLE_FINGER, getLittleFingerShape());
		shapes.put(Position.THUMB_PRINT_MARKER, getThumbShape());
		shapes.put(Position.INDEX_FINGER_PRINT_MARKER, getIndexFingerShape());
		shapes.put(Position.MIDDLE_FINGER_PRINT_MARKER, getMiddleFingerShape());
		shapes.put(Position.RING_FINGER_PRINT_MARKER, getRingFingerShape());
		shapes.put(Position.LITTLE_FINGER_PRINT_MARKER, getLittleFingerShape());
		shapes.put(Position.PALM_CONTOUR, getPalmShape());
		shapes.put(Position.PALM_CREASES, getPalmCreaseShapes());
		shapes.put(Position.FINGERNAILS, getFingernailShapes());

		fingerRotationShapes = new Hashtable<HandComponent.Position, Shape>();
		fingerRotationShapes.put(Position.THUMB, getThumbprintMarkerShape());
		fingerRotationShapes.put(Position.INDEX_FINGER, getIndexFingerprintMarkerShape());
		fingerRotationShapes.put(Position.MIDDLE_FINGER, getMiddleFingerprintMarkerShape());
		fingerRotationShapes.put(Position.RING_FINGER, getRingFingerprintMarkerShape());
		fingerRotationShapes.put(Position.LITTLE_FINGER, getLittleFingerprintMarkerShape());

		fingerSettings = new Hashtable<HandComponent.Position, FingerShapeSettings>();
		fingerSettings.put(Position.THUMB, new FingerShapeSettings(false));
		fingerSettings.put(Position.INDEX_FINGER, new FingerShapeSettings(false));
		fingerSettings.put(Position.MIDDLE_FINGER, new FingerShapeSettings(false));
		fingerSettings.put(Position.RING_FINGER, new FingerShapeSettings(false));
		fingerSettings.put(Position.LITTLE_FINGER, new FingerShapeSettings(false));

		drawSettings = new Hashtable<Position, ShapeDrawSettings>();
		drawSettings.put(Position.PALM, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.UPPER_PALM, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.LOWER_PALM, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.THENAR, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.HYPOTHENAR, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.INTERDIGITAL, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.SLAP, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.THUMB_PRINT, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.INDEX_FINGER_PRINT, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.MIDDLE_FINGER_PRINT, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.RING_FINGER_PRINT, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.LITTLE_FINGER_PRINT, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.THUMB, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.INDEX_FINGER, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.MIDDLE_FINGER, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.RING_FINGER, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.LITTLE_FINGER, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.THUMB_PRINT_MARKER, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.INDEX_FINGER_PRINT_MARKER, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.MIDDLE_FINGER_PRINT_MARKER, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.RING_FINGER_PRINT_MARKER, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));
		drawSettings.put(Position.LITTLE_FINGER_PRINT_MARKER, new ShapeDrawSettings(new Color(255, 255, 255, 0), null, null));

		drawSettings.put(Position.PALM_CONTOUR, new ShapeDrawSettings(null, new BasicStroke(1.3224779f, 0, 0, 4.0f, null, 0.0f), new Color(0, 0, 0, 255)));
		drawSettings.put(Position.PALM_CREASES, new ShapeDrawSettings(null, new BasicStroke(1, 0, 0, 4.0f, null, 0.0f), new Color(0, 0, 0, 0)));
		drawSettings.put(Position.FINGERNAILS, new ShapeDrawSettings(null, new BasicStroke(0.9f, 0, 0, 4.0f, null, 0.0f), new Color(0, 0, 0, 255)));

		drawOrder = new ArrayList<Position>();
		drawOrder.add(Position.PALM);
		drawOrder.add(Position.UPPER_PALM);
		drawOrder.add(Position.LOWER_PALM);
		drawOrder.add(Position.THENAR);
		drawOrder.add(Position.HYPOTHENAR);
		drawOrder.add(Position.INTERDIGITAL);
		drawOrder.add(Position.SLAP);
		drawOrder.add(Position.THUMB);
		drawOrder.add(Position.INDEX_FINGER);
		drawOrder.add(Position.MIDDLE_FINGER);
		drawOrder.add(Position.RING_FINGER);
		drawOrder.add(Position.LITTLE_FINGER);
		drawOrder.add(Position.THUMB_PRINT);
		drawOrder.add(Position.INDEX_FINGER_PRINT);
		drawOrder.add(Position.MIDDLE_FINGER_PRINT);
		drawOrder.add(Position.RING_FINGER_PRINT);
		drawOrder.add(Position.LITTLE_FINGER_PRINT);
		drawOrder.add(Position.PALM_CONTOUR);
		drawOrder.add(Position.PALM_CREASES);
		drawOrder.add(Position.THUMB_PRINT_MARKER);
		drawOrder.add(Position.INDEX_FINGER_PRINT_MARKER);
		drawOrder.add(Position.MIDDLE_FINGER_PRINT_MARKER);
		drawOrder.add(Position.RING_FINGER_PRINT_MARKER);
		drawOrder.add(Position.LITTLE_FINGER_PRINT_MARKER);
		drawOrder.add(Position.FINGERNAILS);

		this.flipped = false;
	}

	// ===========================================================
	// Private methods
	// ===========================================================

	private double getOptimalScale() {
		double scale = 1;
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		double xScale;
		double yScale;

		if (originalWidth > panelWidth || originalHeight > panelHeight) {
			xScale = (double) panelWidth / originalWidth;
			yScale = (double) panelHeight / originalHeight;
			scale = Math.min(xScale, yScale);
		} else if (originalWidth < panelWidth && originalHeight < panelHeight) {
			xScale = (double) panelWidth / originalWidth;
			yScale = (double) panelHeight / originalHeight;
			scale = Math.min(xScale, yScale);
		} else {
			scale = 1;
		}
		return scale;
	}

	private double getXOffset(double scale) {
		return (getWidth() / scale - originalWidth) / 2;
	}

	private double getYOffset(double scale) {
		return (getHeight() / scale - originalHeight) / 2;
	}

	private void drawShape(Shape shape, Graphics2D g, ShapeDrawSettings drawSettings, float origAlpha) {
		drawShape(shape, g, drawSettings.getFillColor(), drawSettings.getStroke(), drawSettings.getStrokeColor(), origAlpha);
	}

	private void drawShape(Shape shape, Graphics2D g, Color fillColor, Stroke stroke, Color strokeColor, float origAlpha) {
		AffineTransform transform = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		if (fillColor != null) {
			g.setPaint(fillColor);
			g.fill(shape);
		}
		if (stroke != null) {
			if (strokeColor != null) {
				g.setPaint(strokeColor);
			}
			g.setStroke(stroke);
			g.draw(shape);
		}
		g.setTransform(transform);
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
	}

	private void drawRotation(Graphics2D g, Shape shape) {
		if (shape != null) {
			int offset = shape.getBounds().width / 7;
			Shape rotation = drawRotationShape(shape.getBounds().x + offset, shape.getBounds().y, 7 * (shape.getBounds().width + offset) / 10, 7 * shape.getBounds().width / 10, offset, flipped);

			angle = angle + Math.PI / 12;
			AffineTransform at = new AffineTransform();
			if (flipped) {
				at.rotate(-angle, rotation.getBounds().getCenterX(), rotation.getBounds().getCenterY());
			} else {
				at.rotate(angle, rotation.getBounds().getCenterX(), rotation.getBounds().getCenterY());
			}
			rotation = at.createTransformedShape(rotation);

			g.setStroke(new BasicStroke(2.5f));
			g.setPaint(Color.BLACK);
			g.draw(rotation);
			g.setPaint(new Color(173, 255, 47));
			g.fill(rotation);
		}
	}

	private Shape drawRotationShape(int x, int y, int width, int height, int offset, boolean flipped) {
		GeneralPath rotationShape = new GeneralPath();

		Area a1 = new Area(new Arc2D.Double(x, y, width, height, 0, 180, Arc2D.PIE));
		Area a2 = new Area(new Arc2D.Double(x + offset, y + offset, width - 2 * offset, height - offset, 0, 180, Arc2D.PIE));
		a1.subtract(a2);
		rotationShape.append(a1, false);

		int x0 = x + width - offset / 2;
		int y0 = y + height / 2 + offset;
		int[] arrowTopX = { x0, x0 + 3 * offset / 2, x0 - 3 * offset / 2 };
		int[] arrowTopY = { y0, y0 - 3 * offset / 2, y0 - 3 * offset / 2 };
		Polygon arrowHead = new Polygon(arrowTopX, arrowTopY, arrowTopX.length);
		rotationShape.append(arrowHead, false);

		AffineTransform at = new AffineTransform();
		at.rotate(Math.PI, rotationShape.getBounds().width / 2 + x - offset / 2, rotationShape.getBounds().height + y);
		rotationShape.append(at.createTransformedShape(rotationShape), false);

		if (flipped) {
			x = rotationShape.getBounds().x + rotationShape.getBounds().width / 2;
			y = rotationShape.getBounds().y;
			at = new AffineTransform();
			at.translate(x, y);
			at.scale(-1, 1);
			at.translate(-x, -y);
			return at.createTransformedShape(rotationShape);
		}
		return rotationShape;
	}

	private Shape getPalmShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(207.82687f, 247.55421f);
		((GeneralPath) shape).curveTo(222.279f, 226.72037f, 265.24445f, 188.34183f, 281.81494f, 177.04662f);
		((GeneralPath) shape).curveTo(298.38544f, 165.7514f, 301.96838f, 157.85278f, 295.18704f, 152.59564f);
		((GeneralPath) shape).curveTo(288.40567f, 147.3385f, 278.8991f, 149.11708f, 265.50168f, 157.27129f);
		((GeneralPath) shape).curveTo(252.10423f, 165.42549f, 220.63307f, 194.08798f, 208.2036f, 192.09888f);
		((GeneralPath) shape).curveTo(195.77412f, 190.10977f, 190.56598f, 175.9663f, 188.96289f, 169.31296f);
		((GeneralPath) shape).curveTo(187.3598f, 162.65962f, 188.82788f, 149.59735f, 191.7699f, 133.53833f);
		((GeneralPath) shape).curveTo(194.71191f, 117.4793f, 204.44856f, 59.95836f, 206.15813f, 45.996666f);
		((GeneralPath) shape).curveTo(207.86769f, 32.034973f, 208.31667f, 19.818426f, 201.01103f, 18.253906f);
		((GeneralPath) shape).curveTo(193.7054f, 16.689386f, 188.02205f, 24.459137f, 183.79173f, 37.184425f);
		((GeneralPath) shape).curveTo(179.56142f, 49.909714f, 171.31763f, 99.00846f, 168.02788f, 109.87885f);
		((GeneralPath) shape).curveTo(164.73811f, 120.749245f, 163.39212f, 128.94365f, 157.20412f, 127.45943f);
		((GeneralPath) shape).curveTo(151.01611f, 125.9752f, 150.69547f, 115.62984f, 150.21512f, 103.574295f);
		((GeneralPath) shape).curveTo(149.73474f, 91.51874f, 145.00916f, 54.007244f, 144.51608f, 37.129723f);
		((GeneralPath) shape).curveTo(144.023f, 20.252197f, 141.57533f, 5.613919f, 131.0215f, 5.149766f);
		((GeneralPath) shape).curveTo(120.46766f, 4.6856136f, 120.49317f, 23.877863f, 120.33579f, 41.419075f);
		((GeneralPath) shape).curveTo(120.17841f, 58.96029f, 124.026f, 106.33713f, 124.29769f, 115.38001f);
		((GeneralPath) shape).curveTo(124.56938f, 124.42289f, 123.81463f, 130.89949f, 118.482864f, 132.10461f);
		((GeneralPath) shape).curveTo(113.15109f, 133.30974f, 108.31851f, 124.77164f, 105.99233f, 117.589096f);
		((GeneralPath) shape).curveTo(103.66615f, 110.40656f, 91.95055f, 77.00288f, 82.953735f, 58.07853f);
		((GeneralPath) shape).curveTo(73.95692f, 39.15418f, 72.35938f, 27.472631f, 61.148064f, 30.896036f);
		((GeneralPath) shape).curveTo(49.936745f, 34.319443f, 53.767227f, 52.676064f, 59.7507f, 68.57206f);
		((GeneralPath) shape).curveTo(65.73418f, 84.468056f, 81.79861f, 125.41948f, 83.07861f, 133.75217f);
		((GeneralPath) shape).curveTo(84.35862f, 142.08484f, 84.671196f, 147.76654f, 80.61903f, 150.65152f);
		((GeneralPath) shape).curveTo(76.56687f, 153.53648f, 72.42102f, 154.79716f, 63.89511f, 143.84785f);
		((GeneralPath) shape).curveTo(55.3692f, 132.89854f, 55.376717f, 131.1691f, 47.192135f, 121.662285f);
		((GeneralPath) shape).curveTo(39.007553f, 112.15547f, 22.636168f, 90.45816f, 12.895294f, 99.6667f);
		((GeneralPath) shape).curveTo(3.1544218f, 108.87525f, 21.950375f, 123.18102f, 30.08988f, 135.02676f);
		((GeneralPath) shape).curveTo(38.229385f, 146.8725f, 45.37584f, 153.71452f, 52.42599f, 170.6531f);
		((GeneralPath) shape).curveTo(59.476143f, 187.59169f, 62.56909f, 212.8175f, 65.341255f, 231.83533f);
		((GeneralPath) shape).curveTo(68.11342f, 250.85315f, 85.30353f, 282.4479f, 120.9046f, 282.15955f);
		((GeneralPath) shape).curveTo(156.50568f, 281.8712f, 185.13513f, 284.24875f, 207.82686f, 247.5542f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getSlapShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(191.77547f, 133.54031f);
		((GeneralPath) shape).curveTo(194.71748f, 117.481285f, 204.45413f, 59.96035f, 206.1637f, 45.998657f);
		((GeneralPath) shape).curveTo(207.87326f, 32.036964f, 208.32224f, 19.820417f, 201.0166f, 18.255898f);
		((GeneralPath) shape).curveTo(193.71097f, 16.691378f, 188.02762f, 24.461128f, 183.7973f, 37.186417f);
		((GeneralPath) shape).curveTo(179.56699f, 49.911705f, 171.3232f, 99.01045f, 168.03345f, 109.88084f);
		((GeneralPath) shape).curveTo(164.74368f, 120.75123f, 163.39769f, 128.94563f, 157.20969f, 127.46141f);
		((GeneralPath) shape).curveTo(151.02168f, 125.97718f, 150.70103f, 115.63182f, 150.22069f, 103.57627f);
		((GeneralPath) shape).curveTo(149.74033f, 91.52073f, 145.01474f, 54.009228f, 144.52167f, 37.131706f);
		((GeneralPath) shape).curveTo(144.02858f, 20.254185f, 141.58092f, 5.6159053f, 131.02708f, 5.1517525f);
		((GeneralPath) shape).curveTo(120.47324f, 4.687599f, 120.49875f, 23.879848f, 120.34137f, 41.421062f);
		((GeneralPath) shape).curveTo(120.18399f, 58.96228f, 124.03158f, 106.33911f, 124.30327f, 115.38199f);
		((GeneralPath) shape).curveTo(124.57496f, 124.424866f, 123.820206f, 130.90147f, 118.48844f, 132.1066f);
		((GeneralPath) shape).curveTo(113.15667f, 133.31172f, 108.32409f, 124.77362f, 105.99791f, 117.59108f);
		((GeneralPath) shape).curveTo(103.67173f, 110.40854f, 91.95613f, 77.00487f, 82.95932f, 58.080517f);
		((GeneralPath) shape).curveTo(73.9625f, 39.156162f, 72.36497f, 27.474617f, 61.15365f, 30.898022f);
		((GeneralPath) shape).curveTo(49.94233f, 34.321426f, 53.772808f, 52.678047f, 59.756287f, 68.57405f);
		((GeneralPath) shape).curveTo(65.73975f, 84.47004f, 81.804184f, 125.42146f, 83.0842f, 133.75415f);
		((GeneralPath) shape).curveTo(84.3642f, 142.08682f, 84.67678f, 147.76852f, 80.62462f, 150.6535f);
		((GeneralPath) shape).curveTo(76.57246f, 153.53847f, 72.426605f, 154.79915f, 63.900696f, 143.84984f);
		((GeneralPath) shape).curveTo(55.374786f, 132.90053f, 55.382294f, 131.17108f, 47.197716f, 121.66427f);
		((GeneralPath) shape).curveTo(39.013138f, 112.157455f, 22.641748f, 90.460144f, 12.900875f, 99.668686f);
		((GeneralPath) shape).curveTo(3.1600027f, 108.877235f, 21.955956f, 123.18301f, 30.095455f, 135.02875f);
		((GeneralPath) shape).curveTo(38.234966f, 146.87448f, 45.381416f, 153.7165f, 52.431576f, 170.65509f);
		((GeneralPath) shape).curveTo(99.83827f, 136.23268f, 129.36807f, 119.35366f, 191.77547f, 133.54031f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getThumbPrintShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(281.81693f, 177.04697f);
		((GeneralPath) shape).curveTo(298.38742f, 165.75175f, 301.97037f, 157.85313f, 295.18903f, 152.596f);
		((GeneralPath) shape).curveTo(288.40765f, 147.33885f, 278.9011f, 149.11743f, 265.50366f, 157.27164f);
		((GeneralPath) shape).curveTo(252.10622f, 165.42584f, 255.42075f, 173.42746f, 258.41016f, 177.60355f);
		((GeneralPath) shape).curveTo(261.39957f, 181.77963f, 268.5169f, 181.99171f, 281.81693f, 177.04697f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getIndexFingerprintShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(199.0f, 18.0625f);
		((GeneralPath) shape).curveTo(193.2828f, 18.18265f, 188.63666f, 24.273207f, 184.96875f, 33.90625f);
		((GeneralPath) shape).curveTo(183.25652f, 40.097313f, 183.9029f, 51.417072f, 191.53125f, 53.28125f);
		((GeneralPath) shape).curveTo(199.1596f, 55.145428f, 204.20116f, 48.812202f, 206.875f, 39.25f);
		((GeneralPath) shape).curveTo(207.80421f, 28.287476f, 207.12666f, 19.55535f, 201.03125f, 18.25f);
		((GeneralPath) shape).curveTo(200.34634f, 18.103327f, 199.65652f, 18.048702f, 199.0f, 18.0625f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getLittleFingerprintShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(17.90625f, 97.4375f);
		((GeneralPath) shape).curveTo(16.117006f, 97.53166f, 14.428261f, 98.217415f, 12.90625f, 99.65625f);
		((GeneralPath) shape).curveTo(7.250569f, 105.00286f, 11.211841f, 112.07092f, 17.125f, 119.375f);
		((GeneralPath) shape).curveTo(21.396173f, 124.65086f, 29.684076f, 124.50986f, 32.46875f, 122.03125f);
		((GeneralPath) shape).curveTo(35.253426f, 119.55264f, 39.380444f, 113.01229f, 32.75f, 105.5625f);
		((GeneralPath) shape).curveTo(27.739922f, 100.83126f, 22.483685f, 97.19661f, 17.90625f, 97.4375f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getMiddleFingerprintShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(130.0625f, 5.15625f);
		((GeneralPath) shape).curveTo(123.47682f, 5.650727f, 121.40982f, 14.568961f, 120.71875f, 25.59375f);
		((GeneralPath) shape).curveTo(121.72313f, 37.208668f, 127.75092f, 40.63631f, 132.46875f, 40.3125f);
		((GeneralPath) shape).curveTo(137.18658f, 39.98869f, 143.13023f, 36.933952f, 143.53125f, 23.9375f);
		((GeneralPath) shape).curveTo(142.05296f, 13.277542f, 138.66934f, 5.4921684f, 131.03125f, 5.15625f);
		((GeneralPath) shape).curveTo(130.70145f, 5.141745f, 130.37196f, 5.133014f, 130.0625f, 5.15625f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getRingFingerprintShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(64.0625f, 30.3125f);
		((GeneralPath) shape).curveTo(63.166412f, 30.37441f, 62.20731f, 30.585306f, 61.15625f, 30.90625f);
		((GeneralPath) shape).curveTo(53.68248f, 33.188385f, 52.89184f, 42.098286f, 54.96875f, 52.5f);
		((GeneralPath) shape).curveTo(58.60893f, 62.43337f, 67.25274f, 65.16097f, 71.185974f, 63.762115f);
		((GeneralPath) shape).curveTo(75.11921f, 62.36326f, 80.15833f, 58.004265f, 77.65625f, 46.09375f);
		((GeneralPath) shape).curveTo(73.31994f, 35.964996f, 70.47927f, 29.869202f, 64.0625f, 30.3125f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getThenarShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(137.78125f, 282.0313f);
		((GeneralPath) shape).curveTo(137.79276f, 282.1385f, 137.80095f, 282.26904f, 137.81245f, 282.37506f);
		((GeneralPath) shape).lineTo(137.9687f, 282.0313f);
		((GeneralPath) shape).curveTo(165.78041f, 281.57693f, 188.87196f, 278.19104f, 207.81245f, 247.56256f);
		((GeneralPath) shape).curveTo(219.03065f, 233.16953f, 214.19829f, 201.42792f, 200.81245f, 189.09381f);
		((GeneralPath) shape).curveTo(193.58987f, 184.07458f, 190.2083f, 174.45726f, 188.9687f, 169.31256f);
		((GeneralPath) shape).curveTo(188.80254f, 168.6229f, 188.695f, 167.85567f, 188.5937f, 167.03131f);
		((GeneralPath) shape).lineTo(188.7812f, 166.56256f);
		((GeneralPath) shape).curveTo(188.6951f, 166.61226f, 188.61703f, 166.66904f, 188.5312f, 166.71881f);
		((GeneralPath) shape).curveTo(150.52913f, 186.53587f, 129.86691f, 233.68907f, 137.78125f, 282.0313f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getHypothenarShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(158.03125f, 127.75f);
		((GeneralPath) shape).curveTo(154.33537f, 154.71793f, 126.0511f, 188.98291f, 61.09375f, 203.9375f);
		((GeneralPath) shape).lineTo(60.8125f, 204.1875f);
		((GeneralPath) shape).curveTo(60.919563f, 204.1604f, 61.018185f, 204.1209f, 61.125f, 204.0938f);
		((GeneralPath) shape).curveTo(62.802197f, 213.72206f, 64.111755f, 223.392f, 65.34375f, 231.8438f);
		((GeneralPath) shape).curveTo(68.11591f, 250.86163f, 85.305176f, 282.44464f, 120.90625f, 282.1563f);
		((GeneralPath) shape).curveTo(126.72483f, 282.10922f, 132.33133f, 282.1177f, 137.78125f, 282.0313f);
		((GeneralPath) shape).curveTo(137.79276f, 282.1385f, 137.80095f, 282.26904f, 137.81245f, 282.37506f);
		((GeneralPath) shape).lineTo(137.9687f, 282.0313f);
		((GeneralPath) shape).curveTo(129.28615f, 234.79898f, 151.80783f, 184.71426f, 188.5937f, 167.03131f);
		((GeneralPath) shape).lineTo(188.7812f, 166.56256f);
		((GeneralPath) shape).curveTo(188.6951f, 166.61226f, 188.61703f, 166.66904f, 188.5312f, 166.71881f);
		((GeneralPath) shape).curveTo(187.77484f, 159.8709f, 189.0866f, 148.717f, 191.4687f, 135.37506f);
		((GeneralPath) shape).lineTo(191.74995f, 135.31256f);
		((GeneralPath) shape).curveTo(191.66516f, 135.28256f, 191.58472f, 135.24846f, 191.49995f, 135.21886f);
		((GeneralPath) shape).curveTo(177.67723f, 129.86499f, 170.4813f, 127.82264f, 158.03125f, 127.75001f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getInterdigitalShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(158.03125f, 127.75f);
		((GeneralPath) shape).curveTo(113.5668f, 127.49062f, 72.62974f, 155.12473f, 52.75f, 171.46875f);
		((GeneralPath) shape).curveTo(52.70914f, 171.50195f, 52.66586f, 171.52934f, 52.625f, 171.56245f);
		((GeneralPath) shape).lineTo(52.78125f, 171.53125f);
		((GeneralPath) shape).curveTo(56.493103f, 180.76479f, 59.06405f, 192.3152f, 61.09375f, 203.9375f);
		((GeneralPath) shape).lineTo(60.8125f, 204.1875f);
		((GeneralPath) shape).curveTo(60.919563f, 204.1604f, 61.018185f, 204.1209f, 61.125f, 204.0938f);
		((GeneralPath) shape).curveTo(129.63605f, 188.30571f, 154.19742f, 154.85709f, 158.03125f, 127.74999f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getLittleFingerShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(80.62462f, 150.65349f);
		((GeneralPath) shape).curveTo(76.57246f, 153.53845f, 72.426605f, 154.79913f, 63.900696f, 143.84982f);
		((GeneralPath) shape).curveTo(55.374786f, 132.90051f, 55.382294f, 131.17107f, 47.197716f, 121.66425f);
		((GeneralPath) shape).curveTo(39.013138f, 112.15744f, 22.641748f, 90.46013f, 12.900875f, 99.66867f);
		((GeneralPath) shape).curveTo(3.1600027f, 108.87722f, 21.955956f, 123.18299f, 30.095455f, 135.02873f);
		((GeneralPath) shape).curveTo(38.234966f, 146.87447f, 45.381416f, 153.71649f, 52.431576f, 170.65509f);
		((GeneralPath) shape).curveTo(63.81379f, 162.2804f, 66.92714f, 160.2605f, 80.62462f, 150.65349f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getRingFingerShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(118.48844f, 132.1066f);
		((GeneralPath) shape).curveTo(113.15667f, 133.31172f, 108.32409f, 124.77362f, 105.99791f, 117.59108f);
		((GeneralPath) shape).curveTo(103.67173f, 110.40854f, 91.95613f, 77.00487f, 82.95932f, 58.080517f);
		((GeneralPath) shape).curveTo(73.9625f, 39.156162f, 72.36497f, 27.474617f, 61.15365f, 30.898022f);
		((GeneralPath) shape).curveTo(49.94233f, 34.321426f, 53.772808f, 52.678047f, 59.756287f, 68.57405f);
		((GeneralPath) shape).curveTo(65.73975f, 84.47004f, 81.804184f, 125.42146f, 83.0842f, 133.75415f);
		((GeneralPath) shape).curveTo(84.3642f, 142.08682f, 84.67678f, 147.76852f, 80.62462f, 150.6535f);
		((GeneralPath) shape).curveTo(97.26386f, 141.54077f, 107.82449f, 136.5349f, 118.48844f, 132.10661f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getMiddleFingerShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(157.20969f, 127.4614f);
		((GeneralPath) shape).curveTo(151.02168f, 125.97717f, 150.70103f, 115.63181f, 150.22069f, 103.57626f);
		((GeneralPath) shape).curveTo(149.74033f, 91.52073f, 145.01474f, 54.009228f, 144.52167f, 37.131706f);
		((GeneralPath) shape).curveTo(144.02858f, 20.254185f, 141.58092f, 5.6159053f, 131.02708f, 5.1517525f);
		((GeneralPath) shape).curveTo(120.47324f, 4.687599f, 120.49875f, 23.879848f, 120.34137f, 41.421062f);
		((GeneralPath) shape).curveTo(120.18399f, 58.96228f, 124.03158f, 106.33911f, 124.30327f, 115.38199f);
		((GeneralPath) shape).curveTo(124.57496f, 124.424866f, 123.820206f, 130.90147f, 118.48844f, 132.1066f);
		((GeneralPath) shape).curveTo(134.47234f, 128.85204f, 142.16379f, 127.90278f, 157.20969f, 127.461395f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getIndexFingerShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(191.77547f, 133.54031f);
		((GeneralPath) shape).curveTo(194.71748f, 117.481285f, 204.45413f, 59.96035f, 206.1637f, 45.998657f);
		((GeneralPath) shape).curveTo(207.87326f, 32.036964f, 208.32224f, 19.820417f, 201.0166f, 18.255898f);
		((GeneralPath) shape).curveTo(193.71097f, 16.691378f, 188.02762f, 24.461128f, 183.7973f, 37.186417f);
		((GeneralPath) shape).curveTo(179.56699f, 49.911705f, 171.3232f, 99.01045f, 168.03345f, 109.88084f);
		((GeneralPath) shape).curveTo(164.74368f, 120.75123f, 163.39769f, 128.94563f, 157.20969f, 127.46141f);
		((GeneralPath) shape).curveTo(172.39989f, 129.23279f, 181.00117f, 130.58302f, 191.77547f, 133.54031f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getThumbShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(207.82687f, 247.55421f);
		((GeneralPath) shape).curveTo(222.279f, 226.72037f, 265.24445f, 188.34183f, 281.81494f, 177.04662f);
		((GeneralPath) shape).curveTo(298.38544f, 165.7514f, 301.96838f, 157.85278f, 295.18704f, 152.59564f);
		((GeneralPath) shape).curveTo(288.40567f, 147.3385f, 278.8991f, 149.11708f, 265.50168f, 157.27129f);
		((GeneralPath) shape).curveTo(252.10423f, 165.42549f, 220.63307f, 194.08798f, 208.2036f, 192.09888f);
		((GeneralPath) shape).curveTo(208.2036f, 192.09888f, 207.84851f, 247.46823f, 207.82687f, 247.5542f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getLittleFingerprintMarkerShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(21.223303f, 88.57498f);
		((GeneralPath) shape).curveTo(10.173162f, 88.57498f, 1.1985226f, 97.54962f, 1.1985226f, 108.59976f);
		((GeneralPath) shape).curveTo(1.1985226f, 119.6499f, 10.173162f, 128.62454f, 21.223303f, 128.62454f);
		((GeneralPath) shape).curveTo(32.273445f, 128.62454f, 41.248085f, 119.6499f, 41.248085f, 108.59976f);
		((GeneralPath) shape).curveTo(41.248085f, 97.54963f, 32.273445f, 88.57498f, 21.223305f, 88.57498f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getRingFingerprintMarkerShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(64.34671f, 23.296076f);
		((GeneralPath) shape).curveTo(53.29657f, 23.296076f, 44.32193f, 32.270725f, 44.32193f, 43.320854f);
		((GeneralPath) shape).curveTo(44.32193f, 54.370995f, 53.29657f, 63.345634f, 64.34671f, 63.345634f);
		((GeneralPath) shape).curveTo(75.39685f, 63.345634f, 84.37149f, 54.370995f, 84.37149f, 43.320854f);
		((GeneralPath) shape).curveTo(84.37149f, 32.270725f, 75.39685f, 23.296076f, 64.34671f, 23.296076f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getMiddleFingerprintMarkerShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(131.87541f, 0.66866046f);
		((GeneralPath) shape).curveTo(120.82527f, 0.66866046f, 111.85063f, 9.643304f, 111.85063f, 20.693441f);
		((GeneralPath) shape).curveTo(111.85063f, 31.743582f, 120.82527f, 40.718224f, 131.87541f, 40.718224f);
		((GeneralPath) shape).curveTo(142.92555f, 40.718224f, 151.90019f, 31.743584f, 151.90019f, 20.693443f);
		((GeneralPath) shape).curveTo(151.90019f, 9.643308f, 142.92555f, 0.668663f, 131.87541f, 0.668663f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getIndexFingerprintMarkerShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(197.98988f, 12.689475f);
		((GeneralPath) shape).curveTo(186.93974f, 12.689475f, 177.9651f, 21.66412f, 177.9651f, 32.714256f);
		((GeneralPath) shape).curveTo(177.9651f, 43.764397f, 186.93974f, 52.739037f, 197.98988f, 52.739037f);
		((GeneralPath) shape).curveTo(209.04002f, 52.739037f, 218.01466f, 43.764397f, 218.01466f, 32.714256f);
		((GeneralPath) shape).curveTo(218.01466f, 21.66412f, 209.04002f, 12.689476f, 197.98988f, 12.689476f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getThumbprintMarkerShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(282.1356f, 140.6758f);
		((GeneralPath) shape).curveTo(271.08545f, 140.6758f, 262.1108f, 149.65044f, 262.1108f, 160.70058f);
		((GeneralPath) shape).curveTo(262.1108f, 171.75072f, 271.08545f, 180.72536f, 282.1356f, 180.72536f);
		((GeneralPath) shape).curveTo(293.18573f, 180.72536f, 302.16037f, 171.75072f, 302.16037f, 160.70058f);
		((GeneralPath) shape).curveTo(302.16037f, 149.65044f, 293.18573f, 140.6758f, 282.1356f, 140.6758f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getUpperPalmShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(130.0625f, 5.15625f);
		((GeneralPath) shape).curveTo(120.46883f, 5.8765783f, 120.49621f, 24.4132f, 120.34375f, 41.40625f);
		((GeneralPath) shape).curveTo(120.18637f, 58.947464f, 124.04081f, 106.33212f, 124.3125f, 115.375f);
		((GeneralPath) shape).curveTo(124.58419f, 124.41788f, 123.80052f, 130.88863f, 118.46875f, 132.09375f);
		((GeneralPath) shape).curveTo(113.13698f, 133.29887f, 108.32618f, 124.77629f, 106.0f, 117.59375f);
		((GeneralPath) shape).curveTo(103.67382f, 110.41121f, 91.96556f, 77.0181f, 82.96875f, 58.09375f);
		((GeneralPath) shape).curveTo(73.97194f, 39.1694f, 72.36757f, 27.482845f, 61.15625f, 30.90625f);
		((GeneralPath) shape).curveTo(49.94493f, 34.329655f, 53.766525f, 52.666504f, 59.75f, 68.5625f);
		((GeneralPath) shape).curveTo(65.733475f, 84.458496f, 81.81374f, 125.41732f, 83.09375f, 133.75f);
		((GeneralPath) shape).curveTo(84.37376f, 142.08267f, 84.67716f, 147.77129f, 80.625f, 150.65625f);
		((GeneralPath) shape).curveTo(76.57284f, 153.54121f, 72.43216f, 154.79306f, 63.90625f, 143.84375f);
		((GeneralPath) shape).curveTo(55.38034f, 132.89444f, 55.372078f, 131.16306f, 47.1875f, 121.65625f);
		((GeneralPath) shape).curveTo(39.002922f, 112.14944f, 22.647121f, 90.44771f, 12.90625f, 99.65625f);
		((GeneralPath) shape).curveTo(3.1653776f, 108.8648f, 21.954245f, 123.18551f, 30.09375f, 135.03125f);
		((GeneralPath) shape).curveTo(38.233253f, 146.87698f, 45.38735f, 153.71767f, 52.4375f, 170.65625f);
		((GeneralPath) shape).curveTo(56.90567f, 181.3914f, 59.785065f, 195.45367f, 62.0f, 209.15625f);
		((GeneralPath) shape).curveTo(62.03058f, 209.23364f, 188.96875f, 169.3125f, 188.96875f, 169.3125f);
		((GeneralPath) shape).curveTo(187.36566f, 162.65916f, 188.83923f, 149.59029f, 191.78125f, 133.53125f);
		((GeneralPath) shape).curveTo(194.72327f, 117.47222f, 204.44669f, 59.961693f, 206.15625f, 46.0f);
		((GeneralPath) shape).curveTo(207.86581f, 32.038307f, 208.30563f, 19.81452f, 201.0f, 18.25f);
		((GeneralPath) shape).curveTo(193.69437f, 16.68548f, 188.01157f, 24.46221f, 183.78125f, 37.1875f);
		((GeneralPath) shape).curveTo(179.55093f, 49.91279f, 171.32101f, 99.00461f, 168.03125f, 109.875f);
		((GeneralPath) shape).curveTo(164.74149f, 120.74539f, 163.40675f, 128.95297f, 157.21875f, 127.46875f);
		((GeneralPath) shape).curveTo(151.03075f, 125.98452f, 150.6991f, 115.61804f, 150.21875f, 103.5625f);
		((GeneralPath) shape).curveTo(149.73839f, 91.50695f, 145.02432f, 54.002525f, 144.53125f, 37.125f);
		((GeneralPath) shape).curveTo(144.03818f, 20.247477f, 141.58508f, 5.6204033f, 131.03125f, 5.15625f);
		((GeneralPath) shape).curveTo(130.70145f, 5.141745f, 130.37196f, 5.133014f, 130.0625f, 5.15625f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getLowerPalmShape() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(62.0f, 209.15625f);
		((GeneralPath) shape).curveTo(63.279922f, 217.07443f, 64.3285f, 224.87885f, 65.34375f, 231.84375f);
		((GeneralPath) shape).curveTo(68.11591f, 250.86157f, 85.305176f, 282.4446f, 120.90625f, 282.15625f);
		((GeneralPath) shape).curveTo(156.50732f, 281.8679f, 185.12079f, 284.25705f, 207.8125f, 247.5625f);
		((GeneralPath) shape).curveTo(207.67207f, 248.04774f, 208.21875f, 192.09375f, 208.21875f, 192.09375f);
		((GeneralPath) shape).curveTo(197.95073f, 190.45055f, 192.60669f, 180.53261f, 190.15625f, 173.375f);
		((GeneralPath) shape).curveTo(189.64043f, 171.8683f, 189.24751f, 170.4695f, 188.96875f, 169.3125f);
		((GeneralPath) shape).curveTo(188.96875f, 169.3125f, 62.048157f, 209.2984f, 62.0f, 209.15625f);
		((GeneralPath) shape).closePath();
		return shape;
	}

	private Shape getPalmCreaseShapes() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(53.199375f, 171.92584f);
		((GeneralPath) shape).curveTo(60.49624f, 169.63065f, 69.19375f, 166.09746f, 77.59456f, 152.12685f);

		((GeneralPath) shape).moveTo(75.65002f, 110.49594f);
		((GeneralPath) shape).curveTo(85.7764f, 111.342094f, 94.03781f, 107.50901f, 100.39875f, 99.88934f);

		((GeneralPath) shape).moveTo(62.568542f, 76.90837f);
		((GeneralPath) shape).curveTo(71.06209f, 75.89729f, 79.05938f, 71.90868f, 86.610176f, 65.24111f);

		((GeneralPath) shape).moveTo(46.992584f, 121.0186f);
		((GeneralPath) shape).curveTo(42.660442f, 127.16448f, 38.13765f, 130.86877f, 30.041628f, 134.18402f);

		((GeneralPath) shape).moveTo(62.21499f, 140.90154f);
		((GeneralPath) shape).curveTo(57.54996f, 150.29f, 51.042603f, 152.30914f, 44.53732f, 154.33658f);

		((GeneralPath) shape).moveTo(120.5513f, 51.452522f);
		((GeneralPath) shape).curveTo(129.4188f, 54.09935f, 137.72816f, 53.397423f, 145.6536f, 50.39186f);

		((GeneralPath) shape).moveTo(121.96551f, 90.3434f);
		((GeneralPath) shape).curveTo(132.42639f, 93.27742f, 142.1136f, 94.41108f, 148.83557f, 88.929184f);

		((GeneralPath) shape).moveTo(178.8876f, 58.170036f);
		((GeneralPath) shape).curveTo(186.23134f, 62.423546f, 194.69214f, 65.30474f, 202.92923f, 63.119785f);

		((GeneralPath) shape).moveTo(172.17009f, 92.46472f);
		((GeneralPath) shape).curveTo(179.18546f, 96.11468f, 186.91283f, 99.161095f, 197.62595f, 97.41447f);

		((GeneralPath) shape).moveTo(237.93102f, 176.61043f);
		((GeneralPath) shape).curveTo(241.05023f, 189.24072f, 246.71545f, 196.2698f, 254.54802f, 198.53073f);

		((GeneralPath) shape).moveTo(60.86144f, 203.8653f);
		((GeneralPath) shape).curveTo(80.87279f, 198.9981f, 138.17485f, 184.04472f, 153.8613f, 142.3274f);

		((GeneralPath) shape).moveTo(188.7871f, 166.35738f);
		((GeneralPath) shape).curveTo(150.30016f, 185.31763f, 114.332f, 224.8982f, 94.7419f, 248.73532f);

		((GeneralPath) shape).moveTo(137.77002f, 282.13724f);
		((GeneralPath) shape).curveTo(130.86717f, 238.07428f, 145.6397f, 190.07666f, 188.87549f, 166.269f);

		((GeneralPath) shape).moveTo(208.23254f, 191.81322f);
		((GeneralPath) shape).curveTo(211.93044f, 201.02867f, 212.72853f, 213.32819f, 220.25336f, 222.21881f);

		((GeneralPath) shape).moveTo(82.721085f, 146.5584f);
		((GeneralPath) shape).curveTo(91.32071f, 143.85132f, 97.710884f, 143.3537f, 115.247986f, 131.70915f);

		((GeneralPath) shape).moveTo(121.61196f, 129.58781f);
		((GeneralPath) shape).curveTo(132.65688f, 130.48305f, 144.29745f, 129.07532f, 154.40051f, 125.26123f);

		((GeneralPath) shape).moveTo(162.2706f, 125.69874f);
		((GeneralPath) shape).curveTo(172.97443f, 129.88982f, 182.79713f, 131.8781f, 191.61552f, 131.35559f);
		return shape;
	}

	private Shape getFingernailShapes() {
		Shape shape = new GeneralPath();
		((GeneralPath) shape).moveTo(73.6523f, 41.669384f);
		((GeneralPath) shape).curveTo(74.96574f, 47.750267f, 73.98396f, 51.43113f, 68.8526f, 53.171165f);
		((GeneralPath) shape).curveTo(62.741543f, 55.243412f, 57.90391f, 53.28957f, 56.05629f, 45.511314f);
		((GeneralPath) shape).curveTo(54.618416f, 39.458054f, 57.617104f, 33.64085f, 62.47611f, 32.579933f);
		((GeneralPath) shape).curveTo(67.33511f, 31.519014f, 72.33887f, 35.5885f, 73.652306f, 41.669388f);
		((GeneralPath) shape).closePath();

		((GeneralPath) shape).moveTo(140.3718f, 20.078232f);
		((GeneralPath) shape).curveTo(139.97618f, 26.502075f, 138.1155f, 28.263954f, 132.49608f, 28.921593f);
		((GeneralPath) shape).curveTo(127.79951f, 29.471233f, 122.72296f, 27.169516f, 123.20195f, 18.879147f);
		((GeneralPath) shape).curveTo(123.57325f, 12.453005f, 126.66421f, 7.1502886f, 131.40553f, 7.481406f);
		((GeneralPath) shape).curveTo(136.14685f, 7.8125243f, 140.76743f, 13.654392f, 140.3718f, 20.078232f);
		((GeneralPath) shape).closePath();

		((GeneralPath) shape).moveTo(25.943838f, 103.10065f);
		((GeneralPath) shape).curveTo(29.487078f, 106.878746f, 30.353306f, 109.91763f, 27.404356f, 113.329315f);
		((GeneralPath) shape).curveTo(23.892376f, 117.39237f, 19.576538f, 118.047066f, 14.923692f, 113.28532f);
		((GeneralPath) shape).curveTo(11.302712f, 109.579544f, 10.975025f, 104.16452f, 14.018155f, 101.352104f);
		((GeneralPath) shape).curveTo(17.061283f, 98.539696f, 22.400593f, 99.322556f, 25.943838f, 103.100685f);
		((GeneralPath) shape).closePath();

		((GeneralPath) shape).moveTo(204.42728f, 36.497585f);
		((GeneralPath) shape).curveTo(202.38452f, 42.600822f, 195.98712f, 42.57682f, 193.33275f, 41.61549f);
		((GeneralPath) shape).curveTo(188.88673f, 40.00528f, 186.8056f, 34.945286f, 188.85265f, 29.114563f);
		((GeneralPath) shape).curveTo(190.9849f, 23.041124f, 196.95361f, 18.606434f, 201.21489f, 20.711489f);
		((GeneralPath) shape).curveTo(205.47615f, 22.81654f, 206.51013f, 30.274529f, 204.42728f, 36.497585f);
		((GeneralPath) shape).closePath();

		((GeneralPath) shape).moveTo(288.90518f, 170.7923f);
		((GeneralPath) shape).curveTo(287.9601f, 171.52382f, 275.33722f, 162.56815f, 277.75262f, 160.48888f);
		((GeneralPath) shape).curveTo(280.17398f, 158.06906f, 286.90732f, 153.74542f, 289.0549f, 153.60774f);
		((GeneralPath) shape).curveTo(292.4369f, 153.39095f, 296.99295f, 156.88591f, 297.1265f, 160.43735f);
		((GeneralPath) shape).curveTo(297.60303f, 163.79489f, 288.90518f, 170.79231f, 288.90518f, 170.79231f);
		((GeneralPath) shape).closePath();

		return shape;
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	public final List<Position> getAffectedPositions(MouseEvent e) {
		if (!(e.getSource() instanceof HandComponent)) {
			throw new UnsupportedOperationException("Only MouseEvents provided by HandComponent are supported.");
		}

		List<Position> positions = new ArrayList<Position>();
		Point2D p;
		double scale = getOptimalScale();
		if (flipped) {
			p = new Point((int) (originalWidth - e.getX() / scale + getXOffset(scale)), (int) (e.getY() / scale - getYOffset(scale)));
		} else {
			p = new Point((int) (e.getX() / scale - getXOffset(scale)), (int) (e.getY() / scale - getYOffset(scale)));
		}

		Enumeration<Position> shapeEnumerator = shapes.keys();
		while (shapeEnumerator.hasMoreElements()) {
			Position position = shapeEnumerator.nextElement();
			if (shapes.get(position).contains(p)) {
				positions.add(position);
			}
		}
		return positions;
	}

	/**
	 * @return the flipped
	 */
	public final boolean isFlipped() {
		return flipped;
	}

	/**
	 * @param flipped the flipped to set
	 */
	public final void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}

	public final void setPositionFillColor(Position position, Color fillColor) {
		drawSettings.get(position).setFillColor(fillColor);
	}

	public final void setPositionStroke(Position position, Stroke stroke) {
		drawSettings.get(position).setStroke(stroke);
	}

	public final void setPositionStrokeColor(Position position, Color strokeColor) {
		drawSettings.get(position).setStrokeColor(strokeColor);
	}

	public final Color getPositionFillColor(Position position) {
		return drawSettings.get(position).getFillColor();
	}

	public final Stroke getPositionStroke(Position position) {
		return drawSettings.get(position).getStroke();
	}

	public final Color getPositionStrokeColor(Position position) {
		return drawSettings.get(position).getStrokeColor();
	}

	public final void setPositionRotate(Position position, boolean isRotate) {
		FingerShapeSettings setting = fingerSettings.get(position);
		if (setting != null) {
			setting.setRotate(isRotate);
		}
	}

	public final boolean isPositionRotate(Position position) {
		return fingerSettings.get(position).isRotate();
	}

	/**
	 * Paints the transcoded SVG image on the specified graphics context. You
	 * can install a custom transformation on the graphics context to scale the
	 * image.
	 * @param g1d Graphics context.
	 */
	@Override
	public final void paint(Graphics g1d) {
		super.paint(g1d);
		Graphics2D g = (Graphics2D) g1d;

		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g.setRenderingHints(renderHints);
		double scale = getOptimalScale();
		if (this.flipped) {
			g.scale(-scale, scale);
			g.translate(-originalWidth - getXOffset(scale), getYOffset(scale));
		} else {
			g.scale(scale, scale);
			g.translate(getXOffset(scale), getYOffset(scale));
		}

		float origAlpha = 1.0f;
		Composite origComposite = ((Graphics2D) g).getComposite();
		if (origComposite instanceof AlphaComposite) {
			AlphaComposite origAlphaComposite = (AlphaComposite) origComposite;
			if (origAlphaComposite.getRule() == AlphaComposite.SRC_OVER) {
				origAlpha = origAlphaComposite.getAlpha();
			}
		}

		AffineTransform defaultTransform = g.getTransform();
		//
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform defaultTransform0 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		// _0
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform defaultTransform00 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		// _0_0
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		for (int i = 0; i < drawOrder.size(); i++) {
			Position position = drawOrder.get(i);
			drawShape(shapes.get(position), g, drawSettings.get(position), origAlpha);
		}

		for (int i = 0; i < drawOrder.size(); i++) {
			Position position = drawOrder.get(i);
			Shape shape = fingerRotationShapes.get(position);
			FingerShapeSettings fingerShapeSettings = fingerSettings.get(position);
			if (fingerShapeSettings != null && fingerShapeSettings.isRotate()) {
				drawRotation(g, shape);
			}
		}

		g.setTransform(defaultTransform00);
		g.setTransform(defaultTransform0);
		g.setTransform(defaultTransform);
		g.dispose();
	}

}

class FingerShapeSettings {
	private boolean isRotate;

	public FingerShapeSettings(boolean isRotate) {
		this.isRotate = isRotate;
	}

	public boolean isRotate() {
		return isRotate;
	}

	public void setRotate(boolean isRotate) {
		this.isRotate = isRotate;
	}

}

class ShapeDrawSettings {
	private Color fillColor;
	private Color strokeColor;
	private Stroke stroke;

	public ShapeDrawSettings(Color fillColor, Stroke stroke, Color strokeColor) {
		this.fillColor = fillColor;
		this.stroke = stroke;
		this.strokeColor = strokeColor;
	}

	/**
	 * @return the fillColor
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * @return the strokeColor
	 */
	public Color getStrokeColor() {
		return strokeColor;
	}

	/**
	 * @return the stroke
	 */
	public Stroke getStroke() {
		return stroke;
	}

	/**
	 * @param fillColor the fillColor to set
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	/**
	 * @param strokeColor the strokeColor to set
	 */
	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	/**
	 * @param stroke the stroke to set
	 */
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}
}
