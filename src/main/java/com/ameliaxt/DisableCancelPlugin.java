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

	private boolean itemIgnored(int itemId) {
		final boolean cfgIgnoreAllItems = config.ignoreAllItems();

		if (cfgIgnoreAllItems) {
			return true;
		}

		final String itemName = itemManager.getItemComposition(itemId).getMembersName().toLowerCase();
		final String[] cfgItemsToIgnore = config.itemsToIgnore().toLowerCase().split(" *, *");

		if (Arrays.asList(cfgItemsToIgnore).contains(itemName)) {
			return true;
		}

		return false;
	}

	private boolean spellIgnored(Widget widget) {
		final boolean cfgIgnoreAllSpells = config.ignoreAllSpells();

		if (cfgIgnoreAllSpells) {
			return true;
		}

		int spellId = widget.getId();

		String spellString = TARGETABLE_SPELL_MAP.get(spellId);

		if (spellString == null) {
			return false;
		}

		final String[] cfgSpellsToIgnore = config.spellsToIgnore().toLowerCase().split(" *, *");

		if (Arrays.asList(cfgSpellsToIgnore).contains(spellString)) {
			return true;
		}

		return false;
	}

	private boolean shouldIgnoreRightClickMenu() {
		final boolean cfgLeftClickOnly = config.leftClickOnly();

		if (cfgLeftClickOnly && client.isMenuOpen()) {
			return true;
		}

		return false;
	}

	private boolean shouldDisableCancel(MenuOptionClicked option) {
		final Widget selectedWidget = client.getSelectedWidget();

		final MenuAction action = option.getMenuAction();

		if (selectedWidget == null || action != MenuAction.CANCEL) {
			return false;
		}

		final int itemId = selectedWidget.getItemId();

		final boolean isItem = itemId > 0;

		if (isItem && itemIgnored(itemId)) {
			return false;
		}

		if (!isItem && spellIgnored(selectedWidget)) {
			return false;
		}

		if (shouldIgnoreRightClickMenu()) {
			return false;
		}

		return true;
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked option) {
		if (!shouldDisableCancel(option)) {
			return;
		}

		option.consume();
	}

	@Provides
	DisableCancelConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(DisableCancelConfig.class);
	}
}
