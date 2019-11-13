package coffeecatteam.rocketevolve;

import coffeecatteam.coffeecatutils.ArgUtils;
import coffeecatteam.coffeecatutils.DevEnvUtils;
import coffeecatteam.coffeecatutils.logger.CatLoggerUtils;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import java.io.File;

/**
 * @author CoffeeCatRailway
 * Created: 2/01/2019
 */
public class Launcher {

    public static void main(String[] args) {
        ArgUtils.setARGS(args);

        /* Set natives path */
        if (!DevEnvUtils.isRunningFromDevEnviroment()) {
            final String nativesPath = new File("natives").getAbsolutePath();
            System.setProperty("org.lwjgl.librarypath", nativesPath);
            System.setProperty("java.library.path", nativesPath);
        }

        /* Initialize logger */
        CatLoggerUtils.setOutputLog(false);
        CatLoggerUtils.init();

        /* Start INSTANCE */
        try {
            /* Width, height & title */
            int width = 1280;
            int height = 720;
            String title = "Rocket Evolve";

            Game game = new Game(args, title, width, height);
            AppGameContainer app = new AppGameContainer(game);
            app.setDisplayMode(width, height, false);

            if (ArgUtils.hasArgument("-fullscreen")) {
                game.setWidth(app.getScreenWidth());
                game.setHeight(app.getScreenHeight());
                app.setDisplayMode(game.getWidth(), game.getHeight(), true);
            }

            app.setIcons(new String[]{"icons/32.png", "icons/64.png"});
            if (!ArgUtils.hasArgument("-faaast")) {
                app.setTargetFrameRate(60);
                app.setVSync(true);
            }
            app.setUpdateOnlyWhenVisible(false);
            app.setAlwaysRender(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
