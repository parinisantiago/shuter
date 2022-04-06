package com.shuter.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.shuter.MotionState;

public class CollisionComponent implements Component {
    //Informacion de la posicion y angulo del objeto
    public MotionState motionState;
    //El informacion del cuerpo del elemento: contiene la masa del objeto, el movimiento
    public btRigidBody.btRigidBodyConstructionInfo bodyInfo;
    //El cuerpo propiamente dicho que se agregara al sistema de colisiones
    //El cuerpo puede ser estatico(como una arbol) o dinamico (como el jugador o npc)
    public btCollisionObject body;
}
