/* 
 -----------------------------------------------------------------------------
                    Cogaen - Component-based Game Engine V3
 -----------------------------------------------------------------------------
 This software is developed by the Cogaen Development Team. Please have a 
 look at our project home page for further details: http://www.cogaen.org
    
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (c) 2010-2011 Roman Divotkey

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

package org.cogaen.state;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.action.Action;
import org.cogaen.core.Core;

public class ActionState extends BasicState {

	private List<Action> enterActions = new ArrayList<Action>();
	private List<Action> exitActions = new ArrayList<Action>();

	public ActionState(Core core) {
		super(core);
	}
	
	public void addEnterAction(Action action) {
		this.enterActions.add(action);
	}
	
	public void addExitAction(Action action) {
		this.exitActions.add(action);
	}
	
	public void removeEnterAction(Action action) {
		this.enterActions.remove(action);
	}
	
	public void removeExitAction(Action action) {
		this.exitActions.remove(action);
	}
	
	public boolean hasEnterAction(Action action) {
		return this.enterActions.contains(action);
	}

	public boolean hasExitAction(Action action) {
		return this.exitActions.contains(action);
	}
	
	@Override
	public void onEnter() {
		super.onEnter();
		for (Action action : this.enterActions) {
			action.execute();
		}
	}

	@Override
	public void onExit() {
		for (Action action : this.exitActions) {
			action.execute();
		}
		super.onExit();
	}

}
