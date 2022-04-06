package com.shuter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {
    Core game;
    GameWorld gameWorld;

    public GameScreen(Core game){
        this.game = game;
        this.gameWorld = new GameWorld();
        Gdx.input.setCursorCatched(true);
        System.out.println("Game Screen up");
    }

    @Override
    public void render(float delta) {
        gameWorld.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        gameWorld.resize(width,height);
    }

    @Override
    public void dispose() {
        gameWorld.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void show() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

}
