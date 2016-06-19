//
// Copyright (C) 2014 United States Government as represented by the
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

package gov.nasa.jpf.awt;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.util.JPFLogger;
import gov.nasa.jpf.vm.ApplicationContext;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.MJIEnv;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * a registry to look up component refs by name
 * This needs to be outside peers since we use it in UIAction checks
 */
public class ComponentRegistry {
  
  final static char ORDINAL_PREFIX = '#';
  final static char NAME_PREFIX = '$';
  
  static JPFLogger log = JPF.getLogger("awt");
  
  static class ComponentEntry {
    String id;
    int componentRef;
    int toplevelRef;

    ComponentEntry (String id, int toplevelRef, int componentRef){
      this.id = id;
      this.componentRef = componentRef;
      this.toplevelRef = toplevelRef;
    }
  }
  
  static HashMap<ApplicationContext,ComponentRegistry> registries = new HashMap<ApplicationContext,ComponentRegistry>();

  protected HashMap<String,ComponentEntry> componentMap = new HashMap<String,ComponentEntry>();

  public static ComponentRegistry createNewRegistry (MJIEnv env){
    ComponentRegistry reg = new ComponentRegistry();
    registries.put( env.getApplicationContext(), reg);
    return reg;
  }
  
  public static ComponentRegistry getRegistry (MJIEnv env){
    return registries.get( env.getApplicationContext());
  }
  
  public int getComponentRef (String id) {
    ComponentEntry e = componentMap.get(id);
    if (e != null){
      int cref =  e.componentRef;
      return cref;
    }

    return MJIEnv.NULL;
  }

  
  protected String getId (MJIEnv env, int objref) {
    String id = null;

    if (env.isInstanceOf(objref, "java.awt.Component")) {

      // 'name' field has precedence
      id = env.getStringField(objref, "name");

      // if we don't have a name, try to deduce it from type specific fields
      if (id == null) {

        // <2do> we need to support a lot more
        if (env.isInstanceOf(objref, "javax.swing.JFrame")) {
          id = env.getStringField(objref, "title");

        } else if (env.isInstanceOf(objref, "javax.swing.JLabel")) {
          id = env.getStringField(objref, "text");

        } else if (env.isInstanceOf(objref, "javax.swing.AbstractButton")) {
          id = env.getStringField(objref, "text");

        } else if (env.isInstanceOf(objref, "javax.swing.text.JTextComponent")) {
          int lblRef = env.getReferenceField(objref, "labeledBy");
          if (lblRef != MJIEnv.NULL) {
            id = env.getStringField(lblRef, "text");
            id += ":input";
          } else {
            id = "input";
          }

        } else if (env.isInstanceOf(objref, "javax.swing.JList")) {
          id = "list";

        } else if (env.isInstanceOf(objref, "javax.swing.JTable")) {
          id = "table";

        } else if (env.isInstanceOf(objref, "javax.swing.JComponent")) {

          // <2do> we might check this for JLists and JTables too
          int borderRef = env.getReferenceField(objref, "border");
          if (borderRef != MJIEnv.NULL
              && env.isInstanceOf(borderRef, "javax.swing.border.TitledBorder")) {
            id = env.getStringField(borderRef, "title");
          }
        }
      }

      if (id != null) {
        id = id.replace(' ', '_');
      }
    }

    return id;
  }
  
  
  public void storeComponentIds (MJIEnv env, int topref, int objref){

    String id = getId( env, objref);
    if (id != null){
      storeComponentId(env, id, topref, objref, NAME_PREFIX);

      int parentRef = env.getReferenceField(objref, "parent");
      while (parentRef != MJIEnv.NULL){
        String pid = getId( env, parentRef);
        if (pid != null){
          id = pid + ':' + id;
          storeComponentId(env, id, topref, objref, NAME_PREFIX);
        }
        parentRef = env.getReferenceField(parentRef, "parent");
      }
    }
  }
  
  protected void storeComponentId (MJIEnv env, String id, int topref, int objref, char prefix ){
    id = prefix + id;
    id = id.replace(' ','_'  );

    if (log.isLoggable(Level.FINE)) {
      log.fine("mapping component: " + id + " => " + env.getElementInfo(objref));
    }

    ComponentEntry e = new ComponentEntry(id, topref, objref);
    componentMap.put(id, e);
  }

  public void updateComponents (MJIEnv env, int topref, int objref, int level, int index) {
    assert env.isInstanceOf(objref, "java.awt.Component");

    ClassInfo ci = env.getClassInfo(objref);
    String id = Integer.toString(componentMap.size());
    storeComponentId(env, id, topref, objref, ORDINAL_PREFIX);

    storeComponentIds( env, topref, objref);

    if (ci.isInstanceOf("java.awt.Container")) {
      int aref = env.getReferenceField(objref, "component");
      if (aref != MJIEnv.NULL) {
        int len = env.getArrayLength(aref);
        for (int i=0; i<len; i++) {
          int cref = env.getReferenceArrayElement(aref, i);
          updateComponents(env, topref, cref, level+1, i);
        }
      }
    }
  }

  public void removeTopLevelComponent (MJIEnv env, int objRef){
    for (ComponentEntry e : componentMap.values()){
      if (e.toplevelRef == objRef){
        componentMap.remove(e.id);
      }
    }
  }
  
  public boolean containsKey (String key){
    return componentMap.containsKey(key);
  }
}
