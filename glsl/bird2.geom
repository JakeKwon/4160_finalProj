#define FUR_LAYERS 4

#define FUR_LENGTH 0.1

layout(triangles) in;

layout(triangle_strip, max_vertices = 12) out;

in gl_PerVertex
{
    vec4  gl_Position;
    vec3 v_normal;
	vec4 rawpos;
	vec4 TexCoord;
} gl_in[];

out vec3 v_normal;
out vec4 rawpos;
out vec4 TexCoord;

void main(void)
{
	vec3 normal = normalize(cross(vec3(gl_in[0].gl_Position) - vec3(gl_in[1].gl_Position), vec3(gl_in[0].gl_Position) - vec3(gl_in[2].gl_Position)));

	
	const float FUR_DELTA = 1.0 / float(FUR_LAYERS);
	
	float d = 0.0;

	for(int i = 0; i < gl_in.length(); i++)
	{
	
		v_normal = gl_in[i].v_normal; 
		rawpos = gl_in[i].rawpos;
		TexCoord = gl_in[i].TexCoord;

		gl_Position = gl_in[i].gl_Position;
		EmitVertex();
	}
	EndPrimitive();/**/

	for (int furLayer = 1; furLayer < FUR_LAYERS; furLayer++)
	{
		d += FUR_DELTA;
		
		for(int i = 0; i < gl_in.length()-1; i++)
		{
			

			v_normal = gl_in[i].v_normal; 
			rawpos = gl_in[i].rawpos;
			TexCoord = gl_in[i].TexCoord;
			
			// If the distance of the layer is getting bigger to the original surface, the layer gets more transparent.   
			//v_furStrength = 1.0 - d;
			
			// Displace a layer along the surface normal.
			gl_Position = gl_in[i].gl_Position;//(gl_in[i].gl_Position + vec4(normal * d * FUR_LENGTH, 0.0));
	
			EmitVertex();
		}
		
		gl_Position = (gl_in[2].gl_Position + vec4(normal * FUR_LENGTH * furLayer, 0.0) + vec4(gl_in[0].gl_Position * FUR_LENGTH * furLayer) + vec4(gl_in[1].gl_Position * FUR_LENGTH * furLayer));
		EmitVertex();
		
		EndPrimitive();
	}
}