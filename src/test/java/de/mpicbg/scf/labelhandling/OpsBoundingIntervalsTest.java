package de.mpicbg.scf.labelhandling;

import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.geom.geom3d.mesh.Facet;
import net.imagej.ops.geom.geom3d.mesh.Mesh;
import net.imagej.ops.geom.geom3d.mesh.Vertex;
import net.imglib2.RandomAccess;
import net.imglib2.RealLocalizable;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.roi.Regions;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.logic.BoolType;
import org.junit.Test;
import org.scijava.Context;

/**
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: July 2016
 */
public class OpsBoundingIntervalsTest {
    @Test
    public void testMeshBoundingBox() {
        OpService ops = new Context(OpService.class, OpMatchingService.class).getService(OpService.class);

        Img<BitType> img = ArrayImgs.bits(new long[]{10, 10, 10});
        RandomAccess<BitType> ra = img.randomAccess();

        int count = 0;
        for (int x = 2; x < 6; x++) {
            for (int y = 2; y < 6; y++) {
                for (int z = 2; z < 6; z++) {
                    Utilitities.setPixel(ra, new long[]{x, y, z}, true);
                    count ++;
                }
            }
        }
        Mesh mesh = ops.geom().marchingCubes(img);

        RealLocalizable[] positions = new RealLocalizable[mesh.getVertices().size()];
        mesh.getVertices().toArray(positions);

        System.out.println("Mesh Minimum x: " + getMinimum(positions, 0));
        System.out.println("Mesh Maximum x: " + getMaximum(positions, 0));

        System.out.println("Mesh source voxel count: " + count);
        System.out.println("Mesh Size: " + ops.geom().size(mesh));
        System.out.println("Mesh Centroid: " + ops.geom().centroid(mesh));
        System.out.println("Img Centroid: " + ops.geom().centroid(Regions.iterable(img)));
    }

    @Test
    public void testPolygonBoundingBox()
    {
        OpService ops = new Context(OpService.class, OpMatchingService.class).getService(OpService.class);

        Img<BitType> img = ArrayImgs.bits(new long[]{10, 10});
        RandomAccess<BitType> ra = img.randomAccess();

        int count = 0;
        for (int x = 2; x < 6; x++) {
            for (int y = 2; y < 6; y++) {
                Utilitities.setPixel(ra, new long[]{x, y}, true);
                count ++;
            }
        }
        Polygon polygon = ops.geom().contour(img, true);

        RealLocalizable[] positions = new RealLocalizable[polygon.getVertices().size()];

        polygon.getVertices().toArray(positions);

        System.out.println("Polygon Minimum x: " + getMinimum(positions, 0));
        System.out.println("Polygon Maximum x: " + getMaximum(positions, 0));

        System.out.println("Polygon source pixel count: " + count);
        System.out.println("Polygon Size: " + ops.geom().size(polygon));
        System.out.println("Polygon Centroid: " + ops.geom().centroid(polygon));
        System.out.println("Img Centroid: " + ops.geom().centroid(Regions.iterable(img)));
    }


    private double getMinimum(RealLocalizable[] positions, int d)
    {
        double minimum = Double.POSITIVE_INFINITY;

        for (RealLocalizable vertex : positions)
        {
            if (vertex.getDoublePosition(d) < minimum)
            {
                minimum = vertex.getDoublePosition(d);
            }
        }
        return minimum;
    }

    private double getMaximum(RealLocalizable[] positions, int d)
    {
        double maximum = Double.NEGATIVE_INFINITY;

        for (RealLocalizable vertex : positions)
        {
            if (vertex.getDoublePosition(d) > maximum)
            {
                maximum = vertex.getDoublePosition(d);
            }
        }
        return maximum;
    }



}
