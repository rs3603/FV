//Copyright (C) 2008 United States Government as represented by the
//Administrator of the National Aeronautics and Space Administration
//(NASA).  All Rights Reserved.

//This software is distributed under the NASA Open Source Agreement
//(NOSA), version 1.3.  The NOSA has been approved by the Open Source
//Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
//directory tree for the complete NOSA document.

//THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
//KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
//LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
//SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
//A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
//THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
//DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
package javax.swing.plaf.basic;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.plaf.ListUI;

/**
 * minimalistic plaf UI to preserve the component hierarchy of JLists
 * (the CellRendererPane is added by the UI)
 */
public class BasicListUI extends ListUI {

  JList list = null;
  CellRendererPane rendererPane;

  
  // we need this because the plaf UI silently adds a CellRendererPane
  // to the list
  public void installUI (JComponent c) {
      list = (JList)c;

      rendererPane = new CellRendererPane();
      list.add(rendererPane);
  }
  
  public Rectangle getCellBounds (JList list, int index1, int index2) {
    return new Rectangle(42,42,42,42);
  }

  public Point indexToLocation (JList list, int index) {
    return new Point(42,42);
  }

  public int locationToIndex (JList list, Point location) {
    return 0;
  }

  
}
