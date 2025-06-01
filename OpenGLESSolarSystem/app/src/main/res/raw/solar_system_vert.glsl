#version 300 es
layout (location = 0) in vec3 a_position;
layout (location = 1) in vec3 a_normal;
layout (location = 2) in vec2 a_texCoord;

uniform mat4 u_model;
uniform mat4 u_view;
uniform mat4 u_projection;
uniform float u_time;

out vec3 v_normal;
out vec3 v_worldPos;
out vec2 v_texCoord;

void main() {
    mat4 mvp = u_projection * u_view * u_model;
    gl_Position = mvp * vec4(a_position, 1.0);

    v_normal = mat3(transpose(inverse(u_model))) * a_normal;
    v_worldPos = (u_model * vec4(a_position, 1.0)).xyz;
    v_texCoord = a_texCoord;
}