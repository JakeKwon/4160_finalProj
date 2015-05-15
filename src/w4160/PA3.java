package w4160;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.glu.Cylinder;


public class PA3 {

    String windowTitle = "3D Shapes";
    public boolean closeRequested = false;

    long lastFrameTime; // used to calculate delta
    
    float triangleAngle; // Angle of rotation for the triangles
    float quadAngle; // Angle of rotation for the quads

    ShaderProgram shader;
    ShaderProgram asteroid;

    public void run() {

        createWindow();
        getDelta(); // Initialise delta timer
        initGL();
        initShaders();

        int loc = GL20.glGetUniformLocation(asteroid.program, "bird");

        Utilities.loadTexture("HUMBIRD1.jpg", GL13.GL_TEXTURE0);
        Bird b = new Bird();
    	//b.draw();
    	int i = 0;
        
        while (!closeRequested) {
            pollInput();
            updateLogic(getDelta());
            renderGL(b, loc, i);
            //i = (i + 5) % 360;
            Display.update();
        }
        
        cleanup();
    }
    
    private void initGL() {

        /* OpenGL */
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();

        GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
        GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
        GL11.glLoadIdentity(); // Reset The Projection Matrix
        GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
        GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
        GL11.glLoadIdentity(); // Reset The Modelview Matrix

        GL11.glShadeModel(GL11.GL_SMOOTH); // Enables Smooth Shading
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0f); // Depth Buffer Setup
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations
        
        FloatBuffer position0 = BufferUtils.createFloatBuffer(4);
        position0.put(new float[] { 30.0f, 30.0f, 30.0f, 0.0f});
        position0.flip(); 
        
        FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
	    diffuse.put(new float[] { 0.8f, 0.8f, 0.8f, 1f });
        diffuse.flip();
        
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, diffuse);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, position0);
        
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        
        Camera.create();        
    }
    
