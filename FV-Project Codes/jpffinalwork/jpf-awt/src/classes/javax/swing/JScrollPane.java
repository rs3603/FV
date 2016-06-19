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

import java.awt.Component;

/**
 * very rudimentary model version.
 * So far, we only model component hierarchy, but without the
 * decorations
 */
public class JScrollPane extends JComponent implements ScrollPaneConstants {
  
  JViewport viewport;
  
  protected int verticalScrollBarPolicy = VERTICAL_SCROLLBAR_AS_NEEDED;
  protected int horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_AS_NEEDED;

  //--- we need to model these to keep the component hierarchy ths same
  JScrollBar vScrollBar, hScrollBar;
  
  // <2do> not yet 
  JViewport rowHeader;
  JViewport columnHeader;
  
  Component topLeft, topRight, bottomLeft, bottomRight;
  
  
  public JScrollPane() {
    this(null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
  }
  
  public JScrollPane(Component view) {
    this(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
  }
  
  
  public JScrollPane (Component view, int vscrollPolicy, int hscrollPolicy) {

    // watch out - we have to create components in the same order
    
    setViewport( createViewport());
    if (view != null) {
      setViewportView(view);
    }
    
    setVerticalScrollBarPolicy(vscrollPolicy);
    setHorizontalScrollBarPolicy(hscrollPolicy);
    setVerticalScrollBar(createVerticalScrollBar());
    setHorizontalScrollBar(createHorizontalScrollBar());
  }
  
  public JViewport getViewport() {
    return viewport;
  }

  public void setViewport (JViewport newViewport) {
    JViewport oldViewport = getViewport();
    viewport = newViewport;
    if (viewport != null) {
        add(viewport, VIEWPORT);
    } else if (oldViewport != null) {
        remove(oldViewport);
    }
    firePropertyChange("viewport", oldViewport, newViewport);

    // no geometry modeling yet
  }
  
  protected JViewport createViewport() {
    return new JViewport();
  }

  public void setViewportView(Component view) {
    if (getViewport() == null) {
        setViewport(createViewport());
    }
    getViewport().setView(view);
  }
  
  
  public int getVerticalScrollBarPolicy() {
    return verticalScrollBarPolicy;
  }
  public void setVerticalScrollBarPolicy (int newPolicy) {
    if ((newPolicy != VERTICAL_SCROLLBAR_AS_NEEDED) &&
        (newPolicy != VERTICAL_SCROLLBAR_NEVER) &&
        (newPolicy != VERTICAL_SCROLLBAR_ALWAYS)) {
      throw new IllegalArgumentException("invalid verticalScrollBarPolicy");
    }
    
    int oldPolicy = verticalScrollBarPolicy;
    verticalScrollBarPolicy = newPolicy;
    firePropertyChange("verticalScrollBarPolicy", oldPolicy, newPolicy);
  }

  
  public int getHorizontalScrollBarPolicy() {
    return horizontalScrollBarPolicy;
  }
  public void setHorizontalScrollBarPolicy (int newPolicy) {
    if (newPolicy != HORIZONTAL_SCROLLBAR_AS_NEEDED &&
        newPolicy != HORIZONTAL_SCROLLBAR_NEVER &&
        newPolicy != HORIZONTAL_SCROLLBAR_ALWAYS) {
      throw new IllegalArgumentException("invalid horizontalScrollBarPolicy");
    }
    
    int oldPolicy = horizontalScrollBarPolicy;
    horizontalScrollBarPolicy = newPolicy;
    firePropertyChange("horizontalScrollBarPolicy", oldPolicy, newPolicy);
  }
  
  public JScrollBar createHorizontalScrollBar() {
    // that's not right - JList has it's own JScrollBar class, but we don't care for now
    return new JScrollBar(JScrollBar.HORIZONTAL);
  }

  public JScrollBar createVerticalScrollBar() {
    // that's not right - JList has it's own JScrollBar class, but we don't care for now
    return new JScrollBar(JScrollBar.VERTICAL);
  }

  public JScrollBar getVerticalScrollBar() {
    return vScrollBar;
  }
  public JScrollBar getHorizontalScrollBar() {
    return hScrollBar;
  }
  
  public void setVerticalScrollBar (JScrollBar newSb) {
    JScrollBar oldSb = getVerticalScrollBar();
    vScrollBar = newSb;
    add( newSb, VERTICAL_SCROLLBAR);
    firePropertyChange("verticalScrollBar", oldSb, newSb);
  }

  public void setHorizontalScrollBar (JScrollBar newSb) {
    JScrollBar oldSb = getHorizontalScrollBar();
    hScrollBar = newSb;
    add( newSb, HORIZONTAL_SCROLLBAR);
    firePropertyChange("horizontalScrollBar", oldSb, newSb);
  }

}
