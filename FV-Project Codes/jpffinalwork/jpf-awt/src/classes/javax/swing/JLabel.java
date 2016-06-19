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

// <2do> why do we need to model this? 
public class JLabel extends JComponent implements SwingConstants {

  String text;
  Component labelFor;
  private int alignment;
 
  public JLabel () {
  }
  
  public JLabel (String text) {
    this.text = text;
  }
  
  public JLabel (String text, int horizontalAlignment) {
    this.text = text;
    alignment = horizontalAlignment;
  }  

  public JLabel(Icon image) {
	  // FIXME: should call Ctor JLabel(String, Icon, int)
      this(null, CENTER);
  }
  
  public JLabel(String text,Icon image, int horizontalAlignment) {
      this(text, horizontalAlignment);
  }

  
  public void setText (String text) {
    this.text = text;
  }
  
  public String getText() {
    return text;
  }
  
  public void setLabelFor (Component c) {
    labelFor = c;

    if (c instanceof JComponent){
      // <2do> this should set a clientProperty of the target component
      ((JComponent)c).labeledBy = this;
    }
  }
  
  public Component getLabelFor () {
    return labelFor;
  }

  public void setHorizontalAlignment(int alignment) {
    this.alignment = alignment;
  }

  public int getHorizontalAlignment() {
    return alignment;
  }
}
