//
// Copyright (C) 2013 United States Government as represented by the
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

import static gov.nasa.jpf.awt.UICheck.log;
import gov.nasa.jpf.util.event.Event;
import gov.nasa.jpf.util.event.CheckEvent;
import gov.nasa.jpf.util.event.EventTree;
import gov.nasa.jpf.util.event.NoEvent;
import gov.nasa.jpf.vm.MJIEnv;

/**
 * an EventTree with UIAction events and specialized constructors
 */
public class UIActionTree extends EventTree {
  
  //--- override so that we ensure we only got UIActions in the tree
  public Event event (String spec){
    return new UIAction(spec, null);
  }
  
  public Event event (String spec, Object... arguments){
    return new UIAction(spec, arguments);
  }
  
  //--- those are our specialized event constructors
  protected UIAction action (String target, String method, boolean transferFocus, Object... arguments){
    return new UIAction( target, method, transferFocus, arguments);
  }
  
  protected UIAction action (String target, String method, boolean transferFocus){
    return new UIAction( target, method, transferFocus, null);
  }
  
  protected Event noAction (){
    return new NoEvent();
  }
  
  
  //--- specialized event composers
  
  protected Event noneOrAny ( String[] targets, String method, boolean transferFocus){
    Event eFirst = noAction();
    Event eLast = eFirst;
    
    for (String target : targets){
      Event e = action( target, method, transferFocus);
      eLast.addAlternative(e);
      eLast = e;
    }
    
    return eFirst;
  }

  
  protected Event click (String target, boolean transferFocus){
    return action( target, "doClick", transferFocus);
  }
  
  protected Event clickAnyCombination (String[] targets, boolean transferFocus) {
    UIAction[] actions = new UIAction[targets.length];
    for (int i=0; i<actions.length; i++){
      actions[i] = new UIAction(targets[i], "doClick", false);
    }
    
    return anyCombination(actions);
  }
  
  protected Event setText (String target, boolean transferFocus, String text){
    return action( target, "setText", transferFocus, text);
  }
  
  protected Event selectAny (String selectableTarget, boolean transferFocus, int minIndex, int maxIndex){
    Event eFirst = action( selectableTarget, "setSelectedIndex", transferFocus, minIndex);
    Event eLast = eFirst;
    
    for (int i=minIndex + 1; i<=maxIndex; i++){
      Event e = action( selectableTarget, "setSelectedIndex", transferFocus, i);
      eLast.addAlternative(e);
      eLast = e;
    }
    
    return eFirst;
  }
  
  protected Event selectAny (String selectableTarget, boolean transferFocus){
    return action( selectableTarget, "__selectAny", transferFocus);
  }
  
  
  //--- check events and supporting methods
  
  public class SingleSelectionCheck extends UICheck {
    SingleSelectionCheck (String targetName){
      super("singleSelection(\"" + targetName + "\")", targetName);
    }
    
    @Override
    public boolean evaluate (MJIEnv env, int objRef){
      log.info("checking ", name);
      
      int tgtRef = getTargetRef( env, (String)arguments[0]);
      int minIndex = callIntMethod( env, tgtRef, "getMinSelectionIndex()I");
      int maxIndex = callIntMethod( env, tgtRef, "getMaxSelectionIndex()I");
      
      return (minIndex == maxIndex && minIndex != -1);
    }
  }
  
  protected CheckEvent checkSingleSelection (String selectableTarget){
    return new SingleSelectionCheck( selectableTarget);
  }
  
  
  public class HasMatchingTextCheck extends UICheck {
    HasMatchingTextCheck (String targetName, String regex) {
      super("hasMatchingText(\"" + targetName + ',' + regex + "\")", targetName, regex);
    }

    @Override
    public boolean evaluate (MJIEnv env, int objRef){
      log.info("checking ", name);
     
      int tgtRef = getTargetRef( env, (String)arguments[0]);
      String text = callStringMethod( env, tgtRef, "getText()Ljava/lang/String;");
      
      if (text != null){
        String regex = (String)arguments[1];
        return text.matches(regex);
      } else {
        return false;
      }
    }
  }
  
  protected CheckEvent checkMatchingText (String textComponent, String regex){
    return new HasMatchingTextCheck( textComponent, regex);
  }
}
