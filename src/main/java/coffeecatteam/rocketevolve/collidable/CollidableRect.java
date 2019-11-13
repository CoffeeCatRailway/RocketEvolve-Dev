package coffeecatteam.rocketevolve.collidable;

import coffeecatteam.coffeecatutils.NumberUtils;
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
public class CollidableRect extends Collidable {

    public CollidableRect(Vector2D pos, int width, int height) {
        super(pos, width, height);
        int cOff = NumberUtils.getRandomInt(127);
        this.color = new Color(255 - cOff, 255 - cOff, 255 - cOff);
    }

    @Override
    public boolean contains(Vector2D pos) {
        return pos.x > this.pos.x && pos.x < this.pos.x + this.width && pos.y > this.pos.y && pos.y < this.pos.y + this.height;
    }

    @Override
    public void update(GameContainer container, int delta) {
        super.update(container, delta);

        if (Game.getInstance().isLeftDown() && contains(new Vector2D(Game.getInstance().getMouseX(), Game.getInstance().getMouseY()))) {
            if (container.getInput().isKeyDown(Input.KEY_LSHIFT)) {
                int nw = (int) ((Game.getInstance().getMouseX() > pos.x) ? Game.getInstance().getMouseX() - pos.x : pos.x - Game.getInstance().getMouseX()) + 10;
                if (nw < 0) nw = 0;
                int nh = (int) ((Game.getInstance().getMouseY() > pos.y) ? Game.getInstance().getMouseY() - pos.y : pos.y - Game.getInstance().getMouseY()) + 10;
                if (nh < 0) nh = 0;
                this.width = nw;
                this.height = nh;
            } else
                this.pos = new Vector2D(Game.getInstance().getMouseX() - width / 2d, Game.getInstance().getMouseY() - height / 2d);
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        g.setColor(color);
        g.fillRect((float) pos.x, (float) pos.y, width, height);
    }
}
