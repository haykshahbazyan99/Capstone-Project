package Utilities;

public class Utility
{
    public static long roundLong(double number) {
        final long PRECISION = 1000;
        return (long) (number * PRECISION);
    }

    public static boolean valueInRange(final double value, final double min, final double max) {
        return (value >= min && value <= max);
    }
}
