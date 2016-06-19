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
package javax.swing.plaf.basic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonListener;

public class BasicButtonUI extends ButtonUI {
  
  static final BasicButtonUI singleton = new BasicButtonUI();
  
  protected void clearTextShiftOffset() {
  }
  
  protected BasicButtonListener createButtonListener (AbstractButton b) {
    return new BasicButtonListener(b);
  }
  
  public static ComponentUI createUI (JComponent c) {
    return singleton;
  }
             
  public int getDefaultTextIconGap (AbstractButton b) {
    return 42;
  }
             
  public Dimension getMaximumSize (JComponent c) {
    return new Dimension(100, 20);
  } 
  
  public Dimension getMinimumSize (JComponent c) {
    return new Dimension(100, 20);    
  } 
  
  public Dimension getPreferredSize (JComponent c) {
    return new Dimension(100, 20);
  } 

  protected String getPropertyPrefix() {
    return "Button.";
  } 
             
  protected int getTextShiftOffset() {
    return 42;
  } 
             
  protected void installDefaults (AbstractButton b) {} 
             
  protected void installKeyboardActions (AbstractButton b) {} 
             
  protected void installListeners (AbstractButton b) {} 
             
  public void installUI (JComponent c) {} 

  public void paint (Graphics g, JComponent c) {} 

  protected void paintButtonPressed (Graphics g, AbstractButton b) {} 
             
  protected void paintFocus (Graphics g, AbstractButton b, Rectangle viewRect,
                             Rectangle textRect, Rectangle iconRect) {} 
             
  protected void paintIcon (Graphics g, JComponent c, Rectangle iconRect) {}
             
  protected void paintText (Graphics g, AbstractButton b, Rectangle textRect, String text) {} 

  protected void paintText (Graphics g, JComponent c, Rectangle textRect, String text) {} 

  protected void setTextShiftOffset() {} 
             
  protected void uninstallDefaults (AbstractButton b) {}
             
  protected void uninstallKeyboardActions (AbstractButton b) {}
             
  protected void uninstallListeners (AbstractButton b) {} 
             
  public void uninstallUI (JComponent c) {} 
}
