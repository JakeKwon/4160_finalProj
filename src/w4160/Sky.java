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

        //GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_LINE);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glColor3f(0.2f, 0.2f, 0.2f);
        GL11.glVertex3f(size*1.0f , size*1.0f , size*-1.0f);
        GL11.glVertex3f(size*-1.0f, size*1.0f , size*-1.0f);
        GL11.glVertex3f(size*-1.0f, size*1.0f , size*1.0f );
        GL11.glVertex3f(size*1.0f , size*1.0f , size*1.0f );

        GL11.glColor3f(0.1f, 0.1f, 0.1f);
        GL11.glVertex3f(size*1.0f , size*-1.0f, size*1.0f );
        GL11.glVertex3f(size*-1.0f, size*-1.0f, size*1.0f );
        GL11.glVertex3f(size*-1.0f, size*-1.0f, size*-1.0f);
        GL11.glVertex3f(size*1.0f , size*-1.0f, size*-1.0f);

        GL11.glColor3f(0.3f, 0.3f, 0.3f);
        GL11.glVertex3f(size*1.0f , size*1.0f , size*1.0f );
        GL11.glVertex3f(size*-1.0f, size*1.0f , size*1.0f );
        GL11.glVertex3f(size*-1.0f, size*-1.0f, size*1.0f );
        GL11.glVertex3f(size*1.0f , size*-1.0f, size*1.0f );

        GL11.glColor3f(0.4f, 0.4f, 0.4f);
        GL11.glVertex3f(size*1.0f , size*-1.0f, size*-1.0f);
        GL11.glVertex3f(size*-1.0f, size*-1.0f, size*-1.0f);
        GL11.glVertex3f(size*-1.0f, size*1.0f , size*-1.0f);
        GL11.glVertex3f(size*1.0f , size*1.0f , size*-1.0f);

        GL11.glColor3f(0.5f, 0.5f, 0.5f);
        GL11.glVertex3f(size*-1.0f, size*1.0f , size*1.0f );
        GL11.glVertex3f(size*-1.0f, size*1.0f , size*-1.0f);
        GL11.glVertex3f(size*-1.0f, size*-1.0f, size*-1.0f);
        GL11.glVertex3f(size*-1.0f, size*-1.0f, size*1.0f );

        GL11.glColor3f(0.6f, 0.6f, 0.6f);
        GL11.glVertex3f(size*1.0f , size*1.0f , size*-1.0f);
        GL11.glVertex3f(size*1.0f , size*1.0f , size*1.0f );
        GL11.glVertex3f(size*1.0f , size*-1.0f, size*1.0f );
        GL11.glVertex3f(size*1.0f , size*-1.0f , size*-1.0f);
        GL11.glEnd();

        GL11.glPopMatrix();
    }
}
