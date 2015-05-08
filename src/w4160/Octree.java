package w4160;

import java.lang.Math.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

public class Octree 
{
    private int MAX_OBJECTS = 1;
    private int MAX_LEVELS = 100;

    private int level;
    private ArrayList<Asteroid> asteroids;
    private Octree[] children;

    private Vector3f origin;
    private float size;
    private String index;

    /*
     * Constructor
     */
    public Octree(int in_level, Vector3f in_origin, float in_size, String in_index) {
        this.level = in_level;
        this.asteroids = new ArrayList<Asteroid>();
        this.origin = new Vector3f(in_origin);
        this.children = new Octree[8];
        this.size = in_size;
        this.index = in_index;
    }

    /*
     * Get all the elements in the octree and store in objects.
     * This includes the elements of all its subtrees.
    */
    void get_elements(ArrayList<Asteroid> objects) {
        for(Asteroid a : asteroids)
            objects.add(a);
        if (children[0] != null)
            for (int i = 0; i < children.length; i++)
                children[i].get_elements(objects);
    }

    /*
    * Clears the octree
    */
    public void clear() {
        asteroids.clear();
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

        boolean top = position.y > subSize;
        boolean far = position.z > subSize;
        boolean right = position.x > subSize;

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
    * and all of its asteroids to its own children.
    */
    public void insert(Asteroid ast) {
        Vector3f position = ast.getPos();

        /* out of bounds attempt
        System.out.println(ast);
        System.out.println(origin.x);
        System.out.println(size);

        if (((position.x < origin.x) && (position.y < origin.y) && (position.z < origin.z)) || 
            ((position.x > origin.x + size) && (position.y > origin.y + size) && (position.z > origin.z + size)) {
                System.out.println("Out of bounds: " + ast);
                return;
            }
        */

        if (children[0] != null) {
            int index = getIndex(position);
            children[index].insert(ast);
            return;
        }

        asteroids.add(ast);

        if (asteroids.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (children[0] == null) { 
                split(); 
            }

            int i = 0;
            while (i < asteroids.size()) {
                int index = getIndex(asteroids.get(i).getPos());
                if (index != -1)
                    children[index].insert(asteroids.remove(i));
                else
                    i++;
            }
        }
    }

    /*
        Gets all neighbors of element c in the radius rad.
    */
    public ArrayList<Asteroid> get_inRange(Vector3f position, float rad) {
        // Maintain a stack of all octrees whose elements will be compared
        Stack<Octree> s = new Stack<Octree>();
        ArrayList<Asteroid> neighbors = new ArrayList<Asteroid>();

        // Push root onto stack
        s.push(this);

        while(!s.empty()) {
            Octree T = s.pop();

            // If T is a leaf, check if the elements in it lie in the radius
            if (T.isLeaf()) {
                for (Asteroid a : T.asteroids) {
                    if(get_distance(position, a.getPos()) < rad)
                        neighbors.add(a);
                }
            } else {
                // For all subtrees, push onto the stack those that intersect with the area around c that is being considered.
                for(int i=0; i < T.children.length; i++) {
                    Octree C = T.children[i];
                    if (C.isLeaf()) {
                        for (Asteroid a : C.asteroids) {
                            if(get_distance(position, a.getPos()) < rad)
                                neighbors.add(a);
                        }
                    } else if (C.intersects(new Vector3f(position.x - ((float) rad/2),
                                                         position.y - ((float) rad/2),
                                                         position.z - ((float) rad/2)), rad)) {
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
     * Traverse tree and print out asteroids with what level they are at.
     * Mainly for testing.
     */
    public void traverse() {
        // print out elements
        for(Asteroid a : asteroids) {
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
        return new Vector3f(origin.x + length,
                            origin.y,
                            origin.z + length);
    }
    // Bot Bot Right
    private Vector3f get_bbr(float length) {
        return new Vector3f(origin.x,
                            origin.y,
                            origin.z + length);
    }
}