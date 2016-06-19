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

import java.awt.IllegalComponentStateException;
import java.util.Locale;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleStateSet;

/**
 * overly abstracted
 */
public class JButton extends AbstractButton {

  private AccessibleContext accessibleContext;

  public JButton() {
    this(null,null);
  }
  
  public JButton(String text, Icon icon) {
    this.text = text;
    setModel(new DefaultButtonModel());
  }

  public JButton(Icon icon) {
      this(null, icon);
  }
  
  public JButton (String text) {
    this(text,null);
  }

  public AccessibleContext getAccessibleContext() {
    if (accessibleContext == null) {
      accessibleContext = new AccessibleJButton();
    }
    return accessibleContext;
  }

  protected class AccessibleJButton extends AccessibleContext {
    public AccessibleRole getAccessibleRole() {
      return AccessibleRole.PUSH_BUTTON;
    }

    @Override
    public AccessibleStateSet getAccessibleStateSet() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getAccessibleIndexInParent() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getAccessibleChildrenCount() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Accessible getAccessibleChild(int i) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Locale getLocale() throws IllegalComponentStateException {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  } 
}
