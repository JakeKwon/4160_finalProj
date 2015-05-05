package w4160;
import java.util.List;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

public class Octree {

    private int MAX_OBJECTS = 10;
    private int MAX_LEVELS = 5;

    private int level;
    private List asteroids;
    private Octree[] nodes;

    private Vector3f origin;

    private float size;

    /*
    * Constructor
    */
    public Octree(int slevel, Vector3f origin, float size) {
        level = slevel;
        asteroids = new ArrayList<Ast>();
        origin = new Vector3f(origin);
        nodes = new Octree[4];
    }
}