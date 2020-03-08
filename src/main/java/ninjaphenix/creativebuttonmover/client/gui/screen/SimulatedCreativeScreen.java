package ninjaphenix.creativebuttonmover.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import ninjaphenix.creativebuttonmover.client.Config;
import ninjaphenix.creativebuttonmover.client.ConfigManager;
import ninjaphenix.creativebuttonmover.client.CreativeButtonMover;
import ninjaphenix.creativebuttonmover.client.gui.DummyButtonWidget;

@SuppressWarnings("ConstantConditions")
public class SimulatedCreativeScreen extends Screen
{
	private static final Identifier BACKGROUND_TEXTURE = new Identifier("textures/gui/container/creative_inventory/tab_item_search.png");
	private static final Identifier TAB_TEXTURE = new Identifier("textures/gui/container/creative_inventory/tabs.png");
	private static final TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
	private static final int containerHeight = 136;
	private static final int containerWidth = 195;
	private final Screen returnTo;
	private int left;
	private int top;
	private DummyButtonWidget prev;
	private DummyButtonWidget next;

	public SimulatedCreativeScreen(Screen parent)
	{
		super(new LiteralText(""));

		returnTo = parent;
	}

	@Override
	protected void init()
	{
		super.init();
		left = (this.width - containerWidth) / 2;
		top = (this.height - containerHeight) / 2;
		prev = this.addButton(new DummyButtonWidget(Config.INSTANCE.PrevButton, left, top));
		next = this.addButton(new DummyButtonWidget(Config.INSTANCE.NextButton, left, top));
		final int w = 90;
		final int g = 5;
		this.addButton(new ButtonWidget(width / 2 + g, height / 2 + 96, w, 20, "Save", (widget) -> onClose()));
		this.addButton(new ButtonWidget(width / 2 - w - g, height / 2 + 96, w, 20, "Reload", this::reloadValues));
	}

	private void reloadValues(ButtonWidget buttonWidget)
	{
		Config.INSTANCE = ConfigManager.loadConfig(Config.class, CreativeButtonMover.CONFIG_FILE);
		prev.update(Config.INSTANCE.PrevButton);
		next.update(Config.INSTANCE.NextButton);
	}

	@Override
	public void render(int int_1, int int_2, float float_1)
	{
		this.renderBackground();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableAlphaTest();
		for (int i = 1; i < 12; i++)
		{
			textureManager.bindTexture(TAB_TEXTURE);
			renderItemGroup(ItemGroup.GROUPS[i]);
		}
		textureManager.bindTexture(BACKGROUND_TEXTURE);
		drawRect(left, top, 0, 0, containerWidth, containerHeight);
		textureManager.bindTexture(TAB_TEXTURE);
		renderItemGroup(ItemGroup.GROUPS[0]);
		RenderSystem.disableAlphaTest();
		super.render(int_1, int_2, float_1);
		text.drawWithShadow("Page Button Mover", width / 2f - text.getStringWidth("Page Button Mover") / 2f, top - 40, 5636095);
	}

	private void renderItemGroup(ItemGroup itemGroup_1)
	{
		final int column = itemGroup_1.getColumn();
		int offY = 0;
		int x = left + 28 * column;
		int y = top;
		if (itemGroup_1.getIndex() == 0) { offY += 32; }
		if (itemGroup_1.isSpecial()) { x = left + containerWidth - 28 * (6 - column); }
		else if (column > 0) { x += column; }
		if (itemGroup_1.isTopRow()) { y -= 28; }
		else
		{
			offY += 64;
			y += containerHeight - 4;
		}
		this.drawRect(x, y, column * 28, offY, 28, 32);
		if (client.world != null)
		{
			x += 6;
			y += 8 + (itemGroup_1.isTopRow() ? 1 : -1);
			RenderSystem.enableRescaleNormal();
			ItemStack itemStack_1 = itemGroup_1.getIcon();
			this.itemRenderer.renderGuiItem(itemStack_1, x, y);
			this.itemRenderer.renderGuiItemOverlay(text, itemStack_1, x, y);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		for (AbstractButtonWidget child : buttons)
		{
			if (child.isFocused()) { child.changeFocus(false); }
		}
		return super.mouseClicked(mouseX, mouseY, button);

	}

	@Override
	public void onClose()
	{
		prev.save();
		next.save();
		ConfigManager.saveConfig(Config.INSTANCE, CreativeButtonMover.CONFIG_FILE);
		client.openScreen(returnTo);
	}
}
