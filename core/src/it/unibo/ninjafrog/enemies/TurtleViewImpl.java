package it.unibo.ninjafrog.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import it.unibo.ninjafrog.game.utilities.GameConst;
import it.unibo.ninjafrog.screens.PlayScreen;

public class TurtleViewImpl extends Sprite implements TurtleView {
    private TurtleState currentState;
    private TurtleState previousState;
    private final EnemyController controller;
    private final PlayScreen screen;
    private Array<TextureRegion> frames;
    private final TextureRegion spikes;
    private final TextureRegion noSpikes;
    private final Animation<TextureRegion> spikesInAnimation;
    private final Animation<TextureRegion> spikesOutAnimation;
    private float time;
    public TurtleViewImpl(final PlayScreen screen, final float x, final float y, final EnemyControllerImpl enemyControllerImpl) {
        this.controller = enemyControllerImpl;
        this.screen = screen;
        setPosition(x, y);
        frames = new Array<>();
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("ninjaAndEnemies"), i * 44, 98, 44, 27));
        }
        spikesInAnimation = new Animation<>(0.1f, frames);
        frames.clear();
        frames = new Array<>();
        for (int i = 7; i >= 0; i--) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("ninjaAndEnemies"), i * 44, 98, 44, 27));
        }
        spikesOutAnimation = new Animation<>(0.1f, frames);
        spikes = new TextureRegion(screen.getAtlas().findRegion("ninjaAndEnemies"), 7 * 44, 98, 44, 27);
        noSpikes = new TextureRegion(screen.getAtlas().findRegion("ninjaAndEnemies"), 0, 98, 44, 27);
        setBounds(getX(), getY(), 25 / GameConst.PPM, 17 / GameConst.PPM);
        this.currentState = TurtleState.NO_SPIKES;
    }

    @Override
    public final void update(final Body body, final float dt) {
        // TODO Auto-generated method stub
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(body, dt));
    }

    @Override
    public final void draw(final Batch batch) {
        if (!controller.isDestroyed(this) || controller.getStateTime(this) < 0.5) {
            super.draw(batch);
        }
    }

    private TextureRegion getFrame(final Body body, final float dt) {
        currentState = getState(dt);
        TextureRegion region;
        switch (currentState) {
        case SPIKES_IN:
            region = this.spikesInAnimation.getKeyFrame(time);
            break;
        case SPIKES_OUT:
            region = this.spikesOutAnimation.getKeyFrame(time);
            break;
        case NO_SPIKES:
            region = this.noSpikes;
            break;
        case SPIKES:
        default:
            region = this.spikes;
            break;
        }
        time = currentState == previousState ? time + dt : 0;
        previousState = currentState;
        return region;
    }

    private TurtleState getState(final float dt) {
        if (this.currentState == TurtleState.NO_SPIKES || this.currentState == TurtleState.SPIKES) {
            if (this.time > 2) {
                if (this.currentState == TurtleState.NO_SPIKES) {
                    this.time = 0;
                    return TurtleState.SPIKES_IN;
                } else {
                    this.time = 0;
                    return TurtleState.SPIKES_OUT;
                }
            } else {
                return this.currentState;
                }
            } else {
               if (this.time > 2) {
                   if (this.currentState == TurtleState.SPIKES_IN) {
                        this.time = 0 ;
                        return TurtleState.SPIKES;
                    } else {
                        this.time = 0;
                    return TurtleState.NO_SPIKES;
                    }
            } else {
                return this.currentState;
            }
        }
    }
    public final void setDesthRegion() {
          setRegion(new TextureRegion(screen.getAtlas().findRegion("ninjaAndEnemies"), 440, 98, 45, 27));
    }
    @Override
    public final boolean hasSpike() {
        if (this.currentState == TurtleState.SPIKES || this.currentState == TurtleState.SPIKES_IN) {
            return true;
        }
        return false;
    }



}
