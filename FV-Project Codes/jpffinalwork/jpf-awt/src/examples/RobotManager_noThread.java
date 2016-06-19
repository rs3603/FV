
import gov.nasa.jpf.awt.UIActionTree;
import gov.nasa.jpf.util.event.Event;

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

/**
 * test event generator for RobotManager run without async data acquisition
 */
public class RobotManager_noThread extends UIActionTree {

  @Override
  public Event createRoot () {
    String[] options = { "$FORCED", "$QUEUED", "$SINGLE_STEP" };
    
    return sequence(
            // enter command
            setText("$Command:input", true, "turn"),
            
            // try all option combinations, not bothering about focus changes
            clickAnyCombination( options, true),
            
            // try all robots in list
            selectAny("$Robots:list", true),
            
            //checkSingleSelection("$Robots:list").and( checkMatchingText("$Command:input", "turn")),
            
            // send command
            click("$Send", true)
           );
  }

}
