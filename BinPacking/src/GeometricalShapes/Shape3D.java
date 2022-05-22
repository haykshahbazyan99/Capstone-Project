package GeometricalShapes;

import Utilities.Utility;

public abstract class Shape3D
{
    public Shape3D() {
        this.side1 = this.side2 = this.side3 = null;
    }

    public void setSide1(Side side) {
        this.side1 = side;
    }

    public Side getSide1() {
        return this.side1;
    }

    public void setSide2(Side side) {
        this.side2 = side;
    }

    public Side getSide2() {
        return this.side2;
    }

    public void setSide3(Side side) {
        this.side3 = side;
    }

    public Side getSide3() {
        return this.side3;
    }

    public Side getOriginalSide1() {
        if (this.side1.getOrientation() == Side.Orientation.WIDTH) {
            return this.side1;
        } else if (this.side2.getOrientation() == Side.Orientation.WIDTH) {
            return this.side2;
        }
        return this.side3;
    }

    public Side getOriginalSide2() {
        if (this.side2.getOrientation() == Side.Orientation.HEIGHT) {
            return this.side2;
        } else if (this.side3.getOrientation() == Side.Orientation.HEIGHT) {
            return this.side3;
        }
        return this.side1;
    }

    public Side getOriginalSide3() {
        if (this.side3.getOrientation() == Side.Orientation.LENGTH) {
            return this.side3;
        } else if (this.side2.getOrientation() == Side.Orientation.LENGTH) {
            return this.side2;
        }
        return this.side1;
    }

    public double volume() {
        return getSide1().getLength() * getSide2().getLength() * getSide3().getLength();
    }
    
    public double areaWithHeight() {
        return getSide1().getLength() * getSide2().getLength();
    }
    
    public double areaWithLenght() {
        return getSide1().getLength() * getSide3().getLength();
    }

    public long comparesTo(Shape3D rhs) {
        final long volume1 = Utility.roundLong(rhs.volume());
        final long volume2 = Utility.roundLong(rhs.volume());
        return volume1 - volume2;
    }

    public double getWidthLocation() {
        return this.widthLocation;
    }

    public void setWidthLocation(final double widthLocation) {
        this.widthLocation = widthLocation;
    }

    public double getHeightLocation() {
        return this.heightLocation;
    }

    public void setHeightLocation(final double heightLocation) {
        this.heightLocation = heightLocation;
    }

    public double getLengthLocation() {
        return this.lengthLocation;
    }

    public void setLengthLocation(final double lengthLocation) {
        this.lengthLocation = lengthLocation;
    }

    public String getName() {
        return this.name;
    }

    public  void setName(String name) {
        this.name = name;
    }

    private Side side1;
    private Side side2;
    private Side side3;

    private double widthLocation;
    private double heightLocation;
    private double lengthLocation;
    private String name;
}
