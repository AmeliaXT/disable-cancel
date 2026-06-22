package com.ameliaxt;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("disable-cancel")
public interface DisableCancelConfig extends Config
{
	@ConfigItem(
		keyName = "disableForAllItems",
		name = "All items",
		description = "Prevent the cancellation of actions on all items."
	)

	default boolean disableForAllItems()
	{
		return true;
	}

	@ConfigItem(
		keyName = "disableForAllSpells",
		name = "All spells",
		description = "Prevent the cancellation of actions on all spells."
	)

	default boolean disableForAllSpells()
	{
		return true;
	}

	@ConfigItem(
		keyName = "disableCancelOnItems",
		name = "Specific items only",
		description = "Allows individual items to ignore the prevention of cancellation. Does not work if 'All items' is checked. Comma separated list. Supports '*' as a wildcard, e.g. 'law rune, *herb, *rune'"
	)

	default String disableCancelOnItems()
	{
        return "";
    }

	@ConfigItem(
		keyName = "disableCancelOnSpells",
		name = "Specific spells only",
		description = "Allows inidvidual spells to ignore the prevention of cancellation. Does not work if 'All spells' is checked. Comma separated list. Supports '*' as a wildcard, e.g. 'telekinetic grab, *barrage, *surge'"
	)

	default String disableCancelOnSpells()
	{
        return "";
    }

	@ConfigItem(
		keyName = "leftClickOnly",
		name = "Left click only",
		description = "Prevent the cancellation of actions on left click only. The right click menu can be used as normal."
	)

	default boolean leftClickOnly()
	{
		return true;
	}

	@ConfigItem(
		keyName = "reclickSpellToCancel",
		name = "Re-click spell to cancel",
		description = "Allow re-clicking the currently selected spell to cancel the selection."
	)

	default boolean reclickSpellToCancel()
	{
		return true;
	}
}
