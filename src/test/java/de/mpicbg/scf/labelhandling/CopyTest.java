package de.mpicbg.scf.labelhandling;

import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.roi.Regions;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.logic.BoolType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;

import java.util.ArrayList;
import java.util.Random;

/**
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: July 2016
 */
public class CopyTest {
    OpService ops;

    @Before
    public void initialisation()
    {
        ops = new Context(OpService.class, OpMatchingService.class).getService(OpService.class);
    }

    @Test
    public void copyTest()
    {
        Random random = new Random();
        for (int radius = 5; radius < 100; radius += 15) {

            int copyCount = 0;
            int copyCount2 = 0;

            for (int i = 0; i < 1000; i++) {

                Img<FloatType> testImage = Utilitities.getNDimensionalTestImage(2, 200, 100, radius);

                // create a labeling of it
                ImgLabeling<Integer, IntType> labeling = Utilitities.getIntIntImgLabellingFromLabelMapImg(testImage);

                // create a ROI list (with one item) containing the circle region
                ArrayList<RandomAccessibleInterval<BoolType>> labelMap = Utilitities.getRegionsFromImgLabeling(labeling);

                // get the ROI
                RandomAccessibleInterval<BoolType> roi = labelMap.get(0);

                if (random.nextBoolean()) {
                    convertBoolTypeImgToBitType(roi);
                    copyCount++;
                } else {
                    convertBoolTypeImgToBitType2(roi);
                    copyCount2++;
                }
            }

            System.out.println("When processing a circle with radius " + radius);
            System.out.println("average copy duration:  " + (durationCopy / copyCount) + "ns (" + copyCount + " runs)");
            System.out.println("average copy2 duration: " + (durationCopy2 / copyCount2) + "ns (" + copyCount2 + " runs)");
        }
    }

    static long durationCopy = 0;
    public Img<BitType> convertBoolTypeImgToBitType(RandomAccessibleInterval<BoolType> rai) {
        long startTimeStamp = System.nanoTime();
        long[] dims = new long[rai.numDimensions()];
        for (int d = 0; d < rai.numDimensions(); d++)
        {
            dims[d] = rai.max(d) + 1;
        }

        Img<BitType> map = ArrayImgs.bits(dims);
        Cursor<Void> cur = Regions.iterable(rai).cursor();

        RandomAccess<BitType> ra = map.randomAccess();

        long[] position = new long[rai.numDimensions()];
        while (cur.hasNext())
        {
            cur.next();
            cur.localize(position);

            ra.setPosition(position);
            ra.get().set(true);
        }
        durationCopy += System.nanoTime() - startTimeStamp;
        return map;
    }

    static long durationCopy2 = 0;
    public Img<BitType> convertBoolTypeImgToBitType2(final RandomAccessibleInterval<BoolType> rai) {
        long startTimeStamp = System.nanoTime();
        Img<BitType> map = ops.create().img(rai, new BitType());
        Cursor<BoolType> cur = Views.flatIterable(rai).cursor();
        Cursor<BitType> res = Views.flatIterable(map).cursor();

        while (cur.hasNext()) {
            res.next().set(cur.next().get());
        }
        durationCopy2 += System.nanoTime() - startTimeStamp;

        return map;
    }
}
