package coffeecatteam.rocketevolve;

import coffeecatteam.coffeecatutils.ArgUtils;
import coffeecatteam.coffeecatutils.DevEnvUtils;
import coffeecatteam.coffeecatutils.NumberUtils;
import coffeecatteam.coffeecatutils.logger.CatLogger;
import coffeecatteam.coffeecatutils.position.Vector2D;
import coffeecatteam.rocketevolve.collidable.Collidable;
import coffeecatteam.rocketevolve.collidable.CollidableEllipse;
import coffeecatteam.rocketevolve.collidable.CollidableRect;
import coffeecatteam.rocketevolve.rocket.DNA;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 2/01/2019
 */
public class Game extends BasicGame {

    private static Game INSTANCE;
    private CatLogger logger;

    private String[] args;
    private int width, height, fps;

    /*
     * Mouse values
     */
    private int mouseX, mouseY;
    private boolean leftPressed, rightPressed;
    private boolean leftDown, rightDown;

    /*
     * Main values
     */
    private Population population;
    private int lifespan, currentLifespan, generation, rocketsCrashed;
    private float maxForce;
    private boolean showControls = false;

    private List<Collidable> targets, obstacles;
    private ColorsRenderer colorsRenderer;

    private List<Color> COLORS = new ArrayList<>();

    public Game(String[] args, String title, int width, int height) {
        super(title);
        this.args = args;
        this.width = width;
        this.height = height;
        this.logger = new CatLogger(title);
        INSTANCE = this;
    }

    private void generateColors() {
        COLORS.clear();

        int colorAmt = ArgUtils.hasArgument("-colorAmt") ? NumberUtils.parseInt(ArgUtils.getArgument("-colorAmt")) : 5;
        for (int i = 0; i < colorAmt; i++) {
            Color color = generateNewColor();
            if (!COLORS.contains(color))
                COLORS.add(color);
            else
                i--;
        }
    }

    private Color generateNewColor() {
        return new Color(NumberUtils.getRandomInt(255), NumberUtils.getRandomInt(255), NumberUtils.getRandomInt(255), NumberUtils.getRandomInt(127, 255));
    }

    public Color getColor() {
        return COLORS.get(COLORS.size() > 1 ? NumberUtils.getRandomInt(COLORS.size() - 1) : 0);
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        if (DevEnvUtils.isRunningFromDevEnviroment())
            logger.warn("Running from dev environment!");

        generateColors();
        Discord.INSTANCE.setup();

        lifespan = 500;
        currentLifespan = 0;
        generation = 0;
        rocketsCrashed = 0;

        maxForce = 0.2f;

        targets = new ArrayList<>();
        obstacles = new ArrayList<>();

        int minYOff = -100, maxYOff = 50;

        // Target(s)
        for (int i = 0; i < NumberUtils.getRandomInt(1, 3); i++) {
            int radius = 12 + NumberUtils.getRandomInt(5);
            targets.add(new CollidableEllipse(new Vector2D(NumberUtils.getRandomFloat(width / 4f, width - width / 4f), 150 - NumberUtils.getRandomFloat(minYOff, maxYOff)), radius, getColor()));
        }

        // Obstacle(s)
        float yOff = 150, y = height / 2f - 40;
        int w = width / 4, h = 20;

        for (int i = 0; i < NumberUtils.getRandomInt(2, 5); i++) {
            float div = 20f;
            float nx = NumberUtils.getRandomFloat(width / div, width - w - width / div);
            float ny = y + NumberUtils.getRandomFloat(-yOff, yOff);
            int nw = w + NumberUtils.getRandomInt(-(width / 6), 10);

            obstacles.add(new CollidableRect(new Vector2D(nx, ny), nw, h));
        }

        // Population
        population = new Population(500);

        colorsRenderer = new ColorsRenderer();
        colorsRenderer.setColors();
    }

    @Override
    public boolean closeRequested() {
        Discord.INSTANCE.shutdown();
        return true;
    }

    public boolean isControlDownAndKeyPressed(GameContainer container, final int key) {
        return (container.getInput().isKeyDown(Input.KEY_LCONTROL) || container.getInput().isKeyDown(Input.KEY_RCONTROL)) && container.getInput().isKeyPressed(key);
    }

    private void setNewDNA(int div) {
        for (int i = 0; i < NumberUtils.getRandomInt(0, population.getPopsize() / div); i++)
            population.randomRocket().setDna(new DNA());
        colorsRenderer.setColors();
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        this.fps = container.getFPS();
        if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) container.exit();
        if (container.getInput().isKeyPressed(Input.KEY_R)) init(container);

