package coffeecatteam.rocketevolve.collidable;

import coffeecatteam.coffeecatutils.position.Vector2D;
import coffeecatteam.rocketevolve.Game;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * @author CoffeeCatRailway
 * Created: 2/01/2019
 */
public abstract class Collidable {

    public Vector2D pos;
    public int width, height;
    public Color color;

    public Collidable(Vector2D pos) {
        this(pos, 10, 10);
    }

    public Collidable(Vector2D pos, int width, int height) {
        this(pos, width, height, Color.white);
    }

    public Collidable(Vector2D pos, int width, int height, Color color) {
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public abstract boolean contains(Vector2D pos);

    public void update(GameContainer container, int delta) {
        if (pos.x < 0) pos.x = 0;
        if (pos.x + width > Game.getInstance().getWidth()) pos.x = Game.getInstance().getWidth() - width;
        if (pos.y < 0) pos.y = 0;
        if (pos.y + height > Game.getInstance().getHeight()) pos.y = Game.getInstance().getHeight() - height;
    }

    public abstract void render(GameContainer container, Graphics g);

    public Vector2D copyPos() {
        return this.pos.copy();
    }
}
