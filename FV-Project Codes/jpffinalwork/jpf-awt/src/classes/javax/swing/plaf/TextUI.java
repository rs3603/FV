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
package javax.swing.plaf;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;

/**
 * just here to simplify Basic*UI implementation (can go away
 * once the Basic*UIs are complete)
 */
public abstract class TextUI extends ComponentUI {
  public TextUI () {}
  
  public abstract void damageRange (JTextComponent t, int p0, int p1);
  public abstract void damageRange (JTextComponent t, int p0, int p1, Position.Bias firstBias, Position.Bias secondBias);
  public abstract EditorKit getEditorKit (JTextComponent t); 
  public abstract int getNextVisualPositionFrom (JTextComponent t, int pos, Position.Bias b, int direction, Position.Bias[] biasRet); 
  public abstract View getRootView (JTextComponent t);
  
  public String getToolTipText (JTextComponent t, Point pt) {
    return null;
  }
  
  public abstract Rectangle modelToView (JTextComponent t, int pos); 
  public abstract Rectangle modelToView (JTextComponent t, int pos, Position.Bias bias); 
  public abstract int viewToModel (JTextComponent t, Point pt); 
  public abstract int viewToModel (JTextComponent t, Point pt, Position.Bias[] biasReturn); 
}
