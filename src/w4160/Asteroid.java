package w4160;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Asteroid {
	
	float X = 0.525731112119133606f; 
    float Z = 0.850650808352039932f;
    
    float[][] data = {      {-X, 0.0f, Z}, {X, 0.0f, Z}, {-X, 0.0f, -Z}, {X, 0.0f, -Z},    
    		   				{0.0f, Z, X}, {0.0f, Z, -X}, {0.0f, -Z, X}, {0.0f, -Z, -X},    
    		   				{Z, X, 0.0f}, {-Z, X, 0.0f}, {Z, -X, 0.0f}, {-Z, -X, 0.0f} 
    				};
    
    int [][] indices = { 
    		   {0,4,1}, {0,9,4}, {9,5,4}, {4,5,8}, {4,8,1},    
    		   {8,10,1}, {8,3,10}, {5,3,8}, {5,2,3}, {2,7,3},    
    		   {7,10,3}, {7,6,10}, {7,11,6}, {11,0,6}, {0,1,6}, 
    		   {6,1,10}, {9,0,11}, {9,11,2}, {9,2,5}, {7,2,11} };
    
    public void draw(Vector3f origin, float scale){
    	//int buildingDisplayList = GL11.glGenLists(1);
        //GL11.glNewList(buildingDisplayList, GL11.GL_COMPILE);
        //{
		    GL11.glPushMatrix();
		    GL11.glScalef(scale, scale, scale);
		    GL11.glTranslatef(origin.x, origin.y, origin.z);
	    	GL11.glBegin(GL11.GL_TRIANGLES);    
		    for (int i = 0; i < 20; i++) {    
		       /* color information here */ 
		       GL11.glVertex3f(data[indices[i][0]][0], data[indices[i][0]][1], data[indices[i][0]][2]); 
		       GL11.glVertex3f(data[indices[i][1]][0], data[indices[i][1]][1], data[indices[i][1]][2]); 
		       GL11.glVertex3f(data[indices[i][2]][0], data[indices[i][2]][1], data[indices[i][2]][2]); 
		    }
		    GL11.glEnd();
		    GL11.glPopMatrix();
        //}
        //GL11.glEndList();
        
        //GL11.glCallList(buildingDisplayList);
    }
}