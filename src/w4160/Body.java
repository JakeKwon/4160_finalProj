package w4160;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Body {

	List<Vector3f> vertices = new ArrayList<Vector3f>();
    List<Vector3f> normals = new ArrayList<Vector3f>();
    List<Face> faces = new ArrayList<Face>();
    List<Vector2f> texCords = new ArrayList<Vector2f>();
    
    public Body(){};
    
    public static Body LoadModel(File f) throws IOException, FileNotFoundException
	{
		BufferedReader reader = new BufferedReader(new FileReader(f));
		Body body = new Body();
		String line;

		//parse the file
		while((line = reader.readLine()) != null){
			if(line.startsWith("v ")){ //vertices
				//System.out.print(line + "\n");
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				body.vertices.add(new Vector3f(x, y, z));
				//asteroid.normals.add(new Vector3f(x, y, z));
			}
			else if(line.startsWith("vn ")){ //normals
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				body.normals.add(new Vector3f(x, y, z));
			}
			else if(line.startsWith("vt ")){ //texture
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				body.texCords.add(new Vector2f(x, y));
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
						Float.valueOf(line.split(" ")[1].split("/")[2]),
						Float.valueOf(line.split(" ")[2].split("/")[2]),
						Float.valueOf(line.split(" ")[3].split("/")[2])
					);/**/
				}
				Vector3f TextureIndices = new Vector3f(
						Float.valueOf(line.split(" ")[1].split("/")[1]),
						Float.valueOf(line.split(" ")[2].split("/")[1]),
						Float.valueOf(line.split(" ")[3].split("/")[1])
					);
				Face face = new Face(VertexIndices, NormalIndices);
				face.tex = TextureIndices;
				body.faces.add(face);
			}
			
		}

		reader.close();
		return body;
	}
    
    public void draw(Vector3f origin, float scale)
    {
    	Body body = null;
    	try {
			body = LoadModel(new File("mesh/body.OBJ"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	GL11.glPushMatrix();
	    GL11.glScalef(scale, scale, scale);
    	GL11.glBegin(GL11.GL_TRIANGLES);
		for(Face face : body.faces){
			//System.out.print(face.normal + "\n");
			Vector3f n1 = body.normals.get((int) face.normal.x - 1);
			GL11.glNormal3f(n1.x, n1.z, n1.y);
			Vector3f v1 = body.vertices.get((int) face.vertex.x - 1);
			GL11.glVertex3f(v1.x, v1.y, v1.z);
			Vector2f t1 = body.texCords.get((int) face.tex.x - 1);
			GL11.glTexCoord2f(t1.x, t1.y);

			Vector3f n2 = body.normals.get((int) face.normal.y - 1);
			GL11.glNormal3f(n2.x, n2.z, n2.y);
			Vector3f v2 = body.vertices.get((int) face.vertex.z - 1);
			GL11.glVertex3f(v2.x, v2.y, v2.z);
			Vector2f t2 = body.texCords.get((int) face.tex.y - 1);
			GL11.glTexCoord2f(t2.x, t2.y);

			Vector3f n3 = body.normals.get((int) face.normal.z - 1);
			GL11.glNormal3f(n3.x, n3.z, n3.y);
			Vector3f v3 = body.vertices.get((int) face.vertex.y - 1);
			GL11.glVertex3f(v3.x, v3.y, v3.z);
			Vector2f t3 = body.texCords.get((int) face.tex.z - 1);
			GL11.glTexCoord2f(t3.x, t3.y);
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	
    }
    
}
