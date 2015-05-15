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

public class Sky {
    
    private Vector3f position;
    private float size;

    public Sky(Vector3f in_position, float in_size) {
        position = new Vector3f(in_position);
        size = in_size;
    }

    public void Draw() {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.position.x, this.position.y, this.position.z);

        // Set to wireframe
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_LINE);
        GL11.glColor3f(1.0f, 1.0f, 0.0f);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex3f(size*1.0f , size*1.0f , size*-1.0f);
        GL11.glVertex3f(size*-1.0f, size*1.0f , size*-1.0f);
        GL11.glVertex3f(size*-1.0f, size*1.0f , size*1.0f );
        GL11.glVertex3f(size*1.0f , size*1.0f , size*1.0f );

        GL11.glVertex3f(size*1.0f , size*-1.0f, size*1.0f );
        GL11.glVertex3f(size*-1.0f, size*-1.0f, size*1.0f );
        GL11.glVertex3f(size*-1.0f, size*-1.0f, size*-1.0f);
        GL11.glVertex3f(size*1.0f , size*-1.0f, size*-1.0f);

        GL11.glVertex3f(size*1.0f , size*1.0f , size*1.0f );
        GL11.glVertex3f(size*-1.0f, size*1.0f , size*1.0f );
        GL11.glVertex3f(size*-1.0f, size*-1.0f, size*1.0f );
        GL11.glVertex3f(size*1.0f , size*-1.0f, size*1.0f );

        GL11.glVertex3f(size*1.0f , size*-1.0f, size*-1.0f);
        GL11.glVertex3f(size*-1.0f, size*-1.0f, size*-1.0f);
        GL11.glVertex3f(size*-1.0f, size*1.0f , size*-1.0f);
        GL11.glVertex3f(size*1.0f , size*1.0f , size*-1.0f);

        GL11.glVertex3f(size*-1.0f, size*1.0f , size*1.0f );
        GL11.glVertex3f(size*-1.0f, size*1.0f , size*-1.0f);
        GL11.glVertex3f(size*-1.0f, size*-1.0f, size*-1.0f);
        GL11.glVertex3f(size*-1.0f, size*-1.0f, size*1.0f );

        GL11.glVertex3f(size*1.0f , size*1.0f , size*-1.0f);
        GL11.glVertex3f(size*1.0f , size*1.0f , size*1.0f );
        GL11.glVertex3f(size*1.0f , size*-1.0f, size*1.0f );
        GL11.glVertex3f(size*1.0f , size*-1.0f , size*-1.0f);

        GL11.glEnd();

        // Set back to fill
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_FILL);

        GL11.glPopMatrix();
    }
}
