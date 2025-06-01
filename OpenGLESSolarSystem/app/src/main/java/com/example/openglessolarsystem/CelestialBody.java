package com.example.openglessolarsystem;

public class CelestialBody {
    public int[] vaoId = new int[1];

    public int[] vboPositionId = new int[1];
    public int[] vboNormalId = new int[1];
    public int[] vboTexCoordId = new int[1];
    public int[] vboIndexId = new int[1];

    public int numIndices;

    public float[] color;
    public float orbitSpeed;
    public float rotationSpeed;
    public float orbitRadius;
    public float radius;
}