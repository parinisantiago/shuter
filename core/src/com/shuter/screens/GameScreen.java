package com.shuter.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.shuter.Core;
import com.shuter.GameWorld;
import com.shuter.Settings;
import com.shuter.ui.GameUI;

public class GameScreen implements Screen {
    Core game;
    GameWorld gameWorld;
    GameUI gameUI;

    public GameScreen(Core game){
        this.game = game;
        this.gameUI = new GameUI(game);
        this.gameWorld = new GameWorld(gameUI);
        Settings.Paused = false;
        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void render(float delta) {
        gameUI.update(delta);
        gameWorld.render(delta);
        gameUI.render();
    }

    @Override
    public void resize(int width, int height) {
        gameUI.resize(width, height);
        gameWorld.resize(width,height);
    }

    @Override
    public void dispose() {
        gameWorld.dispose();
        gameUI.dispose();
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
