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
import java.lang.Math.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

public class Octree 
{
    private int MAX_OBJECTS = 5;
    private int MAX_LEVELS = 30;

    private int level;
    private ArrayList<Bird> birds;
    private Octree[] children;

    private Vector3f origin;
    private float size;
    private String index;

    /*
     * Constructor
     */
    public Octree(Vector3f in_origin, float in_size) {
        this(0, in_origin, in_size, "#");
    }

    /*
     * Copy Constructor
     * Will not copy the elements in the octree.
     */
    public Octree(Octree o) {
        this(0, o.origin, o.size, "#");
    }



    /*
     * Constructor
     */
    private Octree(int in_level, Vector3f in_origin, float in_size, String in_index) {
        this.level = in_level;
        this.birds = new ArrayList<Bird>();
        this.origin = new Vector3f(in_origin);
        this.children = new Octree[8];
        this.size = in_size;
        this.index = in_index;
    }

    /*
     * Get all the elements in the octree and store in objects.
     * This includes the elements of all its subtrees.
    */
    void get_elements(ArrayList<Bird> objects) {
        for(Bird a : birds)
            objects.add(a);
        if (children[0] != null)
            for (int i = 0; i < children.length; i++)
                children[i].get_elements(objects);
    }

    /*
    * Clears the octree
    */
    public void clear() {
        birds.clear();
        if (children[0] != null)
            for (int i = 0; i < children.length; i++) {
                children[i].clear();
                children[i] = null;
            }
    }

    /*
     * Splits the octree into 8 half sized octress in children
     */
    private void split() {
        float subSize = (float) this.size / 2;

        children[0] = new Octree(level+1, get_ttl(subSize), subSize, index + "0");
        children[1] = new Octree(level+1, get_ttr(subSize), subSize, index + "1");
        children[2] = new Octree(level+1, get_tbr(subSize), subSize, index + "2");
        children[3] = new Octree(level+1, get_tbl(subSize), subSize, index + "3");

        children[4] = new Octree(level+1, get_btl(subSize), subSize, index + "4");
        children[5] = new Octree(level+1, get_btr(subSize), subSize, index + "5");
        children[6] = new Octree(level+1, get_bbr(subSize), subSize, index + "6");
        children[7] = new Octree(level+1, get_bbl(subSize), subSize, index + "7");
    }

    /*
     * Determine which node the position lies in
     */
    private int getIndex(Vector3f position) {
        float subSize = (float) this.size / 2;

        boolean top = position.y > origin.y + subSize;
        boolean far = position.z > origin.z + subSize;
        boolean right = position.x > origin.x + subSize;

        if (top)
            if (far)
                if(right)
                    return 2;
                else
                    return 3;
            else
                if(right)
                    return 1;
                else
                    return 0;
        else
            if (far)
                if(right)
                    return 6;
                else
                    return 7;
            else
                if(right)
                    return 5;
                else
                    return 4;
    }

    /*
    * Insert the asteroid into the octree. 
    * If the child doesn't have space, it will split
    * and all of its birds to its own children.
    */
    public void insert(Bird ast) {
        Vector3f position = ast.getPos();

        /*
        // If root check if out of bounds
        if (index == "#")
            if (!((position.x >= origin.x) && (position.y >= origin.y) && (position.z >= origin.z) && 
                (position.x <= origin.x + size) && (position.y <= origin.y + size) && (position.z <= origin.z + size))) {
                System.out.println("Out of bounds: " + ast);
                return;
            }
        */

        // If tree is already split
        if (children[0] != null) {
            // get which child to put into
            int index = getIndex(position);
            // insert into that child
            children[index].insert(ast);
            return;
        }

        birds.add(ast);

        // If octree exceeds capacity
        if (birds.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            split();
            for (Bird a : birds) {
                int index = getIndex(a.getPos());
                children[index].insert(a);
            }
            birds.clear();
        }
    }

