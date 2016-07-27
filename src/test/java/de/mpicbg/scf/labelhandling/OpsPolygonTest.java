package de.mpicbg.scf.labelhandling;


import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.Regions;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.logic.BoolType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import org.junit.Test;
import org.scijava.Context;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: July 2016
 */
public class OpsPolygonTest {
    @Test
    public void testOpsPolygonGeneration()
    {
        OpService ops = new Context(OpService.class, OpMatchingService.class).getService(OpService.class);

        // create test image:
        // 2 dimensions
        // 20x20 in size
        // circle positioned at 10x10
        // circle radius 4
        Img<FloatType> testImage = Utilitities.getNDimensionalTestImage(2, 200, 60, 4);
        //Img<FloatType> testImage = getNDimensionalTestImage(2, 200, 100, 10);


        // print test image on console
        //System.out.println(ops.image().ascii(testImage));

        // create a labeling of it
        ImgLabeling<Integer, IntType> labeling = Utilitities.getIntIntImgLabellingFromLabelMapImg(testImage);

        // create a ROI list (with one item) containing the circle region
        ArrayList<RandomAccessibleInterval<BoolType>> labelMap = Utilitities.getRegionsFromImgLabeling(labeling);

        // get the ROI
        RandomAccessibleInterval<BoolType> roi = labelMap.get(0);


        // get its polygon
        Polygon polygon = ops.geom().contour(roi, true);

        // analyse polygon
        RealLocalizable point2 = ops.geom().centroid(Views.iterable(roi));
        System.out.println("centroid region:  " + point2);
        RealLocalizable point = ops.geom().centroid(polygon);
        System.out.println("centroid polygon: " + point);

        DoubleType size2 = ops.geom().size(Views.iterable(roi));
        System.out.println("size region:  " + size2);
        DoubleType size = ops.geom().size(polygon);
        System.out.println("size polygon: " + size);

    }

    @Test
    public void testOpsRegions()
    {
        OpService ops = new Context(OpService.class, OpMatchingService.class).getService(OpService.class);

        // create test image:
        // 2 dimensions
        // 20x20 in size
        // circle positioned at 10x10
        // circle radius 4
        Img<FloatType> testImage = Utilitities.getNDimensionalTestImage(2, 200, 60, 4);
        //Img<FloatType> testImage = getNDimensionalTestImage(2, 200, 100, 10);


        // print test image on console
        //System.out.println(ops.image().ascii(testImage));

        // create a labeling of it
        ImgLabeling<Integer, IntType> labeling = Utilitities.getIntIntImgLabellingFromLabelMapImg(testImage);

        // create a ROI list (with one item) containing the circle region
        ArrayList<RandomAccessibleInterval<BoolType>> labelMap = Utilitities.getRegionsFromImgLabeling(labeling);

        // get the ROI
        RandomAccessibleInterval<BoolType> roi = labelMap.get(0);

        long[] dims = new long[roi.numDimensions()];
        RandomAccessibleInterval<BitType> bitRoi = ArrayImgs.bits(dims);

        IterableRegion<BoolType> reg = Regions.iterable(roi);

        // analyse polygon
        RealLocalizable point3 = ops.geom().centroid(Views.iterable(roi));
        System.out.println("centroid region:  " + point3);
        System.out.println("-------------");
        RealLocalizable point2 = ops.geom().centroid(reg);
        System.out.println("-------------");
        System.out.println("centroid region:  " + point2);

        DoubleType size2 = ops.geom().size(Views.iterable(roi));
        System.out.println("size region:  " + size2);

    }



}
