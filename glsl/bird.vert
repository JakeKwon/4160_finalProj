#version 330 core

out vec3 v_normal;
out vec4 abc;
out vec4 rawpos;
out vec4 TexCoord;

    void main() {
        abc = gl_ModelViewMatrix * gl_Vertex;
		v_normal = normalize(gl_NormalMatrix * gl_Normal);
		gl_Position = ftransform();
		rawpos = gl_Vertex;
		TexCoord = vec4(gl_MultiTexCoord0);
    }