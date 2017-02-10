package de.mpicbg.scf.labelhandling;

import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imagej.ops.geom.CentroidMesh;
import net.imagej.ops.geom.geom3d.mesh.DefaultMesh;
import net.imagej.ops.geom.geom3d.mesh.TriangularFacet;
import net.imagej.ops.geom.geom3d.mesh.Vertex;
import net.imglib2.RealPoint;
import org.junit.Test;
import org.scijava.Context;

import java.util.Arrays;
import java.util.Random;

/**
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: July 2016
 */
public class OpsMeshTest {
    @Test
    public void testCentroidMesh()
    {
        OpService ops = new Context(OpService.class, OpMatchingService.class).getService(OpService.class);

        DefaultMesh mesh = new DefaultMesh();
        mesh.addFace(new TriangularFacet(
                new Vertex(0,0,0),
                new Vertex(0,1,0),
                new Vertex(0,0,1)
        ));
        mesh.addFace(new TriangularFacet(
                new Vertex(0,0,0),
                new Vertex(0,1,0),
                new Vertex(1,0,0)
        ));
        mesh.addFace(new TriangularFacet(
                new Vertex(0,0,0),
                new Vertex(1,0,0),
                new Vertex(0,0,1)
        ));
        mesh.addFace(new TriangularFacet(
                new Vertex(1,0,0),
                new Vertex(0,0,1),
                new Vertex(0,1,0)
        ));
        final RealPoint c = (RealPoint) ops.run(CentroidMesh.class, mesh);
        System.out.println("C: " + c);




    }
}
