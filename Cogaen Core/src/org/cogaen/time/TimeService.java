/* 
 -----------------------------------------------------------------------------
                    Cogaen - Component-based Game Engine V3
 -----------------------------------------------------------------------------
 This software is developed by the Cogaen Development Team. Please have a 
 look at our project home page for further details: http://www.cogaen.org
    
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (c) Roman Divotkey, 2010-2011

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */

package org.cogaen.time;

import java.util.HashMap;
import java.util.Map;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.Updateable;
import org.cogaen.logging.LoggingService;
import org.cogaen.name.CogaenId;

public class TimeService extends AbstractService implements Updateable {

	public static final CogaenId ID = new CogaenId("org.cogaen.time.TimeService");
	public static final String NAME = "Cogaen Time Service";
	private static final String LOGGING_SOURCE = "TIME";
	public static final CogaenId DEFAULT_TIMER_ID = new CogaenId("Default Timer");
	
	private Map<CogaenId, Timer> timers = new HashMap<CogaenId, Timer>();
	
	public TimeService() {
		addDependency(LoggingService.ID);
	}
	
	public Timer createTimer(CogaenId timerId) {
		Timer newTimer = new Timer();
		Timer oldTimer = this.timers.put(timerId, newTimer);
		if (oldTimer != null) {
			this.timers.put(timerId, oldTimer);
			throw new RuntimeException("timer with id " + timerId + " already exist");
		}

		LoggingService.getInstance(getCore()).logInfo(LOGGING_SOURCE, "new timer created " + timerId);

		return newTimer;
	}
	
	public boolean hasTimer(CogaenId timerId) {
		return this.timers.containsKey(timerId);
	}

	public Timer getTimer(CogaenId timerId) {
		Timer timer = this.timers.get(timerId);
		if (timer == null) {
			throw new RuntimeException("unknown timer " + timerId);
		}
		
		return timer;
	}
	
	public Timer getTimer() {
		return getTimer(DEFAULT_TIMER_ID);
	}
	
	@Override
	public CogaenId getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	public static TimeService getInstance(Core core) {
		return (TimeService) core.getService(ID);
	}

	@Override
	public void update() {
		for (Timer timer : this.timers.values()) {
			timer.update(getCore().getDeltaTime());
		}
	}

	@Override
	protected void doPause() {
		getCore().removeUpdateable(this);
	}

	@Override
	protected void doResume() {
		getCore().addUpdateable(this);
	}

	@Override
	protected void doStart() {
		getCore().addUpdateable(this);
	}

	@Override
	protected void doStop() {
		if (getStatus() != Status.PAUSED) {
			getCore().removeUpdateable(this);
		}
	}

}
