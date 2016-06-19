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

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

/**
 * we don't model groups yet
 */
public class JToggleButton extends AbstractButton {
  
  public JToggleButton () {
    this(null, null, false);
  }
  
  public JToggleButton(Icon icon) {
    this(null, icon, false);
  }
  
  public JToggleButton(Icon icon, boolean selected) {
    this(null, icon, selected);
  }
  
  public JToggleButton (String text) {
    this(text, null, false);
  }
  
  public JToggleButton (String text, boolean selected) {
    this(text, null, selected);
  }  

  public JToggleButton(String text, Icon icon) {
    this(text, icon, false);
  }
  
  public JToggleButton (String text, Icon icon, boolean isSelected) {
    this.text = text;
    
    setModel(new ToggleButtonModel());
    model.setSelected( isSelected);
  }
  
  public static class ToggleButtonModel extends DefaultButtonModel {

    public boolean isSelected() {
      return (stateMask & SELECTED) != 0;
    }

    public void setSelected (boolean isSelected) {  
      ButtonGroup group = getGroup();
      if (group != null) {
        group.setSelected(this, isSelected);
        isSelected = group.isSelected(this);
      }

      if (isSelected() != isSelected){
        if (isSelected) {
          stateMask |= SELECTED;
        } else {
          stateMask &= ~SELECTED;
        }

        ItemEvent e = new ItemEvent( this, ItemEvent.ITEM_STATE_CHANGED, this,
                                     isSelected() ? ItemEvent.SELECTED : ItemEvent.DESELECTED);
        fireItemStateChanged(e);
      }
    }
    
    public void setPressed (boolean b) {
      if ((isPressed() == b) || !isEnabled()) {
          return;
      }

      if (b == false && isArmed()) {
          setSelected( !this.isSelected());
      } 

      if (b) {
          stateMask |= PRESSED;
      } else {
          stateMask &= ~PRESSED;
      }

      fireStateChanged();

      if(!isPressed() && isArmed()) {
          int modifiers = 0;
          /** no currentEvent yet
          AWTEvent currentEvent = EventQueue.getCurrentEvent();
          if (currentEvent instanceof InputEvent) {
              modifiers = ((InputEvent)currentEvent).getModifiers();
          } else if (currentEvent instanceof ActionEvent) {
              modifiers = ((ActionEvent)currentEvent).getModifiers();
          }
          **/
          ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getActionCommand(),
                                          EventQueue.getMostRecentEventTime(),modifiers);
          fireActionPerformed(e);
      }
    }

  }     
}
