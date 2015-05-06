package w4160;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Asteroid {
    
    private Vector3f position;

    public Asteroid(float x, float y, float z) {
        position = new Vector3f(x, y, z);
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

    public Vector3f getPos() {
        return new Vector3f(position);
    }

    @Override
    public String toString() {
        return ("(" + position.x + ", " + position.y + ", " + position.z + ")");
    }
}
