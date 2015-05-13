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

public class Player {
    
    private Vector3f position;
    private int life;

    public Player(Vector3f in_position) {
        position = new Vector3f(in_position);
        life = 4;
    }

    public Vector3f getPos() {
        return new Vector3f(position); 
    }

    public int damage() {
        life -= 1;
        return life;
    }
}
