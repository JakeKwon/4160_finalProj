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

    public Bird(Vector3f in_position, float in_size, float in_velocity, Vector3f in_direction) {
        position = new Vector3f(in_position);
        size = in_size;
        direction = new Vector3f();
        velocity = in_velocity;
        in_direction.normalise(direction);
        sphere = new Sphere();
        acceleration = 0.000f;
    }

    public Bird(Bird b) {
        this(b.getPos(), b.getSize(), b.getVel(), b.getDir());
    }

    public void Draw() {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.position.x, this.position.y, this.position.z);
        GL11.glColor3f(1.0f, 1.0f, 0.0f);
        sphere.draw(size, 40, 40);
        GL11.glPopMatrix();
    }

    public void Move() {
        position.x += velocity * direction.x;
        position.y += velocity * direction.y;
        position.z += velocity * direction.z;
        velocity += acceleration;
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
        new_dir.normalise(this.direction);
    }

    @Override
    public String toString() {
        return ("(" + position.x + ", " + position.y + ", " + position.z + ")");
    }
}
