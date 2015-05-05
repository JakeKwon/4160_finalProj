package w4160;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Ast {
	
    private double x;
    private double y;
    private double z;

    public Ast(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public String toString() {
        return ("(" + x + ", " + y + ", " + z + ")");
    }
}
