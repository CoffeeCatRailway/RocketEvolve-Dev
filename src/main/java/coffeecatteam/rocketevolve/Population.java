package coffeecatteam.rocketevolve;

import coffeecatteam.coffeecatutils.NumberUtils;
import coffeecatteam.rocketevolve.rocket.DNA;
import coffeecatteam.rocketevolve.rocket.Rocket;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 3/01/2019
 */
public class Population {

    private Rocket[] rockets;
    private List<Rocket> matingpool;
    private int popsize;

    public Population(int popsize) {
        this.popsize = popsize;

        this.rockets = new Rocket[this.popsize];
        for (int i = 0; i < this.popsize; i++) {
            this.rockets[i] = new Rocket();
        }
    }

    public void evaluate() {
        double maxfit = 0;
        for (int i = 0; i < this.popsize; i++) {
            this.rockets[i].calcFitness();
            if (this.rockets[i].getFitness() > maxfit) {
                maxfit = this.rockets[i].getFitness();
            }
        }
        Game.getInstance().getLogger().info("Max fitness [" + maxfit + "]");

        for (int i = 0; i < this.popsize; i++) {
            this.rockets[i].divFitness(maxfit);
        }

        this.matingpool = new ArrayList<>();
        for (int i = 0; i < this.popsize; i++) {
            int n = (int) (this.rockets[i].getFitness() * 100);
            for (int j = 0; j < n; j++) {
                this.matingpool.add(this.rockets[i]);
            }
        }
    }

    public void selection() {
        Rocket[] newRockets = new Rocket[rockets.length];
        for (int i = 0; i < this.rockets.length; i++) {
            DNA parentA = randomRocketFromMatingPool().getDna();
            DNA parentB = randomRocketFromMatingPool().getDna();
            DNA child = parentA.crossover(parentB);
            child.mutate();
            if (NumberUtils.getRandomFloat(5000) < 0.01f) {
                Game.getInstance().getLogger().info("A mega mutate has occurred!");
                child = new DNA();
            }
            newRockets[i] = new Rocket(child);
        }
        this.rockets = newRockets;
    }

    private Rocket randomRocketFromMatingPool() {
        int i = NumberUtils.getRandomInt(0, this.matingpool.size() - 1);
        return this.matingpool.get(i);
    }

    public Rocket randomRocket() {
        int i = NumberUtils.getRandomInt(0, this.rockets.length - 1);
        return this.rockets[i];
    }

    public void update(GameContainer container, int delta) {
        for (int i = 0; i < this.popsize; i++) {
            if (!this.rockets[i].isCrashed()) {
                this.rockets[i].update(container, delta);
            }
        }
    }

    public void render(GameContainer container, Graphics g) {
        for (int i = 0; i < this.popsize; i++) {
            this.rockets[i].render(container, g);
        }
    }

    public Rocket[] getRockets() {
        return rockets;
    }

    public List<Rocket> getMatingpool() {
        return matingpool;
    }

    public int getPopsize() {
        return popsize;
    }

    public void setPopsize(int popsize) {
        this.popsize = popsize;
    }

    public void addPopsize(int amt) {
        this.popsize += amt;
    }
}
