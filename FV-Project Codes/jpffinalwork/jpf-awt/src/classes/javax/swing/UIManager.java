//Copyright (C) 2006 United States Government as represented by the
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
package javax.swing;

import java.util.Locale;
import java.awt.Component;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicListUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.plaf.basic.BasicViewportUI;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * creates UI objects - very incomplete
 * (only List required so far)
 */
public class UIManager {

  /**
   * that's very simplistic, but we just return dummy objects anyways
   */
  public static ComponentUI getUI (JComponent c) {
    if (c instanceof AbstractButton) {
      return new BasicButtonUI();
    }
    if (c instanceof JTextField) {
      return new BasicTextFieldUI();
    }
    if (c instanceof JList) {
      return new BasicListUI();
    }
    if (c instanceof JViewport) {
      return new BasicViewportUI();
    }
    if (c instanceof JScrollPane) {
      return new BasicScrollPaneUI();
    }
    if (c instanceof JTabbedPane){
      return new BasicTabbedPaneUI();
    }
    if (c instanceof JComboBox){
      return new BasicComboBoxUI();
    }
    
    return null;
  }
  
  private static native String getString0(String key, Locale loc);
  
  public static String getString (Object key, Locale loc) {
    return getString0(key.toString(), loc);
  }
  
  // this is called from within non-modeled javax.swing (e.g. JOptionPane), so we need it
  static String getString (Object key, Component c) {
    Locale loc = null;
    if (c != null){
      loc = c.getLocale();
    } else {
      loc = Locale.getDefault();
    }
    return getString( key, loc);
  }

  public static UIDefaults getLookAndFeelDefaults() {
    MetalLookAndFeel laf = new MetalLookAndFeel();
    return laf.getDefaults();
  }


}
