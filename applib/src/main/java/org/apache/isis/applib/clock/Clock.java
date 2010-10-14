/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


package org.apache.isis.applib.clock;

import java.util.Calendar;

import org.apache.isis.applib.ApplicationException;
import org.apache.isis.applib.fixtures.FixtureClock;


/**
 * Provides a mechanism to get (and possible to set) the current time.
 * 
 * <p>
 * The clock is used primarily by the temporal value classes, and is accessed by the NOF as a singleton. The
 * actual implementation used can be configured at startup, but once specified the clock instance cannot be
 * changed.
 * 
 * <p>
 * Unless another {@link Clock} implementation has been installed, the first call to {@link #getInstance()}
 * will instantiate an implementation that just uses the system's own clock. Alternate implementations can be 
 * created via suitable subclasses, but this must be done <b><i>before</i></b> the first call to {@link #getInstance()}.
 * See for example {@link FixtureClock#getInstance()}.
 */
public abstract class Clock {
    private static Clock instance;
    private static boolean isReplaceable = true;

    /**
     * Returns the (singleton) instance of {@link Clock}.
     * 
     * <p>
     * Unless it has been otherwise created, will lazily instantiate an
     * implementation that just delegate to the computer's own system clock
     * (as per {@link System#currentTimeMillis()}.
     * 
     * @return
     */
    public final static Clock getInstance() {
        if (!isInitialized()) {
            instance = new SystemClock();
            isReplaceable = false;
        }
        return instance;
    }

    /**
     * Whether has been initialized or not.
     */
	public static boolean isInitialized() {
		return instance != null;
	}

    public static long getTime() {
        return getInstance().time();
    }

    public static Calendar getTimeAsCalendar() {
        return getInstance().timeAsCalendar();
    }

	private static void ensureReplaceable() {
		if (!isReplaceable && instance != null) {
            throw new ApplicationException("Clock already set up");
        }
	}

	/**
	 * Allows subclasses to remove their implementation.
	 * 
	 * @return whether a clock was removed.
	 */
    protected static boolean remove()  {
        ensureReplaceable();
        if(instance == null) {
        	return false;
        }
        instance = null;
        return true;
    }

    protected Clock() {
        ensureReplaceable();
        instance = this;
    }

	public final Calendar timeAsCalendar() {
		Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Clock.getTime());
		return cal;
	}

    /**
     * The current time since midnight, January 1, 1970 UTC.
     * 
     * <p>
     * Measured in milliseconds, modeled after (and possibly implemented by)
     * {@link System#currentTimeMillis()}.
     */
    protected abstract long time();


}

final class SystemClock extends Clock {
    @Override
    protected long time() {
        return System.currentTimeMillis();
    }
}
