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

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.util.JPFLogger;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.MethodLocator;
import gov.nasa.jpf.util.event.Event;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ClassLoaderInfo;
import gov.nasa.jpf.vm.DirectCallStackFrame;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import java.util.logging.Level;

/**
 * this models a single user interaction, which maps to a (reflection) call
 * of a method in a java.awt.Component instance (or a static method if
 * there is no target spec)
 */
public class UIAction extends Event implements MethodLocator {
  static JPFLogger log = JPF.getLogger("event");
  
  public final char TRANSFER_FOCUS_MARKER = '*';

  protected String target;      // the component id (enumeration number)
  protected String action;      // the method name
  
  protected boolean transferFocus;   // transfer focus before action is executed 

  protected Object[] arguments;

  protected int line;           // from script

  public UIAction (String target, String action, boolean transferFocus){
    this(target,action,transferFocus,null);
  }
  
  public UIAction (String target, String action, boolean transferFocus, Object[] arguments){
    super( target + '.' + action, arguments);
    
    this.target = target;
    this.action = action;
    this.arguments = arguments;
    this.transferFocus = transferFocus;
  }
  
  /**
   * 'spec' has the form $target.action
   * target is either a number or a hierarchical string of ids, e.g. $MyFrame.Ok
   */
  public UIAction (String spec, Object[] args) {
    super(spec, args);

    if (spec.charAt(0) == TRANSFER_FOCUS_MARKER) {
      spec = spec.substring(1);
      transferFocus = true;
    }

    int i = spec.lastIndexOf('.');
    if (i > 0) {
      target = spec.substring(0, i);
      spec = spec.substring(i + 1);
    }

    action = spec;
  }

  public boolean transferFocus(){
    return transferFocus;
  }
  
  public String getTarget() {
    return target;
  }

  public void setTarget (String s) {
    target = s;
  }

  public String getAction() {
    return action;
  }

  public void setAction (String s) {
    action = s;
  }

  public Object[] getArguments() {
    return arguments;
  }

  public int getLine () {
    return line;
  }

  public Class[] getArgumentTypes() {
    if (arguments == null) {
      return new Class[0];
    } else {
      Class[] list = new Class[arguments.length];
      for (int i=0; i<arguments.length; i++) {
        Object a = arguments[i];

        if (a instanceof String) {
          list[i] = String.class;
        } else if (a instanceof Double) {
          list[i] = double.class;
        } else if (a instanceof Integer) {
          list[i] = int.class;
        } else {
          assert false : "unsupported argument type in event: " + a;
        }
      }
      return list;
    }
  }

  public Object[] getBoxedArguments(Class<?>[] argTypes) {
    if (arguments == null) {
      return new Object[0];
    } else {
      Object[] list = new Object[arguments.length];
      for (int i=0; i<arguments.length; i++) {
        Object a = arguments[i];

        if (a instanceof String) {
          list[i] = a;
        } else if (a instanceof Double) {
          list[i] = new Double(((Number)a).doubleValue());
        } else if (a instanceof Integer) {
          list[i] = new Integer(((Number)a).intValue());
        } else {
          assert false : "unsupported argument type in UIAction: " + a;
        }
      }
      return list;
    }
  }

  // <2do> this thing is screwed - there is not enough type info in UIAction to get the method
  public String getMethodName () {
    StringBuilder sb = new StringBuilder(action);

    sb.append('(');
    if (arguments != null) {
      for (Object a : arguments) {
        if (a == null) {
          // nope, could be anything
          sb.append("Ljava/lang/Object;");
        } else if (a instanceof String) {
          sb.append("Ljava/lang/String;");
        } else if (a instanceof Double) {
          sb.append('J');
        } else if (a instanceof Integer) {
            sb.append('I');
        } else {
          assert false : "unsupported argument type in UIAction: " + a;
        }
      }
    }

    // Hmm, we should handle non-void return types
    sb.append(")V");

    return sb.toString();
  }

  public boolean match (MethodInfo mi) {
    if (mi.getName().equals(action)) {
      byte[] atypes = mi.getArgumentTypes();
      if (atypes.length == arguments.length) {
        for (int i=0; i<atypes.length; i++) {
          Object a = arguments[i];

        }
      }
    }
    return false;
  }

