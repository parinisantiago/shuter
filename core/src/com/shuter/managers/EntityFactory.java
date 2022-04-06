package com.shuter.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.shuter.MotionState;
import com.shuter.components.*;
import com.shuter.systems.CollisionSystem;

public class EntityFactory {

    private static ModelBuilder modelBuilder;
    private static Texture playerTexture;
    private static Material material;
    private static Model playerModel;

    static {
        modelBuilder = new ModelBuilder();
        playerTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
        material = new Material(
                TextureAttribute.createDiffuse(playerTexture),
                ColorAttribute.createSpecular(1,1,1,1),
                FloatAttribute.createShininess(8f));
        playerModel = modelBuilder.createCapsule(2f,6f,16,material,
                VertexAttribute.Position().usage | VertexAttribute.Normal().usage
                );
    }

    private static Entity createCharacter(CollisionSystem collisionSystem, float x, float y, float z){
        Entity entity = new Entity();
        ModelComponent modelComponent = new ModelComponent(playerModel, x, y, z);
        entity.add(modelComponent);

        CharacterComponent characterComponent = new CharacterComponent();
        characterComponent.ghostObject = new btPairCachingGhostObject();
        characterComponent.ghostObject.setWorldTransform(modelComponent.instance.transform);
        characterComponent.ghostShape = new btCapsuleShape(2f,2f);
        characterComponent.ghostObject.setCollisionShape(characterComponent.ghostShape);
        characterComponent.ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
        characterComponent.characterController =
                new btKinematicCharacterController(characterComponent.ghostObject,
                        characterComponent.ghostShape, .35f, new Vector3(0,1f,0));
        characterComponent.ghostObject.userData = entity;
        entity.add(characterComponent);

        collisionSystem.collisionWorld.addCollisionObject(
                entity.getComponent(CharacterComponent.class).ghostObject,
                (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
                (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);

        collisionSystem.collisionWorld.addAction(entity.getComponent(CharacterComponent.class).characterController);
        return entity;
    }

    public static Entity createPlayer(CollisionSystem collisionSystem, float x, float y, float z){
        Entity entity = createCharacter(collisionSystem, x, y, z);
        entity.add(new PlayerComponent());
        return entity;
    }

    public static Entity createStaticEntity(Model model, float x, float y, float z) {
        final BoundingBox boundingBox = new BoundingBox();
        model.calculateBoundingBox(boundingBox);
        Vector3 tmpV = new Vector3();
        btCollisionShape col = new btBoxShape(tmpV.set(
                boundingBox.getWidth() * 0.5f,
                boundingBox.getHeight() * 0.5f,
                boundingBox.getDepth() * 0.5f
                ));
        Entity entity = new Entity();
        ModelComponent modelComponent = new ModelComponent(model, x, y, z);
        entity.add(modelComponent);
        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(0,null,col, Vector3.Zero);
        collisionComponent.body = new btRigidBody(collisionComponent.bodyInfo);
        collisionComponent.body.userData = entity;
        collisionComponent.motionState = new MotionState(modelComponent.instance.transform);
        ((btRigidBody) collisionComponent.body).setMotionState(collisionComponent.motionState);
        entity.add(collisionComponent);
        return entity;
    }

    public static Entity createEnemy(CollisionSystem collisionSystem, float x, float y, float z){
        Entity entity = createCharacter(collisionSystem, x, y ,z);
        entity.add(new EnemyComponent(EnemyComponent.STATE.HUNTING));
        entity.add(new StatusComponent());
        return entity;
    }
}
