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
import java.awt.Frame;

/**
 * mostly here to keep the hierarchy
 */
public class JFrame extends Frame implements WindowConstants {

  JRootPane rootPane;
  int closeOp;

  public JFrame () {
    createRootPane();
  }
  
  public JFrame (String title) {
    super(title);
    
    // <2do> that's oversimplified, should use setRootPane
    createRootPane();
  }
  
  void createRootPane () {
    // that takes care of creating the content and glass pane
    rootPane = new JRootPane();
    super.addImpl(rootPane,null,-1); // don't get recursive
  }
  
  public JRootPane getRootPane() { 
    return rootPane; 
  }

  public Container getContentPane () {
    return rootPane.getContentPane();
  }

  public void setContentPane(Container contentPane) {
    getRootPane().setContentPane(contentPane);
  }
  
  public Component getGlassPane () {
    return rootPane.getGlassPane();
  }
  
  public void setJMenuBar (JMenuBar menu) {
    getRootPane().setJMenuBar(menu);
  }
  
  public JMenuBar getJMenuBar() {
    return getRootPane().getJMenuBar();
  }
  
  public static void setDefaultLookAndFeelDecorated (boolean isDecorated) {
    // ignore
  }
  
  public void setDefaultCloseOperation (int operation) {
    closeOp = operation;
  }

  
  protected void addImpl (Component child, Object constraints, int index) {
    // <2do> no rootPaneCheckingEnabled yet
    
    // for those who haven't heard of the contentPane
    getContentPane().add(child, constraints, index);
  }

}
