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

import java.awt.image.BufferedImage;
import java.util.Locale;

/**
 * that's just stubbed out
 */
public abstract class GraphicsEnvironment {

  static GraphicsEnvironment singleton;

  public static synchronized GraphicsEnvironment getLocalGraphicsEnvironment() {
    if (singleton == null) {
      singleton = new JPFGraphicsEnvironment();
    }
    return singleton;
  }
  
  public static boolean isHeadless() {
    return getLocalGraphicsEnvironment().isHeadlessInstance();
  }

  static String getHeadlessMessage() {
    return JPFGraphicsEnvironment.getHeadlessMessage();
  }
  
  static void checkHeadless() throws HeadlessException {
    // nothing
  }
  
  public boolean isHeadlessInstance() {
    return true;
  }
  public abstract GraphicsDevice[] getScreenDevices() throws HeadlessException;
  public abstract GraphicsDevice getDefaultScreenDevice() throws HeadlessException;
  public abstract Graphics2D createGraphics (BufferedImage img);
  public abstract Font[] getAllFonts();
  public abstract String[] getAvailableFontFamilyNames();
  public abstract String[] getAvailableFontFamilyNames(Locale loc);
  
  public void preferLocaleFonts() {}
  public void preferProportionalFonts() {}
  public Point getCenterPoint() throws HeadlessException {
    return null;
  }
  public Rectangle getMaximumWindowBounds() throws HeadlessException {
    return null;
  }

}

class JPFGraphicsEnvironment extends GraphicsEnvironment {

  static String getHeadlessMessage () {
    return "FortyTwo";
  }
  
  public GraphicsDevice[] getScreenDevices() throws HeadlessException {
    throw new HeadlessException();
  }
  
  public GraphicsDevice getDefaultScreenDevice() throws HeadlessException {
    throw new HeadlessException();
  }

  public boolean isHeadlessInstance() {
    return true;
  }
  
  public Graphics2D createGraphics (BufferedImage img) {
    return null;
  }

  public Font[] getAllFonts () {
    return null;
  }

  public String[] getAvailableFontFamilyNames () {
    return null;
  }

  public String[] getAvailableFontFamilyNames (Locale loc) {
    return null;
  }

}