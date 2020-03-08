package ninjaphenix.creativebuttonmover.client;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import ninjaphenix.creativebuttonmover.client.gui.screen.SimulatedCreativeScreen;

public class CreativeButtonMoverModMenuPlugin implements ModMenuApi
{
	@Override
	public String getModId() { return "creativebuttonmover"; }

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() { return SimulatedCreativeScreen::new; }
}
