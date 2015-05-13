package w4160;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Bird {
	
	Body a = new Body();
	LWing lw = new LWing(); 
	RWing rw = new RWing();
	
	Vector3f direction = new Vector3f();
	
	public void draw(){
		GL11.glPushMatrix();
		a.draw(new Vector3f(0, 0, 0), 1);
		rw.flap();
		lw.flap();
		lw.draw(new Vector3f(0, 0, 0), 1);
		rw.draw(new Vector3f(0, 0, 0), 1);
		GL11.glPopMatrix();
	}
}
