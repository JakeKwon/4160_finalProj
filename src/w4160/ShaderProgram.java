package w4160;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ARBGeometryShader4;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Loads GLSL vertex and fragment shader code and compiles them
 * Some of the code are borrowed online: https://searchcode.com/codesearch/view/51724444/
 * 
 */
public class ShaderProgram {

    public final int program;
    public final int vertex;
    public final int fragment;
    public final int geometry;

    protected String s;
    protected static FloatBuffer buff;

    public ShaderProgram(String vert, String frag, String geom, boolean useGeom) throws LWJGLException {

        vertex = createShader(vert, GL_VERTEX_SHADER);
        fragment = createShader(frag, GL_FRAGMENT_SHADER);

		/*******This is not used if useGeom is false**********/
		if(useGeom){
			geometry = ARBShaderObjects.glCreateShaderObjectARB(ARBGeometryShader4.GL_GEOMETRY_SHADER_ARB);
			ARBShaderObjects.glShaderSourceARB(geometry, geom);
			ARBShaderObjects.glCompileShaderARB(geometry);
			if (ARBShaderObjects.glGetObjectParameteriARB(geometry, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader: " + ARBShaderObjects.glGetInfoLogARB(geometry, ARBShaderObjects.glGetObjectParameteriARB(geometry, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB)));
		}
		else
			geometry = 0;
		/*******This is not used if useGeom is false**********/
			
        program = glCreateProgram();

        glAttachShader(program, vertex);
        glAttachShader(program, fragment);
		if(useGeom)
			glAttachShader(program, geometry);
        
        //ARBShaderObjects.glAttachObjectARB(program, geometry);
        
        glLinkProgram(program);
        GL20.glValidateProgram(program);

        String log = glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH)); 
        if (log != null && log.trim().length() != 0)
        {
            s += log;
            System.out.println(s + "\n");
        }
        if (glGetProgrami(program, GL_LINK_STATUS) == GL11.GL_FALSE)
            System.err.println("Could not link shader program\n" + s);

        glDetachShader(program, vertex);
        glDetachShader(program, fragment);
		if(useGeom)
			glDetachShader(program, geometry);
        
        glDeleteShader(vertex);
        glDeleteShader(fragment);
        if(useGeom)
			glDeleteShader(geometry);

    }

    /**
     * Compile the shader's source code and return its ID
     * 
     * @param source
     * @param type
     * @return
     */
    protected int createShader(String source, int type) throws LWJGLException {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);

        String log = glGetShaderInfoLog(shader, glGetShaderi(shader, GL_INFO_LOG_LENGTH));
        if (log != null && log.trim().length() != 0)
            System.out.println(getName(type) + " error: " + log);
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL11.GL_FALSE)
            System.err.println(getName(type) + " did not compile\n" + log);

        if (type == 0x8b31)
            System.out.println("Created vert shader");
        if (type == 0x8b30)
            System.out.println("Created frag shader");
        return shader;
    }

    protected String getName(int shaderType) {
        if (shaderType == GL_VERTEX_SHADER)
            return "GL_VERTEX_SHADER";
        if (shaderType == GL_FRAGMENT_SHADER)
            return "GL_FRAGMENT_SHADER";
        else
            return "shader";
    }

    /**
     * Starts the shader program, call before end()
     */
    public void begin() {
        glUseProgram(program);
    }

    /**
     * Ends shader program, call after begin()
     */
    public void end() {
        glUseProgram(0);
    }

    public void destroy() {
        glDeleteProgram(program);
    }

    public void setUniformi(int loc, int i) {
        if (loc == -1) return;
        glUniform1i(loc, i);
    }

    public void setUniform3f(String name, float v1, float v2, float v3) {
        setUniform3f(glGetUniformLocation(program, name), v1, v2, v3);
    }
    
    public void setUniform3f(int loc, float v1, float v2, float v3) {
        if ( loc == -1 ) return;
        glUniform3f(loc, v1, v2, v3);
    }
    
    public void setUniformMatrix(int loc, boolean transposed, Matrix4f mat) {
        if (loc == -1) return;
        if (buff == null)
            // 4 x 4 matrix = 16
            buff = BufferUtils.createFloatBuffer(16);
        
        buff.clear();
        mat.store(buff);
        buff.flip();
        glUniformMatrix4(loc, transposed, buff);
    }

}