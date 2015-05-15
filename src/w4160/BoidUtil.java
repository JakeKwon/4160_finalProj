package w4160;

import java.lang.Math.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

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
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;


public class BoidUtil {

	public static Vector3f get_avg_pos(ArrayList<Bird> birds) {
		float len = birds.size();
		float sum_x = 0;
		float sum_y = 0;
		float sum_z = 0;

		for(Bird b : birds) {
			sum_x += b.getX();
			sum_y += b.getY();
			sum_z += b.getZ();
		}

		return new Vector3f(sum_x/len, sum_y/len, sum_z/len);
	}

	public static Vector3f get_avg_dir(ArrayList<Bird> birds) {
		float len = birds.size();
		float sum_x = 0;
		float sum_y = 0;
		float sum_z = 0;

		for(Bird b : birds) {
			sum_x += b.getDX();
			sum_y += b.getDY();
			sum_z += b.getDZ();
		}

		return new Vector3f(sum_x/len, sum_y/len, sum_z/len);	
	}

	public static Vector3f steer(Vector3f start, Vector3f goal) {
		Vector3f steer = new Vector3f();
		Vector3f.sub(goal, start, steer);
		if (steer.x != 0 || steer.y != 0 || steer.z != 0) {
			steer.normalise(steer);
		}
		//System.out.println(steer);
		return steer;
	}

	public static void cohesion(Bird b, ArrayList<Bird> birds, float stepCount) {
		if (birds.size() == 0)
			return;

		Vector3f new_dir = new Vector3f();
		Vector3f curr_dir = new Vector3f(b.getDir());
		Vector3f delta_dir = steer(b.getPos(), get_avg_pos(birds));
		delta_dir.scale(1/stepCount);

		Vector3f.add(curr_dir, delta_dir, new_dir);
		b.setDir(new_dir);
	}

	public static void seperation(Bird b, ArrayList<Bird> birds, float stepCount) {
		if (birds.size() == 0)
			return;

		Vector3f new_dir = new Vector3f();
		Vector3f curr_dir = new Vector3f(b.getDir());
		Vector3f delta_dir = steer(get_avg_pos(birds), b.getPos());
		delta_dir.scale(1/stepCount);

		Vector3f.add(curr_dir, delta_dir, new_dir);
		b.setDir(new_dir);
	}

	public static void alignment(Bird b, ArrayList<Bird> birds, float stepCount) {
		if (birds.size() == 0) {
			return;
		}

		Vector3f new_dir = new Vector3f();
		Vector3f curr_dir = new Vector3f(b.getDir());
		Vector3f delta_dir = steer(b.getDir(), get_avg_dir(birds));
		delta_dir.scale(1/stepCount);

		Vector3f.add(curr_dir, delta_dir, new_dir);
		b.setDir(new_dir);
	}

	public static void reset(Bird b, float x_bound, float y_bound, float z_bound) {
		if (b.getX() >= x_bound)
			b.setX(0);
		else if(b.getX() <= 0)
			b.setX(x_bound);

		if (b.getY() >= y_bound)
			b.setY(0);
		else if(b.getY() <= 0)
			b.setY(y_bound);

		if (b.getZ() >= z_bound)
			b.setZ(0);
		else if(b.getZ() <= 0)
			b.setZ(z_bound);
	}
}