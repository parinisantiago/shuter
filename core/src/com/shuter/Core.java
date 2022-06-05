package com.shuter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.shuter.screens.MainMenuScreen;

public class Core extends ApplicationAdapter {
    public static final float VIRTUAL_WIDTH = 960;
    public static final float VIRTUAL_HEIGHT = 540;
    Screen screen;

    @Override
    public void create(){
        new Assets();
        new Settings().load();
        setScreen(new MainMenuScreen(this));
        System.out.println("Core up");
    }

    @Override
    public void render(){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height){
        screen.resize(width,height);
    }

    @Override
    public void dispose(){
        Assets.dispose();
        Settings.save();
    }

    public void setScreen(Screen screen){
        if(this.screen != null){
            this.screen.hide();
            this.screen.dispose();
        }
        this.screen = screen;
        if(this.screen != null){
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }
}
