package coffeecatteam.rocketevolve.rocket;

import coffeecatteam.rocketevolve.rocket.genes.GeneColor;
import coffeecatteam.rocketevolve.rocket.genes.GeneFloat;
import coffeecatteam.rocketevolve.rocket.genes.GeneLooks;
import coffeecatteam.rocketevolve.rocket.genes.GeneMove;

/**
 * @author CoffeeCatRailway
 * Created: 2/01/2019
 */
public class DNA {

    /**
     * Genes for the rocket
     */
    private GeneMove geneMove;
    private GeneColor geneColor;
    private GeneLooks geneLooks;
    private GeneFloat geneSpeed;

    public DNA() {
        this.geneMove = new GeneMove();
        this.geneColor = new GeneColor();
        this.geneLooks = new GeneLooks();
        this.geneSpeed = new GeneFloat();
    }

    public DNA(GeneMove geneMove, GeneColor geneColor, GeneLooks geneLooks, GeneFloat geneSpeed) {
        this.geneMove = geneMove;
        this.geneColor = geneColor;
        this.geneLooks = geneLooks;
        this.geneSpeed = geneSpeed;
    }

    public DNA crossover(DNA partner) {
        GeneMove newGeneMove = geneMove.crossover(partner.getGeneMove());
        GeneColor newGeneColor = geneColor.crossover(partner.getGeneColor());
        GeneLooks newGeneLooks = geneLooks.crossover(partner.getGeneLooks());
        GeneFloat newGeneSpeed = geneSpeed.crossover(partner.getGeneSpeed());
        return new DNA(newGeneMove, newGeneColor, newGeneLooks, newGeneSpeed);
    }

    public void mutate() {
        geneMove.mutate();
        geneColor.mutate();
        geneLooks.mutate();
        geneSpeed.mutate();
    }

    public GeneMove getGeneMove() {
        return geneMove;
    }

    public GeneColor getGeneColor() {
        return geneColor;
    }

    public GeneLooks getGeneLooks() {
        return geneLooks;
    }

    public GeneFloat getGeneSpeed() {
        return geneSpeed;
    }
}
