/*
 * ome.xml.r201004.ListAnnotation
 *
 *-----------------------------------------------------------------------------
 *
 *  Copyright (C) @year@ Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee,
 *      University of Wisconsin-Madison
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *-----------------------------------------------------------------------------
 */

/*-----------------------------------------------------------------------------
 *
 * THIS IS AUTOMATICALLY GENERATED CODE.  DO NOT MODIFY.
 * Created by callan via xsd-fu on 2010-04-22 17:27:24+0100
 *
 *-----------------------------------------------------------------------------
 */

package ome.xml.r201004;

import java.util.ArrayList;
import java.util.List;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ome.xml.r201004.enums.*;

public class ListAnnotation extends Annotation
{
	// -- Constants --

	public static final String NAMESPACE = "http://www.openmicroscopy.org/Schemas/SA/2010-04";

	// -- Instance variables --

	// Reference AnnotationRef
	private List<Annotation> annotationList = new ArrayList<Annotation>();

	// -- Constructors --

	/** Default constructor. */
	public ListAnnotation()
	{
		super();
	}

	/** 
	 * Constructs ListAnnotation recursively from an XML DOM tree.
	 * @param element Root of the XML DOM tree to construct a model object
	 * graph from.
	 * @throws EnumerationException If there is an error instantiating an
	 * enumeration during model object creation.
	 */
	public ListAnnotation(Element element) throws EnumerationException
	{
		update(element);
	}

	/** 
	 * Updates ListAnnotation recursively from an XML DOM tree. <b>NOTE:</b> No
	 * properties are removed, only added or updated.
	 * @param element Root of the XML DOM tree to construct a model object
	 * graph from.
	 * @throws EnumerationException If there is an error instantiating an
	 * enumeration during model object creation.
	 */
	public void update(Element element) throws EnumerationException
	{	
		super.update(element);
		String tagName = element.getTagName();
		if (!"ListAnnotation".equals(tagName))
		{
			System.err.println(String.format(
					"WARNING: Expecting node name of ListAnnotation got %s",
					tagName));
			// TODO: Should be its own Exception
			//throw new RuntimeException(String.format(
			//		"Expecting node name of ListAnnotation got %s",
			//		tagName));
		}
		// *** IGNORING *** Skipped back reference AnnotationRef
	}

	// -- ListAnnotation API methods --


	// Reference which occurs more than once
	public int sizeOfLinkedAnnotationList()
	{
		return annotationList.size();
	}

	public List<Annotation> copyLinkedAnnotationList()
	{
		return new ArrayList<Annotation>(annotationList);
	}

	public Annotation getLinkedAnnotation(int index)
	{
		return annotationList.get(index);
	}

	public Annotation setLinkedAnnotation(int index, Annotation o)
	{
		return annotationList.set(index, o);
	}

	public boolean linkAnnotation(Annotation o)
	{
		o.linkListAnnotation(this);
		return annotationList.add(o);
	}

	public boolean unlinkAnnotation(Annotation o)
	{
		o.unlinkListAnnotation(this);
		return annotationList.remove(o);
	}

	public Element asXMLElement(Document document)
	{
		return asXMLElement(document, null);
	}

	protected Element asXMLElement(Document document, Element ListAnnotation_element)
	{
		// Creating XML block for ListAnnotation
		if (ListAnnotation_element == null)
		{
			ListAnnotation_element =
					document.createElementNS(NAMESPACE, "ListAnnotation");
		}

		if (annotationList != null)
		{
			// Reference property AnnotationRef which occurs more than once
			for (Annotation annotationList_value : annotationList)
			{
				Element annotationList_value_element = 
						document.createElementNS(NAMESPACE, "AnnotationRef");
				annotationList_value_element.setAttribute(
						"ID", annotationList_value.getID());
				ListAnnotation_element.appendChild(annotationList_value_element);
			}
		}
		return super.asXMLElement(document, ListAnnotation_element);
	}
}