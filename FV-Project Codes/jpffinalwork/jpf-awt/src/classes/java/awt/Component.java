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

import gov.nasa.jpf.awt.JPFGraphics;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.im.InputContext;
import java.awt.peer.ComponentPeer;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import sun.awt.AppContext;

public abstract class Component {

  public static final float TOP_ALIGNMENT = 0.0f;
  public static final float CENTER_ALIGNMENT = 0.5f;
  public static final float BOTTOM_ALIGNMENT = 1.0f;
  public static final float LEFT_ALIGNMENT = 0.0f;
  public static final float RIGHT_ALIGNMENT = 1.0f;

  transient AppContext appContext;

  static class AWTTreeLock {
    // just an empty type
  }

  // this one is interesting because it is a global lock that can be a great
  // source of deadlocks
  static final Object LOCK = new AWTTreeLock();

  ComponentPeer peer; // we just model this in terms of Component state

  String name;
  Container parent;
  int x, y, width, height;
  Dimension preferredSize, minSize, maxSize;
  boolean enabled;
  List<FocusListener> focusListeners = new ArrayList<FocusListener>();
  List<MouseListener> mouseListeners = new ArrayList<MouseListener>();
  private boolean isVisible = true;

  // at some point, we want to add ComponentEvents, and maybe Key and Mouse

