varying vec3 v_normal;
varying vec4 abc;
varying vec4 rawpos;
varying vec4 TexCoord;

    void main() {
        abc = gl_ModelViewMatrix * gl_Vertex;
		v_normal = normalize(gl_NormalMatrix * gl_Normal);
		gl_Position = ftransform();
		rawpos = gl_Vertex;
		TexCoord = vec4(gl_MultiTexCoord0);
    }