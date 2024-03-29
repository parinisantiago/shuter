package com.shuter.listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.shuter.components.CharacterComponent;
import com.shuter.components.EnemyComponent;
import com.shuter.components.PlayerComponent;
import com.shuter.components.StatusComponent;

public class MyContactListener extends ContactListener {
    @Override
    public void onContactStarted(btCollisionObject colObj0, btCollisionObject colObj1){
        if(colObj0.userData instanceof Entity && colObj1.userData instanceof Entity){
            Entity entity0 = (Entity) colObj0.userData;
            Entity entity1 = (Entity) colObj1.userData;
            if(entity0.getComponent(CharacterComponent.class) != null && entity1.getComponent(CharacterComponent.class) != null){
                if(entity0.getComponent(EnemyComponent.class) != null){
                    if(entity0.getComponent(StatusComponent.class).alive)
                        entity1.getComponent(PlayerComponent.class).health -= 10;
                    entity0.getComponent(StatusComponent.class).alive = false;
                } else {
                    if(entity1.getComponent(StatusComponent.class).alive)
                        entity0.getComponent(PlayerComponent.class).health -= 10;
                    entity1.getComponent(StatusComponent.class).alive = false;
                }
            }
        }
    }
}
