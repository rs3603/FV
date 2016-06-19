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

import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

/**
 * we don't model rendering, so this is just here to intercept all rendering
 * calls
 */
public abstract class Graphics {
    
  protected Graphics () {
    // nothing yet
  }
  public Object clone () {
    try {
      return super.clone();
    } catch (CloneNotSupportedException cnsx){
      return null;
    }
  }
  public Graphics create(){
    return (Graphics)clone();
  }
  public Graphics create(int x, int y, int w, int h) {
    return (Graphics)clone();
  }
  
  public void translate(int dx, int dy) {}
  
  public Color getColor() { return Color.WHITE; }
  public void setColor (Color c) {}
  public void setPaintMode() {}
  public void setXORMode(Color c) {}
  public Font getFont() { return null;}
  public void setFont(Font f) {}
  public FontMetrics getFontMetrics() { return null;}
  public FontMetrics getFontMetrics(Font f) { return null;}
  public Rectangle getClipBounds() { return new Rectangle(0,0,0,0); }
  public void clipRect(int x, int y, int w, int h) {}
  public void setClip(int x, int y, int w, int h) {}
  public Shape getClip() { return null; }
  public void setClip(Shape s) {}
  public void copyArea(int x, int y, int w, int h, int dx, int dy) {}
  public void drawLine(int x0, int y0, int x1, int y1) {}
  public void fillRect(int x, int y, int w, int h) {}
  public void drawRect(int x, int y, int w, int h) {}
  public void clearRect(int x, int y, int w, int h) {}
  public void drawRoundRect(int x, int y, int w, int h, int arcw, int arch) {}
  public void fillRoundRect(int x, int y, int w, int h, int arcw, int arch) {}
  public void draw3DRect(int x, int y, int w, int h, boolean raised) {}
  public void fill3DRect(int x, int y, int w, int h, boolean raised) {}
  public void drawOval(int x, int y, int w, int h) {}
  public void fillOval(int x, int y, int w, int h) {}
  public void drawArc(int x, int y, int w, int h, int startAngle, int endAngle) {}
  public void fillArc(int x, int y, int w, int h, int startAngle, int endAngle) {}
  public void drawPolyline(int[] x, int[] y, int nPoints) {}
  public void drawPolygon(int[] x, int[] y, int nPoints) {}
  public void drawPolygon(Polygon p) {}
  public void fillPolygon(int[] x, int[] y, int nPoints) {}
  public void fillPolygon(Polygon p) {}
  public void drawString(String s, int x, int y) {}
  public void drawString(AttributedCharacterIterator ac, int x, int y) {}
  public void drawChars(char[] data, int off, int len, int x, int y) {}
  public void drawBytes(byte[] data, int off, int len, int x, int y) {}
  public boolean drawImage(Image img, int x, int y, ImageObserver obs) { return true;}
  public boolean drawImage(Image img, int x, int y, int w, int h, ImageObserver obs) {return true;}
  public boolean drawImage(Image img, int x, int y, Color c, ImageObserver obs) {return true;}
  public boolean drawImage(Image img, int x, int y, int w, int h, Color c, ImageObserver obs) {return true;}
  public boolean drawImage(Image img, int x, int y, int w, int h, int dx, int dy, int dw, int dh, ImageObserver obs) {return true;}
  public boolean drawImage(Image img, int x, int y, int w, int h, int dx, int dy, int dw, int dh, Color c, ImageObserver obs) {return true;}
  public void dispose() {}
  public void finalize() {}
  public Rectangle getClipRect() { return new Rectangle();}
  public boolean hitClip(int x, int y, int w, int h) { return false; }
  public Rectangle getClipBounds(Rectangle r) { return r; }
}
