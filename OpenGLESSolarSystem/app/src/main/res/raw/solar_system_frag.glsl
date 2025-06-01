#version 300 es
precision highp float;

in vec3 v_normal;
in vec3 v_worldPos;
in vec2 v_texCoord;

uniform sampler2D u_textureSampler;
uniform vec3 u_lightPos;
uniform vec3 u_viewPos;
uniform vec3 u_objectColor;
uniform float u_time;
uniform int u_isSun;

out vec4 fragColor;

vec3 getSunGlowColor(float time) {
    float strength = 0.5 + 0.5 * sin(time * 2.0);
    vec3 glowColor = vec3(1.0, 0.6, 0.2);
    return glowColor * strength;
}

void main() {
    vec3 normal = normalize(v_normal);
    vec3 lightDir = normalize(u_lightPos - v_worldPos);
    vec3 viewDir = normalize(u_viewPos - v_worldPos);

    float ambientStrength = 0.1;
    vec3 ambient = ambientStrength * u_objectColor;

    float diff = max(dot(normal, lightDir), 0.0);
    vec3 diffuse = diff * u_objectColor;

    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
    vec3 specular = spec * vec3(1.0);

    vec3 finalColor;

    if (u_isSun == 1) {
        finalColor = getSunGlowColor(u_time);
    } else {
        finalColor = (ambient + diffuse) + specular;
    }
    fragColor = vec4(finalColor, 1.0);
}