package w4160;
import org.lwjgl.util.vector.Vector3f;
import java.util.ArrayList;

public class Test {
	public static void main(String[] arg) {
		System.out.println("TESTING");

		Octree octree = new Octree(0, new Vector3f(0,0,0), 100, "#");
		Vector3f dir = new Vector3f(1,1,1);

		Asteroid a1 = new Asteroid(new Vector3f(10, 10, 10), 1, 1, dir);
		Asteroid a2 = new Asteroid(new Vector3f(40, 40, 40), 1, 1, dir);

		Asteroid b = new Asteroid(new Vector3f(25, 25, 75), 1, 1, dir);
		Asteroid c = new Asteroid(new Vector3f(75, 25, 25), 1, 1, dir);
		Asteroid d = new Asteroid(new Vector3f(75, 25, 75), 1, 1, dir);

		Asteroid e = new Asteroid(new Vector3f(25, 75, 25), 1, 1, dir);
		Asteroid f = new Asteroid(new Vector3f(25, 75, 75), 1, 1, dir);
		Asteroid g = new Asteroid(new Vector3f(75, 75, 25), 1, 1, dir);
		Asteroid h = new Asteroid(new Vector3f(75, 75, 75), 1, 1, dir);

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