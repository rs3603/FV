//
// Copyright (C) 2010 United States Government as represented by the
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

/**
 * rudimentary modeled Dialog class
 */
public class Dialog extends Window {
  
  public static enum ModalityType {
    MODELESS,
    DOCUMENT_MODAL,
    APPLICATION_MODAL,
    TOOLKIT_MODAL
  };

  public final static ModalityType DEFAULT_MODALITY_TYPE = ModalityType.APPLICATION_MODAL;
  
  public static enum ModalExclusionType {
    NO_EXCLUDE,
    APPLICATION_EXCLUDE,
    TOOLKIT_EXCLUDE
  };

  
  protected String title;
  
  // we should model this one
  protected ModalityType mod;
  
  protected boolean isResizable;
  
  //modal dialog blocks all other open windows
  protected boolean modal;
  
  private transient boolean keepBlocking = false;

  public Dialog (Frame owner){
    this(owner, "", false);
  }
  
  public Dialog (Frame owner, boolean modal){
    this(owner, "", modal);
  }
  
  public Dialog (Frame owner, String title){
    this(owner, title, false);  
  }
  
  public Dialog (Frame owner, String title, boolean modal){
    this(owner, title, modal ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
  }
  
  public Dialog (Frame owner, String title, boolean modal, GraphicsConfiguration gc){
    this(owner, title, modal ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS, gc);
  }
  
  public Dialog (Dialog owner){ 
    this(owner, "", false);
  }
  
  public Dialog (Dialog owner, String title){ 
    this(owner, title, false);
  }
  
  public Dialog (Dialog owner, String title, boolean modal){
    this(owner, title, modal ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
  }
  
  public Dialog (Dialog owner, String title, boolean modal, GraphicsConfiguration gc){
    this(owner, title, modal ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS, gc);  
  }
  
  public Dialog (Window owner){
    this(owner, null, ModalityType.MODELESS);  
  }
  
  public Dialog (Window owner, String title){
    this(owner, title, ModalityType.MODELESS);
  }
  
  public Dialog (Window owner, ModalityType modalityType){
    this(owner, null, modalityType);
  }
  
  public Dialog (Window owner, String title, ModalityType modalityType){
      
      super(owner);

      if ((owner != null) &&
          !(owner instanceof Frame) &&
          !(owner instanceof Dialog))
      {
          throw new IllegalArgumentException("Wrong parent window");
      }

      this.title = title;
      setModalityType(modalityType);
  }
  
  public Dialog (Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc){
      super(owner, gc);

      if ((owner != null) &&
          !(owner instanceof Frame) &&
          !(owner instanceof Dialog))
      {
          throw new IllegalArgumentException("wrong owner window");
      }

      this.title = title;
      setModalityType(modalityType);  
  }

  public void show(){
   
    if(!isModal()){
        
    }
    else{
      modalDialogs.add(this);
      numModalDialogs++;
       
      keepBlocking = true;
      //start a new loop until the dialog is dismissed 
      EventDispatchThread dispatchThread = (EventDispatchThread)Thread.currentThread();
      dispatchThread.pumpEvents(new Conditional() {
        public boolean evaluate() {
          return keepBlocking;
        }
      });           
    }  
  }  
  
  public void setVisible(boolean b) {
    //System.out.println("------Dialog: setVisible dialog with modality: "+ isModal() +", title: "+title);
    if(modal){
      modalDialogs.add(this);
      numModalDialogs++;
    }
    super.setVisible(b);
  }  

  public void dispose () {
    //System.out.println("------Dialog: dispose dialog with modality: "+ isModal() +", title: "+title);
    keepBlocking = false;
    
    if(modal){
        modalDialogs.remove(this);
        numModalDialogs--;
    }
    super.dispose();

}  
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle (String title){
    this.title = title;
  }
  
  public boolean isModal(){
    return mod != ModalityType.MODELESS;
  }
  
  public void setModal (boolean isModal) {
    setModalityType( isModal ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
  }
  
  public ModalityType getModalityType() {
    return mod;
  }
  
  public void setModalityType (ModalityType mod){
    this.mod = mod;
  }

  public void setResizable (boolean isResizable){
    this.isResizable = isResizable;
  }
  
  public boolean isResizable() {
    return isResizable;
  }
}
