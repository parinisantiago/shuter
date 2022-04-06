package com.shuter.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.shuter.components.CharacterComponent;
import com.shuter.components.CollisionComponent;
import com.shuter.listeners.MyContactListener;

public class CollisionSystem extends EntitySystem implements EntityListener {

    public final btCollisionConfiguration collisionConfiguration;
    public final btCollisionDispatcher dispatcher;
    public final btBroadphaseInterface broadphase;
    public final btConstraintSolver solver;
    public final btDiscreteDynamicsWorld collisionWorld;
    private btGhostPairCallback ghostPairCallback;
    public int maxSubsSteps = 5;
    public float fixedTimeStep = 1f / 60f;

    public CollisionSystem(){
        MyContactListener contactListener = new MyContactListener();
        contactListener.enable();
        this.collisionConfiguration = new btDefaultCollisionConfiguration();
        this.dispatcher = new btCollisionDispatcher(this.collisionConfiguration);
        this.broadphase = new btAxisSweep3(
                new Vector3(-1000,-1000,-1000),
                new Vector3(1000,1000,1000)
        );
        this.solver = new btSequentialImpulseConstraintSolver();
        this.collisionWorld = new btDiscreteDynamicsWorld(
                this.dispatcher,this.broadphase, this.solver,this.collisionConfiguration);
        this.ghostPairCallback = new btGhostPairCallback();
        this.broadphase.getOverlappingPairCache().setInternalGhostPairCallback(ghostPairCallback);
        this.collisionWorld.setGravity(new Vector3(0,-0.5f,0));
    }

    @Override
    public void addedToEngine(Engine engine){
        engine.addEntityListener(Family.all(CollisionComponent.class).get(),this);
    }

    @Override
    public void update(float deltaTime){
        collisionWorld.stepSimulation(deltaTime, maxSubsSteps, fixedTimeStep);
    }

    @Override
    public void entityAdded(Entity entity) {
        CollisionComponent collisionComponent = entity.getComponent(CollisionComponent.class);
        if(collisionComponent.body != null){
            this.collisionWorld.addRigidBody((btRigidBody) collisionComponent.body);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {

    }

    public void dispose(){
        this.collisionWorld.dispose();
        if(this.solver != null) this.solver.dispose();
        if(this.broadphase != null) this.broadphase.dispose();
        if(this.dispatcher != null) this.dispatcher.dispose();
        if(this.collisionConfiguration != null) this.collisionConfiguration.dispose();
    }

    public void removeBody(Entity entity){
        CollisionComponent collisionComponent = entity.getComponent(CollisionComponent.class);
        if(collisionComponent != null) this.collisionWorld.removeCollisionObject(collisionComponent.body);
        CharacterComponent character = entity.getComponent(CharacterComponent.class);
        if (character != null) {
            collisionWorld.removeAction(character.characterController);
            collisionWorld.removeCollisionObject(character.ghostObject);
        }
    }
}
