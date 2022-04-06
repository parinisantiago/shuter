package com.shuter;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.shuter.components.CharacterComponent;
import com.shuter.components.ModelComponent;
import com.shuter.managers.EntityFactory;
import com.shuter.systems.*;

public class GameWorld {

    private static final float FOV = 67F;
    //renderiza los modelos
    private ModelBatch batch;
    //Maneja la luz
    private Environment environment;
    private PerspectiveCamera camera;
    private Engine engine;
    public CollisionSystem collisionSystem;
    public ModelBuilder modelBuilder = new ModelBuilder();
    //jugador
    private Entity character;
    public Model wallHorizontal = modelBuilder.createBox(40,20,1,
            new Material(
                    ColorAttribute.createDiffuse(Color.WHITE),
                    ColorAttribute.createSpecular(Color.RED),
                    FloatAttribute.createShininess(16f)),
            VertexAttribute.Position().usage | VertexAttribute.Normal().usage);
    public Model wallVertical = modelBuilder.createBox(1,20,40,
            new Material(
                    ColorAttribute.createDiffuse(Color.GREEN),
                    ColorAttribute.createSpecular(Color.WHITE),
                    FloatAttribute.createShininess(16f)),
            VertexAttribute.Position().usage | VertexAttribute.Normal().usage);
    public Model groundModel = modelBuilder.createBox(40,1,40,
            new Material(
                    ColorAttribute.createDiffuse(Color.YELLOW),
                    ColorAttribute.createSpecular(Color.BLACK),
                    FloatAttribute.createShininess(16f)),
            VertexAttribute.Position().usage | VertexAttribute.Normal().usage);

    public GameWorld(){
        Bullet.init();
        this.initCamera();
        this.initEnviroment();
        this.initBatch();
        this.addSystems();
        this.addEntities();
        System.out.println("GameWorld Up");
    }

    private void addEntities() {
        this.createGround();
        this.createPlayer(5,3,5);
    }

    private void createPlayer(float x, float y , float z) {
        this.character = EntityFactory.createPlayer(this.collisionSystem, x, y, z);
        this.engine.addEntity(this.character);
    }

    private void createGround() {
        engine.addEntity(EntityFactory.createStaticEntity(groundModel,0,0,0));
        engine.addEntity(EntityFactory.createStaticEntity(wallHorizontal, 0, 10, -20));
        engine.addEntity(EntityFactory.createStaticEntity(wallHorizontal, 0, 10, 20));
        engine.addEntity(EntityFactory.createStaticEntity(wallVertical, 20, 10, 0));
        engine.addEntity(EntityFactory.createStaticEntity(wallVertical, -20, 10, 0));
    }

    private void addSystems(){
        this.engine = new Engine();
        this.engine.addSystem(new RenderSystem(this.batch, this.environment));
        this.engine.addSystem(this.collisionSystem = new CollisionSystem());
        this.engine.addSystem(new PlayerSystem(this, this.camera));
        this.engine.addSystem(new EnemySystem(this));
        this.engine.addSystem(new StatusSystem(this));
    }

    private void initCamera(){
        this.camera = new PerspectiveCamera(FOV,Core.VIRTUAL_WIDTH,Core.VIRTUAL_HEIGHT);
    }

    private void initEnviroment(){
        this.environment = new Environment();
        this.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f,0.3f, 0.3f, 1f));
    }

    private void initBatch(){
        this.batch = new ModelBatch();
    }

    public void dispose(){
        this.collisionSystem.collisionWorld.removeAction(character.getComponent(CharacterComponent.class).characterController);
        this.collisionSystem.collisionWorld.removeCollisionObject(character.getComponent(CharacterComponent.class).ghostObject);
        this.collisionSystem.dispose();
        this.collisionSystem = null;
        this.wallHorizontal.dispose();
        this.wallVertical.dispose();
        this.groundModel.dispose();
        this.batch.dispose();
        this.batch = null;
        this.character.getComponent(CharacterComponent.class).characterController.dispose();
        this.character.getComponent(CharacterComponent.class).ghostObject.dispose();
        this.character.getComponent(CharacterComponent.class).ghostShape.dispose();
    }

    public void resize(int width, int height){
        this.camera.viewportWidth = width;
        this.camera.viewportHeight = height;
    }

    public void render(float delta){
        this.renderWorld(delta);
    }

    private void renderWorld(float delta) {
        this.batch.begin(this.camera);
        engine.update(delta);
        this.batch.end();
    }

    public void remove(Entity entity) {
        engine.removeEntity(entity);
        collisionSystem.removeBody(entity);
    }
}
