package com.shuter.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component {
    public float energy;
    public float oxygen;
    public float health;
    public static int score;
    public PlayerComponent(){
        this.energy = 100;
        this.oxygen = 100;
        this.health = 100;
        PlayerComponent.score = 0;
    }
}
