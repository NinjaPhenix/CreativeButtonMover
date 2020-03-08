package ninjaphenix.creativebuttonmover.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import ninjaphenix.creativebuttonmover.Vec2i;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class CreativeButtonMover implements ClientModInitializer
{
	public static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("creativebuttonmover.json").toFile();
	public static final Logger LOGGER = LogManager.getLogger("creativebuttonmover");
	public static LinkedHashMap<Identifier, Vec2i> PreviousButtonTextures = new LinkedHashMap<>(0);
	public static LinkedHashMap<Identifier, Vec2i> NextButtonTextures = new LinkedHashMap<>(0);

	private static Optional<Pair<Identifier, Vec2i>> tryReadPngSize(Resource resource)
	{
		final TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		textureManager.bindTexture(resource.getId());
		final int width = GlStateManager.getTexLevelParameter(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH);
		final int height = GlStateManager.getTexLevelParameter(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT);
		if (height % 3.0D == 0 && height > 0 && width > 0) { return Optional.of(new Pair<>(resource.getId(), new Vec2i(width, height))); }
		LOGGER.error("Texture, " + resource.getId().toString() + ", is not valid, width must be non-zero and height must be multiple of 3 and non-zero.");
		return Optional.empty();
	}

	@Override
	public void onInitializeClient()
	{
		/*
		ResourceManagerHelper.get(CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener()
		{
			@Override
			public Identifier getFabricId() { return new Identifier("creativebuttonmover", "button_finder"); }

			@Override
			public void apply(ResourceManager manager)
			{
				final Predicate<String> isPNG = (name) -> name.endsWith(".png");
				final Function<Identifier, Optional<Resource>> resourceGetter = id -> {
					try (Resource res = manager.getResource(id)) { return Optional.of(res); }
					catch (IOException ioError)
					{
						return Optional.empty();
					}
				};
				final BinaryOperator<Vec2i> keyMerger = (u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };

				NextButtonTextures = manager.findResources("textures/gui/creativebuttons/next/", isPNG)
											.stream()
											.map(resourceGetter)
											.filter(Optional::isPresent)
											.map(Optional::get)
											.map(CreativeButtonMover::tryReadPngSize)
											.filter(Optional::isPresent)
											.map(Optional::get)
											.collect(Collectors.toMap(Pair::getLeft, Pair::getRight, keyMerger, LinkedHashMap::new));

				PreviousButtonTextures = manager.findResources("textures/gui/creativebuttons/prev/", isPNG)
												.stream()
												.map(resourceGetter)
												.filter(Optional::isPresent)
												.map(Optional::get)
												.map(CreativeButtonMover::tryReadPngSize)
												.filter(Optional::isPresent)
												.map(Optional::get)
												.collect(Collectors.toMap(Pair::getLeft, Pair::getRight, keyMerger, LinkedHashMap::new));

			}
		});
		 */
		Config.INSTANCE = ConfigManager.loadConfig(Config.class, CONFIG_FILE);
		if (false)
		{
			for (int i = 0; i < 20; i++)
			{
				FabricItemGroupBuilder.build(new Identifier("creativebuttonmover", "tab_" + i),
						() -> new ItemStack(Registry.ITEM.getRandom(new Random()), 1));
			}
		}
	}
}
