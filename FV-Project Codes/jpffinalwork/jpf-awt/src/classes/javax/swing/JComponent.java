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

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Locale;

import javax.accessibility.AccessibleContext;
import javax.swing.border.Border;
import javax.swing.event.AncestorListener;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.ComponentUI;

/**
 * not much we model at this level (yet)
 */
public class JComponent extends Container {

  protected ComponentUI ui;
  protected Border border;
  protected AccessibleContext accessibleContext;
  protected EventListenerList listenerList = new EventListenerList();

  private Object INPUT_VERIFIER_KEY = new Object();
  
  ArrayTable clientProperties; // initialized on demand

  // <2do> this should be a clientProperty, but we have to access it from
  // native, which would be a pain
  JComponent labeledBy;

  public static Locale getDefaultLocale() {
    // not yet
    return Locale.getDefault();
  }
  
  protected void setUI (ComponentUI newUI) {
    if (ui != null) {
      ui.uninstallUI(this);
    }
    ui = newUI;
    if(ui != null) {
      ui.installUI(this);
    }
  }
  
  protected void paintComponent(Graphics g) {
	// We don't want to create Graphics objects
  }
  
  public boolean isOpaque() {
    return false;
  }
  
  public final Object getClientProperty (Object key) {
    if (clientProperties != null) {
      return clientProperties.get(key);
    } else {
      return null;
    }
  }
  
  public final void putClientProperty (Object key, Object value) {
    Object oldValue = null;

    if (clientProperties == null) {
      clientProperties = new ArrayTable();
    }
    
    synchronized(clientProperties) {
      oldValue = clientProperties.get(key);
      if (value != null) {
        clientProperties.put(key, value);
      } else if (oldValue != null) {
        clientProperties.remove(key);
      } else {
        // if both old and new value are null, we ignore
        return;
      }
    }
    
    clientPropertyChanged( key, oldValue, value);
    firePropertyChange( key.toString(), oldValue, value);
  }
  
  void clientPropertyChanged (Object key, Object oldValue, Object newValue) {
    // for package internal overriding
  }

  public void scrollRectToVisible(Rectangle aRect) {}
  
  public void setOpaque (boolean isOpaque) {} 
  
  public void setBorder (Border border) {
    this.border = border;
  }

  public Border getBorder () {
    return border;
  }
  
  public void setAlignmentY(float f) {}
  public void setAlignmentX(float f) {}
  
  public void setComponentPopupMenu (JPopupMenu m) {}
  
  public void setToolTipText (String text) {}
  
  public void revalidate () {
    // no layout yet
  }

  public void addAncestorListener(AncestorListener listener) {
  }

  void setUIProperty(String propertyName, Object value) {
    
  }

  public void setInputVerifier(InputVerifier inputVerifier) {
    putClientProperty(INPUT_VERIFIER_KEY, inputVerifier);
  }

  public InputVerifier getInputVerifier() {
      return (InputVerifier)getClientProperty(INPUT_VERIFIER_KEY);
  }
}
