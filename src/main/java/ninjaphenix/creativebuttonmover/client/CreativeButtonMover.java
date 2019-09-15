package ninjaphenix.creativebuttonmover.client;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@SuppressWarnings("SpellCheckingInspection")
public class CreativeButtonMover
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static Integer x;
    public static Integer y;

    @SuppressWarnings("unused")
    public static void loadValues()
    {
        try (FileInputStream inputStream = new FileInputStream(new File(FabricLoader.getInstance().getConfigDirectory(), "creativebuttonmover.properties")))
        {
            Properties props = new Properties();
            props.load(inputStream);
            x = Integer.valueOf((String) props.computeIfAbsent("x", (v) -> "170"));
            y = Integer.valueOf((String) props.computeIfAbsent("y", (v) -> "4"));
        }
        catch (IOException e)
        {
            LOGGER.info("[creativebuttonmover] Failed to read values from config.");
            x = 170;
            y = 4;
        }
        saveValues();
    }

    public static void saveValues()
    {
        Properties props = new Properties();
        props.put("x", x.toString());
        props.put("y", y.toString());
        try (FileOutputStream outputStream = new FileOutputStream(new File(FabricLoader.getInstance().getConfigDirectory(), "creativebuttonmover.properties")))
        {
            props.store(outputStream, "Allows you to move the creative page buttons added by fabric-api. See mod menu entry for a way to move these in-game.");
        }
        catch (IOException e)
        {
            LOGGER.info("[creativebuttonmover] Failed to save values to config.");
        }
    }
}
