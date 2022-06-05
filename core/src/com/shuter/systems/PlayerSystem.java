package com.shuter.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.shuter.GameWorld;
import com.shuter.Settings;
import com.shuter.components.*;
import com.badlogic.gdx.graphics.Camera;
import com.shuter.ui.GameUI;

import java.awt.font.GlyphMetrics;

public class PlayerSystem extends EntitySystem implements EntityListener {
    private Entity player;
    private PlayerComponent playerComponent;
    private CharacterComponent characterComponent;
    private ModelComponent modelComponent;
    private final Vector3 tmp = new Vector3();
    private final Camera camera;
    private GameWorld gameWorld;
    private Vector3 rayFrom = new Vector3();
    private Vector3 rayTo = new Vector3();
    private ClosestRayResultCallback rayTestCB;
    private GameUI gameUI;

    public PlayerSystem(GameWorld gameWorld, Camera camera, GameUI gameUI){
        this.camera = camera;
        this.rayTestCB = new ClosestRayResultCallback(Vector3.Zero, Vector3.Z);
        this.gameWorld = gameWorld;
        this.gameUI = gameUI;
    }

    @Override
    public void addedToEngine(Engine engine){
        engine.addEntityListener(Family.all(PlayerComponent.class).get(),this);
    }

    @Override
    public void entityAdded(Entity entity) {
        this.player = entity;
        this.playerComponent = entity.getComponent(PlayerComponent.class);
        this.characterComponent = entity.getComponent(CharacterComponent.class);
        this.modelComponent = entity.getComponent(ModelComponent.class);
    }

    @Override
    public void entityRemoved(Entity entity) {

    }

    @Override
    public void update(float delta){
        if(this.player == null) return;
        this.updateMovement(delta);
        updateStatus();
        checkGameOver();
    }

    private void checkGameOver() {
        if (playerComponent.health <= 0 && !Settings.Paused) {
            Settings.Paused = true;
            gameUI.gameOverWidget.gameOver();
        }
    }

    private void updateStatus() {
        gameUI.healthWidget.setValue(playerComponent.health);
    }

    private void updateMovement(float delta) {
        float deltaX = -Gdx.input.getDeltaX();
        float deltaY = -Gdx.input.getDeltaY();
        this.tmp.set(0,0,0);
        this.camera.rotate(this.camera.up,deltaX);
        this.tmp.set(this.camera.direction).crs(this.camera.up).nor();
        this.camera.direction.rotate(this.tmp, deltaY);
        this.tmp.set(0,0,0);
        this.characterComponent.characterDirection.set(-1, 0, 0).rot(modelComponent.instance.transform).nor();
        this.characterComponent.walkDirection.set(0,0,0);
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            this.characterComponent.walkDirection.add(camera.direction);
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            this.tmp.set(camera.direction).crs(camera.up).scl(-1);
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            this.characterComponent.walkDirection.sub(camera.direction);
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            this.tmp.set(camera.direction).crs(camera.up);
        if(Gdx.input.justTouched()) fire();
        characterComponent.walkDirection.add(tmp);
        characterComponent.walkDirection.scl(100f * delta);
        characterComponent.characterController.setWalkDirection(characterComponent.walkDirection);
        //move
        Matrix4 ghost = new Matrix4();
        Vector3 translation = new Vector3();
        this.characterComponent.ghostObject.getWorldTransform(ghost);
        ghost.getTranslation(translation);
        this.modelComponent.instance.transform.set(
            translation.x, translation.y, translation.z,
            this.camera.direction.x, this.camera.direction.y, this.camera.direction.z, 0
        );
        this.camera.position.set(translation.x, translation.y, translation.z);
        camera.update(true);
    }

    private void fire(){
        Ray ray = camera.getPickRay(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        rayFrom.set(ray.origin);
        rayTo.set(ray.direction).scl(50f).add(rayFrom);
        rayTestCB.setCollisionObject(null);
        rayTestCB.setClosestHitFraction(1f);
        rayTestCB.setRayFromWorld(rayFrom);
        rayTestCB.setRayToWorld(rayTo);
        gameWorld.collisionSystem.collisionWorld.rayTest(rayFrom,rayTo,rayTestCB);
        if(rayTestCB.hasHit()){
            final btCollisionObject obj = rayTestCB.getCollisionObject();
            if(((Entity) obj.userData).getComponent(EnemyComponent.class) != null){
                ((Entity) obj.userData).getComponent(StatusComponent.class).alive = false;
                PlayerComponent.score += 100;
            }
        }
    }
}
