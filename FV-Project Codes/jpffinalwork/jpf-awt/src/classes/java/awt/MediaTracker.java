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

public class MediaTracker implements java.io.Serializable {

  Component target;
  MediaEntry head;
  private static final long serialVersionUID = -483174189758638095L;

  public MediaTracker(Component comp) {
    target = comp;
  }

  public void addImage(Image image, int id) {
    addImage(image, id, -1, -1);
  }

  public synchronized void addImage(Image image, int id, int w, int h) {
  }

  public static final int LOADING = 1;
  public static final int ABORTED = 2;
  public static final int ERRORED = 4;
  public static final int COMPLETE = 8;

  public boolean checkAll() {
    return checkAll(false, true);
  }

  public boolean checkAll(boolean load) {
    return checkAll(load, true);
  }

  private boolean checkAll(boolean load, boolean verify) {
    return true;
  }

  public synchronized boolean isErrorAny() {
    return false;
  }

  public Object[] getErrorsAny() {
    return null;
  }

  public void waitForAll() throws InterruptedException {
    waitForAll(0);
  }

  public boolean waitForAll(long ms) throws InterruptedException {
    return true;
  }

  public int statusAll(boolean load) {
    return statusAll(load, true);
  }

  private int statusAll(boolean load, boolean verify) {
    return COMPLETE;
  }

  public boolean checkID(int id) {
    return checkID(id, false, true);
  }

  public boolean checkID(int id, boolean load) {
    return checkID(id, load, true);
  }

  private boolean checkID(int id, boolean load, boolean verify) {
    return true;
  }

  public boolean isErrorID(int id) {
    return false;
  }

  public synchronized Object[] getErrorsID(int id) {
    return null;
  }

  public void waitForID(int id) throws InterruptedException {
    waitForID(id, 0);
  }

  public boolean waitForID(int id, long ms) throws InterruptedException {
    return true;
  }

  public int statusID(int id, boolean load) {
    return statusID(id, load, true);
  }

  private int statusID(int id, boolean load, boolean verify) {
    return COMPLETE;
  }

  public void removeImage(Image image) {
  }

  public void removeImage(Image image, int id) {
  }

  public synchronized void removeImage(Image image, int id, int width, int height) {
  }
}
