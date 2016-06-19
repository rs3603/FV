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
package gov.nasa.jpf.test.java.awt;

import gov.nasa.jpf.awt.UIActionTree;
import gov.nasa.jpf.util.test.TestJPF;
import java.awt.AWTEvent;
import java.awt.Button;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.junit.Test;

/**
 * here, we will only work on focus testing
 */
public class ComponentTest extends TestJPF {

  public static class EmptyEventTree extends UIActionTree {
    // nothing here, empty
  }
  
  private final static String[] JPF_ARGS = {"+log.info=event", "+event.class=.test.java.awt.ComponentTest$EmptyEventTree"};

  public static void main(String[] args) {
    runTestsOfThisClass(args);
  }

  @Test
  public void testAddFocusListener() {
    if (verifyNoPropertyViolation(JPF_ARGS)) {
      Component c = new Button();
      FocusListener mock1 = new MockFocusListener();
      FocusListener mock2 = new MockFocusListener();
      c.addFocusListener(mock1);
      c.addFocusListener(mock2);
      FocusListener[] focusListeners = c.getFocusListeners();
      assertEquals(focusListeners.length, 2);
      assertEquals(focusListeners[0], mock1);
      assertEquals(focusListeners[1], mock2);
    }
  }

  @Test
  public void testRemoveFocusListener() {
    if (verifyNoPropertyViolation(JPF_ARGS)) {
      Component c = new Button();
      FocusListener mock1 = new MockFocusListener();
      FocusListener mock2 = new MockFocusListener();
      c.addFocusListener(mock1);
      c.addFocusListener(mock2);
      c.removeFocusListener(mock1);
      FocusListener[] focusListeners = c.getFocusListeners();
      assertEquals(focusListeners.length, 1);
      assertEquals(focusListeners[0], mock2);
    }
  }

  @Test
  public void testHasFocusAndIsFocusOwner() {
    if (verifyNoPropertyViolation(JPF_ARGS)) {
      Component button = new Button();
      assertFalse(button.hasFocus());
      assertEquals(button.hasFocus(), button.isFocusOwner());
    }
  }

  @Test
  public void testFocusGained() throws InterruptedException {
    if (verifyNoPropertyViolation(JPF_ARGS)) {
      final Component button = new Button();
      final AtomicBoolean focusGained = new AtomicBoolean(false);
      button.addFocusListener(new FocusListener() {

        public void focusGained(FocusEvent e) {
          assertTrue(button.hasFocus());
          assertEquals(button.hasFocus(), button.isFocusOwner());
          focusGained.set(true);
        }

        public void focusLost(FocusEvent e) {
          fail();
        }
      });
      JFrame frame = new JFrame();
      frame.add(button);
      frame.setVisible(true);

      assertTrue(focusGained.get());
    }
  }

  @Test
  public void testIsFocusable() {
    if (verifyNoPropertyViolation(JPF_ARGS)) {
      assertTrue(new Button().isFocusable());
    }
  }

  @Test
  public void testFocusLost() throws InterruptedException {
    if (verifyNoPropertyViolation(JPF_ARGS)) {
      final Component button = new Button();
      final AtomicInteger focus = new AtomicInteger(0);
      button.addFocusListener(new FocusListener() {

        public void focusGained(FocusEvent e) {
          focus.incrementAndGet();
        }

        public void focusLost(FocusEvent e) {
          focus.incrementAndGet();
        }
      });
      JFrame frame = new JFrame();
      frame.add(button);
      frame.setVisible(true);

      assertTrue(button.hasFocus());
      button.setVisible(false);
      assertFalse(button.hasFocus());
      assertEquals(2, focus.get());
    }
  }

  @Test
  public void testFocusLostIndirect() throws InterruptedException {
    if (verifyNoPropertyViolation(JPF_ARGS)) {
      final Component button = new Button();
      final AtomicInteger focus = new AtomicInteger(0);
      button.addFocusListener(new FocusListener() {

        public void focusGained(FocusEvent e) {
          focus.incrementAndGet();
        }

        public void focusLost(FocusEvent e) {
          focus.incrementAndGet();
        }
      });
      JFrame frame = new JFrame();
      frame.add(button);
      frame.setVisible(true);

      assertTrue(button.hasFocus());
      frame.setVisible(false);
      assertFalse(button.hasFocus());
      assertEquals(2, focus.get());
    }
  }

  @Test
  public void testRequestFocus() throws InterruptedException {
    if (verifyNoPropertyViolation(JPF_ARGS)) {
      final Component button = new Button();
      final AtomicInteger focus = new AtomicInteger(0);
      button.addFocusListener(new FocusListener() {

        public void focusGained(FocusEvent e) {
          focus.incrementAndGet();
        }

        public void focusLost(FocusEvent e) {
        }
      });
      button.setVisible(false);
      JFrame frame = new JFrame();
      frame.add(button);
      frame.setVisible(true);

      assertFalse(button.hasFocus());
      button.setVisible(true);
      assertFalse(button.hasFocus());
      button.requestFocus();
      assertTrue(button.hasFocus());
      assertEquals(1, focus.get());
    }
  }

