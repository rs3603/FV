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

import gov.nasa.jpf.vm.Verify;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ListUI;
import javax.swing.text.Position;

/**
 * JList model that preserves swing List- and SelectionModels.
 * The main purpose is to model object selection
 * 
 * <2do> we should add a non-standard selection API that
 * enumerates all current items, to easy script writing
 */
public class JList extends JComponent implements SwingConstants {

  // some public constants
  public static final int VERTICAL = 0;
  public static final int VERTICAL_WRAP = 1;
  public static final int HORIZONTAL_WRAP = 2;

  // some static utility stuff
  static ListModel createListModel (final Object[] data) {
    return new AbstractListModel () {
      public int getSize() {
        return data.length;
      }
      
      public Object getElementAt (int idx) {
        return data[idx];
      }
    };
  }

  static ListModel createListModel (final Vector<?> data) {
    return new AbstractListModel () {
      public int getSize() {
        return data.size();
      }
      
      public Object getElementAt (int idx) {
        return data.get(idx);
      }
    };
  }

  
  static ListModel emptyModel;
  static ListModel getEmptyModel () {
    if (emptyModel != null) {
      emptyModel = new AbstractListModel () {
        public int getSize() {
          return 0;
        }
        
        public Object getElementAt (int idx) {
          // interestingly, this returns the following string instead of null 
          return "No Data Model";
        }
      };
    }
    return emptyModel;
  }

  
  //--- fields for the stuff we model
  private ListModel dataModel;
  private ListSelectionModel selectionModel;
  private ListSelectionListener selectionListener;

  
  //--- public ctors
  public JList() {
    this(getEmptyModel());
  }
  
  public JList (ListModel model) {
    if (model == null) {
      throw new IllegalArgumentException("dataModel must be non null");
    }

    dataModel = model;
    selectionModel = new DefaultListSelectionModel();
    
    // we need the plaf object because it creates the CellRendererPane
    updateUI();
  }

  public JList (Object[] data) {
    this(createListModel(data));
  }
  
  public JList (Vector<?> data) {
    this(createListModel(data));    
  }

  //--- stuff that is model based
  public ListModel getModel() {
    return dataModel;
  }

  public void setModel (ListModel newModel) {
    if (newModel == null) {
      throw new IllegalArgumentException("model must be non null");
    }
    
    ListModel oldModel = dataModel;
    dataModel = newModel;
    firePropertyChange("model", oldModel, newModel);
    
    clearSelection();
  }

  public void setListData(Object[] listData) {
    setModel( createListModel(listData));
  }
  public void setListData(Vector<?> listData) {
    setModel( createListModel(listData));
  }

  protected ListSelectionModel createSelectionModel() {
    return new DefaultListSelectionModel();
  }

