package de.morrigan.dev.utils;

import org.apache.commons.lang3.Validate;

/**
 * Stellt diverse Hilfsmethoden bereit zur bitweisen Manipulation von {@code long} Werten.
 * 
 * @author morrigan
 */
public class BitUtil {

  /**
   * Setzt beim übergebenen Wert das Bit an der angegebenen Position auf 0.
   * 
   * <pre>
   * BitUtils.removeLongBit(-1, 0)  = 0b1111 ... 1110
   * BitUtils.removeLongBit(-1, 63) = 0b0111 ... 1111
   * BitUtils.removeLongBit(-1, -1) = IllegalArgumentException
   * BitUtils.removeLongBit(-1, 64) = IllegalArgumentException
   * </pre>
   * 
   * @param value ein {@code long} Wert
   * @param position eine Position, an der das Bit auf 0 gesetzt werden soll <i>([0;63])</i>
   * @return übergebener Wert mit korrigiertem Bit
   * @throws IllegalArgumentException falls {@code position} ungültig ist
   */
  public static long removeLongBit(long value, int position) {
    Validate.inclusiveBetween(0, 63, position);
    return value & ~setLongBit(position);
  }

  /**
   * Setzt beim übergebenen Wert Bits auf 0 entsprechend der angegebenen Bitmaske.
   * 
   * <pre>
   * BitUtils.removeLongBitsByMask(-1, 0b1000 ... 0001L) = 0b0111 ... 1110
   * </pre>
   * 
   * @param value ein {@code long} Wert
   * @param mask eine Bitmaske, mittel der Bits auf 0 gesetzt werden
   * @return übergebene Wert, an dem entsprechend der Maske Bits auf 0 gesetzt wurden
   */
  public static long removeLongBitsByMask(long value, long mask) {
    return value & ~mask;
  }

  /**
   * Setzt alle Bits in einem {@code long} Wert.
   * 
   * @return ein Wert an dem alle Bits auf 1 gesetzt sind
   */
  public static long setAllBits() {
    return -1L;
  }

  /**
   * Setzt ein Bit an der angegebenen Position auf 1.
   * 
   * <pre>
   * BitUtils.setLongBit(0)  = 0b0000 ... 0001
   * BitUtils.setLongBit(63) = 0b1000 ... 0000
   * BitUtils.setLongBit(-1) = IllegalArgumentException
   * BitUtils.setLongBit(64) = IllegalArgumentException
   * </pre>
   * 
   * @param position Position, an der das Bit gesetzt werden soll <i>([0;63])</i>
   * @return Wert an dem genau ein Bit auf 1 gesetzt ist
   * @throws IllegalArgumentException falls {@code position} ungültig ist
   */
  public static long setLongBit(int position) {
    Validate.inclusiveBetween(0, 63, position);
    return 1L << position;
  }

  private BitUtil() {
    super();
  }
}
