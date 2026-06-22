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

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.regex.Pattern;

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

		return matchesAny(itemName, cfgDisableCancelOnItems);
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

		return matchesAny(spellString, cfgDisableCancelOnSpells);
	}

	static boolean matchesAny(String name, String[] patterns) {
		for (String pattern : patterns) {
			if (pattern.isEmpty()) {
				continue;
			}

			if (pattern.indexOf('*') < 0) {
				if (pattern.equals(name)) {
					return true;
				}
				continue;
			}

			StringBuilder regex = new StringBuilder();
			int start = 0;
			for (int i = 0; i < pattern.length(); i++) {
				if (pattern.charAt(i) == '*') {
					if (i > start) {
						regex.append(Pattern.quote(pattern.substring(start, i)));
					}
					regex.append(".*");
					start = i + 1;
				}
			}
			if (start < pattern.length()) {
				regex.append(Pattern.quote(pattern.substring(start)));
			}

			if (name.matches(regex.toString())) {
				return true;
			}
		}

		return false;
	}

	private boolean isReclickOnSelectedWidget(Widget selectedWidget) {
		if (selectedWidget.isHidden()) {
			return false;
		}

		final Rectangle bounds = selectedWidget.getBounds();
		final Point mouse = client.getMouseCanvasPosition();

		if (bounds == null || mouse == null) {
			return false;
		}

		return bounds.contains(mouse.getX(), mouse.getY());
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
				if (config.reclickSpellToCancel() && isReclickOnSelectedWidget(selectedWidget)) {
					return false;
				}

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
