package de.mpicbg.scf.labelhandling;

import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccess;
import net.imglib2.algorithm.labeling.ConnectedComponents;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;
import org.junit.Test;
import org.scijava.Context;

/**
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: August 2016
 */
public class FillHolesTest {
    @Test
    public void testFillHoles()
    {
        OpService ops = new Context(OpService.class, OpMatchingService.class).getService(OpService.class);

        Img<BitType> img = ArrayImgs.bits(new long[]{10, 10});
        RandomAccess<BitType> ra = img.randomAccess();

        for (int x = 2; x < 6; x++) {
            for (int y = 2; y < 6; y++) {

                if (x != 4 || y != 4) {
                    Utilitities.setPixel(ra, new long[]{x, y}, true);
                }
            }
        }

        System.out.println("A:" + ops.image().ascii(img));

        Img<BitType> imgFilled = ArrayImgs.bits(new long[]{10, 10});
        ops.morphology().extractHoles(imgFilled, img);

        System.out.println("B:" + ops.image().ascii(img));
        System.out.println("C:" + ops.image().ascii(imgFilled));
    }
}
