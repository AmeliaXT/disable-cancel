# Disable Cancel

This plugin allows you to disable the cancel behaviour of actions which require a source and target, such as using items of casting spells.
In the event of no target, whether in the game world or interface, the action will not be cancelled until a target is clicked.

No longer do you need to re-select an item or spell when you accidentally click the ground next to what you actually meant to click!

## Options

| Option               | Default |                                                                                                                                                             Description |
| :------------------- | :-----: | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
| All Items            |  True   |                                                                                                                               Disables the Cancel option for all items. |
| All Spells           |  True   |                                                                                                                              Disables the Cancel option for all spells. |
| Left Click Only      |  True   |                                                                      Only disables the left click cancel option, so the right click menu Cancel can be used if desired. |
| Specific items only  |   ""    |             Allows individual items to ignore the prevention of cancellation. Does not work if 'All items' is checked. Comma separated list, e.g. 'law rune, guam herb' |
| Specific spells only |   ""    | Allows inidvidual spells to ignore the prevention of cancellation. Does not work if 'All spells' is checked. Comma separated list, e.g. 'telekinetic grab, ice barrage' |
