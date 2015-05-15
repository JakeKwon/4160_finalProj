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


public class Util {

	public static Vector3f get_avg_pos(ArrayList<Bird> birds) {
		float len = birds.size();
		//System.out.println(len);
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

	public static Vector3f steer(Vector3f start, Vector3f goal, float stepCount) {
		Vector3f steer = new Vector3f();
		Vector3f.sub(start, goal, steer);
		steer.scale(1/stepCount);
		return steer;
	}

	public static void cohesion(Bird b, ArrayList<Bird> birds) {
		//System.out.println(steer(b.getDir(), get_avg_pos(birds), 10));
		b.setDir(steer(b.getDir(), get_avg_pos(birds), 10));
	}
}