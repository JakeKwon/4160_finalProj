package w4160;
import org.lwjgl.util.vector.Vector3f;
import java.util.ArrayList;

public class Test {
	public static void main(String[] arg) {
		System.out.println("TESTING");

		Octree octree = new Octree(0, new Vector3f(0,0,0), 100, "#");
		Vector3f dir = new Vector3f(1,1,1);

		Asteroid a1 = new Asteroid(10, 10, 10, dir);
		Asteroid a2 = new Asteroid(40, 40, 40, dir);

		Asteroid b = new Asteroid(25, 25, 75, dir);
		Asteroid c = new Asteroid(75, 25, 25, dir);
		Asteroid d = new Asteroid(75, 25, 75, dir);

		Asteroid e = new Asteroid(25, 75, 25, dir);
		Asteroid f = new Asteroid(25, 75, 75, dir);
		Asteroid g = new Asteroid(75, 75, 25, dir);
		Asteroid h = new Asteroid(75, 75, 75, dir);

		octree.insert(a1);
		octree.insert(a2);

		octree.insert(b);
		octree.insert(c);
		octree.insert(d);
		octree.insert(e);
		octree.insert(f);
		octree.insert(g);
		octree.insert(h);

		ArrayList<Asteroid> n = octree.get_inRange(new Vector3f(10, 10, 10), 1);

		for(Asteroid as : n)
			System.out.println(as);
	}
}