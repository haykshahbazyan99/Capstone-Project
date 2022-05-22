package GeometricalShapes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Utilities.Utility;

class Item3DHeightLocationComperator implements Comparator<Item3D> {
    public int compare(Item3D a, Item3D b) {
        return (int) (Utility.roundLong(a.getHeightLocation()) - Utility.roundLong(b.getHeightLocation()));
    }
}

public class Bin3D extends Shape3D {
    public Bin3D() {
        setUsed(false);
        setId(-1);
    }

    public int getId() {
        return this.uniqueId;
    }

    public void setId(final int id) {
        this.uniqueId = id;
    }

    public Bin3D getParentBin() {
        return this.parentBin;
    }

    public void setParentBin(Bin3D parentBin) {
        this.parentBin = parentBin;
    }

    public boolean hasParent() {
        return this.parentBin != null;
    }

    public Bin3D getRoot() {
        return this.rootBin;
    }

    public void setRoot(Bin3D originalParentBin) {
        this.rootBin = rootBin;
    }

    public boolean hasRoot() {
        return this.rootBin != null;
    }

    public double getWidthLocation() {
        return this.widthOffsetFromRoot;
    }

    public void setWidthLocation(final double widthOffsetFromRoot) {
        this.widthOffsetFromRoot = widthOffsetFromRoot;
    }

    public double getHeightLocation() {
        return this.heightOffsetFromRoot;
    }

    public void setHeightLocation(final double heightOffsetFromRoot) {
        this.heightOffsetFromRoot = heightOffsetFromRoot;
    }

    public double getLengthLocation() {
        return this.lengthOffsetFromRoot;
    }

    public void setLengthLocation(final double lengthOffsetFromRoot) {
        this.lengthOffsetFromRoot = lengthOffsetFromRoot;
    }

    public Bin3D getXSubBin() {
        return this.xSubBin;
    }

    public void setXSubBin(Bin3D bin) {
        if (getSide1().getOrientation() == Side.Orientation.WIDTH) {
            this.xSubBin = bin;
        } else if (getSide2().getOrientation() == Side.Orientation.WIDTH) {
            this.ySubBin = bin;
        } else if (getSide3().getOrientation() == Side.Orientation.WIDTH) {
            this.zSubBin = bin;
        }
    }

    public Bin3D getYSubBin() {
        return this.ySubBin;
    }

    public void setYSubBin(Bin3D bin) {
        if (getSide1().getOrientation() == Side.Orientation.HEIGHT) {
            this.xSubBin = bin;
        } else if (getSide2().getOrientation() == Side.Orientation.HEIGHT) {
            this.ySubBin = bin;
        } else if (getSide3().getOrientation() == Side.Orientation.HEIGHT) {
            this.zSubBin = bin;
        }
    }

    public Bin3D getZSubBin() {
        return this.zSubBin;
    }

    public void setZSubBin(Bin3D bin) {
        if (getSide1().getOrientation() == Side.Orientation.LENGTH) {
            this.xSubBin = bin;
        } else if (getSide2().getOrientation() == Side.Orientation.LENGTH) {
            this.ySubBin = bin;
        } else if (getSide3().getOrientation() == Side.Orientation.LENGTH) {
            this.zSubBin = bin;
        }
    }

    public Item3D getItem() {
        return this.item;
    }

    public void setItem(Item3D item) {
        this.item = item;
    }

    public void setUsed(final boolean used) {
        this.used = used;
    }

    public boolean isUsed() {
        return this.used;
    }

    public void itemsInBin(ArrayList<Item3D> items) {
        if (this.item != null) {
            items.add(this.item);
        }
        if (getXSubBin() != null) {
            getXSubBin().itemsInBin(items);
        }
        if (getYSubBin() != null) {
            getYSubBin().itemsInBin(items);
        }
        if (getZSubBin() != null) {
            getZSubBin().itemsInBin(items);
        }
    }

    public void ground() {
        ArrayList<Item3D> items = new ArrayList<Item3D>();
        itemsInBin(items);
        boolean slide = true;
        while (slide) {
            slide = false;
            Collections.sort(items, new Item3DHeightLocationComperator());
            for (Item3D testItem : items) {
                if (testItem.getHeightLocation() == 0) {
                    continue;
                }
                double highestBellow = 0;
                for (Item3D bellowItem : items) {
                    if (bellowItem.isAboveBellow(testItem)) {
                        final double height = bellowItem.getHeightLocation() + bellowItem.getSide2().getLength();
                        if (height > highestBellow) {
                            highestBellow = height;
                        }
                    }
                }
                /// slide down
                if (highestBellow < testItem.getHeightLocation()) {
                    testItem.setHeightLocation(highestBellow);
                    slide = true;
                    break;
                }
            }
        }
    }

    public boolean fit(Item3D item) {
        if (!(item.getSide1().getLength() <= getSide1().getLength() &&
                item.getSide2().getLength() <= getSide2().getLength() &&
                item.getSide3().getLength() <= getSide3().getLength())) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "Bin '" + getName() + "':" +
                " w = " + getSide1().getLength() +
                " h = " + getSide2().getLength() +
                " l = " + getSide3().getLength() +
                " y = " + getHeightLocation() +
                " hasXsub = " + (getXSubBin() != null) +
                " hasYsub = " + (getYSubBin() != null) +
                " hasZsub = " + (getZSubBin() != null) +
                " hasRoot = " + hasRoot();
    }

    private Bin3D parentBin;
    private Bin3D rootBin;
    private Bin3D xSubBin;
    private Bin3D ySubBin;
    private Bin3D zSubBin;
    private double widthOffsetFromRoot;
    private double heightOffsetFromRoot;
    private double lengthOffsetFromRoot;
    private Item3D item;
    private boolean used; /// Or this.itme != null
    private int uniqueId;
}
