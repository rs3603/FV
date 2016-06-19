//Copyright (C) 2010 United States Government as represented by the
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

package gov.nasa.jpf.awt;

import gov.nasa.jpf.annotation.MJI;
import java.util.Locale;

import javax.swing.UIManager;

import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.NativePeer;

public class JPF_javax_swing_UIManager extends NativePeer {

  @MJI
  public int getString0__Ljava_lang_String_2Ljava_util_Locale_2__Ljava_lang_String_2 (MJIEnv env, int clsObjRef, int keyRef, int locRef){
    // <2do> we only support strings as keys, and no locales yet
    String key = env.getStringObject(keyRef);
    
    String s = UIManager.getString(key, Locale.getDefault());
    
    return env.newString(s);
  }
}