        if (isControlDownAndKeyPressed(container, Input.KEY_1))
            setNewDNA(2);
        if (isControlDownAndKeyPressed(container, Input.KEY_2))
            setNewDNA(1);

        if (container.getInput().isKeyPressed(Input.KEY_T)) showControls = !showControls;

        /*
         * Mouse values
         */
        this.mouseX = container.getInput().getMouseX();
        this.mouseY = container.getInput().getMouseY();
        this.leftPressed = container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON);
        this.rightPressed = container.getInput().isMousePressed(Input.MOUSE_RIGHT_BUTTON);
        this.leftDown = container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
        this.rightDown = container.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON);

        /*
         * Game code
         */
        population.update(container, delta);

        for (int i = 0; i < obstacles.size(); i++)
            obstacles.get(i).update(container, delta);

        for (int i = 0; i < targets.size(); i++)
            targets.get(i).update(container, delta);

        colorsRenderer.update(container, delta);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
//        g.clear();
        g.setBackground(new Color(22, 22, 22));
        population.render(container, g);

        // Draw info text
        g.setColor(Color.white);
        float x = 10;
        float y = 22;
        float yOff = 12;
        g.drawString("Lifespan: " + currentLifespan + "/" + lifespan, x, y);
        g.drawString("Generation: " + generation, x, y + yOff);
        g.drawString("Crashed: " + rocketsCrashed, x, y + yOff * 2);
        g.drawString("Alive: " + (population.getPopsize() - rocketsCrashed), x, y + yOff * 3);
        g.drawString("Population: " + population.getPopsize(), x, y + yOff * 4);

        if (showControls) {
            g.drawString("Controls:", x, y + yOff * 6);
            g.drawString("Press: [Control + 1] to reset 'DNA' of half the rockets", x, y + yOff * 7);
            g.drawString("Press: [Control + 2] to reset 'DNA' of all the rockets", x, y + yOff * 8);
            g.drawString("Press: [R] to reset everything", x, y + yOff * 9);
            g.drawString("Press: [Left-Shift + Hold Left-Click] to resize target(s) or obstacle(s)", x, y + yOff * 10);
            g.drawString("Press: [Left-Click] to move target(s) or obstacle(s)", x, y + yOff * 11);
            g.drawString("Press: [Control + S] to save colors of all rockets to " + ColorsRenderer.SAVE_FILE_PATH, x, y + yOff * 12);
            g.drawString("Press: [Left-Click] one of the boxes below to select or deselect all rockets with that color", x, y + yOff * 13);
        } else {
            g.drawString("Press: [T] to show/hide controls", x, y + yOff * 6);
        }

        if (currentLifespan >= lifespan) {
            population.evaluate();
            population.selection();
            currentLifespan = 0;
            generation++;
            rocketsCrashed = 0;
            colorsRenderer.setColors();
            ColorsRenderer.selected = null;
        }
        currentLifespan++;

        for (int i = 0; i < obstacles.size(); i++)
            obstacles.get(i).render(container, g);

        for (int i = 0; i < targets.size(); i++)
            targets.get(i).render(container, g);

        colorsRenderer.render(container, g);
    }

    public static Game getInstance() {
        return INSTANCE;
    }

    public CatLogger getLogger() {
        return logger;
    }

    public String[] getArgs() {
        return args;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFps() {
        return fps;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isLeftDown() {
        return leftDown;
    }

    public boolean isRightDown() {
        return rightDown;
    }

    public Population getPopulation() {
        return population;
    }

    public int getLifespan() {
        return lifespan;
    }

    public int getCurrentLifespan() {
        return currentLifespan;
    }

    public int getGeneration() {
        return generation;
    }

    public int getRocketsCrashed() {
        return rocketsCrashed;
    }

    public void setRocketsCrashed(int rocketsCrashed) {
        this.rocketsCrashed = rocketsCrashed;
    }

    public void addRocketsCrashed(int amt) {
        this.rocketsCrashed += amt;
    }

    public float getMaxForce() {
        return maxForce;
    }

    public boolean isShowControls() {
        return showControls;
    }

    public List<Collidable> getTargets() {
        return targets;
    }

    public List<Collidable> getObstacles() {
        return obstacles;
    }

    public ColorsRenderer getColorsRenderer() {
        return colorsRenderer;
    }
}
