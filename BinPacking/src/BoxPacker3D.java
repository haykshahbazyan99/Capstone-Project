import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Utilities.*;
import GeometricalShapes.*;


public class BoxPacker3D
{
    BoxPacker3D(Bin3D bin, ArrayList<Item3D> items, BinSortingInterface binSorter, ItemSortingInterface itemSorter) {
        this.bin = bin;
        this.items = items;
        this.binSorter = binSorter;
        this.itemSorter = itemSorter;
        this.packSequanceNumber = 0;
        this.uniqueBinId = 1; /// 0 for root
    }
    
    public void pack() {
        this.itemSorter.sortItems(this.items);
        ArrayList<Bin3D> bins = new ArrayList<>();
        bins.add(this.bin);

        for (Item3D item : this.items) {
            this.binSorter.sortBins(bins);
            int binIndex = 0;
            boolean binFound = false;
            for (Bin3D currentBin : bins) {
                if (!currentBin.isUsed()) {
                    if (packIt(currentBin, item, bins)) {
                        binFound = true;
                        break;
                    }
                }
                ++binIndex;
            }
            if (binFound) {
                bins.get(binIndex).setUsed(true);
            }
        }
        bin.ground();
    }
    
    private boolean packIt(Bin3D bin, Item3D item, ArrayList<Bin3D> bins) {
        if (!fit(bin, item)) {
            return false;
        }
        bin.setItem(item);
        item.setPackSequanceNumber(packSequanceNumber++);
        item.setWidthLocation(bin.getWidthLocation());
        item.setHeightLocation(bin.getHeightLocation());
        item.setLengthLocation(bin.getLengthLocation());
        
        splitBinWidth(bin, item);
        splitBinHeight(bin, item);
        splitBinLength(bin, item);
        
        if (bin.getXSubBin() != null) {
            /// we have created a new bin from the unused space when the item was added to the bin
            /// check if this space could be merged with any previously unused space in the user specified bin
            if (!merge(bin, bin.getXSubBin(), bins)) {
                bins.add(bin.getXSubBin());
            } else {
                bin.setXSubBin(null);
            }
        }
        
        if (bin.getYSubBin() != null) {
            /// we have created a new bin from the unused space when the item was added to the bin
            /// check if this space could be merged with any previously unused space in the user specified bin
            if (!merge(bin, bin.getYSubBin(), bins)) {
                bins.add(bin.getYSubBin());
            } else {
                bin.setYSubBin(null);
            }
        }
        
        if (bin.getZSubBin() != null) {
            /// we have created a new bin from the unused space when the item was added to the bin
            /// check if this space could be merged with any previously unused space in the user specified bin
            if (!merge(bin, bin.getZSubBin(), bins)) {
                bins.add(bin.getZSubBin());
            } else {
                bin.setZSubBin(null);
            }
        }
        
        return true;
    }
    
    private boolean fit(Bin3D bin, Item3D item) {
        if (bin.fit(item)) {
            return true;
        }
        
        item.spin(Item3D.Axis.AROUND_X);
        if (bin.fit(item)) {
            return true;
        }
        
        item.spin(Item3D.Axis.AROUND_X);
        item.spin(Item3D.Axis.AROUND_Y);
        if (bin.fit(item)) {
            return true;
        }
        
        item.spin(Item3D.Axis.AROUND_Y);
        item.spin(Item3D.Axis.AROUND_Z);
        if (bin.fit(item)) {
            return true;
        }
        
        item.spin(Item3D.Axis.AROUND_Z);
        return false;
    }
    
    private void splitBinWidth(Bin3D bin, Item3D item) {
        Map<String, Double> subBinPartitons = findSubBinSizes(bin, item);         
        final double dxWidth = subBinPartitons.get("dx_w");
        final double dxHeight = subBinPartitons.get("dx_h");
        final double dxLength = subBinPartitons.get("dx_l");
        if (dxLength < 0) {
            bin.setXSubBin(null);
            return;
        }
        /// TODO: Use builder pattern
        Bin3D subBinX = new Bin3D();
        subBinX.setId(uniqueBinId++);
        subBinX.setSide1(bin.getSide1().sizeSideTo(dxWidth));
        subBinX.setSide2(bin.getSide2().sizeSideTo(dxHeight));
        subBinX.setSide3(bin.getSide3().sizeSideTo(dxLength));
        subBinX.setWidthLocation(bin.getWidthLocation() + item.getSide1().getLength());
        subBinX.setHeightLocation(bin.getHeightLocation());
        subBinX.setLengthLocation(bin.getLengthLocation());
        subBinX.setName(bin.getName() + "_X_bin");
        bin.setXSubBin(subBinX);
        split(subBinX, bin);
    }
    
