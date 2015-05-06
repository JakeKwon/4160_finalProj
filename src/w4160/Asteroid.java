package w4160;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Asteroid {
    
    private Vector3f position;
    private Vector3f direction;
    private float velocity;

    public Asteroid(float x, float y, float z, Vector3f in_direction) {
        position = new Vector3f(x, y, z);
        in_direction.normalise(direction);
    }

    public void Move() {
        position.x += velocity * direction.x;
        position.y += velocity * direction.y;
        position.z += velocity * direction.z;
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
