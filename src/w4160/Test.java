package w4160;
import org.lwjgl.util.vector.Vector3f;
import java.util.ArrayList;

public class Test {
	public static void main(String[] arg) {
		System.out.println("TESTING");

		Octree octree = new Octree(new Vector3f(0,0,0), 100);
		Vector3f dir = new Vector3f(1,1,1);

		Asteroid a1 = new Asteroid(new Vector3f(10, 10, 10), 1, 1, dir);
		Asteroid a2 = new Asteroid(new Vector3f(40, 40, 40), 1, 1, dir);

		Asteroid b = new Asteroid(new Vector3f(25, 25, 80), 1, 1, dir);
		Asteroid c = new Asteroid(new Vector3f(80, 25, 25), 1, 1, dir);
		Asteroid d = new Asteroid(new Vector3f(80, 25, 80), 1, 1, dir);

		Asteroid e = new Asteroid(new Vector3f(25, 80, 25), 1, 1, dir);
		Asteroid f = new Asteroid(new Vector3f(25, 80, 80), 1, 1, dir);
		Asteroid g = new Asteroid(new Vector3f(80, 80, 25), 1, 1, dir);

		Asteroid h1 = new Asteroid(new Vector3f(60, 60, 60), 1, 1, dir);
		Asteroid h2 = new Asteroid(new Vector3f(80, 80, 80), 1, 1, dir);

		//octree.insert(a1);
		//octree.insert(a2);

		//octree.insert(b);
		//octree.insert(c);
		//octree.insert(d);
		//octree.insert(e);
		//octree.insert(f);
		//octree.insert(g);
		octree.insert(h1);
		octree.insert(h2);

		octree.traverse();
		/*
		ArrayList<Asteroid> n = octree.get_inRange(new Vector3f(10, 10, 10), 1);

		for(Asteroid as : n)
			System.out.println(as);
		*/
	}
}