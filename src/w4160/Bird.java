package w4160;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.glu.Sphere;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Bird {
    
    private Vector3f position;
    private Vector3f direction;
    private float velocity;
    private float acceleration;
    private float size;
    private Sphere sphere;
    public Body body;
    public LWing lwing;
    public RWing rwing;
	private int objectDisplayList;

    public Bird(Vector3f in_position, float in_size, float in_velocity, Vector3f in_direction) {
        position = new Vector3f(in_position);
        size = in_size;
        direction = new Vector3f();
        velocity = in_velocity;
        in_direction.normalise(direction);
        sphere = new Sphere();
        acceleration = 0.000f;
        body = new Body();
        lwing = new LWing(); 
        rwing = new RWing();
    }

    public Bird(Vector3f in_position, float in_size, float in_velocity, Vector3f in_direction, Body in_body, LWing in_lwing, RWing in_rwing) {
        position = new Vector3f(in_position);
        size = in_size;
        direction = new Vector3f();
        velocity = in_velocity;
        in_direction.normalise(direction);
        sphere = new Sphere();
        acceleration = 0.000f;
        body = in_body;
        lwing = in_lwing; 
        rwing = in_rwing;
		
		lwing.init();
		rwing.init();
		body.init();
		/*objectDisplayList = GL11.glGenLists(1);
        GL11.glNewList(objectDisplayList, GL11.GL_COMPILE);
        {
			in_body.draw(new Vector3f(0, 0, 0), 1);
			in_lwing.draw(new Vector3f(0, 0, 0), 1);
			in_rwing.draw(new Vector3f(0, 0, 0), 1);
		}
        GL11.glEndList();*/
    }


    public Bird(Bird b) {
        this(b.getPos(), b.getSize(), b.getVel(), b.getDir(), b.body, b.lwing, b.rwing);
        //this(b.getPos(), b.getSize(), b.getVel(), b.getDir());
    }

    public void Draw() {

        //Bird 
        float rotationY = (float) Math.toDegrees(Math.atan(direction.x/direction.z));
        //float rotationZ = 0f;
        //float rotationX = (float) Math.toDegrees(Math.atan(direction.z/direction.y));

        if(direction.z < 0)
            rotationY = (float) Math.toDegrees(Math.atan(direction.x/direction.z)) + 180;
        //if(direction.x < 0)
        //rotationZ = (float) Math.toDegrees(Math.atan(direction.y/direction.x)) + 180;
        //if(direction.y < 0)
        //rotationX = (float) Math.toDegrees(Math.atan(direction.z/direction.y)) + 180;

		
        GL11.glPushMatrix();
        GL11.glTranslatef(this.position.x, this.position.y, this.position.z);
        GL11.glRotatef(0, 1f, 0f, 0f);
        GL11.glRotatef(rotationY, 0f, 1f, 0f);
        GL11.glRotatef(0, 0f, 0f, 1f);
        GL11.glTranslatef(-this.position.x, -this.position.y, -this.position.z);
        GL11.glTranslatef(this.position.x, this.position.y, this.position.z);

        //GL11.glCallList(objectDisplayList);
			body.draw(new Vector3f(0, 0, 0), 1);
			lwing.draw(new Vector3f(0, 0, 0), 1);
			rwing.draw(new Vector3f(0, 0, 0), 1);
			rwing.flap();
			lwing.flap();
		GL11.glPopMatrix();
        
        /*GL11.glPushMatrix();
        GL11.glTranslatef(this.position.x, this.position.y, this.position.z);
        GL11.glColor3f(1.0f, 1.0f, 0.0f);
        sphere.draw(size, 4, 4);
        GL11.glPopMatrix();*/
    }

    public void Move() {
        position.x += velocity * direction.x;
        position.y += velocity * direction.y;
        position.z += velocity * direction.z;
        velocity += acceleration;
    }

    public void setX(float x) {
        position.x = x;
    }

    public void setY(float y) {
        position.y = y;
    }

    public void setZ(float z) {
        position.z = z;
    }

    public double getX() {
        return position.x;
    }

    public double getY() {
        return position.y;
    }

    public double getZ() {
        return position.z;
    }

    public double getDX() {
        return direction.x;
    }

    public double getDY() {
        return direction.y;
    }

    public double getDZ() {
        return direction.z;
    }

    public float getVel() {
        return this.velocity;
    }

    public float getSize() {
        return this.size;
    }

    public Vector3f getPos() {
        return new Vector3f(this.position);
    }

    public Vector3f getDir() {
        return new Vector3f(this.direction);
    }

    public void setPos(Vector3f new_pos) {
        this.position = new Vector3f(new_pos);
    }

    public void setDir(Vector3f new_dir) {
        if (new_dir.x == 0 && new_dir.y == 0 && new_dir.z == 0) {
            System.out.println("zero direction");
            return;
        } else {
            new_dir.normalise(this.direction);
        }
    }

    @Override
    public String toString() {
        return ("(" + position.x + ", " + position.y + ", " + position.z + ")");
    }
}
