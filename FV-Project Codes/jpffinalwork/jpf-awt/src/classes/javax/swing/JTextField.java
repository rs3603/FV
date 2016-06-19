//
// Copyright (C) 2008 United States Government as represented by the
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

/**
 * we only model this because we want to extend the "UI action method"
 * setText(..) to also include ActionListener notification if the
 * text ends with a '\n'. Otherwise we would need two separate
 * events to simulate a user entering text and hitting return
 * 
 * <2do> this is VERY incomplete
 */
public class JTextField extends JTextComponent {

  private int columns;
  private String command;
  private int alignment;
  
  public JTextField() {
    this(null, null, 0);
  }
  
  public JTextField(String text) {
    this(null, text, 0);
  }

  public JTextField(int columns) {
    this(null, null, columns);
  }
  
  public JTextField(Document doc, String text, int columns) {
    if (columns < 0) {
        throw new IllegalArgumentException("columns less than zero.");
    }
    this.columns = columns;
    if (doc == null) {
        doc = createDefaultModel();
    }
    setDocument(doc);
    if (text != null) {
        setText(text);
    }
  }

  protected Document createDefaultModel() {
    return new PlainDocument();
  }

  public int getColumns() {
    return columns;
  }
  public void setColumns (int n) {
    if (n < 0) {
        throw new IllegalArgumentException("columns less than zero.");
    }
    if (n != columns) {
        columns = n;
        invalidate(); // we don't do anything with this yet   
    }
  }

  
  public void addActionListener (ActionListener listener) {
    listenerList.add(ActionListener.class, listener);
  }
  public void removeActionListener (ActionListener listener) {
    listenerList.remove(ActionListener.class, listener);
  }

  public void setActionCommand (String command) {
    this.command = command;
  }
  
  protected void fireActionPerformed () {
    Object[] list = listenerList.getListenerList();
    if (list.length >= 2) {
      
      String text = (command != null) ? command : getText();
      ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                                      text,
                                      0, 0); // <2do> need to implement time and modifiers

      for (int i = list.length-2; i >= 0; i -= 2) {
        if (list[i] == ActionListener.class) {
          ((ActionListener)list[i+1]).actionPerformed(e);
        }
      }
    }
  }

  
  /*
   * we override this in our modeled class to support
   * setText("...\n") input, which should automatically fire
   * the ActionEvent
   */
  public void setText (String text) {

    if (text != null && text.length() > 0 && text.charAt(text.length()-1) == '\n') {
      super.setText(text.substring(0, text.length()-1));
      fireActionPerformed();
    } else {
      super.setText(text);
    }
  }

  public void setHorizontalAlignment(int i) {
    alignment = i;
  }

  public int getHorizontalAlignment() {
    return alignment;
  }
}
