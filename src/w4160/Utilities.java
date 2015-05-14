package w4160;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

public class Utilities {

	public static String LoadGLSL(String filename)
    {
    	BufferedReader brv = null;
		try {
			brv = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String vsrc = "";
    	String line;
    	try {
			while ((line=brv.readLine()) != null) {
			  vsrc += line + "\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return vsrc;
    }
    
    /************************* loading and binding texture ***********************************/
    public static int loadTexture(String filename, int textureUnit) {
        ByteBuffer buffer = null;
        int width = 0;
        int height = 0;
         
        try {
            BufferedImage originalImage = ImageIO.read(new File(filename));
            width = originalImage.getWidth();
            height = originalImage.getHeight();
            int[] pixels = new int[width * height];
            originalImage.getRGB(0, 0, width, height, pixels, 0, width);
            buffer = BufferUtils.createByteBuffer(width * height * 4);
            //System.out.print(width + " " + height + " " + pixels.length + "\n");
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                	//System.out.print(y + " " + x + "\n" );
                    int pixel = pixels[y * width + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                    buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                    buffer.put((byte) (pixel & 0xFF));               // Blue component
                    buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
                }
            }
            buffer.flip();
             
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
         
        // Create a new texture object in memory and bind it
        int texId = GL11.glGenTextures();
        GL13.glActiveTexture(textureUnit);
        //System.out.print(texId + "\n");
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
         
        // All RGB bytes are aligned to each other and each component is 1 byte
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
         
        // Upload the texture data and generate mip maps (for scaling)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
         
        // Setup the ST coordinate system
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
         
        // Setup what to do when the texture has to be scaled
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, 
                GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, 
                GL11.GL_LINEAR_MIPMAP_LINEAR);
         
        return texId;
    }
}
