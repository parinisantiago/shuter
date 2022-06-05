package com.shuter.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shuter.Core;
import com.shuter.ui.widgets.*;

public class GameUI {
    private Core game;
    public Stage stage;
    public HealthWidget healthWidget;
    public ScoreWidget scoreWidget;
    public PauseWidget pauseWidget;
    public CrosshairWidget crosshairWidget;
    public GameOverWidget gameOverWidget;

    public GameUI(Core game){
        this.game = game;
        stage = new Stage(new FitViewport(Core.VIRTUAL_WIDTH, Core.VIRTUAL_HEIGHT));
        setWidgets();
        configureWidgets();
    }

    public void setWidgets(){
        healthWidget = new HealthWidget();
        scoreWidget = new ScoreWidget();
        pauseWidget = new PauseWidget(game, stage);
        crosshairWidget = new CrosshairWidget();
        gameOverWidget = new GameOverWidget(game, stage);
    }

    public void configureWidgets(){
        healthWidget.setSize(140, 25);
        healthWidget.setPosition(Core.VIRTUAL_WIDTH / 2 - healthWidget.getWidth() / 2, 0);
        scoreWidget.setSize(140, 25);
        scoreWidget.setPosition(0, Core.VIRTUAL_HEIGHT - scoreWidget.getHeight());
        pauseWidget.setSize(64, 64);
        pauseWidget.setPosition(Core.VIRTUAL_WIDTH - pauseWidget.getWidth(), Core.VIRTUAL_HEIGHT - pauseWidget.getHeight());
        gameOverWidget.setSize(280, 100);
        gameOverWidget.setPosition(Core.VIRTUAL_WIDTH / 2 - 280 / 2, Core.VIRTUAL_HEIGHT / 2);
        crosshairWidget.setSize(32, 32);
        crosshairWidget.setPosition(Core.VIRTUAL_WIDTH / 2 - 16, Core.VIRTUAL_HEIGHT / 2 - 16);
        stage.addActor(healthWidget);
        stage.addActor(scoreWidget);
        stage.addActor(crosshairWidget);
        stage.addActor(pauseWidget);
        stage.setKeyboardFocus(pauseWidget);
    }

    public void update(float delta){
        stage.act(delta);
    }

    public void render() {
        stage.draw();
    }

    public void resize(int width, int height){
        stage.getViewport().update(width,height);
    }

    public void dispose(){
        stage.dispose();
    }
}
