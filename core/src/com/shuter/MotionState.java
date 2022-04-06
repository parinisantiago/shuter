package com.shuter;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class MotionState extends btMotionState {
    private final Matrix4 transform;

    public MotionState(final Matrix4 transform){
        this.transform = transform;
    }

    @Override
    public void getWorldTransform(final Matrix4 worldTransform){
        worldTransform.set(this.transform);
    }

    @Override
    public void setWorldTransform(final Matrix4 worldTransform){
        this.transform.set(worldTransform);
    }
}
