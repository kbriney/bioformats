//
// WriteUnsignedByteTest.java
//

/*
LOCI Common package: utilities for I/O, reflection and miscellaneous tasks.
Copyright (C) 2005-@year@ Melissa Linkert and Curtis Rueden.

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

package loci.common.utests;

import static org.testng.AssertJUnit.assertEquals;

import java.io.IOException;

import loci.common.IRandomAccess;
import loci.common.utests.providers.IRandomAccessProvider;
import loci.common.utests.providers.IRandomAccessProviderFactory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Tests for reading unsigned bytes from a loci.common.IRandomAccess.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/common/test/loci/common/utests/WriteUnsignedByteTest.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/common/test/loci/common/utests/WriteUnsignedByteTest.java;hb=HEAD">Gitweb</a></dd></dl>
 *
 * @see loci.common.IRandomAccess
 */
@Test(groups="writeTests")
public class WriteUnsignedByteTest {

  private static final byte[] PAGE = new byte[] {
    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
  };

  private static final String MODE = "rw";

  private static final int BUFFER_SIZE = 1024;

  private IRandomAccess fileHandle;

  private boolean checkGrowth;

  @Parameters({"provider", "checkGrowth"})
  @BeforeMethod
  public void setUp(String provider, @Optional("false") String checkGrowth)
    throws IOException {
    this.checkGrowth = Boolean.parseBoolean(checkGrowth);
    IRandomAccessProviderFactory factory = new IRandomAccessProviderFactory();
    IRandomAccessProvider instance = factory.getInstance(provider);
    fileHandle = instance.createMock(PAGE, MODE, BUFFER_SIZE);
  }

  @Test(groups="initialLengthTest")
  public void testLength() throws IOException {
    assertEquals(16, fileHandle.length());
  }

  @Test
  public void testSequential() throws IOException {
    fileHandle.writeByte(1);
    if (checkGrowth) {
      assertEquals(1, fileHandle.length());
    }
    fileHandle.writeByte(2);
    if (checkGrowth) {
      assertEquals(2, fileHandle.length());
    }
    fileHandle.writeByte(3);
    if (checkGrowth) {
      assertEquals(3, fileHandle.length());
    }
    fileHandle.writeByte(4);
    if (checkGrowth) {
      assertEquals(4, fileHandle.length());
    }
    fileHandle.writeByte(5);
    if (checkGrowth) {
      assertEquals(5, fileHandle.length());
    }
    fileHandle.writeByte(6);
    if (checkGrowth) {
      assertEquals(6, fileHandle.length());
    }
    fileHandle.writeByte(7);
    if (checkGrowth) {
      assertEquals(7, fileHandle.length());
    }
    fileHandle.writeByte(8);
    if (checkGrowth) {
      assertEquals(8, fileHandle.length());
    }
    fileHandle.writeByte(9);
    if (checkGrowth) {
      assertEquals(9, fileHandle.length());
    }
    fileHandle.writeByte(10);
    if (checkGrowth) {
      assertEquals(10, fileHandle.length());
    }
    fileHandle.writeByte(11);
    if (checkGrowth) {
      assertEquals(11, fileHandle.length());
    }
    fileHandle.writeByte(12);
    if (checkGrowth) {
      assertEquals(12, fileHandle.length());
    }
    fileHandle.writeByte(13);
    if (checkGrowth) {
      assertEquals(13, fileHandle.length());
    }
    fileHandle.writeByte(14);
    if (checkGrowth) {
      assertEquals(14, fileHandle.length());
    }
    fileHandle.writeByte((byte) 0xFF);
    if (checkGrowth) {
      assertEquals(15, fileHandle.length());
    }
    fileHandle.writeByte((byte) 0xFE);
    if (checkGrowth) {
      assertEquals(16, fileHandle.length());
    }
    fileHandle.seek(0);
    assertEquals(1, fileHandle.readUnsignedByte());
    assertEquals(2, fileHandle.readUnsignedByte());
    assertEquals(3, fileHandle.readUnsignedByte());
    assertEquals(4, fileHandle.readUnsignedByte());
    assertEquals(5, fileHandle.readUnsignedByte());
    assertEquals(6, fileHandle.readUnsignedByte());
    assertEquals(7, fileHandle.readUnsignedByte());
    assertEquals(8, fileHandle.readUnsignedByte());
    assertEquals(9, fileHandle.readUnsignedByte());
    assertEquals(10, fileHandle.readUnsignedByte());
    assertEquals(11, fileHandle.readUnsignedByte());
    assertEquals(12, fileHandle.readUnsignedByte());
    assertEquals(13, fileHandle.readUnsignedByte());
    assertEquals(14, fileHandle.readUnsignedByte());
    assertEquals(255, fileHandle.readUnsignedByte());
    assertEquals(254, fileHandle.readUnsignedByte());
  }

  @Test
  public void testSeekForward() throws IOException {
    fileHandle.seek(7);
    fileHandle.writeByte(8);
    if (checkGrowth) {
      assertEquals(8, fileHandle.length());
    }
    fileHandle.writeByte(9);
    if (checkGrowth) {
      assertEquals(9, fileHandle.length());
    }
    fileHandle.seek(7);
    fileHandle.writeByte((byte) 0xFF);
    fileHandle.writeByte((byte) 0xFE);
    fileHandle.seek(7);
    assertEquals(255, fileHandle.readUnsignedByte());
    assertEquals(254, fileHandle.readUnsignedByte());
  }

  @Test
  public void testReset() throws IOException {
    fileHandle.writeByte(1);
    if (checkGrowth) {
      assertEquals(1, fileHandle.length());
    }
    fileHandle.writeByte(2);
    if (checkGrowth) {
      assertEquals(2, fileHandle.length());
    }
    fileHandle.seek(0);
    fileHandle.writeByte(1);
    fileHandle.writeByte(2);
    fileHandle.seek(0);
    assertEquals(1, fileHandle.readUnsignedByte());
    assertEquals(2, fileHandle.readUnsignedByte());
  }

}
