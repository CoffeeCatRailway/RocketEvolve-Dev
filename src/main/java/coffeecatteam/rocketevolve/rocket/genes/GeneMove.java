package coffeecatteam.rocketevolve.rocket.genes;

import coffeecatteam.coffeecatutils.NumberUtils;
import coffeecatteam.coffeecatutils.position.Vector2D;
import coffeecatteam.rocketevolve.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 5/05/2019
 */
public class GeneMove extends Gene<List<Vector2D>> {

    public GeneMove() {
        chromosomes = new ArrayList<>();
        for (int i = 0; i < Game.getInstance().getLifespan(); i++)
            chromosomes.add(generateMove());
    }

    public GeneMove(List<Vector2D> data) {
        super(data);
    }

    @Override
    public <T extends Gene<List<Vector2D>>> T crossover(T partner) {
        List<Vector2D> newChromosomes = new ArrayList<>();
        int midgenes = NumberUtils.getRandomInt(0, chromosomes.size());
        for (int i = 0; i < chromosomes.size(); i++) {
            if (i > midgenes)
                newChromosomes.add(chromosomes.get(i));
            else
                newChromosomes.add(partner.getChromosomes().get(i));
        }
        return (T) new GeneMove(newChromosomes);
    }

    @Override
    public void mutate() {
        for (int i = 0; i < chromosomes.size(); i++) {
            if (chanceCommon()) {
                Vector2D moveOld = chromosomes.get(i);
                Vector2D moveNew;
                if (chanceSuperRare())
                    moveNew = generateMove();
                else {
                    if (chanceCommon())
                        moveNew = generateMove(moveOld.x, Math.random() * (Math.PI * 2));
                    else
                        moveNew = generateMove(Math.random() * (Math.PI * 2), moveOld.y);
                }
                chromosomes.set(i, moveNew);
            }
        }
    }

    private Vector2D generateMove() {
        double a = Math.random() * (Math.PI * 2);
        double b = Math.random() * (Math.PI * 2);
        return generateMove(a, b);
    }

    private Vector2D generateMove(double a, double b) {
        double x = b * Math.cos(a);
        double y = b * Math.sin(a);
        Vector2D vec = new Vector2D(x, y);
        vec.setMagnitude(Game.getInstance().getMaxForce());
        return vec;
    }
}
