package GeometricalShapes;

import Utilities.Utility;

/// TODO: Add rotational constraints
/// TODO: Add weights to the items.
/// TODO: Maybe the item can be broken if the above item is heavy.
/**
 * TODO: Calculate a score for a given item and rotation type.
 *
 * Scores are higher for rotations that closest match item dimensions to Bin dimensions.
 * For example, rotating the item so the longest side is aligned with the longest Bin side.
 *
 * Example (Bin is 11 x 8.5 x 5.5, Item is 8.1 x 5.2 x 5.2):
 *  Rotation 0:
 *    8.1 / 11  = 0.736
 *    5.2 / 8.5 = 0.612
 *    5.2 / 5.5 = 0.945
 *    -----------------
 *    0.736 ** 2 + 0.612 ** 2 + 0.945 ** 2 = 1.809
 *
 *  Rotation 1:
 *    8.1 / 8.5 = 0.953
 *    5.2 / 11 = 0.473
 *    5.2 / 5.5 = 0.945
 *    -----------------
 *    0.953 ** 2 + 0.473 ** 2 + 0.945 ** 2 = 2.025
 *
 */


public class Item3D extends Shape3D
{
    public enum Axis {

        AROUND_X,
        AROUND_Y,
        AROUND_Z
    }

    public Item3D() {
        setPackSequanceNumber(-1);
    }

    public void spin(Axis axis) {
        switch (axis) {
            case AROUND_X: {
                Side temp = getSide2();
                setSide2(getSide3());
                setSide3(temp);
                break;
            }
            case AROUND_Y: {
                Side temp = getSide1();
                setSide1(getSide3());
                setSide3(temp);
                break;
            }
            case AROUND_Z: {
                Side temp = getSide1();
                setSide1(getSide2());
                setSide2(temp);
                break;
            }
            default:
                System.out.println("Assert: Invalid case.");
        }
    }

    public int getPackSequanceNumber() {
        return this.packSequenceNumber;
    }

    public void setPackSequanceNumber(final int packSequenceNumber) {
        this.packSequenceNumber = packSequenceNumber;
    }

    public boolean isPacked() {
        return getPackSequanceNumber() != -1;
    }

    public boolean isAboveBellow(Item3D rhs) {
        if (getHeightLocation() == rhs.getHeightLocation()) {
            return false;
        }
        final double x1 = getWidthLocation();
        final double x2 = x1 + getSide1().getLength() - 1;
        final double y1 = getLengthLocation();
        final double y2 = y1 + getSide3().getLength() - 1;

        final double rhsX1 = rhs.getWidthLocation();
        final double rhsX2 = rhsX1 + rhs.getSide1().getLength() - 1;
        final double rhsY1 = rhs.getLengthLocation();
        final double rhsY2 = rhsY1 + rhs.getSide3().getLength() - 1;

        final boolean xOverlap = Utility.valueInRange(x1, rhsX1, rhsX2) || Utility.valueInRange(rhsX1, x1, x2);
        final boolean yOverlap = Utility.valueInRange(y1, rhsY1, rhsY2) || Utility.valueInRange(rhsY1, y1, y2);
        return xOverlap || yOverlap;
    }

    public String toString() {
        return "Item '" + getName() + "':" +
                " w = " + getSide1().getLength() +
                " h = " + getSide2().getLength() +
                " l = " + getSide3().getLength() +
                " x = " + getWidthLocation() +
                " y = " + getHeightLocation() +
                " z = " + getLengthLocation() +
                " volume = " + volume() +
                " seq = " + getPackSequanceNumber();
    }

    private int packSequenceNumber;
}