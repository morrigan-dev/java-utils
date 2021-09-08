package de.morrigan.dev.utils.swing;

import java.awt.Insets;

/**
 * Konstanten für verschiedene Insets zur Anordnung von GUI Komponenten.
 * 
 * @author morrigan
 */
public class InsetConstants {

	/** Abstand in Pixel für die Insets der einzelnen GUI-Elemente */
	public static final int DEFAULT_DISTANCE = 5;

	/** Kleinerer Abstand in Pixel für z.B. Buttons in einer Buttonleiste */
	public static final int DEFAULT_SMALL_DISTANCE = 2;

	/** Insets ohne Abstand */
	public static final Insets NO_INSETS = new Insets(0, 0, 0, 0);

	/** Insets mit oberem Abstand */
	public static final Insets TOP_INSETS = new Insets(DEFAULT_DISTANCE, 0, 0, 0);

	/** Insets mit linkem Abstand */
	public static final Insets LEFT_INSETS = new Insets(0, DEFAULT_DISTANCE, 0, 0);

	/** Insets mit unterem Abstand */
	public static final Insets BOTTOM_INSETS = new Insets(0, 0, DEFAULT_DISTANCE, 0);

	/** Insets mit rechtem Abstand */
	public static final Insets RIGHT_INSETS = new Insets(0, 0, 0, DEFAULT_DISTANCE);

	/** Insets mit linkem, oberem Abstand */
	public static final Insets LT_INSETS = new Insets(DEFAULT_DISTANCE, DEFAULT_DISTANCE, 0, 0);

	/** Insets mit linkem, oberem, rechtem Abstand */
	public static final Insets LTR_INSETS = new Insets(DEFAULT_DISTANCE, DEFAULT_DISTANCE, 0, DEFAULT_DISTANCE);

	/** Insets mit linkem, oberem, unterem Abstand */
	public static final Insets LTB_INSETS = new Insets(DEFAULT_DISTANCE, DEFAULT_DISTANCE, DEFAULT_DISTANCE, 0);

	/** Insets mit Abstand in allen Richtungen */
	public static final Insets ALL_INSETS = new Insets(DEFAULT_DISTANCE, DEFAULT_DISTANCE, DEFAULT_DISTANCE,
			DEFAULT_DISTANCE);

	/** Insets mit oberem Abstand */
	public static final Insets SMALL_TOP_INSETS = new Insets(DEFAULT_SMALL_DISTANCE, 0, 0, 0);

	/** Insets mit linkem Abstand */
	public static final Insets SMALL_LEFT_INSETS = new Insets(0, DEFAULT_SMALL_DISTANCE, 0, 0);

	/** Insets mit unterem Abstand */
	public static final Insets SMALL_BOTTOM_INSETS = new Insets(0, 0, DEFAULT_SMALL_DISTANCE, 0);

	/** Insets mit rechtem Abstand */
	public static final Insets SMALL_RIGHT_INSETS = new Insets(0, 0, 0, DEFAULT_SMALL_DISTANCE);

	/** Insets mit linkem, oberem Abstand */
	public static final Insets SMALL_LT_INSETS = new Insets(DEFAULT_SMALL_DISTANCE, DEFAULT_SMALL_DISTANCE, 0, 0);

	/** Insets mit linkem, oberem, rechtem Abstand */
	public static final Insets SMALL_LTR_INSETS = new Insets(DEFAULT_SMALL_DISTANCE, DEFAULT_SMALL_DISTANCE, 0,
			DEFAULT_SMALL_DISTANCE);

	/** Insets mit linkem, oberem, unterem Abstand */
	public static final Insets SMALL_LTB_INSETS = new Insets(DEFAULT_SMALL_DISTANCE, DEFAULT_SMALL_DISTANCE,
			DEFAULT_SMALL_DISTANCE, 0);

	/** Insets mit Abstand in allen Richtungen */
	public static final Insets SMALL_ALL_INSETS = new Insets(DEFAULT_SMALL_DISTANCE, DEFAULT_SMALL_DISTANCE,
			DEFAULT_SMALL_DISTANCE, DEFAULT_SMALL_DISTANCE);

	private InsetConstants() {
		super();
	}
}