    private void splitBinHeight(Bin3D bin, Item3D item) {
        Map<String, Double> subBinPartitons = findSubBinSizes(bin, item);         
        final double dyWidth = subBinPartitons.get("dy_w");
        final double dyHeight = subBinPartitons.get("dy_h");
        final double dyLength = subBinPartitons.get("dy_l");
        if (dyLength < 0) {
            bin.setYSubBin(null);
            return;
        }
        /// TODO: Use builder pattern
        Bin3D subBinY = new Bin3D();
        subBinY.setId(uniqueBinId++);
        subBinY.setSide1(bin.getSide1().sizeSideTo(dyWidth));
        subBinY.setSide2(bin.getSide2().sizeSideTo(dyHeight));
        subBinY.setSide3(bin.getSide3().sizeSideTo(dyLength));
        subBinY.setWidthLocation(bin.getWidthLocation());
        subBinY.setHeightLocation(bin.getHeightLocation() + item.getSide2().getLength());
        subBinY.setLengthLocation(bin.getLengthLocation());
        subBinY.setName(bin.getName() + "_Y_bin");
        bin.setYSubBin(subBinY);
        split(subBinY, bin);
    }
    
    private void splitBinLength(Bin3D bin, Item3D item) {
        Map<String, Double> subBinPartitons = findSubBinSizes(bin, item);         
        final double dzWidth = subBinPartitons.get("dz_w");
        final double dzHeight = subBinPartitons.get("dz_h");
        final double dzLength = subBinPartitons.get("dz_l");
        if (dzLength < 0) {
            bin.setZSubBin(null);
            return;
        }
        /// TODO: Use builder pattern
        Bin3D subBinZ = new Bin3D();
        subBinZ.setId(uniqueBinId++);
        subBinZ.setSide1(bin.getSide1().sizeSideTo(dzWidth));
        subBinZ.setSide2(bin.getSide2().sizeSideTo(dzHeight));
        subBinZ.setSide3(bin.getSide3().sizeSideTo(dzLength));
        subBinZ.setWidthLocation(bin.getWidthLocation());
        subBinZ.setHeightLocation(bin.getHeightLocation());
        subBinZ.setLengthLocation(bin.getLengthLocation() + item.getSide3().getLength());
        subBinZ.setName(bin.getName() + "_Z_bin");
        bin.setZSubBin(subBinZ);
        split(subBinZ, bin);
    }
    
    private void split(Bin3D subBin, Bin3D bin) {
        subBin.setParentBin(bin);
        subBin.setRoot(bin.hasRoot() ? bin.getRoot() : bin);
    }
    