    /*
        Gets all neighbors of element c in the radius rad.
    */
    public ArrayList<Bird> get_inRange(Bird b, float rad) {
        // Maintain a stack of all octrees whose elements will be compared
        Stack<Octree> s = new Stack<Octree>();
        ArrayList<Bird> neighbors = new ArrayList<Bird>();

        // Push root onto stack
        s.push(this);

        while(!s.empty()) {
            Octree T = s.pop();

            // If T is a leaf, check if the elements in it lie in the radius
            if (T.isLeaf()) {
                for (Bird a : T.birds) {
                    if((b != a) && (get_distance(b.getPos(), a.getPos()) < rad))
                        neighbors.add(a);
                }
            } else {
                // For all subtrees, push onto the stack those that intersect with the area around c that is being considered.
                for(int i=0; i < T.children.length; i++) {
                    Octree C = T.children[i];
                    if (C.isLeaf()) {
                        for (Bird a : C.birds) {
                            if((b != a) && (get_distance(b.getPos(), a.getPos()) < rad))
                                neighbors.add(a);
                        }
                    } else if (C.intersects(new Vector3f(b.getPos().x - ((float) rad/2),
                                                         b.getPos().y - ((float) rad/2),
                                                         b.getPos().z - ((float) rad/2)), rad)) {
                        s.push(C);
                    }
                }
            }
        }
        return neighbors;
    }

    /*
     * Returns true if octree isn't split.
     * (Doesn't have children)
     */
    private boolean isLeaf() {
        return children[0] == null;
    }

    /*
     * Traverse tree and print out birds with what level they are at.
     * Mainly for testing.
     */
    public void traverse() {
        // print out elements
        for(Bird a : birds) {
            System.out.print(a);
            System.out.println(" at Index: " + index);
            System.out.println(" at Level: " + level);
        }

        // traverse subtrees
        if (children[0] != null)
            for(int i=0; i < children.length; i++)
                children[i].traverse();
    }

    /*
     * Return true if this octree intersects with the passed cube.
     */
    boolean intersects(Vector3f position, float length) {
        // If A's left face is to the right of the B's right face
        boolean cond1 = this.origin.x        > position.x + length;
        // If A's right face is to the left of the B's left face
        boolean cond2 = this.origin.x + size < position.x;
        // If A's top face is below B's bottom face
        boolean cond3 = this.origin.y + size < position.y;
        // If A's bottom face is above B's top face
        boolean cond4 = this.origin.y        > position.y + length;
        // If A's front face is behind B's back face
        boolean cond5 = this.origin.z + size < position.z;
        // If A's back face is in front of B's front face
        boolean cond6 = this.origin.z        > position.z + length;
        
        boolean seperate = cond1 || cond2 || cond3 || cond4 || cond5 || cond6;
        return !seperate;
    }

    void Draw(float r, float g, float b) {
        float size = (float) this.size/2;

        GL11.glPushMatrix();
        GL11.glTranslatef(this.origin.x + size, this.origin.y + size, this.origin.z + size);
        GL11.glColor3f(r, g, b);

        // Set to wireframe
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_LINE);

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

        // traverse subtrees
        if (children[0] != null)
            for(int i=0; i < children.length; i++)
                children[i].Draw(r, g, b);
    }

    /*
     * Return distance between two vector positions.
     */
    float get_distance(Vector3f a, Vector3f b) {
        return (float) Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2) + Math.pow(b.z - a.z, 2));
    }

    /*
     * Get corner positions of octree
     */

    // Top Corners

    // Top Top Left
    private Vector3f get_ttl(float length) {
        return new Vector3f(origin.x,
                            origin.y + length,
                            origin.z);
    }
    // Top Top Right
    private Vector3f get_ttr(float length) {
        return new Vector3f(origin.x + length,
                            origin.y + length,
                            origin.z);
    }
    // Top Bot Left
    private Vector3f get_tbl(float length) {
        return new Vector3f(origin.x,
                            origin.y + length,
                            origin.z + length);
    }
    // Top Bot Right
    private Vector3f get_tbr(float length) {
        return new Vector3f(origin.x + length, 
                            origin.y + length,
                            origin.z + length);
    }

    // Bot Corners

    // Bot Top Left
    private Vector3f get_btl(float length) {
        return new Vector3f(origin.x,
                            origin.y,
                            origin.z);
    }
    // Bot Top Right
    private Vector3f get_btr(float length) {
        return new Vector3f(origin.x + length,
                            origin.y,
                            origin.z);
    }
    // Bot Bot Left
    private Vector3f get_bbl(float length) {
        return new Vector3f(origin.x,
                            origin.y,
                            origin.z + length);
    }
    // Bot Bot Right
    private Vector3f get_bbr(float length) {
        return new Vector3f(origin.x + length,
                            origin.y,
                            origin.z + length);
    }
}