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

public abstract class KeyboardFocusManager {
        
  private Component focusOwner = null;

  private static KeyboardFocusManager managerInstance = null;
  private FocusTraversalPolicy defaultPolicy = new DefaultFocusTraversalPolicy();

  public static KeyboardFocusManager getCurrentKeyboardFocusManager() {
    if(managerInstance == null ) {
      managerInstance = new JPFKeybordFocusManager();
    }
    return managerInstance;
  }

  public Component getFocusOwner() {
    return focusOwner;
  }

  protected void setGlobalFocusOwner(Component newFocusOwner) {
    focusOwner = newFocusOwner;
  }

  public void clearGlobalFocusOwner() {
    focusOwner = null;
  }
  
  public synchronized FocusTraversalPolicy getDefaultFocusTraversalPolicy() {
    return defaultPolicy;
  } 
  static class JPFKeybordFocusManager extends KeyboardFocusManager {
    
  }
}
