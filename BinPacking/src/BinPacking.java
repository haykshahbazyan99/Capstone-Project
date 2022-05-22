import GeometricalShapes.Side;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import GeometricalShapes.Item3D;
import GeometricalShapes.Bin3D;
import Utilities.ItemSortingInterface;
import Utilities.BinSortingInterface;
import Utilities.Utility;


public class BinPacking {

    public static void main(String[] args) {

        try {
            PrintStream myConsole = new PrintStream(new File("E://java.txt"));
            System.setOut(myConsole);
            myConsole.print(FirstItemMustBeRotated.run());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

//        SimpleTest.run();
//        RotationTest.run();
//        SevenItems.run();
//        BigItemIsPackedFirst.run();
//        FirstItemMustBeRotated.run();
    }
}


class LargestFirstFitStrategy implements ItemSortingInterface {
    class ItemComparator implements Comparator<Item3D> {
        public int compare(Item3D a, Item3D b) {
            return (int) (Utility.roundLong(b.areaWithHeight()) - Utility.roundLong(a.areaWithHeight()));
        }
    }

    @Override
    public void sortItems(ArrayList<Item3D> items) {
        Collections.sort(items, new ItemComparator());
    }
}

class HighestBinLastStrategy implements BinSortingInterface {
    class BinComparator implements Comparator<Bin3D> {
        public int compare(Bin3D a, Bin3D b) {
            return (int) (Utility.roundLong(a.getHeightLocation()) - Utility.roundLong(b.getHeightLocation()));
        }
    }

    @Override
    public void sortBins(ArrayList<Bin3D> bins) {
        Collections.sort(bins, new BinComparator());
    }
}

class SimpleTest {
    public static String run() {

        System.out.println("============ Test1 Simple test ==============");
        Bin3D bin = new Bin3D();
        bin.setName("Bin1");
        bin.setId(0);
        bin.setSide1(new Side(1000, Side.Orientation.WIDTH));
        bin.setSide2(new Side(1000, Side.Orientation.HEIGHT));
        bin.setSide3(new Side(1000, Side.Orientation.LENGTH));

        ArrayList<Item3D> items = new ArrayList<>();
        final int ITEM_COUNT = 3;
        for (int i = 0; i < ITEM_COUNT; ++i) {
            Item3D item = new Item3D();
            item.setName("Item_" + i);
            item.setSide1(new Side(10, Side.Orientation.WIDTH));
            item.setSide2(new Side(10, Side.Orientation.HEIGHT));
            item.setSide3(new Side(10, Side.Orientation.LENGTH));
            System.out.println(item);
            items.add(item);
        }

        BoxPacker3D boxPacker = new BoxPacker3D(bin, items, new HighestBinLastStrategy(), new LargestFirstFitStrategy());
        boxPacker.pack();
        System.out.println("");

        String result = "";
        for (int i = 0; i < ITEM_COUNT; ++i) {
            Item3D item = items.get(i);
//            System.out.println(item);
            result += item.toString() + "\n";
        }
        return result;
//        System.out.println("============ Test1 Simple test finish ==============");
    }
}

class RotationTest {

