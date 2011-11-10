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

package org.cogaen.lwjgl.sound;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.logging.LoggingService;
import org.cogaen.lwjgl.scene.SceneService;
import org.cogaen.name.CogaenId;
import org.cogaen.resource.ResourceService;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

public class SoundService extends AbstractService {

	public static final CogaenId ID = new CogaenId("org.cogaen.lwjgl.SoundService");	
	public static final String NAME = "Cogaen LWJGL Sound Service";
	private static final double SOUND_GAP = 0.05;
	private static final String LOGGING_SOURCE = "SNDS";

	private List<Source> sources = new ArrayList<Source>();
	private Map<CogaenId, Pool> pools = new HashMap<CogaenId, Pool>();
	private LoggingService logger;
	private Timer timer;
		
	/** Position of the listener. */
	FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Velocity of the listener. */
	FloatBuffer listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
	FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();	
	
	public static SoundService getInstance(Core core) {
		return (SoundService) core.getService(ID);
	}
	
	public SoundService() {
		addDependency(LoggingService.ID);
		addDependency(ResourceService.ID);
		addDependency(SceneService.ID);
	}
	
	@Override
	public CogaenId getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		this.logger = LoggingService.getInstance(getCore());
		this.timer = TimeService.getInstance(getCore()).getTimer();
		try {
			AL.create();
			AL10.alGetError();
		} catch (LWJGLException e) {
			throw new ServiceException(e);
		}

		// initialize listener
	    AL10.alListener(AL10.AL_POSITION,    listenerPos);
	    AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
	    AL10.alListener(AL10.AL_ORIENTATION, listenerOri);		
	    
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			this.logger.logError(LOGGING_SOURCE, "unable to initialize open al listener, error: " + error);
		}		
	}

	@Override
	protected void doStop() {
		AL.destroy();
		this.logger = null;
		super.doStop();
	}

	public Source createSource() {
		IntBuffer buffer = BufferUtils.createIntBuffer(1);
		AL10.alGenSources(buffer);
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			this.logger.logError(LOGGING_SOURCE,
					"unable to create open al source, error: " + error);
		}

		Source source = new Source(this, buffer.get(0));
		this.sources.add(source);
		return source;
	}
			
	public boolean hasPool(CogaenId poolId) {
		return this.pools.containsKey(poolId);
	}
	
	public void createPool(CogaenId poolId) {
		Pool old = this.pools.put(poolId, new Pool(this.timer));
		
		if (old != null) {
			this.pools.put(poolId, old);
			throw new RuntimeException("source pool already exists " + poolId);
		}
	}

	public void addToPool(CogaenId poolId, Source source) {
		Pool pool = this.pools.get(poolId);
		if (pool == null) {
			throw new RuntimeException("unknown source pool " + poolId);			
		}
		
		pool.addSource(source);
	}
	
	public void destroyPool(CogaenId poolId) {
		Pool pool = this.pools.remove(poolId);
		if (pool == null) {
			throw new RuntimeException("unknown source pool " + poolId);
		}
		
		destroyPool(pool);
	}
	
	private void destroyPool(Pool pool) {
		for (Source source : pool.sources) {
			destroyOpenAlSource(source);
		}		
		pool.sources.clear();
	}
	
	public void destroyAllPools() {
		for (Pool pool : this.pools.values()) {
			destroyPool(pool);
		}
		
		this.pools.clear();
	}
	
	public void destroyAllSources() {
		while (this.sources.size() > 0) {
			destroySource(this.sources.remove(this.sources.size() - 1));
		}
	}
	
	public void destroyAll() {
		destroyAllSources();
		destroyAllPools();
	}
	
	public Source getSource(CogaenId poolId) {
		Pool pool = this.pools.get(poolId);
		if (pool == null) {
			throw new RuntimeException("unknown source pool " + poolId);			
		}
		
		return pool.getSource();
	}
	
	public void playFromPool(CogaenId poolId) {
		Pool pool = this.pools.get(poolId);
		if (pool == null) {
			throw new RuntimeException("unknown source pool " + poolId);						
		}
		
		if (pool.isReady()) {
			pool.getSource().playSound();			
		}
	}

	private void destroyOpenAlSource(Source source) {
		IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(1).put(source.getId()).rewind();
		AL10.alDeleteSources(buffer);
		
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			this.logger.logError(LOGGING_SOURCE, "unable to delete open al source, error: " + error);
		}		
		
		this.sources.remove(source);
	}
	
	public void destroySource(Source source) {
		destroyOpenAlSource(source);
		
		for (Pool pool : this.pools.values()) {
			pool.removeSource(source);
		}
	}
		
	void play(int id) {
		AL10.alSourcePlay(id);
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			this.logger.logError(LOGGING_SOURCE, "unable to play open al source, error: " + error);
		}		
	}
	
	void stop(int id) {
		AL10.alSourceStop(id);		
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			this.logger.logError(LOGGING_SOURCE, "unable to stop open al source, error: " + error);
		}		
	}
	
	void assignSound(int sourceId, int bufferId) {
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId);
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			this.logger.logError(LOGGING_SOURCE, "unable to assign sound open al source, error: " + error);
		}		
	}
	
	void setPitch(int sourceId, double pitch) {
		AL10.alSourcef(sourceId, AL10.AL_PITCH, (float) pitch);		
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			this.logger.logError(LOGGING_SOURCE, "unable to set pitch of open al source (" + sourceId + ") , error: " + error);
			throw new RuntimeException();
		}		
	}
	
	void setGain(int sourceId, double gain) {
		AL10.alSourcef(sourceId, AL10.AL_GAIN, (float) gain);		
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			this.logger.logError(LOGGING_SOURCE, "unable to set gain of open al source, error: " + error);
		}		
	}
	
	void setLooping(int sourceId, boolean looping) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			this.logger.logError(LOGGING_SOURCE, "unable to set looping of open al source, error: " + error);
		}				
	}
	
	void setPosition(int sourceId, FloatBuffer pos) {
		AL10.alSource(sourceId, AL10.AL_POSITION, pos);		
		
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			this.logger.logError(LOGGING_SOURCE, "unable to set position of open al source, error: " + error);
		}				
	}
	
	void setVelocity(int sourceId, FloatBuffer vel) {
		AL10.alSource(sourceId, AL10.AL_VELOCITY, vel);		
		
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			this.logger.logError(LOGGING_SOURCE, "unable to set velocity of open al source, error: " + error);
		}				
	}
	
	private static class Pool {
		private List<Source> sources = new ArrayList<Source>();
		private int idx = 0;
		private Timer timer;
		private double timeStamp;
		
		public Pool(Timer timer) {
			this.timer = timer;
		}
		
		public void addSource(Source source) {
			this.sources.add(source);
		}
		
		public void removeSource(Source source) {
			this.sources.remove(source);
			if (this.idx >= this.sources.size()) {
				this.idx = 0;
			}
		}
		
		public Source getSource() {
			if (this.sources.isEmpty()) {
				return null;
			}
			
			Source source = this.sources.get(this.idx++);
			if (idx >= this.sources.size()) {
				idx = 0;
			}
			
			this.timeStamp = this.timer.getTime();
			return source;
		}
		
		public boolean isReady() {
			return this.timer.getTime() - this.timeStamp > SOUND_GAP;
		}
	}
}
