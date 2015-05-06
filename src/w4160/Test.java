package w4160;
import org.lwjgl.util.vector.Vector3f;
import java.util.ArrayList;

public class Test {
	public static void main(String[] arg) {
		System.out.println("TESTING");

		Octree octree = new Octree(0, new Vector3f(0,0,0), 100, "#");

		Asteroid a1 = new Asteroid(10, 10, 10);
		Asteroid a2 = new Asteroid(40, 40, 40);

		Asteroid b = new Asteroid(25, 25, 75);
		Asteroid c = new Asteroid(75, 25, 25);
		Asteroid d = new Asteroid(75, 25, 75);

		Asteroid e = new Asteroid(25, 75, 25);
		Asteroid f = new Asteroid(25, 75, 75);
		Asteroid g = new Asteroid(75, 75, 25);
		Asteroid h = new Asteroid(75, 75, 75);

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