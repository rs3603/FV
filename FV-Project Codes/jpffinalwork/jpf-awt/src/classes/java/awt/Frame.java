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

import java.util.ArrayList;

/**
 * just here to keep the hierarchy
 */
public class Frame extends Window {
  
  String title;
  boolean undecorated = false;
  //... and many more to follow (resizable, menu etc.)
  
  public Frame() {
    this("");
  }
  
  public Frame (String title) {
    super((Window)null);
    this.title = title;
  }
  
  public String getTitle () {
    return title;
  }
  
  public void setTitle (String title) {
    String oldTitle = this.title;
    this.title = title;
    
    setTitle0(oldTitle, title);
  }
  
  public void setResizable (boolean isResizable) {
    // nothing
  }
    
  public static Frame[] getFrames() {
    ArrayList<Frame> frames = new ArrayList<Frame>();
    
    for (Window w : toplevels) {
      if (w instanceof Frame) {
        frames.add((Frame)w);
      }
    }
    
    return (Frame[])frames.toArray();
  }

  public void setUndecorated(boolean undecorated) {
    synchronized (getTreeLock()){
      this.undecorated = undecorated;
    }
  }
  
  public boolean isUndecorated() {
    return undecorated;
  }
}
