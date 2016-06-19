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

import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * we only model high level events
 */
public class AbstractButton extends JComponent implements ItemSelectable {

  class Handler implements ActionListener, ChangeListener, ItemListener {
    public void stateChanged (ChangeEvent e) {
      fireStateChanged();
    }

    public void actionPerformed (ActionEvent e) {
      fireActionPerformed(e);
    }

    public void itemStateChanged (ItemEvent e) {
      fireItemStateChanged(e);
    }
  }

  /** this is our callback connection to the model */
  Handler handler = new Handler();
  ChangeEvent changeEvent;
  String text;
  ButtonModel model;
  boolean contentAreaFilled = false;
  boolean paintFocus = true;
  Icon icon;
  boolean borderPainted = true;

  
  public void addActionListener (ActionListener listener) {
    listenerList.add(ActionListener.class, listener);
  }
  public void removeActionListener (ActionListener listener) {
    listenerList.remove(ActionListener.class, listener);
  }
  
  public void addItemListener (ItemListener listener) {
    listenerList.add(ItemListener.class, listener);
  }
  public void removeItemListener (ItemListener listener) {
    listenerList.remove(ItemListener.class, listener);
  }
  
  public void addChangeListener (ChangeListener listener) {
    listenerList.add(ChangeListener.class, listener);
  }
  public void removeChangeListener (ChangeListener listener) {
    listenerList.remove(ChangeListener.class, listener);
  }
  
  public ButtonModel getModel () {
    return model;
  }
  
  public String getActionCommand() {
    String command = getModel().getActionCommand();
    return command != null ? command : getText();
  }

  public void setActionCommand(String actionCommand) {
    getModel().setActionCommand(actionCommand);
  }

  protected void fireItemStateChanged (ItemEvent e) {
    Object[] list = listenerList.getListenerList();
    if (list.length >= 2) {
      e = new ItemEvent( AbstractButton.this, ItemEvent.ITEM_STATE_CHANGED,
                         AbstractButton.this, e.getStateChange());
      for (int i = list.length-2; i >= 0; i -= 2) {
        if (list[i] == ItemListener.class) {
          ((ItemListener)list[i+1]).itemStateChanged(e);
        }
      }
    }
  }

  protected void fireActionPerformed (ActionEvent e) {
    Object[] list = listenerList.getListenerList();
    if (list.length >= 2) {
      
      String command = e.getActionCommand();
      if(command == null) {
         command = getActionCommand();
      }
      e = new ActionEvent(AbstractButton.this,ActionEvent.ACTION_PERFORMED,
                          command, e.getWhen(), e.getModifiers());

      for (int i = list.length-2; i >= 0; i -= 2) {
        if (list[i] == ActionListener.class) {
          ((ActionListener)list[i+1]).actionPerformed(e);
        }
      }
    }
  }

  protected void fireStateChanged() {
    Object[] list = listenerList.getListenerList();  
    if (list.length >= 2) {
      if (changeEvent == null) {
        changeEvent = new ChangeEvent(this);
      }
      
      for (int i = list.length-2; i>=0; i-=2) {
        if (list[i]==ChangeListener.class) {
          ((ChangeListener)list[i+1]).stateChanged(changeEvent);
        }          
      }
    }
  }   
  
  public void setText (String text) {
    this.text = text;
  }
  
  public String getText () {
    return text;
  }
  
  public String getLabel() {
    return text;
  }
  
  public void setLabel (String text) {
    this.text = text;
  }
  
  public void setModel (ButtonModel newModel) {
    
    if (model != null) {
      model.removeActionListener(handler);
      model.removeItemListener(handler);
    }
    
    model = newModel;
    
    if (model != null){
      model.addActionListener(handler);
      model.addItemListener(handler);
    }
  }
  
  public boolean isSelected() {
    return model.isSelected();
  }
  
  public void setSelected (boolean isSelected) {
    model.setSelected( isSelected);
  }

  public void setEnabled (boolean isEnabled) {
    super.setEnabled(isEnabled);
    model.setEnabled(isEnabled);
  }
  
  /**
   * programmatic simulation of a user click. This is the method
   * that is called from our modeled EventDispatchThread (for now), so that we
   * don't have to deal with raw input event sequences, but still set the proper
   * state of the JComponent (which can be queried by the application, and hence
   * should be modeled correctly)
   */
  public void doClick() {
    model.setArmed(true);
    model.setPressed(true);
    model.setPressed(false);
    model.setArmed(false);
  }
  
  public void doClick (int pressTime) {
    doClick();
  }
  public Object[] getSelectedObjects () {
    // TODO Auto-generated method stub
    return null;
  }

  public void setContentAreaFilled(boolean b) {
    contentAreaFilled = b;
  }

  public boolean isContentAreaFilled() {
    return contentAreaFilled;
  }

  public void setFocusPainted(boolean b) {
    paintFocus = b;
  }

  public boolean isFocusPainted() {
    return paintFocus;
  }

  public boolean isFocusOwner() {
    return false;
  }

  public void setIcon(Icon icon) {
    this.icon = icon;
  }

  public Icon getIcon() {
    return icon;
  }

  public void setBorderPainted(boolean b) {
    borderPainted = b;
  }

  public boolean isBorderPainted() {
    return borderPainted;
  }

  public void setHorizontalAlignment(int alignment){
  }
}
