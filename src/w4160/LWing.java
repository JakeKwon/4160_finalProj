package w4160;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class LWing {

	List<Vector3f> vertices = new ArrayList<Vector3f>();
    List<Vector3f> normals = new ArrayList<Vector3f>();
    List<Face> faces = new ArrayList<Face>();

	float rotateZ = 45.0f;
	boolean down = true;
    
    public LWing(){};
    
    public static LWing LoadModel(File f) throws IOException, FileNotFoundException
	{
		BufferedReader reader = new BufferedReader(new FileReader(f));
		LWing lw = new LWing();
		String line;
		//parse the file
		while((line = reader.readLine()) != null){
			if(line.startsWith("v ")){ //vertices
				//System.out.print(line + "\n");
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				lw.vertices.add(new Vector3f(x, y, z));
				//asteroid.normals.add(new Vector3f(x, y, z));
			}
			else if(line.startsWith("vn ")){ //normals
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				lw.normals.add(new Vector3f(x, y, z));
			}
			else if(line.startsWith("f ")){ //faces
				Vector3f VertexIndices = new Vector3f(
					Float.valueOf(line.split(" ")[1].split("/")[0]),
					Float.valueOf(line.split(" ")[2].split("/")[0]),
					Float.valueOf(line.split(" ")[3].split("/")[0])
				);
				Vector3f NormalIndices;
				if(line.split(" ")[1].split("/").length == 1){
					NormalIndices = new Vector3f(
							Float.valueOf(line.split(" ")[1].split("/")[0]),
							Float.valueOf(line.split(" ")[2].split("/")[0]),
							Float.valueOf(line.split(" ")[3].split("/")[0])
						);
				}
				else{
					NormalIndices = new Vector3f(
						Float.valueOf(line.split(" ")[1].split("/")[1]),
						Float.valueOf(line.split(" ")[2].split("/")[1]),
						Float.valueOf(line.split(" ")[3].split("/")[1])
					);/**/
				}
				Face face = new Face(VertexIndices, NormalIndices);
				lw.faces.add(face);
			}
			
		}

		reader.close();
		return lw;
	}
    
    public void draw(Vector3f origin, float scale)
    {
    	LWing lw = null;
    	try {
			lw = LoadModel(new File("mesh/LWing.OBJ"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	GL11.glPushMatrix();
	    GL11.glScalef(scale, scale, scale);
	    GL11.glRotatef(rotateZ, 0, 0, 1);
    	GL11.glBegin(GL11.GL_TRIANGLES);
		for(Face face : lw.faces){
			//System.out.print(face.normal + "\n");
			Vector3f n1 = lw.normals.get((int) face.normal.x - 1);
			GL11.glNormal3f(n1.x, n1.z, n1.y);
			Vector3f v1 = lw.vertices.get((int) face.vertex.x - 1);
			GL11.glVertex3f(v1.x, v1.y, v1.z);

			Vector3f n2 = lw.normals.get((int) face.normal.y - 1);
			GL11.glNormal3f(n2.x, n2.z, n2.y);
			Vector3f v2 = lw.vertices.get((int) face.vertex.y - 1);
			GL11.glVertex3f(v2.x, v2.y, v2.z);

			Vector3f n3 = lw.normals.get((int) face.normal.z - 1);
			GL11.glNormal3f(n3.x, n3.z, n3.y);
			Vector3f v3 = lw.vertices.get((int) face.vertex.z - 1);
			GL11.glVertex3f(v3.x, v3.y, v3.z);
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	
    }
    
    public void flap()
    {
    	if(down)
    		this.rotateZ -= 10.0f;
    	else
    		this.rotateZ += 4.0f;
    		
    	if(this.rotateZ >= 45)
    		down = true;
    	else if (this.rotateZ < -55)
    		down = false;
    }
}
