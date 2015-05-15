package w4160;

import java.lang.Math.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.io.File;
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

public class Game {

    String windowTitle = "ASTEROIDS";
    public boolean closeRequested = false;

    long lastFrameTime; // used to calculate delta
    
    ArrayList<Bird> birds;
    Octree octree;
    Sky sky;
    Player player;

    ShaderProgram birdShader;
    int texturLoc;

    static final float AST_SIZE = 2f;

    public void run() {

        createWindow();
        getDelta(); // Initialise delta timer
        initGL();

        // Set up shaders
        initShaders();
        texturLoc = GL20.glGetUniformLocation(birdShader.program, "bird");
        Utilities.loadTexture("glsl/HUMBIRD1.jpg", GL13.GL_TEXTURE0);

        // Set up initial objects (birds)
        initObjects();
        
        while (!closeRequested) {
            pollInput();
            updateLogic(getDelta());
            renderGL();

            Display.update();
            //break;
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
        GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 1000.0f); // Calculate The Aspect Ratio Of The Window
        GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
        GL11.glLoadIdentity(); // Reset The Modelview Matrix

        GL11.glShadeModel(GL11.GL_SMOOTH); // Enables Smooth Shading
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0f); // Depth Buffer Setup
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations
        
        // For shading
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

    private void initObjects() {
        birds = new ArrayList<Bird>();
        octree = new Octree(new Vector3f(0, 0, 0), 100);
        sky = new Sky(new Vector3f(50,50,50), 51); // Yellow Cube
        player = new Player(new Vector3f(50,1,50));
        Camera.setPos(player.getPos());

        genRandBirds();
    }

    private void initShaders() {
         String asteroid_vertex_shader = Utilities.LoadGLSL("glsl/bird.vert");
        
         String asteroid_fragment_shader = Utilities.LoadGLSL("glsl/bird.frag");
         
         String asteroid_geometry_shader = Utilities.LoadGLSL("glsl/bird2.geom");

        try {
            //shader = new ShaderProgram(vertex_shader, fragment_shader);
            birdShader = new ShaderProgram(asteroid_vertex_shader, asteroid_fragment_shader, asteroid_geometry_shader);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void genRandBirds() {
        for(int i=0; i < 4; i++) {
            Vector3f a1 = new Vector3f(randFloat(0,99),randFloat(0,99),randFloat(0,99));
            Vector3f a1_to_player = new Vector3f();
            Vector3f.sub(player.getPos(), a1, a1_to_player);
            octree.insert(new Bird(a1, AST_SIZE, 0.0f, a1_to_player));
        }
    }

    private float randFloat(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }
    
    private void updateLogic(int delta) {
        // Move Birds and
        // reinsert them to the octree
        birds.clear();
        octree.get_elements(birds);
        Octree newOctree = new Octree(octree);

        for (Bird a : birds) {
            // Apply behavior and move bird
            //Util.cohesion(a, octree.get_inRange(a.getPos(), 5));
            Bird newBird = new Bird(a);
            newBird.Move();
            newOctree.insert(newBird);
        }
        octree = null;
        octree = newOctree;
        birds.clear();
        octree.get_elements(birds);
        //octree.traverse();
    }


    private void renderGL() {

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
        GL11.glLoadIdentity(); // Reset The View
        GL11.glTranslatef(0.0f, 0.0f, 0.0f); // Move Right And Into The Screen

        Camera.apply();

        // Apply shaders
        birdShader.begin();
        GL20.glUniform1i(texturLoc, 0);

        //
        sky.Draw();
        octree.Draw(1f, 0f, 0f);
        for (Bird a : birds) {
            a.Draw();
        } 

        birdShader.end();
        //  
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
            Display.setDisplayMode(new DisplayMode(800, 800));
            Display.setVSyncEnabled(true);
            Display.setTitle(windowTitle);
            Display.create();
            // Disable mouse cursor
            Mouse.setGrabbed(true);
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
        new Game().run();
    }
}
