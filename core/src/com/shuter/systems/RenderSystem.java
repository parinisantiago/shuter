package com.shuter.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.shuter.components.ModelComponent;

public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ModelBatch batch;
    private Environment environment;

    public RenderSystem(ModelBatch batch, Environment environment){
        this.batch = batch;
        this.environment = environment;
    }

    public void addedToEngine(Engine e){
        entities = e.getEntitiesFor(Family.all(ModelComponent.class).get());
    }

    public void update(float delta){
        for (Entity entity: entities) {
            ModelComponent modelComponent = entity.getComponent(ModelComponent.class);
            batch.render(modelComponent.instance,environment);
        }
    }
}
