package com.example.openglessolarsystem;

import android.opengl.GLES20;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

public class SphereGenerator {

    public static CelestialBody createSphere(float radius, int slices, int stacks, float[] color, float orbitSpeed, float rotationSpeed, float orbitRadius) {
        CelestialBody body = new CelestialBody();
        body.color = color;
        body.orbitSpeed = orbitSpeed;
        body.rotationSpeed = rotationSpeed;
        body.orbitRadius = orbitRadius;
        body.radius = radius;

        List<Float> verticesList = new ArrayList<>();
        List<Float> normalsList = new ArrayList<>();
        List<Float> texCoordsList = new ArrayList<>();
        List<Short> indicesList = new ArrayList<>();

        for (int i = 0; i <= stacks; i++) {
            float V = (float) i / stacks;
            float phi = V * (float) Math.PI;

            for (int j = 0; j <= slices; j++) {
                float U = (float) j / slices;
                float theta = U * (float) (Math.PI * 2);

                float x = (float) (radius * Math.sin(phi) * Math.cos(theta));
                float y = (float) (radius * Math.cos(phi));
                float z = (float) (radius * Math.sin(phi) * Math.sin(theta));
                verticesList.add(x);
                verticesList.add(y);
                verticesList.add(z);

                float nx = x / radius;
                float ny = y / radius;
                float nz = z / radius;
                normalsList.add(nx);
                normalsList.add(ny);
                normalsList.add(nz);

                texCoordsList.add(U);
                texCoordsList.add(1.0f - V);
            }
        }

        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j < slices; j++) {
                short first = (short) (i * (slices + 1) + j);
                short second = (short) (first + slices + 1);

                indicesList.add(first);
                indicesList.add(second);
                indicesList.add((short) (first + 1));

                indicesList.add(second);
                indicesList.add((short) (second + 1));
                indicesList.add((short) (first + 1));
            }
        }

        body.numIndices = indicesList.size();

        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(verticesList.size() * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        for (Float f : verticesList) {
            vertexBuffer.put(f);
        }
        vertexBuffer.position(0);

        FloatBuffer normalBuffer = ByteBuffer.allocateDirect(normalsList.size() * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        for (Float f : normalsList) {
            normalBuffer.put(f);
        }
        normalBuffer.position(0);

        FloatBuffer texCoordBuffer = ByteBuffer.allocateDirect(texCoordsList.size() * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        for (Float f : texCoordsList) {
            texCoordBuffer.put(f);
        }
        texCoordBuffer.position(0);

        ShortBuffer indexBuffer = ByteBuffer.allocateDirect(indicesList.size() * 2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        for (Short s : indicesList) {
            indexBuffer.put(s);
        }
        indexBuffer.position(0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            GLES30.glGenVertexArrays(1, body.vaoId, 0);
            GLES30.glBindVertexArray(body.vaoId[0]);
        }

        GLES20.glGenBuffers(1, body.vboPositionId, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, body.vboPositionId[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GLES20.GL_STATIC_DRAW);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            GLES30.glEnableVertexAttribArray(0);
            GLES30.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, 0);
        }

        GLES20.glGenBuffers(1, body.vboNormalId, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, body.vboNormalId[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, normalBuffer.capacity() * 4, normalBuffer, GLES20.GL_STATIC_DRAW);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            GLES30.glEnableVertexAttribArray(1);
            GLES30.glVertexAttribPointer(1, 3, GLES20.GL_FLOAT, false, 0, 0);
        }

        GLES20.glGenBuffers(1, body.vboTexCoordId, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, body.vboTexCoordId[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, texCoordBuffer.capacity() * 4, texCoordBuffer, GLES20.GL_STATIC_DRAW);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            GLES30.glEnableVertexAttribArray(2);
            GLES30.glVertexAttribPointer(2, 2, GLES20.GL_FLOAT, false, 0, 0);
        }

        GLES20.glGenBuffers(1, body.vboIndexId, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, body.vboIndexId[0]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * 2, indexBuffer, GLES20.GL_STATIC_DRAW);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            GLES30.glBindVertexArray(0);
        }
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        return body;
    }
}