    /// Find the sizes of sub-bins. Try to allocate larger space for those partionts
    /// which occupy smaller size (kind of a balanced partitioning)
    /// For example Bin (10, 30, 10) -> Item (2, 23, 1) -> Diffs (8, 7, 9) (means xdiff > ydiff && xdiff < zdiff)
    /// New X Partition (8, 23, 10) (volume 1840)
    /// New Y Partition (10, 7, 10) (volume 700)
    /// New Z partition (2, 23, 9) (volume 414)
    Map<String, Double> findSubBinSizes(Bin3D bin, Item3D item) {
        final double widthDiff = bin.getSide1().getLength() - item.getSide1().getLength();
        final double heightDiff = bin.getSide2().getLength() - item.getSide2().getLength();
        final double lengthDiff = bin.getSide3().getLength() - item.getSide3().getLength();
        
        /// The width, height and lenght of x axis sub bin.
        double dxWidth = 0.0, dxHeight = 0.0, dxLength = 0.0;
        /// The width, height and lenght of y axis sub bin.
        double dyWidth = 0.0, dyHeight = 0.0, dyLength = 0.0;
        /// The width, height and lenght of z axis sub bin.
        double dzWidth = 0.0, dzHeight = 0.0, dzLength = 0.0;
        
        /// For example Bin (10, 30, 10) -> Item (2, 23, 1) -> Diffs (8, 7, 9)
        if (lengthDiff >= widthDiff && widthDiff >= heightDiff) {
            /// New X Partition (8, 23, 10) (volume 1840)
            dxWidth = bin.getSide1().getLength() - item.getSide1().getLength(); /// 10 - 2 = 8
            dxHeight = item.getSide2().getLength(); /// 23
            dxLength = bin.getSide3().getLength(); /// 10
            
            /// New Y Partition (10, 7, 10) (volume 700)
            dyWidth = bin.getSide1().getLength(); /// 10
            dyHeight = bin.getSide2().getLength() - item.getSide2().getLength(); /// 30 - 23 = 7
            dyLength = bin.getSide3().getLength(); /// 10
            
            /// New Z partition (2, 23, 9) (volume 414)
            dzWidth = item.getSide1().getLength(); /// 2
            dzHeight = item.getSide2().getLength(); /// 23
            dzLength = bin.getSide3().getLength() - item.getSide3().getLength(); /// 9
            
        /// For example Bin (10, 30, 10) -> Item (3, 22, 1) -> Diffs (7, 8, 9)
        } else if (lengthDiff >= heightDiff && heightDiff >= widthDiff) {
            /// New X partition (7, 30, 10) (volume 2100)
            dxWidth = bin.getSide1().getLength() - item.getSide1().getLength(); /// 7
            dxHeight = bin.getSide2().getLength(); /// 30
            dxLength = bin.getSide3().getLength(); /// 10
            
            /// New Y partition (3, 8, 10) (volume 240)
            dyWidth = item.getSide1().getLength(); /// 3
            dyHeight = bin.getSide2().getLength() - item.getSide2().getLength(); /// 8
            dyLength = bin.getSide3().getLength(); /// 10
            
            /// New Z partition (3, 22, 9) (volume 594)
            dzWidth = item.getSide1().getLength(); /// 3
            dzHeight = item.getSide2().getLength(); /// 22
            dzLength = bin.getSide3().getLength() - item.getSide3().getLength(); /// 9
            
        /// For example Bin (10, 30, 10) -> Item (1, 22, 3) -> Diffs (9, 8, 7)
        } else if (widthDiff >= heightDiff && heightDiff >= lengthDiff) {
            /// New X partition (9, 22, 3) (volume 594)
            dxWidth = bin.getSide1().getLength() - item.getSide1().getLength(); /// 9            
            dxHeight = item.getSide2().getLength(); /// 22
            dxLength = item.getSide3().getLength(); /// 3
            
            /// New Y partition (10, 8, 3) (volume 240)
            dyWidth = bin.getSide1().getLength(); /// 10
            dyHeight = bin.getSide2().getLength() - item.getSide2().getLength(); /// 8
            dyLength = item.getSide3().getLength(); /// 3
            
            /// New Z partition (10, 30, 7) (volume 2100)
            dzWidth = bin.getSide1().getLength(); /// 10
            dzHeight = bin.getSide2().getLength(); /// 30
            dzLength = bin.getSide3().getLength() - item.getSide3().getLength(); /// 7
            
        /// For example Bin (10, 30, 10) -> Item (1, 23, 2) -> Diffs (9, 7, 8)
        } else if (widthDiff >= lengthDiff && lengthDiff >= heightDiff) {
            /// New X partition (9, 23, 2) (volume 414)
            dxWidth = bin.getSide1().getLength() - item.getSide1().getLength(); /// 9
            dxHeight = item.getSide2().getLength(); /// 23
            dxLength = item.getSide3().getLength(); /// 2
            
            /// New Y partition (10, 7, 10) (volume 700)
            dyWidth = bin.getSide1().getLength(); /// 10
            dyHeight = bin.getSide2().getLength() - item.getSide2().getLength(); /// 7
            dyLength = bin.getSide3().getLength(); /// 10
            
            /// New Y partition (10, 23, 8) (volume 1840)
            dzWidth = bin.getSide1().getLength(); /// 10
            dzHeight = item.getSide2().getLength(); /// 23
            dzLength = bin.getSide3().getLength() - item.getSide3().getLength(); /// 8
            
        /// For example Bin (10, 30, 10) -> Item (2, 21, 3) -> Diffs (8, 9, 7)
        } else if (heightDiff >= widthDiff && widthDiff >= lengthDiff) {
            /// New X partition (8, 30, 3) (volume 720)
            dxWidth = bin.getSide1().getLength() - item.getSide1().getLength(); /// 8
            dxHeight = bin.getSide2().getLength(); /// 30
            dxLength = item.getSide3().getLength(); /// 3
            
            /// New Y partition (2, 9, 3) (volume 54)
            dyWidth = item.getSide1().getLength(); /// 2
            dyHeight = bin.getSide2().getLength() - item.getSide2().getLength(); /// 9
            dyLength = item.getSide3().getLength(); /// 3
            
            /// New Y partition (10, 30, 7) (volume 2100)
            dzWidth = bin.getSide1().getLength(); /// 10
            dzHeight = bin.getSide2().getLength(); /// 30
            dzLength = bin.getSide3().getLength() - item.getSide3().getLength(); /// 7
            
        /// For example Bin (10, 30, 10) -> Item (3, 21, 2) -> Diffs (7, 9, 8)
        } else if (heightDiff >= lengthDiff && lengthDiff >= widthDiff) {
            /// New X partition (7, 30, 10) (volume 2100)
            dxWidth = bin.getSide1().getLength() - item.getSide1().getLength(); /// 7
            dxHeight = bin.getSide2().getLength(); /// 30
            dxLength = bin.getSide3().getLength(); /// 10
            
            /// New Y partition (3, 9, 2) (volume 54)
            dyWidth = item.getSide1().getLength(); /// 3
            dyHeight = bin.getSide2().getLength() - item.getSide2().getLength(); /// 9
            dyLength = item.getSide3().getLength(); /// 2
            
            /// New Y partition (3, 30, 8) (volume 720)
            dzWidth = item.getSide1().getLength(); /// 10
            dzHeight = bin.getSide2().getLength(); /// 30
            dzLength = bin.getSide3().getLength() - item.getSide3().getLength(); /// 8
        }
        
        Map<String, Double> partitions = new HashMap<String, Double>();
        partitions.put("dx_w", dxWidth);
        partitions.put("dx_h", dxHeight);
        partitions.put("dx_l", dxLength);
        
        partitions.put("dy_w", dyWidth);
        partitions.put("dy_h", dyHeight);
        partitions.put("dy_l", dyLength);
        
        partitions.put("dz_w", dzWidth);
        partitions.put("dz_h", dzHeight);
        partitions.put("dz_l", dzLength);
        return partitions;
    }
    
