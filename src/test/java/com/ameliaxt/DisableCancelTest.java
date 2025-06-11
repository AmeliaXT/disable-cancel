package com.ameliaxt;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DisableCancelTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DisableCancelPlugin.class);
		RuneLite.main(args);
	}
}