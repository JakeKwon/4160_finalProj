package w4160;
import org.lwjgl.util.vector.Vector3f;
import java.util.ArrayList;

public class Test {
	public static void main(String[] arg) {
		System.out.println("TESTING");

		Octree octree = new Octree(0, new Vector3f(0,0,0), 100, "#");

		Ast a1 = new Ast(10, 10, 10);
		Ast a2 = new Ast(40, 40, 40);

		Ast b = new Ast(25, 25, 75);
		Ast c = new Ast(75, 25, 25);
		Ast d = new Ast(75, 25, 75);

		Ast e = new Ast(25, 75, 25);
		Ast f = new Ast(25, 75, 75);
		Ast g = new Ast(75, 75, 25);
		Ast h = new Ast(75, 75, 75);

		octree.insert(a1);
		octree.insert(a2);

		octree.insert(b);
		octree.insert(c);
		octree.insert(d);
		octree.insert(e);
		octree.insert(f);
		octree.insert(g);
		octree.insert(h);

		ArrayList<Ast> n = octree.get_inRange(new Vector3f(10, 10, 10), 1);

		for(Ast as : n)
			System.out.println(as);
	}
}