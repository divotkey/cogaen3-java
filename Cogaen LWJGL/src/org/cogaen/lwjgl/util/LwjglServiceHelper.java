package org.cogaen.lwjgl.util;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.input.KeyboardService;
import org.cogaen.lwjgl.input.MouseService;
import org.cogaen.lwjgl.resource.LwjglXmlResourceService;
import org.cogaen.lwjgl.scene.ParticleSystemService;
import org.cogaen.lwjgl.scene.SceneService;
import org.cogaen.lwjgl.sound.SoundService;

public class LwjglServiceHelper {

	private boolean useProperties = true;
	
	public boolean isUseProperties() {
		return useProperties;
	}

	public void setUseProperties(boolean useProperties) {
		this.useProperties = useProperties;
	}

	public void registerAllServices(Core core) {
		registerVisualService(core);
		registerInputServices(core);
		registerAuralServices(core);
	}
	
	public void registerVisualService(Core core) {
		core.addService(new SceneService(isUseProperties()));
		core.addService(new LwjglXmlResourceService());
		core.addService(new ParticleSystemService());
	}
	
	public void registerInputServices(Core core) {
		core.addService(new KeyboardService());
		core.addService(new MouseService());		
	}
	
	public void registerAuralServices(Core core) {
		core.addService(new SoundService());
	}
}
