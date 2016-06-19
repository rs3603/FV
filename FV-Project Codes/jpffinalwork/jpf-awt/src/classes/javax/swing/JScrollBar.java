//Copyright (C) 2008 United States Government as represented by the
//Administrator of the National Aeronautics and Space Administration
//(NASA).  All Rights Reserved.

//This software is distributed under the NASA Open Source Agreement
//(NOSA), version 1.3.  The NOSA has been approved by the Open Source
//Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
//directory tree for the complete NOSA document.

//THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
//KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
//LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
//SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
//A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
//THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
//DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
package javax.swing;

import java.awt.Adjustable;
import java.awt.Dimension;
import java.awt.event.AdjustmentListener;

/**
 * very minimal ScrollBar model - since we don't model low level
 * events, this is only a container for the scroll values
 */
public class JScrollBar extends JComponent implements Adjustable {

  protected BoundedRangeModel model;
  protected int orientation;
  protected int unitIncrement;
  protected int blockIncrement;
  
  
  public JScrollBar (int orientation, int value, int extent, int min, int max) {
    unitIncrement = 1;
    blockIncrement = (extent == 0) ? 1 : extent;
    this.orientation = orientation;
    model = new DefaultBoundedRangeModel(value, extent, min, max);
  }
  public JScrollBar(int orientation) {
    this(orientation, 0, 10, 0, 100);
  }
  public JScrollBar() {
    this(VERTICAL);
  }

  public int getOrientation() {
    return orientation;
  }

  public void setOrientation (int newOrientation) {
    orientation = newOrientation;
    // probably needs a property change notification
  }
  
  public BoundedRangeModel getModel() {
    return model;
  }
  
  public void setModel(BoundedRangeModel newModel) {
    model = newModel;
    // probably needs a property change notification
  }
  
  public int getUnitIncrement() {
    return unitIncrement;
  }
  public int getUnitIncrement (int direction) {
    return unitIncrement;    
  }
  public void setUnitIncrement (int newIncrement) {
    unitIncrement = newIncrement;
    // probably needs a property change notification    
  }


  public int getBlockIncrement(){
    return blockIncrement;
  }
  public int getBlockIncrement(int direction){
    return blockIncrement;
  }
  public void setBlockIncrement(int newIncrement) {
    blockIncrement = newIncrement;
    // probably needs a property change notification    
  }
  
  public int getValue(){
    return getModel().getValue();
  }
  public void setValue (int newValue){
    getModel().setValue(newValue);
  }
  public void setValues (int newValue, int newExtent, int newMin, int newMax) {
    getModel().setRangeProperties(newValue, newExtent, newMin, newMax, false);
  }
  
  public int getVisibleAmount(){
    return getModel().getExtent();
  }
  public void setVisibleAmount(int extent) {
    getModel().setExtent(extent);
  }

  public int getMinimum() {
    return getModel().getMinimum();
  }
  public void setMinimum(int minimum) {
    getModel().setMinimum(minimum);
  }

  public int getMaximum() {
    return getModel().getMaximum();
  }
  public void setMaximum(int maximum) {
    getModel().setMaximum(maximum);
  }
  
  public boolean getValueIsAdjusting(){
    return getModel().getValueIsAdjusting();
  }
  public void setValueIsAdjusting (boolean isAdjusting){
    getModel().setValueIsAdjusting(isAdjusting);
  }

  public void addAdjustmentListener(AdjustmentListener listener){}
  public void removeAdjustmentListener(AdjustmentListener listener){}
  public AdjustmentListener[] getAdjustmentListeners(){ 
    return null;
  }
  protected void fireAdjustmentValueChanged(int id, int type, int value) {
  }

  //--- I don't think we need those
  public Dimension getMinimumSize() {
    Dimension preferred = getPreferredSize();
    if (orientation == VERTICAL) {
      return new Dimension(preferred.width, 5);
    } else {
      return new Dimension(5, preferred.height);
    }
  }
  
  public Dimension getMaximumSize() {
    Dimension preferred = getPreferredSize();
    if (getOrientation() == VERTICAL) {
      return new Dimension(preferred.width, Short.MAX_VALUE);
    } else {
      return new Dimension(Short.MAX_VALUE, preferred.height);
    }
  }
  
}
