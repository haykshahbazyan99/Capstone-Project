package GeometricalShapes;

public class Side
{
    public enum Orientation
    {
        WIDTH,
        HEIGHT,
        LENGTH
    }

    private double length;
    private Orientation orientation;

    public Side(final double length, final Orientation orientation) {
        setLength(length);
        setOrientation(orientation);
    }
    
    public double getLength() {
        return this.length;
    }
    
    public void setLength(final double length) {
        this.length = length;
    }
    
    public Orientation getOrientation() {
        return this.orientation;
    }
    
    public void setOrientation(final Orientation orientation) {
        this.orientation = orientation;
    } 
    
    public Side sizeSideTo(final double newSize) {
        return new Side(newSize, this.getOrientation());
    }
    

}