package com.example.openglessolarsystem;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private final Context mContext;
    private int mProgram;

    private int uModelLoc;
    private int uViewLoc;
    private int uProjectionLoc;
    private int uTimeLoc;
    private int uIsSunLoc;
    private int uObjectColorLoc;
    private int uLightPosLoc;
    private int uViewPosLoc;

    private int aPositionLoc;
    private int aNormalLoc;
    private int aTexCoordLoc;

    private float[] projectionMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] modelMatrix = new float[16];

    private float[] cameraPos = {0.0f, 0.0f, 15.0f};
    private float[] cameraFront = {0.0f, 0.0f, -1.0f};
    private float[] cameraUp = {0.0f, 1.0f, 0.0f};

    private float yaw = -90.0f;
    private float pitch = 0.0f;
    private float lastX;
    private float lastY;
    private boolean firstTouch = true;
    private final float sensitivity = 0.2f;
    private final float zoom = 45.0f;

    private long lastFrameTime;
    private float currentTime;

    private CelestialBody sun;
    private CelestialBody planet1;
    private CelestialBody planet2;
    private CelestialBody moon;

    public MyGLRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, R.raw.solar_system_vert);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, R.raw.solar_system_frag);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);

        uModelLoc = GLES20.glGetUniformLocation(mProgram, "u_model");
        uViewLoc = GLES20.glGetUniformLocation(mProgram, "u_view");
        uProjectionLoc = GLES20.glGetUniformLocation(mProgram, "u_projection");
        uTimeLoc = GLES20.glGetUniformLocation(mProgram, "u_time");
        uIsSunLoc = GLES20.glGetUniformLocation(mProgram, "u_isSun");
        uObjectColorLoc = GLES20.glGetUniformLocation(mProgram, "u_objectColor");
        uLightPosLoc = GLES20.glGetUniformLocation(mProgram, "u_lightPos");
        uViewPosLoc = GLES20.glGetUniformLocation(mProgram, "u_viewPos");

        aPositionLoc = GLES20.glGetAttribLocation(mProgram, "a_position");
        aNormalLoc = GLES20.glGetAttribLocation(mProgram, "a_normal");
        aTexCoordLoc = GLES20.glGetAttribLocation(mProgram, "a_texCoord");

        sun = SphereGenerator.createSphere(0.8f, 30, 30, new float[]{1.0f, 0.5f, 0.0f}, 0.0f, 0.0f, 0.0f);
        planet1 = SphereGenerator.createSphere(0.3f, 20, 20, new float[]{0.2f, 0.5f, 1.0f}, 0.5f, 2.0f, 4.0f);
        planet2 = SphereGenerator.createSphere(0.4f, 25, 25, new float[]{0.8f, 0.2f, 0.1f}, 0.3f, 1.5f, 8.0f);
        moon = SphereGenerator.createSphere(0.1f, 15, 15, new float[]{0.7f, 0.7f, 0.7f}, 2.0f, 3.0f, 1.5f);

        lastFrameTime = System.nanoTime();
        currentTime = 0.0f;
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.perspectiveM(projectionMatrix, 0, zoom, ratio, 0.1f, 100.0f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        long now = System.nanoTime();
        float deltaTime = (now - lastFrameTime) / 1_000_000_000.0f;
        lastFrameTime = now;
        currentTime += deltaTime;

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);

        Matrix.setLookAtM(viewMatrix, 0,
                cameraPos[0], cameraPos[1], cameraPos[2],
                cameraPos[0] + cameraFront[0], cameraPos[1] + cameraFront[1], cameraPos[2] + cameraFront[2],
                cameraUp[0], cameraUp[1], cameraUp[2]);

        GLES20.glUniformMatrix4fv(uViewLoc, 1, false, viewMatrix, 0);
        GLES20.glUniformMatrix4fv(uProjectionLoc, 1, false, projectionMatrix, 0);
        GLES20.glUniform1f(uTimeLoc, currentTime);
        GLES20.glUniform3fv(uLightPosLoc, 1, new float[]{0.0f, 0.0f, 0.0f}, 0);
        GLES20.glUniform3fv(uViewPosLoc, 1, cameraPos, 0);

        Matrix.setIdentityM(modelMatrix, 0);
        GLES20.glUniformMatrix4fv(uModelLoc, 1, false, modelMatrix, 0);
        GLES20.glUniform1i(uIsSunLoc, 1);
        GLES20.glUniform3fv(uObjectColorLoc, 1, sun.color, 0);
        drawSphere(sun);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, currentTime * planet1.orbitSpeed * 360f / (2 * (float)Math.PI), 0.0f, 1.0f, 0.0f);
        Matrix.translateM(modelMatrix, 0, planet1.orbitRadius, 0.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, currentTime * planet1.rotationSpeed * 360f / (2 * (float)Math.PI), 0.0f, 1.0f, 0.0f);
        GLES20.glUniformMatrix4fv(uModelLoc, 1, false, modelMatrix, 0);
        GLES20.glUniform1i(uIsSunLoc, 0);
        GLES20.glUniform3fv(uObjectColorLoc, 1, planet1.color, 0);
        drawSphere(planet1);

        float[] planet1ModelMatrix = new float[16];
        System.arraycopy(modelMatrix, 0, planet1ModelMatrix, 0, 16);


        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, currentTime * planet2.orbitSpeed * 360f / (2 * (float)Math.PI), 0.0f, 1.0f, 0.0f);
        Matrix.translateM(modelMatrix, 0, planet2.orbitRadius, 0.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, currentTime * planet2.rotationSpeed * 360f / (2 * (float)Math.PI), 0.0f, 1.0f, 0.0f);
        GLES20.glUniformMatrix4fv(uModelLoc, 1, false, modelMatrix, 0);
        GLES20.glUniform1i(uIsSunLoc, 0);
        GLES20.glUniform3fv(uObjectColorLoc, 1, planet2.color, 0);
        drawSphere(planet2);

        float[] planet2ModelMatrix = new float[16];
        System.arraycopy(modelMatrix, 0, planet2ModelMatrix, 0, 16);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMM(modelMatrix, 0, planet2ModelMatrix, 0, modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, currentTime * moon.orbitSpeed * 360f / (2 * (float)Math.PI), 0.0f, 1.0f, 0.0f);
        Matrix.translateM(modelMatrix, 0, moon.orbitRadius, 0.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, currentTime * moon.rotationSpeed * 360f / (2 * (float)Math.PI), 0.0f, 1.0f, 0.0f);
        GLES20.glUniformMatrix4fv(uModelLoc, 1, false, modelMatrix, 0);
        GLES20.glUniform1i(uIsSunLoc, 0);
        GLES20.glUniform3fv(uObjectColorLoc, 1, moon.color, 0);
        drawSphere(moon);
    }

    private int loadShader(int type, int resId) {
        String shaderCode = readRawTextFile(resId);
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shader);
            throw new RuntimeException("Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
        }
        return shader;
    }

    private String readRawTextFile(int resId) {
        InputStream inputStream = mContext.getResources().openRawResource(resId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private void drawSphere(CelestialBody body) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            GLES30.glBindVertexArray(body.vaoId[0]);
        } else {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, body.vboPositionId[0]);
            GLES20.glEnableVertexAttribArray(aPositionLoc);
            GLES20.glVertexAttribPointer(aPositionLoc, 3, GLES20.GL_FLOAT, false, 0, 0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, body.vboNormalId[0]);
            GLES20.glEnableVertexAttribArray(aNormalLoc);
            GLES20.glVertexAttribPointer(aNormalLoc, 3, GLES20.GL_FLOAT, false, 0, 0);

            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, body.vboIndexId[0]);
        }

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, body.numIndices, GLES20.GL_UNSIGNED_SHORT, 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            GLES30.glBindVertexArray(0);
        } else {
            GLES20.glDisableVertexAttribArray(aPositionLoc);
            GLES20.glDisableVertexAttribArray(aNormalLoc);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        }
    }

    public boolean handleTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (firstTouch) {
                    lastX = x;
                    lastY = y;
                    firstTouch = false;
                }

                float xoffset = x - lastX;
                float yoffset = lastY - y;
                lastX = x;
                lastY = y;

                yaw += xoffset * sensitivity;
                pitch += yoffset * sensitivity;

                if (pitch > 89.0f) pitch = 89.0f;
                if (pitch < -89.0f) pitch = -89.0f;

                float yawRad = (float) Math.toRadians(yaw);
                float pitchRad = (float) Math.toRadians(pitch);

                cameraFront[0] = (float) (Math.cos(yawRad) * Math.cos(pitchRad));
                cameraFront[1] = (float) Math.sin(pitchRad);
                cameraFront[2] = (float) (Math.sin(yawRad) * Math.cos(pitchRad));

                float length = (float) Math.sqrt(cameraFront[0] * cameraFront[0] +
                        cameraFront[1] * cameraFront[1] +
                        cameraFront[2] * cameraFront[2]);
                if (length > 0) {
                    cameraFront[0] /= length;
                    cameraFront[1] /= length;
                    cameraFront[2] /= length;
                }
                break;
        }
        return true;
    }
}