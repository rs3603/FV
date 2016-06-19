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

import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;

/**
 * just the bare component hierarchy for now
 */
public class Container extends Component {

  static final Component[] EMPTY_COMPONENT = new Component[0];
  
  /** we keep this as a simple array so that we can easily traverse from
   * a native peer
   */
  Component[] component = EMPTY_COMPONENT;
  
  /** only here so that we can set it (we don't model geometry) */
  LayoutManager layoutManager;
  
  public Component add (Component c) {
    addImpl(c, null, -1);
    return c;
  }
  
  public Component add (String name, Component c) {
    addImpl(c, name, -1);
    return c;
  }
  
  public Component add (Component c, int index) {
    addImpl(c, null, index);
    return c;    
  }

  public void add (Component c, Object constraints) {
    // no layout modeling
    addImpl(c, constraints, -1);
  }
  
  public void add (Component c, Object constraints, int index) {
    // no layout modeling
    addImpl(c,constraints,index);
  }
  
  protected void addImpl (Component c, Object constraints, int index) {

    if (component == EMPTY_COMPONENT) {
      component = new Component[1];
      component[0] = c;      
    } else {
      Component[] a = new Component[component.length+1];
      if ((index >= component.length) || (index < 0)) {
        System.arraycopy(component,0,a,0,component.length);
        a[component.length] = c;
      } else {
        if (index > 0) {
          System.arraycopy(component,0,a,0,index);
        }
        a[index] = c;
        if (index < component.length-1) {
          System.arraycopy(component,index,a,index+1,component.length-index);
        }
      }
      component = a;
    }

    c.parent = this;

    if(layoutManager != null) {
      if(layoutManager instanceof LayoutManager2) {
        ((LayoutManager2)layoutManager).addLayoutComponent(c, constraints);
      } else if (constraints instanceof String) {
        layoutManager.addLayoutComponent((String)constraints, c);
      }
    }
  }
  
  public Component[] getComponents() {
    return component.clone();
  }

  public Component getComponent (int idx) {
    return component[idx]; // it has to throw a ArrayIndexOutOfBounds
  }
  
  public int getComponentCount() {
    return component.length;
  }
  
  public void remove(Component comp) {
	  for(int i=0;i<component.length;i++) {
      if(component[i].equals(comp)) {
        comp.parent = null;
        Component temp[] = new Component[component.length-1];
        if(i == 0) {
          System.arraycopy(component, 1, temp, 0, component.length-1);
        }else if(i == component.length-1) {
          System.arraycopy(component, 0, temp, 0, component.length-1);
        } else {
          System.arraycopy(component, 0, temp, 0, i);
          System.arraycopy(component, i+1, temp, i, component.length-i-1);
        }
        component = temp;
      }
    }
  }
  
  public void removeAll() {
    for(int i=0;i<component.length;i++) {
      component[i].parent = null;
    }

    component = EMPTY_COMPONENT;
  }
  
  /*
   * no rendering
   */
  public void paintComponents (Graphics g) {
    // nothing
  }
  
  public LayoutManager getLayout() {
    return layoutManager;
  }
  
  /*
   * we don't model layout yet
   */
  public void doLayout () {
    // not yet
  }
  
  public void invalidate () {
    // we don't do layout
  }
  
  /**
   * but we do store LayoutManagers, since they might be app specific code that
   * has other functions (e.g. being containers)
   */
  public void setLayout (LayoutManager layout) {
    layoutManager = layout;
  }
  
  public void setFocusTraversalPolicy(FocusTraversalPolicy policy) {
    // we don't model focus yet
  }

  public void setVisible(boolean isVisible) {
    super.setVisible(isVisible);
    if(!isVisible) clearFocusInChild();
  }

  public void addContainerListener(ContainerListener l){
    
  }

  public void removeContainerListener(ContainerListener l) {
    
  }

  private void clearFocusInChild() {
    Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
    if(focusOwner != null) {
      Component focusParent = focusOwner.getParent();
      while(focusParent != null) {
        if(focusParent == this) {
          focusOwner.processEvent(new FocusEvent(focusOwner,FocusEvent.FOCUS_LOST ,false));
        }
        focusParent = focusParent.getParent();
      }
    }
  }
}
