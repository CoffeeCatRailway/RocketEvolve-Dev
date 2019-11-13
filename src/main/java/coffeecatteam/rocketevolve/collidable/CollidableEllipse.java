package coffeecatteam.rocketevolve.collidable;

import coffeecatteam.coffeecatutils.position.Vector2D;
import coffeecatteam.rocketevolve.Game;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * @author CoffeeCatRailway
 * Created: 2/01/2019
 */
public class CollidableEllipse extends Collidable {

    public CollidableEllipse(Vector2D pos, int radius, Color color) {
        super(pos, radius, radius, color);
    }

    @Override
    public boolean contains(Vector2D pos) {
        return this.pos.getDistanceFrom(pos) < width;
    }

    @Override
    public void update(GameContainer container, int delta) {
        super.update(container, delta);

        if (Game.getInstance().isLeftDown() && contains(new Vector2D(Game.getInstance().getMouseX(), Game.getInstance().getMouseY()))) {
            if (container.getInput().isKeyDown(Input.KEY_LSHIFT)) {
                int nw = (int) ((Game.getInstance().getMouseX() > pos.x) ? Game.getInstance().getMouseX() - pos.x : pos.x - Game.getInstance().getMouseX()) + 10;
                if (nw < 0) nw = 0;
                this.width = nw;
            } else
                this.pos = new Vector2D(Game.getInstance().getMouseX() - width / 8d, Game.getInstance().getMouseY() - width / 8d);
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        g.setColor(color);
        g.fillOval((float) (pos.x - width), (float) (pos.y - width), (float) width * 2, (float) width * 2);
    }
}
