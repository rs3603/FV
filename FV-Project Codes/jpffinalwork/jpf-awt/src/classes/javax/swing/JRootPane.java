//
// Copyright (C) 2006 United States Government as represented by the
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

package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

/**
 * mostly here to keep the hierarchy and composition (the real thing does a lot
 * with the UI)
 */
public class JRootPane extends JComponent {

  Component glassPane;
  Container contentPane;
  
  JLayeredPane layeredPane;
  JMenuBar menuBar;
  
  public JRootPane() {
    // ould be subclassed, so we have to use the setters
    setGlassPane(createGlassPane());
    setContentPane(createContentPane());
    setLayeredPane(createLayeredPane());
    setLayout(createRootLayout());
    
    add(glassPane);
    add(layeredPane);
    layeredPane.add(contentPane);
  }
  
  public void setGlassPane (Component glassPane) {
    this.glassPane = glassPane;
  }
  
  public void setContentPane (Container contentPane) {
    this.contentPane = contentPane;
    add(contentPane);
  }
  
  public void setLayeredPane (JLayeredPane layeredPane) {
    this.layeredPane = layeredPane;
  }
  
  public void setJMenuBar (JMenuBar menuBar) {
    this.menuBar = menuBar;
  }
  public JMenuBar getJMenuBar() {
    return menuBar;
  }
  @Deprecated
  public void setMenuBar (JMenuBar menuBar) {
    this.menuBar = menuBar;
  }
  @Deprecated
  public JMenuBar getMenuBar() {
    return menuBar;
  }
  
  
  
  public Container getContentPane() {
    return contentPane;
  }
  
  public Component getGlassPane () {
    return glassPane;
  }
  
  Component createGlassPane() {
    return new JPanel();
  }
  
  Container createContentPane() {
    return new JPanel();    
  }
  
  JLayeredPane createLayeredPane() {
    return new JLayeredPane();
  }
  
  LayoutManager createRootLayout() {
    //return new RootLayout();
    
    // that's of course wrong, but we don't care for now (the RootLayout
    // is an inner JRootPane class with lots of logic)
    return new FlowLayout();
  }
}
