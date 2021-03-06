//
// CommentSurgery.java
//

import loci.common.RandomAccessInputStream;
import loci.formats.tiff.TiffParser;
import loci.formats.tiff.TiffSaver;

/**
 * Performs "surgery" on a TIFF ImageDescription comment, particularly the
 * OME-XML comment found in OME-TIFF files. Note that this code must be
 * tailored to a specific need by editing the commented out code below to
 * make desired alterations to the comment.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/bio-formats/utils/CommentSurgery.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/bio-formats/utils/CommentSurgery.java;hb=HEAD">Gitweb</a></dd></dl>
 */
public class CommentSurgery {
  public static void main(String[] args) throws Exception {
    // the -test flag will print proposed changes to stdout
    // rather than actually changing the comment
    boolean test = args[0].equals("-test");

    for (int i=0; i<args.length; i++) {
      String id = args[i];
      if (!test) System.out.println(id + ": ");
      String xml = new TiffParser(id).getComment();
      if (xml == null) {
        System.out.println("ERROR: No OME-XML comment.");
        return;
      }
      int len = xml.length();
      // do something to the comment; e.g.:
      //xml = xml.replaceAll("LogicalChannel:OWS", "LogicalChannel:OWS347-");

      if (test) System.out.println(xml);
      else {
        System.out.println(len + " -> " + xml.length());
        TiffSaver saver = new TiffSaver(id);
        RandomAccessInputStream in = new RandomAccessInputStream(id);
        saver.overwriteComment(in, xml);
        in.close();
      }
    }
  }
}
