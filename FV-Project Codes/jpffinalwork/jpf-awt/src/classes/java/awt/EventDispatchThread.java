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

class EventDispatchThread extends Thread {

  EventQueue queue;
  
  // if this is updated (from within processScriptAction), we will not
  // prune the search until all actions have been processed
  int forceNewState;
  Thread mainThread;
  
  EventDispatchThread (final EventQueue queue) {
    mainThread = Thread.currentThread();
    
    this.queue = queue;
    setDaemon(false);
    setPriority(NORM_PRIORITY + 1);
    setName("AWT-EventQueue-0");
  }
  
  public void run () {
    AWTEvent e;
        
    // avoid anything here that might cause context switches before this thread
    // either gets waiting or dispatches pending messages - this would cause
    // a state explosion
    
    do {
      try {
        mainThread.join();
      } catch (InterruptedException ix) {}
    } while (mainThread.isAlive());
        
    // once the mainThread is gone, we start to process the script actions,
    // but pending events take precedence
    do {
      while ((e = queue.getNextEventNonBlocked()) != null) {
        queue.dispatchEvent(e);
      }
    
      // we stop when there are no more script actions to process. This might
      // be premature for some applications that do continuous background
      // processing, but we are mostly interested in user input here, so we
      // need to make this terminate
    } while (processNextEvent());
    
  }

  void pumpEvents(Conditional cond) {  
    //System.out.println("------EventDispatchThread: pumpEvents deque: " + cond);
    AWTEvent e;
    do{
      while ( (e = queue.getNextEventNonBlocked()) != null)  {          
      
        queue.dispatchEvent(e);

        if ( cond.evaluate() ){
          return;
        }
      
      }

      if ( cond.evaluate() ){
        return;
      }
    
    } while (processNextEvent());
  }
   
  //--- the native methods
  
  // this is where the application comes to life - our action script
  // composed of clicks, selects etc.
  native boolean processNextEvent ();
  
}