//public float intensityX = 1f;

    private void initShaders() {
         String asteroid_vertex_shader = Utilities.LoadGLSL("bird.vert");
        
         String asteroid_fragment_shader = Utilities.LoadGLSL("bird.frag");
         
         String asteroid_geometry_shader = Utilities.LoadGLSL("bird2.geom");

         

        try {
            //shader = new ShaderProgram(vertex_shader, fragment_shader);
            asteroid = new ShaderProgram(asteroid_vertex_shader, asteroid_fragment_shader, asteroid_geometry_shader);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private void updateLogic(int delta) {
        triangleAngle += 0.1f * delta; // Increase The Rotation Variable For The Triangles
        quadAngle -= 0.05f * delta; // Decrease The Rotation Variable For The Quads
    }

    private void renderGL(Bird b, int loc, int i) {

        // start to use shaders
        //shader.begin();
        float dir = (float)(1./Math.sqrt(3));
        Vector3f light = new Vector3f(1.0f, 1.0f, 4.0f);

        //shader.setUniform3f("lightDir", light.x, light.y, light.z);
        // shader.setUniform3f("lightPos", 1.0f, 1.0f, 0.4f);
        // shader.setUniform3f("lightColor", 0.6f, 0.6f, 1f);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
        GL11.glLoadIdentity(); // Reset The View
        GL11.glTranslatef(0.0f, 0.0f, -7.0f); // Move Right And Into The Screen

        Camera.apply();
        // GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
        // GL11.glColor3f(0.0f, 0.0f, 0.0f); // Set The Color To Green
        // GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Top)
        // GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Top)
        // GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Bottom Left Of The Quad (Top)
        // GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Bottom Right Of The Quad (Top)

        // //GL11.glColor3f(1.0f, 0.5f, 0.0f); // Set The Color To Orange
        // GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Top Right Of The Quad (Bottom)
        // GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Top Left Of The Quad (Bottom)
        // GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Bottom)
        // GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Bottom)

        // //GL11.glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
        // GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Front)
        // GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Left Of The Quad (Front)
        // GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad (Front)
        // GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad (Front)

        // //GL11.glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
        // GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Back)
        // GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Back)
        // GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Back)
        // GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Back)

        // //GL11.glColor3f(0.0f, 0.0f, 1.0f); // Set The Color To Blue
        // GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Left)
        // GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Left)
        // GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Left)
        // GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad (Left)

        // GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Right)
        // GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Top Left Of The Quad (Right)
        // GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad (Right)
        // GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Right)
        // GL11.glEnd(); // Done Drawing The Quad
    
        //renderSphere(0f, 0f, 0f);
//renderCylinder(0f, 0f, 0f);
//renderSphere(0f, 0f, 1f);
        //shader.end();
        
    asteroid.begin();
        
        /************************* texture ***********************************/
        GL20.glUniform1i(loc, 0);
        //loadTexture("Craterscape2.jpg", GL13.GL_TEXTURE0);
        //GL20.glUniform1i(env, 1);
        /************************* texture ***********************************/
        
	//Asteroid a = new Asteroid();
	//a.draw2(new Vector3f(0,0,0), 40);
	//a.deform();
	
    //renderSphere(1f, 1f, 1f);
	//a.draw(new Vector3f(-2 ,-2 ,-2), 1);
	GL11.glPushMatrix();
	
	// Create a FloatBuffer of vertices
	//FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
	//verticesBuffer.put(vertices).flip();
	
	// Create a Buffer Object and upload the vertices buffer
	//int vboID = GL15.glGenBuffers();
	//GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
	//GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
	
	//int vaoID = GL30.glGenVertexArrays();
	//GL30.glBindVertexArray(vaoID);

	//int posAttrib = GL20.glGetAttribLocation(asteroid.program, "pos");
	//GL20.glEnableVertexAttribArray(posAttrib);
	//GL20.glVertexAttribPointer(posAttrib, 3, GL11.GL_FLOAT, false, 0, 0);
	
	/*GL11.glBegin(GL11.GL_TRIANGLES);
	GL11.glVertex3f(1f, 1f, 1f);
	GL11.glNormal3f(1f, 1f, 1f);
	GL11.glVertex3f(0.5f, -0.5f, 0f);
	GL11.glNormal3f(0.5f, -0.5f, 0f);
	GL11.glVertex3f(0f, 0.0f, 0f);
	GL11.glNormal3f(0f, 0.0f, 0f);
	GL11.glEnd();
	
	GL11.glBegin(GL11.GL_TRIANGLES);
	GL11.glVertex3f(1f, 1f, 1f);
	GL11.glVertex3f(0.2f, 0.4f, 0.5f);
	GL11.glVertex3f(0.3f, 0.8f, 0.6f);
	GL11.glNormal3f(1f, 1f, 1f);
	GL11.glNormal3f(0.2f, 0.4f, 0.5f);
	GL11.glNormal3f(0.3f, 0.8f, 0.6f);
	
	GL11.glEnd();*/
	GL11.glRotatef(i, 0, 1, 0);
	b.draw();
	
	
	//renderSphere(1f, 1f, 1f);
	//GL30.glBindVertexArray(0);
	
	//GL30.glBindVertexArray(vaoID);
	//GL20.glEnableVertexAttribArray(0);

    // Draw a triangle of 3 vertices
	//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 1);
	//GL11.glBegin(GL11.GL_POINTS);
	//GL11.glVertex2f(x, y);

    // Disable our location
	//GL20.glDisableVertexAttribArray(0);
	//GL30.glBindVertexArray(0);
	
	GL11.glPopMatrix();
	asteroid.end();
	
    }


// private void render() {
//      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//      glLoadIdentity();
//      glTranslatef(0.0f, 0.0f, zTranslation);
//      renderSphere(-2f, -0.5f, -1f);
//      renderSphere(-1f, -0.5f, -2f);
//      renderSphere(-0f, -0.5f, -3f);
//      renderSphere(1f, -0.5f, -4f);
//      renderSphere(2f, -0.5f, -5f);
// }

private void renderCylinder(float x, float y, float z) {
     GL11.glPushMatrix();
     GL11.glTranslatef(x, y, z);
     
     Cylinder s = new Cylinder();
     s.draw(1f,1f,1f, 200, 200);
     //Sphere s = new Sphere();
     //s.draw(1f, 20, 20);
     GL11.glPopMatrix();
}
/**
     * Poll Input
     */
    public void pollInput() {
        Camera.acceptInput(getDelta());
        // scroll through key events
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
                    closeRequested = true;
                else if (Keyboard.getEventKey() == Keyboard.KEY_P)
                    snapshot();
            }
        }

        if (Display.isCloseRequested()) {
            closeRequested = true;
        }
    }

    private void renderSphere(float x, float y, float z) {
	     GL11.glPushMatrix();
	     GL11.glTranslatef(x, y, z);
	     Sphere s = new Sphere();
	     s.setTextureFlag(true);
	     s.draw(1f, 50, 50);
	     GL11.glPopMatrix();
	}

	public void snapshot() {
        System.out.println("Taking a snapshot ... snapshot.png");

        GL11.glReadBuffer(GL11.GL_FRONT);

        int width = Display.getDisplayMode().getWidth();
        int height= Display.getDisplayMode().getHeight();
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );

        File file = new File("snapshot.png"); // The file to save to.
        String format = "PNG"; // Example: "PNG" or "JPG"
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
   
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }
           
        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) { e.printStackTrace(); }
    }
	
	public void EnvMap() {

        GL11.glReadBuffer(GL11.GL_FRONT);

        int width = 640;
        int height= 640;
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );

        File file = new File("snapshot.png"); // The file to save to.
        String format = "PNG"; // Example: "PNG" or "JPG"
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
   
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }
           
        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    /** 
     * Calculate how many milliseconds have passed 
     * since last frame.
     * 
     * @return milliseconds passed since last frame 
     */
    public int getDelta() {
        long time = (Sys.getTime() * 1000) / Sys.getTimerResolution();
        int delta = (int) (time - lastFrameTime);
        lastFrameTime = time;
     
        return delta;
    }

    private void createWindow() {
        try {
            Display.setDisplayMode(new DisplayMode(640, 480));
            Display.setVSyncEnabled(true);
            Display.setTitle(windowTitle);
            Display.create();
        } catch (LWJGLException e) {
            Sys.alert("Error", "Initialization failed!\n\n" + e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * Destroy and clean up resources
     */
    private void cleanup() {
        Display.destroy();
    }
    
    public static void main(String[] args) {
        new PA3().run();
    }
    
    public static class Camera {
        public static float moveSpeed = 0.05f;

        private static float maxLook = 85;

        private static float mouseSensitivity = 0.05f;

        private static Vector3f pos;
        private static Vector3f rotation;

        public static void create() {
            pos = new Vector3f(0, 0, 0);
            rotation = new Vector3f(0, 0, 0);
        }

        public static void apply() {
            if (rotation.y / 360 > 1) {
                rotation.y -= 360;
            } else if (rotation.y / 360 < -1) {
                rotation.y += 360;
            }

            //System.out.println(rotation);
            GL11.glRotatef(rotation.x, 1, 0, 0);
            GL11.glRotatef(rotation.y, 0, 1, 0);
            GL11.glRotatef(rotation.z, 0, 0, 1);
            GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
        }

        public static void acceptInput(float delta) {
            //System.out.println("delta="+delta);
            acceptInputRotate(delta);
            acceptInputMove(delta);
        }

        public static void acceptInputRotate(float delta) {
            if (Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
                float mouseDX = Mouse.getDX();
                float mouseDY = -Mouse.getDY();
                //System.out.println("DX/Y: " + mouseDX + "  " + mouseDY);
                rotation.y += mouseDX * mouseSensitivity * delta;
                rotation.x += mouseDY * mouseSensitivity * delta;
                rotation.x = Math.max(-maxLook, Math.min(maxLook, rotation.x));
            }
        }

        public static void acceptInputMove(float delta) {
            boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_W);
            boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_S);
            boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_D);
            boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_A);
            boolean keyFast = Keyboard.isKeyDown(Keyboard.KEY_Q);
            boolean keySlow = Keyboard.isKeyDown(Keyboard.KEY_E);
            boolean keyFlyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
            boolean keyFlyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

            float speed;

            if (keyFast) {
                speed = moveSpeed * 5;
            } else if (keySlow) {
                speed = moveSpeed / 2;
            } else {
                speed = moveSpeed;
            }

            speed *= delta;

            if (keyFlyUp) {
                pos.y += speed;
            }
            if (keyFlyDown) {
                pos.y -= speed;
            }

            if (keyDown) {
                pos.x -= Math.sin(Math.toRadians(rotation.y)) * speed;
                pos.z += Math.cos(Math.toRadians(rotation.y)) * speed;
            }
            if (keyUp) {
                pos.x += Math.sin(Math.toRadians(rotation.y)) * speed;
                pos.z -= Math.cos(Math.toRadians(rotation.y)) * speed;
            }
            if (keyLeft) {
                pos.x += Math.sin(Math.toRadians(rotation.y - 90)) * speed;
                pos.z -= Math.cos(Math.toRadians(rotation.y - 90)) * speed;
            }
            if (keyRight) {
                pos.x += Math.sin(Math.toRadians(rotation.y + 90)) * speed;
                pos.z -= Math.cos(Math.toRadians(rotation.y + 90)) * speed;
            }
        }

        public static void setSpeed(float speed) {
            moveSpeed = speed;
        }

        public static void setPos(Vector3f pos) {
            Camera.pos = pos;
        }

        public static Vector3f getPos() {
            return pos;
        }

        public static void setX(float x) {
            pos.x = x;
        }

        public static float getX() {
            return pos.x;
        }

        public static void addToX(float x) {
            pos.x += x;
        }

        public static void setY(float y) {
            pos.y = y;
        }

        public static float getY() {
            return pos.y;
        }

        public static void addToY(float y) {
            pos.y += y;
        }

        public static void setZ(float z) {
            pos.z = z;
        }

        public static float getZ() {
            return pos.z;
        }

        public static void addToZ(float z) {
            pos.z += z;
        }

        public static void setRotation(Vector3f rotation) {
            Camera.rotation = rotation;
        }

        public static Vector3f getRotation() {
            return rotation;
        }

        public static void setRotationX(float x) {
            rotation.x = x;
        }

        public static float getRotationX() {
            return rotation.x;
        }

        public static void addToRotationX(float x) {
            rotation.x += x;
        }

        public static void setRotationY(float y) {
            rotation.y = y;
        }

        public static float getRotationY() {
            return rotation.y;
        }

        public static void addToRotationY(float y) {
            rotation.y += y;
        }

        public static void setRotationZ(float z) {
            rotation.z = z;
        }

        public static float getRotationZ() {
            return rotation.z;
        }

        public static void addToRotationZ(float z) {
            rotation.z += z;
        }

        public static void setMaxLook(float maxLook) {
            Camera.maxLook = maxLook;
        }

        public static float getMaxLook() {
            return maxLook;
        }

        public static void setMouseSensitivity(float mouseSensitivity) {
            Camera.mouseSensitivity = mouseSensitivity;
        }

        public static float getMouseSensitivity() {
            return mouseSensitivity;
        }
    }
    
    
    
}

