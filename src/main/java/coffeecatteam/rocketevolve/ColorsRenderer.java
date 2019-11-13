package coffeecatteam.rocketevolve;

import coffeecatteam.coffeecatutils.position.AABB;
import coffeecatteam.rocketevolve.rocket.Rocket;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 3/01/2019
 */
public class ColorsRenderer {

    private List<Cell> COLORS = new ArrayList<>();
    public static Cell selected;
    public static String SAVE_FILE_PATH = "./colors.txt";

    public void setColors() {
        COLORS = new ArrayList<>();
        for (Rocket rocket : Game.getInstance().getPopulation().getRockets()) {
            Cell cell = new Cell(rocket.getDna().getGeneColor().getChromosomes());

            if (!contains(cell))
                COLORS.add(cell);
        }
    }

    private boolean contains(Cell cell) {
        for (Cell c : COLORS) {
            if (c.toString().equals(cell.toString()))
                return true;
        }
        return false;
    }

    public void update(GameContainer container, int delta) {
        if (Game.getInstance().isControlDownAndKeyPressed(container, Input.KEY_S)) {
            File colorsFile = new File(SAVE_FILE_PATH);
            if (!colorsFile.exists())
                colorsFile.getParentFile().mkdirs();

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(colorsFile, true));
                writer.append(colorsListToString());

                writer.newLine();
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Game.getInstance().getLogger().info("Saved colors to file [" + colorsFile.getAbsolutePath() + "]");
        }

        for (Cell cell : COLORS)
            cell.update(container, delta);
    }

    private String colorsListToString() {
        StringBuilder builder = new StringBuilder();
        for (Cell cell : COLORS) {
            builder.append("[").append(cell.toString()).append("],");
        }
        return builder.toString();
    }

    public void render(GameContainer container, Graphics g) {
        float padding = 10f, cellSize = 20f;
        int maxCellsX = (int) ((Game.getInstance().getWidth() - padding * 2) / cellSize), maxCellsY = 2;
        int cellsX = 0, cellsY = 0;
        float x = padding, y = Game.getInstance().getHeight() - cellSize * maxCellsY - padding;

        for (Cell cell : COLORS) {
            float nx = x + cellSize * cellsX;

            if (cellsX >= maxCellsX) {
                nx = x;
                if (cellsY < maxCellsY)
                    y += cellSize;
                cellsX = 0;
                cellsY++;
            }

            if (cellsY < maxCellsY)
                cell.render(container, g, nx, y, cellSize);
            cellsX++;
        }
    }

    public class Cell {

        public Color color;
        public AABB bounds = new AABB();
        public boolean selected = false;

        public Cell(Color color) {
            this.color = color;
        }

        public void update(GameContainer container, int delta) {
            if (bounds.contains(Game.getInstance().getMouseX(), Game.getInstance().getMouseY()) && Game.getInstance().isLeftPressed()) {
                ColorsRenderer.selected = this;
                if (selected) {
                    ColorsRenderer.selected = null;
                    selected = false;
                }
            }
            this.selected = ColorsRenderer.selected != null && ColorsRenderer.selected == this;
        }

        public void render(GameContainer container, Graphics g, float x, float y, float size) {
            bounds = new AABB(x, y, (int) size, (int) size);

            g.setColor(color);
            g.fillRect(x, y, size, size);

            if (selected || bounds.contains(Game.getInstance().getMouseX(), Game.getInstance().getMouseY())) {
                g.setColor(Color.white);
                g.setLineWidth(4f);
                g.drawRect(x, y, size, size);
            }
        }

        @Override
        public String toString() {
            return colorToString(color);
        }
    }

    public static String colorToString(Color color) {
        return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }
}
