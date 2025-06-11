package com.ameliaxt;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("disable-cancel")
public interface DisableCancelConfig extends Config
{
	@ConfigItem(
		keyName = "itemsToIgnore",
		name = "Ignore for items",
		description = "Allows items to ignore the prevention of cancellation. Comma separated list, e.g. 'law rune, guam herb'"
	)

	default String itemsToIgnore()
	{
        return "";
    }

	@ConfigItem(
		keyName = "spellsToIgnore",
		name = "Ignore for spells",
		description = "Allows spells to ignore the prevention of cancellation. Comma separated list, e.g. 'telekinetic grab, ice barrage'"
	)

	default String spellsToIgnore()
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
}