  @Test
  public void testRequestFocus2() throws InterruptedException {
    if (verifyNoPropertyViolation(JPF_ARGS)) {
      final Component button = new Button();
      final AtomicInteger focus = new AtomicInteger(0);
      button.addFocusListener(new FocusListener() {

        public void focusGained(FocusEvent e) {
          focus.incrementAndGet();
        }

        public void focusLost(FocusEvent e) {
          focus.incrementAndGet();
        }
      });
      JFrame frame = new JFrame();
      frame.add(button);
      frame.setVisible(true);

      assertTrue(button.hasFocus());
      button.requestFocus();button.requestFocus();button.requestFocus();
      assertTrue(button.hasFocus());
      assertEquals(1, focus.get());
    }
  }

  @Test
  public void testAddMouseListener() {
    if (verifyNoPropertyViolation(JPF_ARGS)) {
      Component c = new Button();
      MouseListener mock1 = new MockMouseListener();
      MouseListener mock2 = new MockMouseListener();
      c.addMouseListener(mock1);
      c.addMouseListener(mock2);
      MouseListener[] mouseListeners = c.getMouseListeners();
      assertEquals(mouseListeners.length, 2);
      assertEquals(mouseListeners[0], mock1);
      assertEquals(mouseListeners[1], mock2);
    }
  }

  @Test
  public void testRemoveMouseListener() {
    if (verifyNoPropertyViolation(JPF_ARGS)) {
      Component c = new Button();
      MouseListener mock1 = new MockMouseListener();
      MouseListener mock2 = new MockMouseListener();
      c.addMouseListener(mock1);
      c.addMouseListener(mock2);
      c.removeMouseListener(mock1);
      MouseListener[] mouseListeners = c.getMouseListeners();
      assertEquals(mouseListeners.length, 1);
      assertEquals(mouseListeners[0], mock2);
    }
  }

  @Test
  public void testMouseClicked() {
    if (verifyNoPropertyViolation(JPF_ARGS)) {
      JLabelTestHelper label = new JLabelTestHelper();
      final AtomicBoolean mouseClicked = new AtomicBoolean();
      final AtomicBoolean mousePressed = new AtomicBoolean();
      final AtomicBoolean mouseReleased = new AtomicBoolean();
      final AtomicBoolean mouseEntered = new AtomicBoolean();
      final AtomicBoolean mouseExited = new AtomicBoolean();
      label.addMouseListener(new MouseListener() {

        public void mouseClicked(MouseEvent e) {
          assertTrue(mouseEntered.get());
          assertTrue(mousePressed.get());
          assertTrue(mouseReleased.get());
          assertFalse(mouseClicked.get());
          assertFalse(mouseExited.get());
          mouseClicked.set(true);
        }

        public void mousePressed(MouseEvent e) {
          assertTrue(mouseEntered.get());
          assertFalse(mousePressed.get());
          assertFalse(mouseReleased.get());
          assertFalse(mouseClicked.get());
          assertFalse(mouseExited.get());
          mousePressed.set(true);
        }

        public void mouseReleased(MouseEvent e) {
          assertTrue(mouseEntered.get());
          assertTrue(mousePressed.get());
          assertFalse(mouseReleased.get());
          assertFalse(mouseClicked.get());
          assertFalse(mouseExited.get());
          mouseReleased.set(true);
        }

        public void mouseEntered(MouseEvent e) {
          assertFalse(mouseEntered.get());
          assertFalse(mousePressed.get());
          assertFalse(mouseReleased.get());
          assertFalse(mouseClicked.get());
          assertFalse(mouseExited.get());
          mouseEntered.set(true);
        }

        public void mouseExited(MouseEvent e) {
          assertTrue(mouseEntered.get());
          assertTrue(mousePressed.get());
          assertTrue(mouseReleased.get());
          assertTrue(mouseClicked.get());
          assertFalse(mouseExited.get());
          mouseExited.set(true);
        }
      });
      assertFalse(mouseEntered.get());
      assertFalse(mousePressed.get());
      assertFalse(mouseReleased.get());
      assertFalse(mouseClicked.get());
      assertFalse(mouseExited.get());

      label.processEvent(new MouseEvent(label, MouseEvent.MOUSE_ENTERED, 0, 0, 0, 0, 0, false,0));
      label.processEvent(new MouseEvent(label, MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 0, false,MouseEvent.BUTTON1));
      label.processEvent(new MouseEvent(label, MouseEvent.MOUSE_RELEASED, 0, 0, 0, 0, 0, false,MouseEvent.BUTTON1));
      label.processEvent(new MouseEvent(label, MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, 0, false,MouseEvent.BUTTON1));
      label.processEvent(new MouseEvent(label, MouseEvent.MOUSE_EXITED, 0, 0, 0, 0, 0, false,0));

      assertTrue(mouseEntered.get());
      assertTrue(mousePressed.get());
      assertTrue(mouseReleased.get());
      assertTrue(mouseClicked.get());
      assertTrue(mouseExited.get());
    }
  }

  class JLabelTestHelper extends JLabel {
    public void processEvent(AWTEvent e) {
      super.processEvent(e);
    }
  }

  class MockMouseListener implements MouseListener {

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
  }

  class MockFocusListener implements FocusListener {

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
    }
  }
}
