package coffeecatteam.rocketevolve.rocket.genes;

import coffeecatteam.coffeecatutils.NumberUtils;

/**
 * @author CoffeeCatRailway
 * Created: 6/05/2019
 */
public class GeneFloat extends Gene<Float> {

    public GeneFloat() {
        chromosomes = 1.0f;
    }

    public GeneFloat(Float chromosomes) {
        super(chromosomes);
    }

    @Override
    public <T extends Gene<Float>> T crossover(T partner) {
        float newChromosome;

        if (chanceUncommon())
            newChromosome = (chromosomes / 2f) + (partner.getChromosomes() / 2f);
        else {
            if (chanceCommon())
                newChromosome = chromosomes;
            else
                newChromosome = partner.getChromosomes();
        }

        return (T) new GeneFloat(newChromosome);
    }

    @Override
    public void mutate() {
        if (chanceUncommon())
            chromosomes += NumberUtils.getRandomFloat(-1f, 1f);
        if (chanceSuperRare())
            chromosomes += NumberUtils.getRandomFloat(-2f, 2f);
    }
}
