package com.ameliaxt;

import javax.inject.Inject;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;

import net.runelite.api.*;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.Arrays;

import static com.ameliaxt.TargetableSpells.TARGETABLE_SPELL_MAP;

@Slf4j
@PluginDescriptor(name = "Disable Cancel")
public class DisableCancelPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private DisableCancelConfig config;

	private boolean disableCancelForItem(int itemId) {
		final boolean cfgDisableForAllItems = config.disableForAllItems();

		if (cfgDisableForAllItems) {
			return true;
		}

		final String itemName = itemManager.getItemComposition(itemId).getMembersName().toLowerCase();
		final String[] cfgDisableCancelOnItems = config.disableCancelOnItems().toLowerCase().split(" *, *");

		if (Arrays.asList(cfgDisableCancelOnItems).contains(itemName)) {
			return true;
		}

		return false;
	}

	private boolean disableCancelForSpell(Widget widget) {
		final boolean cfgDisableForAllSpells = config.disableForAllSpells();

		if (cfgDisableForAllSpells) {
			return true;
		}

		int spellId = widget.getId();

		String spellString = TARGETABLE_SPELL_MAP.get(spellId);

		if (spellString == null) {
			return false;
		}

		final String[] cfgDisableCancelOnSpells = config.disableCancelOnSpells().toLowerCase().split(" *, *");

		if (Arrays.asList(cfgDisableCancelOnSpells).contains(spellString)) {
			return true;
		}

		return false;
	}

	private boolean disableForRightClickOption() {
		final boolean cfgLeftClickOnly = config.leftClickOnly();

		if (cfgLeftClickOnly) {
			return true;
		}

		return false;
	}

	private boolean shouldDisableOption(MenuOptionClicked option) {
		final MenuAction action = option.getMenuAction();

		final boolean isCancel = action == MenuAction.CANCEL;

		if (isCancel) {
			if (client.isMenuOpen() && disableForRightClickOption()) {
				return false;
			}

			final Widget selectedWidget = client.getSelectedWidget();

			if (selectedWidget == null) {
				return false;
			}

			final int itemId = selectedWidget.getItemId();

			final boolean isItem = itemId > 0;

			if (isItem && disableCancelForItem(itemId)) {
				return true;
			}

			if (!isItem && disableCancelForSpell(selectedWidget)) {
				return true;
			}
		}

		return false;
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked option) {
		if (!shouldDisableOption(option)) {
			return;
		}

		option.consume();
	}

	@Provides
	DisableCancelConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(DisableCancelConfig.class);
	}
}
