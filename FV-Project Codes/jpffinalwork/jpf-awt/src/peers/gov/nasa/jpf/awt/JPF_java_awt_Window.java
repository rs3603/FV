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

package gov.nasa.jpf.awt;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.annotation.MJI;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.NativePeer;
import gov.nasa.jpf.vm.ThreadInfo;
import java.util.logging.Logger;

/**
 * some native methods we need to keep track of toplevels to analyze
 */
public class JPF_java_awt_Window extends NativePeer {

  static Logger log = JPF.getLogger("awt");

  protected ComponentRegistry componentRegistry;

  public JPF_java_awt_Window (Config config){
    // Window is the starting point which has to create a new registry
    componentRegistry = ComponentRegistry.createNewRegistry(ThreadInfo.getCurrentThread().getEnv());    
  }
  
  /**
   * a toplevel window becomes visible - create and store its component map
   * (id -> ref). This of course means our constraint is that Windows don't
   * change composition once they become visible
   */
  @MJI
  public void setVisible0__Ljava_lang_String_2Z__V (MJIEnv env, int objref,
                                                          int titleRef, boolean isVisible) {      
    if (isVisible) {
      String title = env.getStringObject(titleRef);

      if (!componentRegistry.containsKey(title)){
        componentRegistry.updateComponents(env,objref,objref,0,0);
      }
    }
  }
  
  @MJI
  public void setTitle0__Ljava_lang_String_2Ljava_lang_String_2__V (MJIEnv env, int objref,
                                                        int oldTitleRef, int newTitleRef) {
    String oldTitle = env.getStringObject(oldTitleRef);
    String newTitle = env.getStringObject(newTitleRef);

    // <2do> we don't deal with this yet - not sure if we should change the component ids
  }

  @MJI
  public void dispose0__Ljava_lang_String_2__V (MJIEnv env, int objRef, int titleRef) {
    componentRegistry.removeTopLevelComponent(env, objRef);
  }

  
}
