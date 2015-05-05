package w4160;
import org.lwjgl.util.vector.Vector3f;

public class Test {
	public static void main(String[] arg) {
		System.out.println("TESTING");
		Octree octree = new Octree(0, new Vector3f(0,0,0), 100);
	}
}