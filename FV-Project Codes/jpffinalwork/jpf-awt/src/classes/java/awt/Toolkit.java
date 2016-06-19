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

package java.awt;

import java.awt.event.KeyEvent;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.beans.PropertyChangeListener;
import java.net.URL;

/**
 * Toolkit is pretty much a dummy for us, so we don't need the usual factory. We
 * keep the hierarchy in case we later on want to extend the modeled functions
 */
public abstract class Toolkit {

  public static Toolkit getDefaultToolkit() {
    return JPFToolkit.singleton; 
  }
  
  static EventQueue getEventQueue () {
    return getDefaultToolkit().getSystemEventQueue();
  }
  
  public abstract EventQueue getSystemEventQueue();
  
  abstract void startEventDispatching();

  static void loadLibraries () {
    // nothing
  }
  
  public abstract int getScreenResolution();
  public abstract Dimension getScreenSize();
  public abstract void sync();
  public abstract Image getImage(URL url);

  public static String getProperty(String key, String defaultValue) {
    return "";
  }

}

class JPFToolkit extends Toolkit {
  static final Toolkit singleton = new JPFToolkit();
  
  EventQueue eventQueue = new EventQueue();
  Thread eventDispatchThread;
  
  JPFToolkit() {
    // nothing yet - maybe we move the EventDispatcher up to this at some point
  }
  
  public EventQueue getSystemEventQueue() {
    return eventQueue;
  }
  
  void startEventDispatching() {
    eventQueue.initDispatchThread();
  }
  
  public int getScreenResolution() {
    return 120; // dpi, whatever
  }
  
  public Dimension getScreenSize() {
    return new Dimension (1280,1024); // whatever
  }
  
  public void sync () {
    // not sure we want to process this - it's said to update
    // the "graphics state" (which would be irrelevant), but in
    // reality it empties the event queue
  }

  public Image getImage(URL url) {
    return new JPFImage();
  }

  public final Object getDesktopProperty(String propertyName) {
    return null;
  }

  public void addPropertyChangeListener(String name,PropertyChangeListener pcl) {
  }

  public boolean getLockingKeyState(int keyCode)
    throws UnsupportedOperationException {
        if (! (keyCode == KeyEvent.VK_CAPS_LOCK || keyCode == KeyEvent.VK_NUM_LOCK ||
               keyCode == KeyEvent.VK_SCROLL_LOCK || keyCode == KeyEvent.VK_KANA_LOCK)) {
            throw new IllegalArgumentException("invalid key for Toolkit.getLockingKeyState");
        }
        throw new UnsupportedOperationException("Toolkit.getLockingKeyState");
    }

  private class JPFImage extends Image {

    public int getWidth(ImageObserver observer) {
      return 0;
    }

    public int getHeight(ImageObserver observer) {
      return 0;
    }

    public ImageProducer getSource() {
      return new ImageProducer() {
        public void addConsumer(ImageConsumer ic) {
          throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isConsumer(ImageConsumer ic) {
          throw new UnsupportedOperationException("Not supported yet.");
        }

        public void removeConsumer(ImageConsumer ic) {
          throw new UnsupportedOperationException("Not supported yet.");
        }

        public void startProduction(ImageConsumer ic) {
          throw new UnsupportedOperationException("Not supported yet.");
        }

        public void requestTopDownLeftRightResend(ImageConsumer ic) {
          throw new UnsupportedOperationException("Not supported yet.");
        }
      };
    }

    public Graphics getGraphics() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getProperty(String name, ImageObserver observer) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
  }
}
