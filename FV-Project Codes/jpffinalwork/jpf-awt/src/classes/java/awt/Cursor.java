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
package java.awt;

public class Cursor {
  
  public static final int DEFAULT_CURSOR = 0;
  public static final int CROSSHAIR_CURSOR = 1;
  public static final int TEXT_CURSOR = 2;
  public static final int WAIT_CURSOR = 3;
  public static final int SW_RESIZE_CURSOR = 4;
  public static final int SE_RESIZE_CURSOR = 5;
  public static final int NW_RESIZE_CURSOR = 6;
  public static final int NE_RESIZE_CURSOR = 7;
  public static final int N_RESIZE_CURSOR = 8;
  public static final int S_RESIZE_CURSOR = 9;
  public static final int W_RESIZE_CURSOR = 10;
  public static final int E_RESIZE_CURSOR = 11;
  public static final int HAND_CURSOR = 12;
  public static final int MOVE_CURSOR = 13;
  
  public static final int CUSTOM_CURSOR = -1;
  
  protected static java.awt.Cursor[] predefined;

  static {
    predefined = new Cursor[14];
    for (int i=0; i<14; i++) {
      predefined[i] = new Cursor(i);
    }
  }
  
  int type;
  protected java.lang.String name;
  
  public static Cursor getPredefinedCursor(int type) {
    if (type < 0 || type > 13) {
      throw new IllegalArgumentException("illegal cursor type");
    }
    return predefined[type];
  }
  
  public static Cursor getSystemCustomCursor (String name)
    throws java.awt.AWTException, java.awt.HeadlessException {
    return null;
  }
  
  public static Cursor getDefaultCursor() {
    return predefined[0];
  }
  
  public Cursor(int type) {
    this.type = type;
  }
  
  protected Cursor(String name) {
    this.name = name;
  }
  
  public int getType() {
    return type;
  }
  
  public String getName() {
    return name;
  }
  
  public String toString() {
    return (getClass().getName() + '[' + name + ']');
  }

}