  protected Component() {
    // nothing yet
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public java.awt.Container getParent() {
    return parent;
  }

  public Object getTreeLock() {
    return LOCK;
  }

  public java.awt.Toolkit getToolkit() {
    return Toolkit.getDefaultToolkit();
  }

  public void show () {
    isVisible = true;
  }

  public void show (boolean showIt){
    if (showIt){
      show();
    } else {
      hide();
    }
  }

  public void hide () {
    isVisible = false;
  }

  public boolean isShowing() {
    // FIXME: not modeled yet
    return true;
  }

  public boolean isVisible() {
    return isVisible;
  }

  public void dispatchEvent(AWTEvent e) {
    processEvent(e);
  }

  protected void processEvent(AWTEvent e) {
    if (e instanceof FocusEvent) {
      processFocusEvent((FocusEvent) e);
    } else if (e instanceof MouseEvent) {
      processMouseEvent((MouseEvent)e);
    }
  }

  protected void processFocusEvent(FocusEvent e) {
    int id = e.getID();
    
    switch (id) {
    case FocusEvent.FOCUS_GAINED:
      KeyboardFocusManager.getCurrentKeyboardFocusManager().setGlobalFocusOwner(this);
      for (int i = 0; i < focusListeners.size(); i++) {
        focusListeners.get(i).focusGained(e);
      }
      break;
    case FocusEvent.FOCUS_LOST:
      KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
      for (int i = 0; i < focusListeners.size(); i++) {
        focusListeners.get(i).focusLost(e);
      }
      break;
    }
  }

  protected void processMouseEvent(MouseEvent e) {
    for (int i = 0; i < mouseListeners.size(); i++) {
      int id = e.getID();
      
      switch(id) {
        case MouseEvent.MOUSE_ENTERED:
          mouseListeners.get(i).mouseEntered(e);
          break;
        case MouseEvent.MOUSE_PRESSED:
          mouseListeners.get(i).mousePressed(e);
          break;
        case MouseEvent.MOUSE_RELEASED:
          mouseListeners.get(i).mouseReleased(e);
          break;
        case MouseEvent.MOUSE_CLICKED:
          mouseListeners.get(i).mouseClicked(e);
          break;
        case MouseEvent.MOUSE_EXITED:
          mouseListeners.get(i).mouseExited(e);
          break;
      }
    }
  }

  protected void enableEvents(long eventMask) {
    // <2do> no enable/disable yet
  }

  public void enableInputMethods(boolean enable) {
    // <2do> no enable/disable yet
  }

  public void setVisible(boolean isVisible) {
    show(isVisible);
    if(!isVisible && isFocusOwner()) {
      processEvent(new FocusEvent(this, FocusEvent.FOCUS_LOST,false));
    }
  }

  public Point getLocationOnScreen() {
    // FIXME: not modeled yet
	  return new Point(x,y);
  }
  /* 
   * no rendering
   */
  public Graphics getGraphics() {
    return new JPFGraphics();
  }

  /*
   * we don't really model geometry and layout (yet)
   */
  public void validate () {
    // not modeled yet
  }

  public void addMouseListener (MouseListener l) {
    mouseListeners.add(l);
  }

  public void removeMouseListener (MouseListener l) {
    mouseListeners.remove(l);
  }

  public MouseListener[] getMouseListeners() {
    return mouseListeners.toArray(new MouseListener[0]);
  }

  public void addMouseMotionListener (MouseMotionListener l) {}
  public void addKeyListener (KeyListener l) {}

  public void addFocusListener(FocusListener l) {
    focusListeners.add(l);
  }

  public void removeFocusListener(FocusListener l) {
    focusListeners.remove(l);
  }

  public FocusListener[] getFocusListeners() {
    return focusListeners.toArray(new FocusListener[0]);
  }

  public void addPropertyChangeListener(PropertyChangeListener l) {
    // <2do> we should probably support this
  }

  public void addComponentListener (ComponentListener l) {
    // <2do> ComponentListener might actually be modeling relevant
  }

  public void setLocation (int x, int w) {}
  public void setLocation (Point p) {}
  public void setBounds (int x, int y, int w, int h) {}
  public void setBounds (Rectangle r) {}
  public void setSize (int w, int h) {}
  public void setSize (Dimension d) {}

  public int getX () {return x;}
  public int getY () {return y;}
  public int getWidth () { return width; }
  public int getHeight () { return height; }
  public Dimension getSize () { return new Dimension(width, height); }
  public Dimension getSize (Dimension d) { d.width = width; d.height = height; return d; }
  public Point getLocation () { return new Point(x,y); }
  public Point getLocation (Point p) { p.x = x; p.y = y; return p; }

  public void setMaximumSize (Dimension d) {}
  public void setMinimumSize (Dimension d) {}
  public void setPreferredSize (Dimension d) {}

  public void setComponentOrientation (ComponentOrientation o) {}

  // very simplistic modeling, just here to return something 
  public Dimension getPreferredSize () {
    if (preferredSize != null) {
      return preferredSize;
    } else {
      return new Dimension (width,height);
    }
  }
  public Dimension getMinimumSize () {
    if (minSize != null) {
      return minSize;
    } else {
      return getPreferredSize();
    }
  }
  public Dimension getMaximumSize () {
    if (maxSize != null) {
      return maxSize;
    } else {
      return getPreferredSize();
    }
  }

  /*
   * those are here to avoid we do any rendering (which might still occur from some
   * of the used auxiliary classes)
   */
  public void paint (Graphics g) {}
  public void paintAll (Graphics g) {}
  public void repaint() {}
  public void repaint (int x, int y, int w, int h) {}
  public void repaint (long to) {}
  public void repaint (long to, int x, int y, int w, int h) {}
  public void update (Graphics g) {}

  public void setBackground(Color clr) {}
  public Color getBackground() { return Color.WHITE; } // just return something
  public void setForeground(Color clr) {}
  public Color getForeground() { return Color.BLACK; } // just return something

  // those we probably have to model soon
  public void setFont (Font font) {}
  public Font getFont() { return new Font("Times New Roman",1,1); }
  public FontMetrics getFontMetrics (Font f) { return null; }   // <2do> must return something
  public Cursor getCursor() { return null; }   // <2do> must return something
  public void setCursor (Cursor cursor) {}

  public void setLocale (Locale loc) {
    // not yet
  }

  public boolean isDisplayable() {
    return true; // not really, depends on peer state
  }

  public void setEnabled (boolean isEnabled) {
    enabled = isEnabled;
  }
  public boolean isEnabled () {
    return enabled;
  }

  public void setFocusable(boolean focusable) {
    // <2do> not yet modeled
  }

  // this is our own housekeeping method executed via direct call from
  // JPF_java_awt_EventDispathThread.runAction() in case the event specified
  // a focus transfer. We need to execute this code by JPF since FocusListeners
  // are part of the user code that should be checked.
  // A requestFocus() won't do because we want to check the precondition that the
  // component is actually focusable, and the postcondition that the focus
  // transfer actually took place
  // <2do> we don't model temporary focus gained/lost events yet
  private void transferFocus() throws AWTException {
    
    // precondition
    if (!isVisible() || !isFocusable() ||  !isDisplayable()){
      // we are in trouble - this component could not receive input
      throw new AWTException("cannot transfer focus to " + this);
    }
    
    KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    Component currentOwner = kfm.getFocusOwner();
    
    if (this != currentOwner){ // otherwise there is nothing to do
      if (currentOwner != null){
        currentOwner.processFocusEvent(new FocusEvent(this, FocusEvent.FOCUS_LOST, false));
      }
      processFocusEvent(new FocusEvent(this, FocusEvent.FOCUS_GAINED,false));
      
      //System.out.println("@@ focus transfered from " + currentOwner + " to " + this + " => " + kfm.getFocusOwner());
       
      // postcondition
      if (kfm.getFocusOwner() != this){
        throw new AWTException("focus not transfered to " + this);
      } 
    }
  }
  
  // <2do> we don't model temporary focus gained/lost events yet
  public void requestFocus() {
    if (isVisible() && isFocusable() && isDisplayable() && !isFocusOwner()) {
      Component previousFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
      if (previousFocusOwner != null) {
        previousFocusOwner.processFocusEvent(new FocusEvent(this, FocusEvent.FOCUS_LOST, false));
      }
      processFocusEvent(new FocusEvent(this, FocusEvent.FOCUS_GAINED,false));
    }
  }

  public boolean hasFocus() {
    return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this;
  }

  public boolean isFocusable() {
    return true;
  }

  public boolean isFocusOwner() {
    return hasFocus();
  }

  public boolean requestFocusInWindow() {
    return false;
  }

  public void firePropertyChange (String propertyName, Object oldValue, Object newValue) {
    // not yet
  }
  public void firePropertyChange (String propertyName, int oldValue, int newValue) {
    // not yet
  }
  public void firePropertyChange (String propertyName, boolean oldValue, boolean newValue) {
    // not yet
  }

  public ComponentOrientation getComponentOrientation() {
    // very European, but we don't render anyways
    return ComponentOrientation.RIGHT_TO_LEFT;
  }

  public Locale getLocale(){
    // <2do> not supported yet
    return Locale.getDefault();
  }

  public InputContext getInputContext() {
    return new JPFInputContext();
  }

  protected void doMouseClick() {
    processEvent(new MouseEvent(this, MouseEvent.MOUSE_ENTERED, 0, 0, x, y, 0, false,0));
    processEvent(new MouseEvent(this, MouseEvent.MOUSE_PRESSED, 0, 0, x, y, 0, false,MouseEvent.BUTTON1));
    processEvent(new MouseEvent(this, MouseEvent.MOUSE_RELEASED, 0, 0, x, y, 0, false,MouseEvent.BUTTON1));
    processEvent(new MouseEvent(this, MouseEvent.MOUSE_CLICKED, 0, 0, x, y, 0, false,MouseEvent.BUTTON1));
    processEvent(new MouseEvent(this, MouseEvent.MOUSE_EXITED, 0, 0, x, y, 0, false,0));
  }

  class JPFInputContext extends InputContext {
    
  }
}
