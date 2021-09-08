package de.morrigan.dev.utils.swing;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import org.apache.commons.lang3.Validate;

/**
 * Diese Klasse stellt Konstanten und Methoden zur Konfiguration eines Gridbaglayouts zur Verfügung, um die GUI
 * Konfiguration übersichtlicher zu gestalten.
 * 
 * @author morrigan
 */
public class GCUtil {

	public static final int WEST = GridBagConstraints.WEST;
	public static final int SOUTH = GridBagConstraints.SOUTH;
	public static final int EAST = GridBagConstraints.EAST;
	public static final int NORTH = GridBagConstraints.NORTH;
	public static final int SOUTHWEST = GridBagConstraints.SOUTHWEST;
	public static final int SOUTHEAST = GridBagConstraints.SOUTHEAST;
	public static final int NORTHWEST = GridBagConstraints.NORTHWEST;
	public static final int NORTHEAST = GridBagConstraints.NORTHEAST;
	public static final int CENTER = GridBagConstraints.CENTER;
	public static final int NONE = GridBagConstraints.NONE;
	public static final int HORI = GridBagConstraints.HORIZONTAL;
	public static final int VERT = GridBagConstraints.VERTICAL;
	public static final int BOTH = GridBagConstraints.BOTH;

	/**
	 * Diese Methode konfiguriert ein übergebenes GridBagConstraints Objekt.
	 * 
	 * @param gc Das zu konfigurierende GridBagConstraints Objekt. (not null).
	 * @param gridx {@link GridBagConstraints#gridx}
	 * @param gridy {@link GridBagConstraints#gridy}
	 * @param anchor {@link GridBagConstraints#anchor}
	 * @param fill {@link GridBagConstraints#fill}
	 * @param weightx {@link GridBagConstraints#weightx}
	 * @param weighty {@link GridBagConstraints#weighty}
	 * @param gridwidth {@link GridBagConstraints#gridwidth}
	 * @param gridheight {@link GridBagConstraints#gridheight}
	 * @param insets {@link GridBagConstraints#insets}
	 */
	public static void configGC(final GridBagConstraints gc, final int gridx, final int gridy, final int anchor,
			final int fill, final double weightx, final double weighty, final int gridwidth, final int gridheight,
			final Insets insets) {
		Validate.notNull(gc, "Folgende Parameter dürfen nicht null sein! gc: {}", gc);

		gc.gridx = gridx;
		gc.gridy = gridy;
		gc.anchor = anchor;
		gc.fill = fill;
		gc.weightx = weightx;
		gc.weighty = weighty;
		gc.gridwidth = gridwidth;
		gc.gridheight = gridheight;
		gc.insets = insets;
	}

	private GCUtil() {
		super();
	}
}
