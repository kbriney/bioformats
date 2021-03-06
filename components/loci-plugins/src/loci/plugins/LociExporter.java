//
// LociExporter.java
//

/*
LOCI Plugins for ImageJ: a collection of ImageJ plugins including the
Bio-Formats Importer, Bio-Formats Exporter, Bio-Formats Macro Extensions,
Data Browser and Stack Slicer. Copyright (C) 2005-@year@ Melissa Linkert,
Curtis Rueden and Christopher Peterson.

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

package loci.plugins;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.util.HashSet;

import loci.common.DebugTools;
import loci.plugins.out.Exporter;
import loci.plugins.util.LibraryChecker;

/**
 * ImageJ plugin for writing files using the LOCI Bio-Formats package.
 * Wraps core logic in {@link loci.plugins.out.Exporter}, to avoid
 * direct references to classes in the external Bio-Formats library.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/loci-plugins/src/loci/plugins/LociExporter.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/loci-plugins/src/loci/plugins/LociExporter.java;hb=HEAD">Gitweb</a></dd></dl>
 *
 * @author Melissa Linkert melissa at glencoesoftware.com
 */
public class LociExporter implements PlugInFilter {

  // -- Fields --

  /** Argument passed to setup method. */
  public String arg;

  private Exporter exporter;

  // -- PlugInFilter API methods --

  /** Sets up the writer. */
  public int setup(String arg, ImagePlus imp) {
    this.arg = arg;
    exporter = new Exporter(this, imp);
    return DOES_ALL + NO_CHANGES;
  }

  /** Executes the plugin. */
  public void run(ImageProcessor ip) {
    DebugTools.enableLogging("INFO");
    if (!LibraryChecker.checkJava() || !LibraryChecker.checkImageJ()) return;
    HashSet<String> missing = new HashSet<String>();
    LibraryChecker.checkLibrary(LibraryChecker.Library.BIO_FORMATS, missing);
    LibraryChecker.checkLibrary(LibraryChecker.Library.OME_JAVA_XML, missing);
    if (!LibraryChecker.checkMissing(missing)) return;
    if (exporter != null) exporter.run();
  }

}
