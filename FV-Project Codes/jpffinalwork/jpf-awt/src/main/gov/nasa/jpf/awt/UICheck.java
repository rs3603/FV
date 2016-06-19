//
// Copyright (C) 2014 United States Government as represented by the
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

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.util.JPFLogger;
import gov.nasa.jpf.util.event.CheckEvent;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.DirectCallStackFrame;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.UncaughtException;

/**
 * a check event in a UI context
 */
public abstract class UICheck extends CheckEvent {

  static JPFLogger log = JPF.getLogger("awt");
  
  protected UICheck (String name, Object... args){
    super(name, args);
  }
  
  public ComponentRegistry getComponentRegistry (MJIEnv env){
    return ComponentRegistry.getRegistry(env);
  }
  
  public int getTargetRef (MJIEnv env, String componentName){
    ComponentRegistry reg = getComponentRegistry(env);
    
    if (reg != null){
      return reg.getComponentRef(componentName);
    } else {
      return MJIEnv.NULL;
    }
  }
  
  public boolean isInstanceOf (MJIEnv env, int objRef, String clsName){
    ClassInfo ci = env.getClassInfo(objRef);
    return ci.isInstanceOf(clsName);
  }
  
  public MethodInfo getMethodInfo (MJIEnv env, int objRef, String mthName){
    ClassInfo ci = env.getClassInfo(objRef);
    return ci.getMethod(mthName, true);
  }
  
  protected int callMethod (MJIEnv env, int objRef, String mthName, String sig){
    if (!mthName.endsWith(sig)){
      log.warning("wrong method signature: ", mthName, " not a ", sig);
      return MJIEnv.NULL;
      
    } else {
      MethodInfo mi = getMethodInfo( env, objRef, mthName);
      if (mi != null){
        ThreadInfo ti = env.getThreadInfo();
        DirectCallStackFrame frame = mi.createDirectCallStackFrame(ti, 0);

        int argOffset = frame.setReferenceArgument(0, objRef, null);
        frame.setFireWall();

        try {
          ti.executeMethodHidden(frame);

        } catch (UncaughtException ux) {  // frame's method is firewalled
          log.warning("calling ", mthName, "failed: ", ux.getCause().toString());
          ti.clearPendingException();
          ti.popFrame(); // this is still the DirectCallStackFrame, and we want to continue execution
          return MJIEnv.NULL;
        }

        // get the return value from the (already popped) frame
        return frame.getResult();        
        
      } else {
        ClassInfo ci = env.getClassInfo(objRef);
        log.warning("no ", mthName, " in class ", ci.getName());
        return MJIEnv.NULL;
      }
    }
  }
  
  protected int callIntMethod (MJIEnv env, int objRef, String mthName){
    return callMethod( env, objRef, mthName, "()I");
  }
  
  protected String callStringMethod (MJIEnv env, int objRef, String mthName){
    int sRef = callMethod(env, objRef, mthName, "()Ljava/lang/String;");
    if (sRef != MJIEnv.NULL){
      return env.getStringObject(sRef);
    } else {
      return null;
    }
  }
}
