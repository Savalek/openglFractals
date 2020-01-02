precision highp float;
uniform vec2 u_Offset;
uniform float u_Scale;
uniform sampler2D u_TextureUnit;

void main() {
    vec2 p = gl_FragCoord.xy;
    vec2 c;
    c.x = p.x * u_Scale - u_Offset.x;
    c.y = p.y * u_Scale - u_Offset.y;

    int maxIter = 70;
    int i;
    vec2 z = c;
    for (i=0; i<maxIter; i++) {
        float x = (z.x * z.x - z.y * z.y) + c.x;
        float y = (z.y * z.x + z.x * z.y) + c.y;

        if ((x * x + y * y) > 4.0) break;
        z.x = x;
        z.y = y;
    }

    float grayPower = float(i) / float(maxIter);
    gl_FragColor = texture2D(u_TextureUnit, vec2(grayPower, 0.0));
}