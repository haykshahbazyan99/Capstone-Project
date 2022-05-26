# Capstone-Project
Details on how the algorithm works, you will find in the original submitted paper.

Execution: For running the program...

Go to "BinPacking" class in the soucre folder and run the main method. The output is going to be in a simple text file. 
This is done for testing and visualising the output via Phthon script.

Moreover. There are already 5 special test cases written for this program in that same class. If you want test your own case, make a subclass and import the data with the same shablon as prepared subclasses: that is create and detemine bin dimensions like the following 

       bin.setSide1(new Side(220, Side.Orientation.WIDTH));
       bin.setSide2(new Side(160, Side.Orientation.HEIGHT));
       bin.setSide3(new Side(100, Side.Orientation.LENGTH));

then fix number of item/boxes you want to pack, then fix the dimensions and traverse through it.

The following method will run the program

BoxPacker3D boxPacker = new BoxPacker3D(bin, items, new HighestBinLastStrategy(), new LargestFirstFitStrategy());
boxPacker.pack();

The output is going to be coordinates in which each item is going to be placed in the bin and will have sequence number which represents the order of placement. If the sequence number is -1 the item cannot be placed.

For visualtions purposes we use pyhton script (included in this repsository).
