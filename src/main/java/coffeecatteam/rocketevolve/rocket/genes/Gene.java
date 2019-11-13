package coffeecatteam.rocketevolve.rocket.genes;

import coffeecatteam.coffeecatutils.NumberUtils;

/**
 * @author CoffeeCatRailway
 * Created: 5/05/2019
 */
public abstract class Gene<E> {

    protected E chromosomes;

    public Gene() {
    }

    public Gene(E chromosomes) {
        this.chromosomes = chromosomes;
    }

    public abstract <T extends Gene<E>> T crossover(T partner);

    public abstract void mutate();

    public E getChromosomes() {
        return chromosomes;
    }

    protected boolean chanceCommon() {
        return NumberUtils.getRandomFloat(10f) < 0.05f;
    }

    protected boolean chanceUncommon() {
        return NumberUtils.getRandomFloat(25f) < 0.05f;
    }

    protected boolean chanceRare() {
        return NumberUtils.getRandomFloat(100f) < 0.01f;
    }

    protected boolean chanceSuperRare() {
        return NumberUtils.getRandomFloat(1000f) < 0.01f;
    }
}
