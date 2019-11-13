package coffeecatteam.rocketevolve.rocket.genes;

import coffeecatteam.coffeecatutils.NumberUtils;
import coffeecatteam.rocketevolve.Game;
import org.newdawn.slick.Color;

/**
 * @author CoffeeCatRailway
 * Created: 5/05/2019
 */
public class GeneColor extends Gene<Color> {

    public GeneColor() {
        chromosomes = Game.getInstance().getColor();
    }

    public GeneColor(Color chromosomes) {
        super(chromosomes);
    }

    @Override
    public <T extends Gene<Color>> T crossover(T partner) {
        int r = crossoverRGBA((int) chromosomes.r, (int) partner.getChromosomes().r);
        int g = crossoverRGBA((int) chromosomes.g, (int) partner.getChromosomes().g);
        int b = crossoverRGBA((int) chromosomes.b, (int) partner.getChromosomes().b);
        int a = crossoverRGBA((int) chromosomes.a, (int) partner.getChromosomes().a);

        Color newChromosomes = new Color(r, g, b, a);
        return (T) new GeneColor(newChromosomes);
    }

    private int crossoverRGBA(int partner1, int partner2) {
        int newRGBA;

        if (chanceSuperRare()) {
            int div = 4;
            int ca = partner1 / div;
            if (chanceSuperRare()) {
                ca += NumberUtils.getRandomInt(-ca, ca);
            }

            int cb = partner2 / div;
            if (chanceSuperRare()) {
                cb += NumberUtils.getRandomInt(-cb, cb);
            }

            newRGBA = ca + cb;
        } else {
            if (chanceCommon()) {
                newRGBA = partner1;
            } else {
                newRGBA = partner2;
            }
        }

        return newRGBA;
    }

    @Override
    public void mutate() {
        chromosomes.r = mutateRGBA((int) chromosomes.r);
        chromosomes.g = mutateRGBA((int) chromosomes.g);
        chromosomes.b = mutateRGBA((int) chromosomes.b);
        chromosomes.a = mutateRGBA((int) chromosomes.a);
    }

    private int mutateRGBA(int rgba) {
        int newRGBA = rgba;

        if (chanceCommon()) {
            if (chanceCommon()) {
                newRGBA = NumberUtils.getRandomInt(0, 255);
            } else {
                newRGBA = rgba + NumberUtils.getRandomInt(-150, 150);
            }
        }

        return newRGBA;
    }
}