    private boolean merge(Bin3D currentPackBin, Bin3D subBin, ArrayList<Bin3D> bins) {
        Bin3D subBinRoot = subBin.getRoot();
        for (Bin3D bin : bins) {
            if (currentPackBin.getId() == bin.getId()) {
                continue;
            }
            if (subBinRoot != null && bin.getRoot() != null && subBinRoot.getId() != bin.getRoot().getId()) {
                continue;
            }
            if (bin.isUsed()) {
                continue;
            }
            /// X side and Y side are the same and are on the same level.
            if (subBin.getSide1().getLength() == bin.getSide1().getLength() &&
                subBin.getSide2().getLength() == bin.getSide2().getLength() &&
                subBin.getWidthLocation() == bin.getWidthLocation() &&
                subBin.getHeightLocation() == bin.getHeightLocation()) {
                   
                /// Sub bin is located just down bellow of Z axis of current bin
                if (subBin.getLengthLocation() + subBin.getSide3().getLength() == bin.getLengthLocation()) {
                    bin.getSide3().setLength(subBin.getSide3().getLength() + bin.getSide3().getLength());
                    bin.setLengthLocation(subBin.getLengthLocation());
                    return true;
                }
                /// Bin is located just down bellow of Z axis of current subBin
                if (bin.getLengthLocation() + bin.getSide3().getLength() == subBin.getLengthLocation()) {
                    bin.getSide3().setLength(subBin.getSide3().getLength() + bin.getSide3().getLength());
                    return true;
                }
            }
            
            /// X side and Z side are the same and are on the same level.
            if (subBin.getSide1().getLength() == bin.getSide1().getLength() &&
                subBin.getSide3().getLength() == bin.getSide3().getLength() &&
                subBin.getWidthLocation() == bin.getWidthLocation() &&
                subBin.getLengthLocation() == bin.getLengthLocation()) {
                   
                /// Sub bin is located just down bellow of Y axis of current bin
                if (subBin.getHeightLocation() + subBin.getSide2().getLength() == bin.getHeightLocation()) {
                    bin.getSide2().setLength(subBin.getSide2().getLength() + bin.getSide2().getLength());
                    bin.setHeightLocation(subBin.getHeightLocation());
                    return true;
                }
                /// Bin is located just down bellow of Y axis of current subBin
                if (bin.getHeightLocation() + bin.getSide2().getLength() == subBin.getHeightLocation()) {
                    bin.getSide2().setLength(subBin.getSide2().getLength() + bin.getSide2().getLength());
                    return true;
                }
            }
            
            /// Y side and Z side are the same and are on the same level.
            if (subBin.getSide2().getLength() == bin.getSide2().getLength() &&
                subBin.getSide3().getLength() == bin.getSide3().getLength() &&
                subBin.getHeightLocation() == bin.getHeightLocation() &&
                subBin.getLengthLocation() == bin.getLengthLocation()) {
                   
                /// Sub bin is located just down bellow of X axis of current bin
                if (subBin.getWidthLocation() + subBin.getSide1().getLength() == bin.getWidthLocation()) {
                    bin.getSide1().setLength(subBin.getSide1().getLength() + bin.getSide1().getLength());
                    bin.setWidthLocation(subBin.getWidthLocation());
                    return true;
                }
                /// Bin is located just down bellow of X axis of current subBin
                if (bin.getWidthLocation() + bin.getSide1().getLength() == subBin.getWidthLocation()) {
                    bin.getSide1().setLength(subBin.getSide1().getLength() + bin.getSide1().getLength());
                    return true;
                }
            }
        }
        return false;
    }
    
    Bin3D bin;
    ArrayList<Item3D> items;
    private BinSortingInterface binSorter;
    private ItemSortingInterface itemSorter;
    int packSequanceNumber;
    int uniqueBinId;



}