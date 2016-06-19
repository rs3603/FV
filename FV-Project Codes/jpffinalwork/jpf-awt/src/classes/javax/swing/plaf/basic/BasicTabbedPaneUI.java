//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package javax.swing.plaf.basic;

import java.awt.Rectangle;

import javax.swing.JTabbedPane;
import javax.swing.plaf.TabbedPaneUI;

/**
 * nothing we model in here
 */
public class BasicTabbedPaneUI extends TabbedPaneUI {

  @Override
  public Rectangle getTabBounds (JTabbedPane pane, int index) {
    return new Rectangle( 42, 42, 42, 42);
  }

  @Override
  public int getTabRunCount (JTabbedPane pane) {
    return 42;
  }

  @Override
  public int tabForCoordinate (JTabbedPane pane, int x, int y) {
    return 0;
  }

}