  public String toString() {
    StringBuilder b = new StringBuilder();
    
    if (transferFocus){
      b.append(TRANSFER_FOCUS_MARKER);
    }
    
    if (target != null) {
      b.append(target);
      b.append('.');
    }
    
    if (action != null) {
      b.append(action);
      b.append('(');
      if (arguments != null) {
        for (int i = 0; i < arguments.length; i++) {
          Object a = arguments[i];
          if (i > 0) {
            b.append(',');
          }
          if (a instanceof String) {
            b.append('"');
            b.append(a);
            b.append('"');
          } else if (a instanceof Integer) {
            b.append(((Integer) a).intValue());
          } else if (a instanceof Double) {
            b.append(((Double) a).doubleValue());
          }
        }
      }
      b.append(')');
    }

    return b.toString();
  }
  
  
  protected MethodInfo getMethodInfo (ClassInfo ci) {
    // <2do> this does not work - we can't deduce the exact method signature from
    // the UI action because of the missing return type and the lossy parameter
    // parsing (numbers are all stored as Double). We have to reverse match
    String methodName = getMethodName();
    MethodInfo mi = ci.getMethod(methodName, true);

    return mi;
  }

    // <2do> very simplistic argument handling for now
  protected void pushArg (MJIEnv env, Object arg, byte typeCode, StackFrame frame) {
    if (arg == null) {
      frame.push(MJIEnv.NULL, false);
    } else if (arg instanceof String) {
      int sRef = env.newString((String)arg);
      frame.push(sRef, true);
    } else if (arg instanceof Double) {
      frame.pushDouble((Double)arg);
    } else if (arg instanceof Integer) {
      frame.push((Integer)arg);
    } else if (arg instanceof Boolean) {
      frame.push(((Boolean)arg) ? 1 : 0, false);
    } else {
      throw new UnsupportedOperationException("argument type not supported: " + arg);
    }
  }

  
  
  protected MethodInfo getTransferFocusMethod (MJIEnv env, int tgtRef){
    ClassInfo ci = ClassLoaderInfo.getSystemResolvedClassInfo("java.awt.Component");

    MethodInfo mi = ci.getMethod("transferFocus()V", true);
    assert mi != null : "no java.awt.Component.transferFocus() method found (check model class)";

    return mi;
  }

  
  protected void run (MJIEnv env, int tgtRef){
    ElementInfo ei = env.getElementInfo(tgtRef);
    ClassInfo ci = ei.getClassInfo();
    if (!ci.isInstanceOf("java.awt.Component")) {
      log.warning("UIAction target reference for : " + action
              + " is not a java.awt.Component: " + ei);
    } else {

      MethodInfo mi = getMethodInfo(ci);

      if (mi == null) {
        log.warning("UIAction " + action + " refers to unknown method " + action + "() in class " + ci.getName());

      } else {
        if (log.isLoggable(Level.FINER)) {
          log.finer("calling UIAction: " + action + " : " + ei + "." + mi.getUniqueName());
        }
        // Ok, now we can finally make the (direct) call
        DirectCallStackFrame frame = ci.createDirectCallStackFrame(env.getThreadInfo(), mi, 0);

        if (!mi.isStatic()) {
          frame.push(tgtRef, true);
        }

        Object[] args = arguments;
        if (args != null) {
          byte[] argTypes = mi.getArgumentTypes();
          for (int i = 0; i < args.length; i++) {
            pushArg(env, args[i], argTypes[i], frame);
          }
        }

        ThreadInfo ti = env.getThreadInfo();
        ti.pushFrame(frame);

        // if the event included a focus transfer directive, do this
        // before invoking the action, to ensure proper invocation of
        // FocusListeners
        if (transferFocus()) {
          MethodInfo miTransferFocus = getTransferFocusMethod(env, tgtRef);
          frame = miTransferFocus.createDirectCallStackFrame(env.getThreadInfo(), 0);
          frame.pushRef(tgtRef);
          ti.pushFrame(frame);
        }
      }
    }
  }
  
  public boolean isInstanceOf (MJIEnv env, int objRef, String clsName){
    ClassInfo ci = env.getClassInfo(objRef);
    return ci.isInstanceOf(clsName);
  }

}
