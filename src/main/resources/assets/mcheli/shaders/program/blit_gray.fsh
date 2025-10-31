#version 120

uniform sampler2D DiffuseSampler;
varying vec2 texCoord;

vec3 heatColor(float t) {
    return mix(
        vec3(0.0, 0.0, 0.2), // cold (blue)
        vec3(1.0, 1.0, 0.0), // hot (yellow)
        clamp(pow(t, 2.0), 0.0, 1.0)
    );
}

void main() {
    vec4 color = texture2D(DiffuseSampler, texCoord);
    float luma = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    vec3 heat = heatColor(luma);
    gl_FragColor = vec4(heat, color.a);
}
