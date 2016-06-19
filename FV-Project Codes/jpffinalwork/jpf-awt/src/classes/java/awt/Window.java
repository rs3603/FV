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

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * only composition and WindowEvents modeled
 */
public class Window extends Container {
  
  /*
   * this is where we store toplevel instances so that they are in the rootset
   * and don't get collected (a bit ad hoc, have to check how I did this in
   * the Kaffe AWT)
   */
  static HashSet<Window> toplevels = new HashSet<Window>();
  //stack of modal dialogs
  static List<Dialog> modalDialogs = new ArrayList<Dialog>();
  static int numModalDialogs = 0;
    
  WindowListener windowListener;
  Window owner;
  private boolean wasVisible = false;
  
  public Window (Frame owner) {
    this.owner = owner;
  }
  public Window (Window owner) {
    this.owner = owner;
  }
  public Window (Window owner, GraphicsConfiguration gc) {
    this.owner = owner;
  }
  
  public void dispose () {
    // nothing yet, should create WindowEvents (if not already in a close cycle)
    toplevels.remove(this);
  }
  
  public void addWindowListener (WindowListener listener) {
    if (windowListener == null) {
      windowListener = listener;
    } else {
      windowListener = AWTEventMulticaster.add(windowListener, listener);
    }
  }
  
  public void addWindowFocusListener (WindowFocusListener listener){
    // we don't model focus yet
  }
  
  protected void processWindowEvent(WindowEvent e) {
    switch(e.getID()) {
    case WindowEvent.WINDOW_OPENED:
      requestFocusOnFirstComponent();
      if(windowListener != null )
        windowListener.windowOpened(e);
      break;
    case WindowEvent.WINDOW_CLOSING:
      if(windowListener != null )
        windowListener.windowClosing(e);
      break;
    case WindowEvent.WINDOW_CLOSED:
      if(windowListener != null )
        windowListener.windowClosed(e);
      break;
    case WindowEvent.WINDOW_ICONIFIED:
      if(windowListener != null )
        windowListener.windowIconified(e);
      break;
    case WindowEvent.WINDOW_DEICONIFIED:
      if(windowListener != null )
        windowListener.windowDeiconified(e);
      break;
    case WindowEvent.WINDOW_ACTIVATED:
      if(windowListener != null )
        windowListener.windowActivated(e);
      break;
    case WindowEvent.WINDOW_DEACTIVATED:
      if(windowListener != null )
        windowListener.windowDeactivated(e);
      break;
    default:
      break;
    }
  }
  
  protected void processEvent (AWTEvent e) {
    if (e instanceof WindowEvent) {
      processWindowEvent((WindowEvent)e);
    } else {
      super.processEvent(e);
    }
  }
  
  public void pack () {
    // no layout modeling yet
  }
  
  public void setVisible (boolean isVisible) {
    // this is not really the right place, but for now we want to
    // delay creation of the EventDispatchThread as much as possible
    Toolkit.getDefaultToolkit().startEventDispatching();
    
    toplevels.add(this);

    setVisible0( getTitle(), isVisible);

    if(isVisible && !wasVisible) {
      wasVisible = true;
      this.processEvent(new WindowEvent(this, WindowEvent.WINDOW_OPENED));
    }

    super.setVisible(isVisible);
  }
  
  public void setAlwaysOnTop (boolean alwaysOnTop) {
    // <2do> should add a state
  }
  public boolean isAlwaysOnTop () {
    return false; // <2do> should return this state
  }
  
  String getTitle() {
    return null;
  }
  
  public void setLocationRelativeTo (Component c){
    // we don't model location
  }
  
  public Component getMostRecentFocusOwner() {
    // focus not modeled yet
    return null;
  }
  
  public void addNotify() {
    // nothing to do, we don't have peers
  }
  
  
  native void setVisible0 (String title, boolean isVisible);
  native void setTitle0 (String oldTitle, String newTitle);
  native void dispose0 (String title);

  private void requestFocusOnFirstComponent() {
    Component[] components = getComponents();
    if(components.length > 0) {
      Component c = findFirstNonContainer(this);
      if(c != null) {
        c.requestFocus();
      } else{
        requestFocus();
      }
    } else {
      requestFocus();
    }
  }

  private Component findFirstNonContainer(Container container) {
    for(int i=0;i<container.getComponentCount();i++) {
      Component c = container.getComponent(i);
      if(c instanceof Container) {
        return findFirstNonContainer((Container)c);
      } else {
        return c;
      }
    }
    return null;
  }
}