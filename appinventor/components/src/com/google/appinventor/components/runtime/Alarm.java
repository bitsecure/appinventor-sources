// -*- mode: java; c-basic-offset: 2; -*-

/*
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;

/**
 * Use AlarmManager to wake up the app after a specified interval.
 *
 * @author appliedautism@gmail.com (Mark D. Adams)
 */
@DesignerComponent(version = YaVersion.ALARM_COMPONENT_VERSION,
    category = ComponentCategory.BASIC,
    description = "<p>A Non-visible component that can launch your app after a delay.</p>" +
    "<p>The <code>Delay</code> can be set in either the Designer or in the Blocks Editor.</p>" +
    "<p>The <code>Delay</code> is measured in milliseconds and must be positive.</p>",
    nonVisible = true,
    iconName = "images/alarm.png")
@SimpleObject
public class Alarm extends AndroidNonvisibleComponent implements Component, Deleteable {
  private static final long DEFAULT_DELAY = 300000;  // ms
  private long Delay;
  private static Activity activity;
  private static Intent newIntent = null;
  private static PendingIntent pendingintent;
  private static AlarmManager newAlarm;

  /**
   * Creates a new Alarm component.
   *
   * @param container the Form that this component is contained in.
   */
  public Alarm(ComponentContainer container) {
    super(container.$form());
    activity = container.$context();
    final String classname = activity.getPackageName() + ".Screen1";
    
    try {
      newIntent = new Intent(activity, Class.forName(classname));
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    newIntent.setAction(Intent.ACTION_MAIN);
    newIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    pendingintent = PendingIntent.getActivity(activity, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    
    newAlarm = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
  }
  
  /**
   * Delay getter method.
   *
   * @return delay in milliseconds
   */
  @SimpleProperty(
      category = PropertyCategory.BEHAVIOR)
  public long Delay() {
    return Delay;
  }

  /**
   * Delay property setter method: sets the delay before alarm activates
   *
   * @param delay in milliseconds
   */
  @DesignerProperty(
      editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
      defaultValue = DEFAULT_DELAY + "")
  @SimpleProperty
  public void Delay(long delay) {
    Delay = delay;
  }
  
  /**
   * Set alarm
   */
  @SimpleFunction (
      description = "Sets the alarm")
  public void SetAlarm() {
    newAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + Delay, pendingintent);
  }  

  /**
   * Cancel any alarms for this activity
   */
  @SimpleFunction (
      description = "Cancels any alarms")
  public void CancelAlarm() {   
    newAlarm.cancel(pendingintent);
  }
  
//Deleteable implementation

  @Override
  public void onDelete() {
    CancelAlarm();
  }

}