    public static void run() {
        System.out.println("============ Test2 Rotation test ==============");
        Bin3D bin = new Bin3D();
        bin.setName("Bin1");
        bin.setId(0);
        bin.setSide1(new Side(296, Side.Orientation.WIDTH));
        bin.setSide2(new Side(296, Side.Orientation.HEIGHT));
        bin.setSide3(new Side(8, Side.Orientation.LENGTH));

        ArrayList<Item3D> items = new ArrayList<Item3D>();
        final int ITEM_COUNT = 1;
        for (int i = 0; i < ITEM_COUNT; ++i) {
            Item3D item = new Item3D();
            item.setName("Item_" + i);
            item.setSide1(new Side(250, Side.Orientation.WIDTH));
            item.setSide2(new Side(2, Side.Orientation.HEIGHT));
            item.setSide3(new Side(250, Side.Orientation.LENGTH));
            System.out.println(item);
            items.add(item);
        }

        BoxPacker3D boxPacker = new BoxPacker3D(bin, items, new HighestBinLastStrategy(), new LargestFirstFitStrategy());
        boxPacker.pack();
        System.out.println("");

        for (int i = 0; i < ITEM_COUNT; ++i) {
            Item3D item = items.get(i);
            System.out.println(item);
        }
        System.out.println("============ Test2 Rotation test finish ==============");
    }
}

class SevenItems {
    public static String run() {
        System.out.println("============ Test3 Seven Items test ==============");
        Bin3D bin = new Bin3D();
        bin.setName("Bin1");
        bin.setId(0);
        bin.setSide1(new Side(220, Side.Orientation.WIDTH));
        bin.setSide2(new Side(160, Side.Orientation.HEIGHT));
        bin.setSide3(new Side(100, Side.Orientation.LENGTH));

        ArrayList<Item3D> items = new ArrayList<>();
        final int ITEM_COUNT = 7;
        double[][] m = {
                {20, 100, 30},
                {100, 20, 30},
                {20, 100, 30},
                {100, 20, 30},
                {100, 20, 30},
                {100, 100, 30},
                {100, 100, 30}};


        for (int i = 0; i < ITEM_COUNT; ++i) {
            Item3D item = new Item3D();
            item.setName("Item_" + i);
            item.setSide1(new Side(m[i][0], Side.Orientation.WIDTH));
            item.setSide2(new Side(m[i][1], Side.Orientation.HEIGHT));
            item.setSide3(new Side(m[i][2], Side.Orientation.LENGTH));
            System.out.println(item);
            items.add(item);
        }

        BoxPacker3D boxPacker = new BoxPacker3D(bin, items, new HighestBinLastStrategy(), new LargestFirstFitStrategy());
        boxPacker.pack();
        System.out.println("");

        String result = "";
        for (int i = 0; i < ITEM_COUNT; ++i) {
            Item3D item = items.get(i);
//            System.out.println(item);
            result += item.toString() + "\n";
        }
        System.out.println("============ Test3 Seven Items test finish ==============");
        return result;
    }
}

class BigItemIsPackedFirst {
    public static String run() {
        System.out.println("============ Test4 Big item packed first test ==============");
        Bin3D bin = new Bin3D();
        bin.setName("Bin1");
        bin.setId(0);
        bin.setSide1(new Side(100, Side.Orientation.WIDTH));
        bin.setSide2(new Side(100, Side.Orientation.HEIGHT));
        bin.setSide3(new Side(100, Side.Orientation.LENGTH));

        ArrayList<Item3D> items = new ArrayList<Item3D>();
        final int ITEM_COUNT = 3;
        double[][] m = {
                {50, 100, 100},
                {100, 100, 100},
                {50, 100, 100}};



        for (int i = 0; i < ITEM_COUNT; ++i) {
            Item3D item = new Item3D();
            item.setName("Item_" + i);
            item.setSide1(new Side(m[i][0], Side.Orientation.WIDTH));
            item.setSide2(new Side(m[i][1], Side.Orientation.HEIGHT));
            item.setSide3(new Side(m[i][2], Side.Orientation.LENGTH));
            System.out.println(item);
            items.add(item);
        }

        BoxPacker3D boxPacker = new BoxPacker3D(bin, items, new HighestBinLastStrategy(), new LargestFirstFitStrategy());
        boxPacker.pack();
        System.out.println("");

        String result = "";
        for (int i = 0; i < ITEM_COUNT; ++i) {
            Item3D item = items.get(i);
            result += item.toString() + '\n';
//            System.out.println(item);
        }

        System.out.println("============ Test4 Big item packed first test finish ==============");
        return result;
    }
}

class FirstItemMustBeRotated {

    public static String run() {
        System.out.println("============ Test5 first item must be rotated test ==============");
        Bin3D bin = new Bin3D();
        bin.setName("Bin1");
        bin.setId(0);
        bin.setSide1(new Side(11, Side.Orientation.WIDTH));
        bin.setSide2(new Side(8.5, Side.Orientation.HEIGHT));
        bin.setSide3(new Side(5.5, Side.Orientation.LENGTH));

        ArrayList<Item3D> items = new ArrayList<Item3D>();
        final int ITEM_COUNT = 2;
        double[][] m = {
                {8.1, 5.1, 2.2},
                {8.1, 5.1, 3.3}};

        for (int i = 0; i < ITEM_COUNT; ++i) {
            Item3D item = new Item3D();
            item.setName("Item_" + i);
            item.setSide1(new Side(m[i][0], Side.Orientation.WIDTH));
            item.setSide2(new Side(m[i][1], Side.Orientation.HEIGHT));
            item.setSide3(new Side(m[i][2], Side.Orientation.LENGTH));
            System.out.println(item);
            items.add(item);
        }

        BoxPacker3D boxPacker = new BoxPacker3D(bin, items, new HighestBinLastStrategy(), new LargestFirstFitStrategy());
        boxPacker.pack();
        System.out.println("");

        String result = "";
        for (int i = 0; i < ITEM_COUNT; ++i) {
            Item3D item = items.get(i);
//            System.out.println(item);
            result += item.toString() + '\n';

        }
        System.out.println("============ Test5 First item must be rotated test finish ==============");
        return result;
    }
}