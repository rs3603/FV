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
import gov.nasa.jpf.util.JPFLogger;
import gov.nasa.jpf.util.event.EventChoiceGenerator;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.JPF_gov_nasa_jpf_EventProducer;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.ThreadInfo;

/**
 * our native peer for driving AWT/Swing application checking
 */
public class JPF_java_awt_EventDispatchThread extends JPF_gov_nasa_jpf_EventProducer {

  static JPFLogger log = JPF.getLogger("event");
  
  protected ComponentRegistry componentRegistry;
  
  // do we want to process all UIActionCGs regardless of state matching
  protected boolean forceActionStates = false;
  protected int counter; // the number of UIActionCGs generated so far

  public JPF_java_awt_EventDispatchThread (Config config){
    super(config);

    counter = 0;
    forceActionStates = config.getBoolean("awt.force_states", true);
  }
  
  
  static final String UIACTION = "[UIAction]";
  static final String CG_NAME = "processScriptAction";

  protected ComponentRegistry getComponentRegistry (MJIEnv env){
    if (componentRegistry == null){
      componentRegistry = ComponentRegistry.getRegistry(env);
    }
    
    return componentRegistry;
  }
  
  //--- these are used to extend the generic processScriptAction with awt specifics
  @Override
  protected boolean hasReturnedFormDirectCall (MJIEnv env, int objRef){
    ThreadInfo ti = env.getThreadInfo();
    return ti.hasReturnedFromDirectCall(UIACTION);
  }
  
  @Override
  protected void processEvent (MJIEnv env, int objRef){
    UIAction action = (UIAction) event;

    if (log.isInfoLogged()) {
      log.info("processing action: ", action);
    }

    ComponentRegistry reg = getComponentRegistry(env);
    if (reg == null){
      // <2do> can we have a processEvent before the first Window was created ?
      log.warning("no ComponentRegistry for process ", env.getApplicationContext().getId());
      
    } else {
      String componentName = action.getTarget();
      int tgtRef = reg.getComponentRef( componentName);

      if (tgtRef == MJIEnv.NULL) {
        log.warning("no component found for UIAction: " + action);

      } else if (!componentEnabled(env, tgtRef)) {
        log.warning("component NOT enabled for UIAction: " + action);

      } else {
        action.run(env, tgtRef);
      }

      env.repeatInvocation(); // we did a direct call
    }
  }
    
  @Override
  protected EventChoiceGenerator processNextCG (MJIEnv env, int objRef, EventChoiceGenerator cg){
    counter++;
    if (forceActionStates) {
      env.setIntField(objRef, "forceNewState", counter);
    }
    return cg;
  }
  
  //TODO: move all checks here
  private boolean componentEnabled(MJIEnv env, int tgtRef){
      
    int listRef = env.getStaticReferenceField("java.awt.Window", "modalDialogs");
    int arrayRef = env.getReferenceField(listRef, "elementData");   
        
    int arrayLength = env.getStaticIntField("java.awt.Window", "numModalDialogs");
        
    if(arrayLength >0){
      int topModalDialogRef = env.getReferenceArrayElement(arrayRef, arrayLength-1);
      //follow references upwards until no parent, get top-level window for current component
      int parentRef = env.getReferenceField(tgtRef, "parent");
      
      ElementInfo ei = env.getElementInfo(parentRef);
      log.fine("Parent :"+ei);
      
      while(parentRef != MJIEnv.NULL){
        parentRef = env.getReferenceField(parentRef, "parent");
        
        ei = env.getElementInfo(parentRef);
        log.fine("Parent :"+ei);
        //found a match
        if(parentRef == topModalDialogRef){
            return true;
        }
      }
      log.warning("action does not belong to top modal dialog");
      return false;    
    }
    //no modal dialogs, no restrictions
    return true;
  }  
  
}
