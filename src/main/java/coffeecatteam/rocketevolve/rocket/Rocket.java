package coffeecatteam.rocketevolve.rocket;

import coffeecatteam.coffeecatutils.NumberUtils;
import coffeecatteam.coffeecatutils.position.Vector2D;
import coffeecatteam.rocketevolve.ColorsRenderer;
import coffeecatteam.rocketevolve.Game;
import coffeecatteam.rocketevolve.collidable.Collidable;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * @author CoffeeCatRailway
 * Created: 2/01/2019
 */
public class Rocket {

    private DNA dna;

    private Vector2D pos, vel, acc;
    private boolean completed, crashed;
    private double fitness;

    private Collidable t;

    public Rocket() {
        this(new DNA());
    }

    public Rocket(DNA dna) {
        this.dna = dna;

        this.pos = new Vector2D(Game.getInstance().getWidth() / 2d, Game.getInstance().getHeight());
        this.vel = new Vector2D();
        this.acc = new Vector2D();
        this.completed = false;
        this.crashed = false;
        this.fitness = 0;

        this.t = Game.getInstance().getTargets().get(0);
    }

    public void applyForce(Vector2D force) {
        this.acc.add(force);
    }

    public void calcFitness() {
        double d = this.pos.getDistanceFrom(Game.getInstance().getTargets().get(0).pos);
        for (int i = 0; i < Game.getInstance().getTargets().size(); i++) {
            double newd = this.pos.getDistanceFrom(Game.getInstance().getTargets().get(i).pos);
            if (newd < d) {
                d = newd;
            }
        }

        this.fitness = NumberUtils.map((float) d, 0, Game.getInstance().getWidth(), Game.getInstance().getWidth(), 0);
        if (this.completed) {
            this.fitness *= 10;
        }
        if (this.crashed) {
            this.fitness /= 10;
        }
    }

    public void update(GameContainer container, int delta) {
        for (int i = 0; i < Game.getInstance().getTargets().size(); i++) {
            if (Game.getInstance().getTargets().get(i).contains(this.pos)) {
                this.completed = true;
                this.pos = Game.getInstance().getTargets().get(i).copyPos();
            }
        }

        for (int i = 0; i < Game.getInstance().getObstacles().size(); i++) {
            if (Game.getInstance().getObstacles().get(i).contains(this.pos)) {
                this.setCrashed(true);
            }
        }

        if (this.pos.x > Game.getInstance().getWidth() || this.pos.x < 0) {
            this.setCrashed(true);
        }
        if (this.pos.y > Game.getInstance().getHeight() || this.pos.y < 0) {
            this.setCrashed(true);
        }

        int index = Game.getInstance().getCurrentLifespan() - 1;
        if (index < 0) index = 0;
        this.applyForce(this.dna.getGeneMove().getChromosomes().get(index));
        if (!this.completed && !this.crashed) {
            this.vel.add(this.acc);
            this.pos.add(this.vel.mult(dna.getGeneSpeed().getChromosomes()));
            this.acc.mult(0);
            this.vel.limit(4);
        }
    }

    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
        if (this.crashed) {
            Game.getInstance().addRocketsCrashed(1);
        } else {
            Game.getInstance().addRocketsCrashed(-1);
            if (Game.getInstance().getRocketsCrashed() < 0) {
                Game.getInstance().setRocketsCrashed(0);
            }
        }
    }

    public void render(GameContainer container, Graphics g) {
        g.pushTransform();

        this.calcClosestTarget();

        Color color = this.dna.getGeneColor().getChromosomes();
        g.setColor(color);

        float x = (float) this.pos.x;
        float y = (float) this.pos.y;
        g.translate(x, y);

        float angle = (float) Math.atan2(this.vel.y, this.vel.x);
        g.rotate(0, 0, (float) Math.toDegrees(angle));

        int[] looks = this.dna.getGeneLooks().getChromosomes();
        g.fillRect(-looks[0] / 2f, -looks[1] / 2f, looks[0], looks[1]);
        if (ColorsRenderer.selected != null && ColorsRenderer.colorToString(ColorsRenderer.selected.color).equals(ColorsRenderer.colorToString(color))) {
            g.setColor(Color.white);
            g.setLineWidth(3f);
            g.drawRect(-looks[0] / 2f, -looks[1] / 2f, looks[0], looks[1]);
        }

        g.translate(-x, -y);

        g.popTransform();
    }

    public void calcClosestTarget() {
        for (int i = 0; i < Game.getInstance().getTargets().size(); i++) {
            double d1 = this.pos.getDistanceFrom(this.t.pos);
            double d2 = this.pos.getDistanceFrom(Game.getInstance().getTargets().get(i).pos);
            if (d2 < d1) {
                this.t = Game.getInstance().getTargets().get(i);
            }
        }
    }

    public DNA getDna() {
        return dna;
    }

    public void setDna(DNA dna) {
        this.dna = dna;
    }

    public Vector2D getPos() {
        return pos;
    }

    public Vector2D getVel() {
        return vel;
    }

    public Vector2D getAcc() {
        return acc;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public double getFitness() {
        return fitness;
    }

    public void divFitness(double amt) {
        fitness /= amt;
    }
}
