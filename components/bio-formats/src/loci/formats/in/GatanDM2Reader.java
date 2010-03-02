//
// GatanDM2Reader.java
//

/*
OME Bio-Formats package for reading and converting biological file formats.
Copyright (C) 2005-@year@ UW-Madison LOCI and Glencoe Software, Inc.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package loci.formats.in;

import java.io.IOException;

import loci.common.DataTools;
import loci.common.DateTools;
import loci.common.Location;
import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.FilterMetadata;
import loci.formats.meta.MetadataStore;

/**
 * GatanDM2Reader is the file format reader for Gatan .dm2 files.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="https://skyking.microscopy.wisc.edu/trac/java/browser/trunk/components/bio-formats/src/loci/formats/in/GatanDM2Reader.java">Trac</a>,
 * <a href="https://skyking.microscopy.wisc.edu/svn/java/trunk/components/bio-formats/src/loci/formats/in/GatanDM2Reader.java">SVN</a></dd></dl>
 */
public class GatanDM2Reader extends FormatReader {

  // -- Constants --

  private static final int HEADER_SIZE = 24;
  private static final int DM2_MAGIC_BYTES = 0x3d0000;

  // -- Fields --

  // -- Constructor --

  /** Constructs a new Gatan .dm2 reader. */
  public GatanDM2Reader() {
    super("Gatan DM2", "dm2");
    domains = new String[] {FormatTools.SPM_DOMAIN};
    suffixNecessary = false;
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 4;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    return stream.readInt() == DM2_MAGIC_BYTES;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    in.seek(HEADER_SIZE);
    readPlane(in, x, y, w, h, buf);
    return buf;
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  protected void initFile(String id) throws FormatException, IOException {
    debug("GatanDM2Reader.initFile(" + id + ")");
    id = new Location(id).getAbsolutePath();
    super.initFile(id);
    in = new RandomAccessInputStream(id);

    int magicBytes = in.readInt();
    if (magicBytes != DM2_MAGIC_BYTES) {
      throw new FormatException("Invalid DM2 file");
    }

    in.skipBytes(8);

    long footerOffset = in.readInt() + 16;

    core[0].sizeX = in.readShort();
    core[0].sizeY = in.readShort();
    int bpp = (int) ((footerOffset - HEADER_SIZE) / (getSizeX() * getSizeY()));

    switch (bpp) {
      case 1:
        core[0].pixelType = FormatTools.UINT8;
        break;
      case 2:
        core[0].pixelType = FormatTools.UINT16;
        break;
      case 4:
        core[0].pixelType = FormatTools.UINT32;
        break;
      default:
        throw new FormatException("Unsupported bytes per pixel: " + bpp);
    }

    core[0].sizeC = 1;
    core[0].sizeT = 1;
    core[0].sizeZ = 1;
    core[0].imageCount = 1;
    core[0].dimensionOrder = "XYZCT";
    core[0].littleEndian = false;

    in.skipBytes(FormatTools.getPlaneSize(this) + 39);

    MetadataStore store =
      new FilterMetadata(getMetadataStore(), isMetadataFiltered());
    MetadataTools.populatePixels(store, this);

    String instrumentID = MetadataTools.createLSID("Instrument", 0);
    store.setInstrumentID(instrumentID, 0);
    store.setImageInstrumentRef(instrumentID, 0);

    String date = null, time = null;

    while (in.getFilePointer() < in.length()) {
      int strlen = in.readShort();
      if (strlen == 0) {
        in.skipBytes(35);
        strlen = in.readShort();
      }
      if (strlen + in.getFilePointer() >= in.length()) break;
      String label = in.readString(strlen);
      StringBuffer value = new StringBuffer();

      int block = in.readInt();
      if (block == 5) {
        in.skipBytes(33);
        while (in.readShort() == 0) {
          in.skipBytes(33);
        }
        in.seek(in.getFilePointer() - 2);
        continue;
      }
      int len = in.readInt();
      if (len + in.getFilePointer() >= in.length()) break;
      String type = in.readString(len);
      in.skipBytes(4);
      int count = in.readInt();

      if (type.equals("TEXT")) {
        value.append(in.readString(count));
      }
      else if (type.equals("long")) {
        count /= 8;
        for (int i=0; i<count; i++) {
          value.append(in.readLong());
          if (i < count - 1) value.append(", ");
        }
        in.skipBytes(4);
      }
      else if (type.equals("bool")) {
        for (int i=0; i<count; i++) {
          value.append(in.read() == 1);
          if (i < count - 1) value.append(", ");
        }
      }
      else if (type.equals("shor")) {
        count /= 2;
        for (int i=0; i<count; i++) {
          value.append(in.readShort());
          if (i < count - 1) value.append(", ");
        }
      }
      else if (type.equals("sing")) {
        in.skipBytes(count);
      }
      else {
        in.skipBytes(count);
      }

      in.skipBytes(16);

      addGlobalMeta(label, value.toString());

      if (label.equals("Acquisition Date")) {
        date = value.toString();
      }
      else if (label.equals("Acquisition Time")) {
        time = value.toString();
      }
      else if (label.equals("Binning")) {
        store.setDetectorSettingsBinning(value + "x" + value, 0, 0);
        String detectorID = MetadataTools.createLSID("Detector", 0);
        store.setDetectorID(detectorID, 0, 0);
        store.setDetectorSettingsDetector(detectorID, 0, 0);
      }
      else if (label.equals("Name")) {
        store.setImageName(value.toString(), 0);
      }
      else if (label.equals("Operator")) {
        String[] name = value.toString().split(" ");
        store.setExperimenterFirstName(name[0], 0);
        if (name.length > 1) {
          store.setExperimenterLastName(name[1], 0);
        }
        String expID = MetadataTools.createLSID("Experimenter", 0);
        store.setExperimenterID(expID, 0);
        store.setImageExperimenterRef(expID, 0);
      }
    }

    if (date != null && time != null) {
      String[] format = new String[] {
        "M/d/yy h:mm:ss a", "d/M/yy h:mm:ss a",
        "M/d/yy H:mm:ss", "d/M/yy H:mm:ss"};
      date += " " + time;
      store.setImageCreationDate(DateTools.formatDate(date, format), 0);
    }
  }

}