  //--- selection handling - we have to model this carefully, since it's our main purpose
  protected void fireSelectionValueChanged (int firstIndex, int lastIndex, boolean isAdjusting) {
    Object[] listeners = listenerList.getListenerList();
    ListSelectionEvent e = new ListSelectionEvent( this, firstIndex, lastIndex, isAdjusting);

    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ListSelectionListener.class) {
        ((ListSelectionListener)listeners[i+1]).valueChanged(e);
      }
    }
  }
    
  // this is what is used to forward selection events from the SelectionModel
  // to the JList
  private class ListSelectionHandler implements ListSelectionListener {
      public void valueChanged(ListSelectionEvent e) {
          fireSelectionValueChanged (e.getFirstIndex(), e.getLastIndex(), e.getValueIsAdjusting());
      }
  }

  public void addListSelectionListener (ListSelectionListener listener) {
    if (selectionListener == null) {
      selectionListener = new ListSelectionHandler();
      getSelectionModel().addListSelectionListener(selectionListener);
    }
    listenerList.add( ListSelectionListener.class, listener);
  }
  public void removeListSelectionListener (ListSelectionListener listener) {
    listenerList.remove( ListSelectionListener.class, listener);
  }
  public ListSelectionListener[] getListSelectionListeners() {
    return (ListSelectionListener[]) listenerList.getListeners( ListSelectionListener.class);
  }
  
  public ListSelectionModel getSelectionModel() {
    return selectionModel;
  }
  
  public void setSelectionModel (ListSelectionModel newModel) {
    if (newModel == null) {
      throw new IllegalArgumentException("selectionModel must be non null");
    }

    if (selectionListener != null) {
      // if we had a selectionListener, we have to unlink/link
      selectionModel.removeListSelectionListener(selectionListener);
      newModel.addListSelectionListener(selectionListener);
    }

    ListSelectionModel oldModel = selectionModel;
    selectionModel = newModel;
    firePropertyChange("selectionModel", oldModel, newModel);
  }

  public void setSelectionMode(int selectionMode) {
    getSelectionModel().setSelectionMode(selectionMode);
  }
  public int getSelectionMode() {
    return getSelectionModel().getSelectionMode();
  }
  
  // lots of delegations
  public int getAnchorSelectionIndex() {
    return getSelectionModel().getAnchorSelectionIndex();
  }
  public int getLeadSelectionIndex() {
    return getSelectionModel().getLeadSelectionIndex();
  }
  public int getMinSelectionIndex() {
    return getSelectionModel().getMinSelectionIndex();
  }
  public int getMaxSelectionIndex() {
    return getSelectionModel().getMaxSelectionIndex();
  }
  public boolean isSelectedIndex(int index) {
    return getSelectionModel().isSelectedIndex(index);
  }
  public boolean isSelectionEmpty() {
    return getSelectionModel().isSelectionEmpty();
  }
  public void clearSelection() {
    getSelectionModel().clearSelection();
  }
  public void setSelectionInterval(int anchor, int lead) {
    getSelectionModel().setSelectionInterval(anchor, lead);
  }
  public void addSelectionInterval(int anchor, int lead) {
    getSelectionModel().addSelectionInterval(anchor, lead);
  }
  public void removeSelectionInterval(int index0, int index1) {
    getSelectionModel().removeSelectionInterval(index0, index1);
  }
  public void setValueIsAdjusting(boolean b) {
    getSelectionModel().setValueIsAdjusting(b);
  }
  public boolean getValueIsAdjusting() {
    return getSelectionModel().getValueIsAdjusting();
  }

  public int getSelectedIndex() {
    return getMinSelectionIndex();
  }
  
  public int[] getSelectedIndices() {
    ListSelectionModel model = getSelectionModel();
    
    int min = model.getMinSelectionIndex();
    int max = model.getMaxSelectionIndex();

    if (min < 0 || max < 0) { // shortcut - no selection
        return new int[0];
    }

    // how many selections to we have
    int n = 0;
    for (int i=min; i<=max; i++) {
      if (model.isSelectedIndex(i)) {
        n++;
      }
    }
    
    int[] sel = new int[n];
    for (int i=min, j=0; i<=max; i++) {
      if (model.isSelectedIndex(i)) {
        sel[j++] = i;
      }
    }
    
    return sel;
  }

  // this is probably the most important method for us
  public void setSelectedIndex (int index) {
    if (index < getModel().getSize()) {
      // note that we might deliberately cause exceptions for negative indices
      getSelectionModel().setSelectionInterval( index, index);
    } else {
      // larger values are silently ignored - interesting
    }
  }

  /**
   * this is a JPF specific method for the purpose of trying any
   * item that currently is in the list
   */
  public void __selectAny (){
    int index = Verify.getInt(0, getModel().getSize()-1);
    getSelectionModel().setSelectionInterval( index, index);
  }
  
  public void setSelectedIndices (int[] indices) {
    ListSelectionModel model = getSelectionModel();

    model.clearSelection();
    int max = getModel().getSize();

    for(int i = 0; i < indices.length; i++) {
      if (indices[i] < max) {
        model.addSelectionInterval(indices[i], indices[i]);
      }
    }
  }

  public Object[] getSelectedValues() {
    ListSelectionModel selModel = getSelectionModel();
    ListModel dataModel = getModel();
    
    int min = selModel.getMinSelectionIndex();
    int max = selModel.getMaxSelectionIndex();

    if (min < 0 || max < 0) { // shortcut - no selection
        return new Object[0];
    }

    // how many selections to we have
    int n = 0;
    for (int i=min; i<=max; i++) {
      if (selModel.isSelectedIndex(i)) {
        n++;
      }
    }
    
    Object[] sel = new Object[n];
    for (int i=min, j=0; i<=max; i++) {
      if (selModel.isSelectedIndex(i)) {
        sel[j++] = dataModel.getElementAt(i);
      }
    }
    
    return sel;
  }

  public Object getSelectedValue() {
    int idx = getMinSelectionIndex();
    if (idx == -1) {
      return null;
    } else {
      return getModel().getElementAt(idx);
    }
  }

  public void setSelectedValue (Object o, boolean shouldScroll) {
    if(o != null) {
      if (o.equals(getSelectedValue())) {
        // silently ignored, no notification
      } else {
        ListModel model = getModel();
        int n = model.getSize();
        
        for (int i=0; i<n; i++) {
          if (o.equals(model.getElementAt(i))) {
            setSelectedIndex(i);
            return;
          }
        }
      }
    }

    // everything else silently resets the selection
    clearSelection();
  }

  
  public int getNextMatch (String prefix, int startIndex, Position.Bias bias) {
    // <2do> do we need this? It's listModel based
    return 42;
  }
  
  public String getUIClassID() {
    return "ListUI";
  }

  public void updateUI() {
    setUI((ListUI)UIManager.getUI(this));
  }

  
  
  //--- various stubs (mostly geometry related)
  public int getFirstVisibleIndex() {
    return 0;
  }
  public int getLastVisibleIndex() {
    return 42;
  }
  public void ensureIndexIsVisible (int index) {
    // sure, whatever
  }
      
  public Rectangle getCellBounds (int index0, int index1) {
    return null;
  }
  
  public Dimension getPreferredScrollableViewportSize() {
    return new Dimension(42,42);
  }

  
  //--- not used, but need to store/retrieve values set by application
  
  int fixedCellWidth;
  public int getFixedCellWidth() {
    return fixedCellWidth;
  }
  public void setFixedCellWidth(int newWidth) {
    int oldWidth = fixedCellWidth;
    fixedCellWidth = newWidth;
    firePropertyChange("fixedCellWidth", oldWidth, newWidth);
  }

  int fixedCellHeight;
  public int getFixedCellHeight() {
    return fixedCellHeight;
  }
  public void setFixedCellHeight (int newHeight) {
    int oldHeight = fixedCellHeight;
    fixedCellHeight = newHeight;
    firePropertyChange("fixedCellHeight", oldHeight, newHeight);
  }

  ListCellRenderer cellRenderer;
  public ListCellRenderer getCellRenderer() {
    return cellRenderer;
  }
  public void setCellRenderer (ListCellRenderer newRenderer) {
    ListCellRenderer oldRenderer = cellRenderer;
    cellRenderer = newRenderer;

    // we don't support geometry yet, so we don't update cell size
    
    firePropertyChange("cellRenderer", oldRenderer, newRenderer);
  }

  Color selectionForeground, selectionBackground;
  public Color getSelectionForeground() {
    return selectionForeground;
  }
  public void setSelectionForeground (Color newForeground) {
    Color oldForeground = selectionForeground;
    selectionForeground = newForeground;
    firePropertyChange("selectionForeground", oldForeground, newForeground);
  }
  public Color getSelectionBackground() {
    return selectionBackground;
  }
  public void setSelectionBackground (Color newBackground) {
    Color oldBackground = selectionBackground;
    selectionBackground = newBackground;
    firePropertyChange("selectionBackground", oldBackground, newBackground);
  }
  
  int visibleRowCount;
  public int getVisibleRowCount() {
    return visibleRowCount;
  }
  public void setVisibleRowCount (int newCount) {
    int oldCount = visibleRowCount;
    visibleRowCount = newCount;
    firePropertyChange("visibleRowCount", oldCount, newCount);
  }
  
  int layoutOrientation;
  public int getLayoutOrientation() {
    return layoutOrientation;
  }
  public void setLayoutOrientation (int newOrientation) {
    if (newOrientation < 0 || newOrientation > 2) {
      throw new IllegalArgumentException("illegal layoutOrientation");
    }
    
    int oldOrientation = layoutOrientation;
    layoutOrientation = newOrientation;
    firePropertyChange("layoutOrientation", oldOrientation, newOrientation);
  }
  
  boolean dragEnabled; // not a property
  public void setDragEnabled(boolean isEnabled) {
    dragEnabled = isEnabled;
  }
  public boolean getDragEnabled() {
    return dragEnabled;    
  }
  
}
