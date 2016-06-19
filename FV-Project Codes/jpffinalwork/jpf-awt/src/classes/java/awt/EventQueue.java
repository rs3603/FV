//
// Copyright  (C) 2006 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
//  (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
//  (NOSA), version 1.3.  The NOSA has been approved by the Open Source
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

import java.awt.event.InvocationEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * the real thing does a lot of native stuff, and is actually a wrapper for a bunch
 * of prioritized queues. We keep it overly simple because we only process high level
 * events at this time
 */
public class EventQueue {
  
  private LinkedList<AWTEvent> queue = new LinkedList<AWTEvent>();
  AWTEvent currentEvent;
  Thread dispatchThread;
  
  public EventQueue () {
    // nothing
  }
  
  void initDispatchThread() {
    if (dispatchThread == null){
      dispatchThread = new EventDispatchThread(this);
      dispatchThread.start();
    }
  }
    
  synchronized boolean isEmpty() {
    return (queue.size() == 0);
  }
  
  public synchronized void postEvent (AWTEvent e) {
    initDispatchThread();
    
    queue.add(e);
    if (queue.size() == 1) {
      notifyAll();
    }
  }
  
  /**
   * our internal, non-blocked version
   */
  synchronized AWTEvent getNextEventNonBlocked () {
    if (queue.isEmpty()) {
      return null;
    } else {
      return queue.removeFirst();
    }
  }
  
  public synchronized AWTEvent getNextEvent () {
    while (queue.isEmpty()) {
      try {
        wait();
      } catch (InterruptedException ix) {}
    }
    return queue.removeFirst();
  }
  
  public synchronized AWTEvent peekEvent () {
    if (queue.isEmpty()) {
      return null;
    } else {
      return queue.getFirst();
    }
  }
  
  public synchronized AWTEvent peekEvent (int id) {
    for (AWTEvent e : queue) {
      if (e.getID() == id) {
        return e;
      }
    }
    return null;
  }
  
  protected void dispatchEvent (AWTEvent e) {
    currentEvent = e;
    if (e instanceof ActiveEvent) {
      // don't need a source
      ((ActiveEvent)e).dispatch();
    } else {
      Object src = e.getSource();
      if (src instanceof Component) {
        ((Component)src).dispatchEvent(e);
      } else if (src instanceof MenuComponent) {
        ((MenuComponent)src).dispatchEvent(e);
      }
    }
    currentEvent = null;
  }
  
  public static long getMostRecentEventTime () {
    return 0;
  }
  
  AWTEvent getCurrentEvent0 () {
    if (Thread.currentThread() == dispatchThread) {
      return currentEvent;
    } else {
      return null;
    }
  }
  
  public static AWTEvent getCurrentEvent () {
    return Toolkit.getEventQueue().getCurrentEvent0();
  }
  
  public synchronized void push (EventQueue e) {
    // we don't model queue replacement yet
  }
  
  protected void pop () throws java.util.EmptyStackException {
    // we don't model queue replacement yet
  }
  
  public static boolean isDispatchThread () {
    EventQueue queue = Toolkit.getEventQueue();
    if (queue != null) {
      return (Thread.currentThread() == queue.dispatchThread);
    } else {
      return false;
    }
  }
  
  public static void invokeLater (Runnable r) {
    EventQueue queue = Toolkit.getEventQueue();
    if (queue != null) {
      queue.postEvent( new InvocationEvent( Toolkit.getDefaultToolkit(), r));
    }
  }
  
  public static void invokeAndWait (Runnable r) 
                throws InvocationTargetException, InterruptedException {
    if (isDispatchThread()) {
      throw new Error("invokeAndWait called within event dispatcher");
    }

    Object lock = new Object();
    InvocationEvent e = new InvocationEvent(Toolkit.getDefaultToolkit(), r, lock, true);

    synchronized (lock) {
      Toolkit.getEventQueue().postEvent(e);
      lock.wait();
    }

    // if the event processing threw an exception, we have to re-throw
    Throwable t = e.getThrowable();
    if (t != null) {
      throw new InvocationTargetException(t);
    }
  }

}
