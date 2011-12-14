package org.cogaen.lwjgl.scene;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.name.CogaenId;
import org.lwjgl.opengl.GL11;

public class ForceVisualizerService extends AbstractService implements RenderSubsystem {

	public static final CogaenId ID = new CogaenId("org.cogaen.lwjgl.scene.ForceVisualizerService");
	private static final String NAME = "Cogaen LWJGL Force Visualizer";

	private List<Force> forces = new ArrayList<Force>();
	private double forceScale = 1;
	private Color forceColor = new Color(Color.RED);
	
	public static ForceVisualizerService getInstance(Core core) {
		return (ForceVisualizerService) core.getService(ID);
	}
	
	public ForceVisualizerService() {
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
		SceneService.getInstance(getCore()).addSubsystem(this);
	}

	@Override
	protected void doStop() {
		SceneService.getInstance(getCore()).removeSubsystem(this);
		super.doStop();
	}

	@Override
	public void render(int mask) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glDisable(GL11.GL_BLEND);
    	this.forceColor.apply();
    	
		GL11.glBegin(GL11.GL_LINES);
		for (Force force : this.forces) {
			GL11.glVertex2d(force.px, force.py);
			GL11.glVertex2d(force.px + force.vx * this.forceScale, force.py + force.vy * this.forceScale);
		}
		GL11.glEnd();
		
		this.forces.clear();
	}
	
	public void addForce(double px, double py, double vx, double vy) {
		this.forces.add(new Force(px, py, vx, vy));
	}
	
	public double getForceScale() {
		return forceScale;
	}

	public void setForceScale(double forceScale) {
		this.forceScale = forceScale;
	}

	private static class Force {
		public double px;
		public double py;
		public double vx;
		public double vy;
		
		public Force(double px, double py, double vx, double vy) {
			super();
			this.px = px;
			this.py = py;
			this.vx = vx;
			this.vy = vy;
		}
	}
}
