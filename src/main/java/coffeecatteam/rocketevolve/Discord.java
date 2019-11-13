package coffeecatteam.rocketevolve;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import coffeecatteam.coffeecatutils.logger.CatLogger;

/**
 * @author CoffeeCatRailway
 * Created: 3/05/2019
 */
public class Discord {

    public static final Discord INSTANCE = new Discord();
    public static boolean READY = false;

    private CatLogger logger;
    private DiscordRPC rpc;

    public void setup() {
        logger = new CatLogger(Game.getInstance().getTitle() + "-rpc");
        logger.println();
        rpc = DiscordRPC.INSTANCE;

        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = user -> {
            Discord.READY = true;
            logger.warn("Connected to discord");
            logger.info("Ready: " + READY);
            logger.println();
        };
        rpc.Discord_Initialize("573716274966691842", handlers, true, "");

        new Thread(() -> {
            logger.warn("Started RPC Callback Handler");
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                if (!READY) {
                    logger.info("Connecting rich presence...");
                    rpc.Discord_UpdateHandlers(handlers);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "RPC-Callback-Handler").start();

        updatePresence("There evolving!");
    }

    public void updatePresence(String details) {
        updatePresence(details, "");
    }

    public void updatePresence(String details, String state) {
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.details = details;
        presence.state = state;
        presence.largeImageKey = "rocket_evolve";
        rpc.Discord_UpdatePresence(presence);
    }

    public void shutdown() {
        rpc.Discord_ClearPresence();
        logger.info("Cleared rich presence!");
        rpc.Discord_Shutdown();
    }
}
