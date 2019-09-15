package ninjaphenix.creativebuttonmover.client;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import ninjaphenix.creativebuttonmover.client.gui.screen.SimulatedCreativeScreen;

import java.util.function.Function;

public class CreativeButtonMoverModMenuPlugin implements ModMenuApi
{
    @Override
    public String getModId() { return "creativebuttonmover"; }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() { return SimulatedCreativeScreen::new; }
}
