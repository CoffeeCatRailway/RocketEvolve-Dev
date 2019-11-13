package coffeecatteam.rocketevolve.rocket.genes;

import coffeecatteam.coffeecatutils.NumberUtils;

/**
 * @author CoffeeCatRailway
 * Created: 5/05/2019
 */
public class GeneLooks extends Gene<int[]> {

    private int minLengthGene = 30, maxLengthGene = 50;
    private int minWidthGene = 5, maxWidthGene = 15;

    public GeneLooks() {
        chromosomes = new int[]{
                NumberUtils.getRandomInt(minLengthGene, maxLengthGene),
                NumberUtils.getRandomInt(minWidthGene, maxWidthGene)
        };
    }

    public GeneLooks(int[] chromosomes) {
        super(chromosomes);
    }

    @Override
    public <T extends Gene<int[]>> T crossover(T partner) {
        // Choose a length gene from the parents
        int newLength;
        if (chanceRare()) {
            int div = 4;
            int la = chromosomes[0] / div;
            if (chanceCommon())
                la += NumberUtils.getRandomInt(-la, la);

            int lb = partner.getChromosomes()[0] / div;
            if (chanceCommon())
                lb += NumberUtils.getRandomInt(-lb, lb);

            newLength = la + lb;
        } else {
            if (chanceCommon())
                newLength = chromosomes[0];
            else
                newLength = partner.getChromosomes()[0];
        }

        // Choose a length gene from the parents
        int newWidth;
        if (chanceRare()) {
            int div = 4;
            int la = chromosomes[1] / div;
            if (chanceCommon())
                la += NumberUtils.getRandomInt(-la, la);

            int lb = partner.getChromosomes()[1] / div;
            if (chanceCommon())
                lb += NumberUtils.getRandomInt(-lb, lb);

            newWidth = la + lb;
        } else {
            if (chanceCommon())
                newWidth = chromosomes[1];
            else
                newWidth = partner.getChromosomes()[1];
        }

        int[] newChromosomes = new int[]{newLength, newWidth};
        this.checkSizeGenes(newChromosomes);
        return (T) new GeneLooks(newChromosomes);
    }

    @Override
    public void mutate() {
        // Mutate the length gene
        if (chanceRare())
            chromosomes[0] = NumberUtils.getRandomInt(this.minLengthGene, this.maxLengthGene);

        // Mutate the width gene
        if (chanceRare())
            chromosomes[1] = NumberUtils.getRandomInt(this.minWidthGene, this.maxWidthGene);

        this.checkSizeGenes(chromosomes);
    }

    private void checkSizeGenes(int[] chromosomes) {
        if (chromosomes[0] < this.minLengthGene)
            this.chromosomes[0] = this.minLengthGene;
        if (chromosomes[0] > this.maxLengthGene)
            this.chromosomes[0] = this.maxLengthGene;

        if (chromosomes[1] < this.minWidthGene)
            this.chromosomes[1] = this.minWidthGene;
        if (chromosomes[1] > this.maxWidthGene)
            this.chromosomes[1] = this.maxWidthGene;
    }
}
