package de.mpicbg.scf.labelhandling;

import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imagej.ops.geom.geom3d.mesh.Mesh;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.scijava.Context;

/**
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: July 2016
 */
public class SuchTest {
    public boolean largerThan(int x, int y)
    {
        OpService ops = new Context(OpService.class, OpMatchingService.class).getService(OpService.class);

        Mesh mesh = null;

        ops.geom().centroid(mesh);
        return x > y;


    }
}
