//
// Copyright (C) 2008 United States Government as represented by the
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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.plaf.TextUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.Position;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.Position.Bias;

public abstract class BasicTextUI extends TextUI implements ViewFactory {

  JTextComponent component;
  RootView rootView = new RootView();
  EditorKit editorKit = new DefaultEditorKit();

  
  public static class BasicCaret extends DefaultCaret implements UIResource {
    // nothing
  }

  public static class BasicHighlighter extends DefaultHighlighter implements UIResource {
    // nothing
  }

  class RootView extends View {
    RootView() {
      super(null);
    }

    public float getPreferredSpan (int arg0) {
      return 0;
    }

    public Shape modelToView (int arg0, Shape arg1, Bias arg2) throws BadLocationException {
      return null;
    }

    public void paint (Graphics arg0, Shape arg1) {
    }

    public int viewToModel (float arg0, float arg1, Shape arg2, Bias[] arg3) {
      return 0;
    }
    
  }
  
  public View create (Element elem) {
    return null;
  }
  
  View  create (Element elem, int p0, int p1) {
    return null;
  }
  
  protected Caret createCaret() {
    return new BasicCaret();
  }
  
  protected Highlighter createHighlighter() {
    return new BasicHighlighter();
  }
  
  protected Keymap createKeymap() {
    String kname = getKeymapName();
    Keymap map = JTextComponent.getKeymap(kname);
    return map;
  }
  
  public void damageRange (JTextComponent tc, int p0, int p1){
    // nothing
  }
  public void damageRange (JTextComponent t, int p0, int p1, Position.Bias p0Bias, Position.Bias p1Bias){
    // nothing
  }
  
  protected  JTextComponent   getComponent() {
    return component;
  }
  
  public EditorKit getEditorKit (JTextComponent tc) {
    return editorKit;
  }
  
  protected String getKeymapName() {
    String name = getClass().getName();
    // chop off the package
    int i = name.lastIndexOf('.');
    if (i >= 0) {
      return name.substring(i+1);
    } else {
      return name; // unlikely
    }
  }
  
  public Dimension getMaximumSize (JComponent c) {
    return new Dimension(200, 20);
  }
  
  public Dimension getMinimumSize (JComponent c) {
    return new Dimension(30, 20);
  }

  public Dimension getPreferredSize (JComponent c) {
    return new Dimension(100, 20);
  }
  
  public int getNextVisualPositionFrom (JTextComponent t, int pos, Position.Bias b, int direction, Position.Bias[] biasRet){
    return 42;
  }
  
  protected abstract String getPropertyPrefix();
  
  public View getRootView (JTextComponent tc) {
    return rootView;
  }
  
  public String  getToolTipText (JTextComponent t, Point pt) {
    return null;
  }
  
  protected  Rectangle    getVisibleEditorRect() {
    return new Rectangle(0,0,42,42);
  }
  
  protected  void installDefaults() {
    
  }
  
  protected  void installKeyboardActions() {
    
  }
   
  protected void installListeners() {
    
  }
  
  public void installUI (JComponent c) {
    component = (JTextComponent)c;
    component.setCaret(createCaret());
  }
  
  protected void modelChanged() {
    
  }
  
  public Rectangle   modelToView (JTextComponent tc, int pos) {
    return new Rectangle(0,0,42,42);
  }
  
  public Rectangle modelToView (JTextComponent tc, int pos, Position.Bias bias) {
    return new Rectangle(0,0,42,42);
  }

  public void paint (Graphics g, JComponent c) {
    // nothing
  }
  
  protected void paintBackground (Graphics g) {
    // nothing
  }
  
  protected  void paintSafely (Graphics g) {
    // nothing
  }
  
  protected  void propertyChange (PropertyChangeEvent evt) {
    
  }

  protected  void setView (View v) {
    
  }
  protected  void uninstallDefaults() {
    
  }
  protected  void uninstallKeyboardActions() {
    
  }
   
  protected  void uninstallListeners() {
    
  }
  
  public void uninstallUI (JComponent c) {
    // nothing
  }
  
  public void update (Graphics g, JComponent c){
    // nothing
  }
  
  public int viewToModel (JTextComponent tc, Point pt) {
    return 42;
  }
  
  public int viewToModel(JTextComponent tc, Point pt, Position.Bias[] biasReturn) {
    return 42;
  }
}
