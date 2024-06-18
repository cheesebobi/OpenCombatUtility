>> Implemented linking
> #### Common
> - Added EventNode system
>   - Added IEventNode
>     - IFramedDevice now extends IEventNode
>     - ModularFrame now implements IEventNode
>     - Added some utility EventNodes:
>       - Added DistributingEventNode (unused)
>       - Added DistributingWorldEventNode
>       - Added BlockPosEventNode (unused)
>   - Added IEventData:
>     - Added LookAtEvent (used by TrackerBase)
>     - Added SetAimEvent
> - Added ItemLinkTool
> - Fixed name of getRedstoneControl() in ModularFramePeripheral 
> #### Fabric
> - Implemented UseHandler so that LinkTool and AimTool don't open guis
> #### Forge
> Nothing, still broken
> #### Art
> - Added LinkTool sprites  
> - Added "eyetool" sprite [by ThePennitentOne]
>> 

>> Added redstone control to Modular Frame
> #### Common
> - Added IRedstoneControlled interface
> - Added PacketServerCycleRedstoneControl which cycles redstone control on BlockEntities with IRedstoneControlled
> - Implemented redstone control for ModuluarFrame
> - Tweaked ResponsiveToggle widget to support multiple states
> - Added get and set methods for RedstoneControl to ModularFramePeripheral
> #### Fabric
> - Implemented redstone change notification from ModularFrame Block to its BlockEntity
> #### Forge
> Nothing, still broken
>> fa873dd [04.06.2024 at 13:10]


>> #### Implemented ActivateOnlyWhenAligned feature to ModularFrame
> #### Common
> - Added Responsive toggle GUI widget
> - Added lock button to ModularFrameGUI and its tooltip translations
> - Added PacketServerToggleRequiresLock and its handler
> - Fixed TrackerDevice still working without energy
> - ModularFrame API
>   - Added isRequiresLock()
>   - Added setRequiresLock(requiresLock: boolean)
> #### Fabric
> - Registered PacketServerToggleRequiresLock
> #### Forge
> Nothing, still broken
> #### Lua
> - added lock tests to dispenser.lua
>> 131ce2e8 [26.05.2024 at 16:09]

>> #### Reworked Contexts (again) + Implemented TrackerBase + Added AimTool Item
> #### Common
> - Made Contexts more modular
> - Added TrackerBase for ModularFrame
> - Added methods aimAt() and aimAtWorld() to BlockEntityModularFrame (not yet present in CC api)
> - Added AimTool Item
> #### Fabric
> - Added AimTool registration
> #### Forge
> Nothing, still broken
>> 799b1c88 [22.05.2024 at 16:15]

>> #### Implemented Reborn Energy on fabric
> #### Common
> - Removed ArchitecturyAPI from dependencies
> - Restored Energy logic for both ModularFrame Devices and Repulsor
> - Added IContext, IEnergyContext, IScopedContext, IRepulsorContext, IModularFrameContext
> - Moved ammo handling from TileEntityModularFrame to Device instances via IModularFrameContext
> #### Fabric
> - Added TeamRebornEnergy dependency
> - Implemented energy storage and handling in ModularFrame and Repulsor
> #### Forge
> Nothing, still broken
>> 6f4e722b [20.05.2024 at 21:31]

>> #### Added COMMITS.md
>
> Added COMMITS.md for more involved commit descriptions in the future 
>> 89fc7cc3 [14.05.2024 at 17:16]

>> #### ModularFrame rework pt2 - Device/State/API separation
> #### Common
> - Renamed IDispenser to IFramedDevice  
> - Removed state related methods from IFramedDevice and introduced IDeviceState
> - Replaced most ModularFrame peripheral methods with getDeviceApi() which returns an object for manipulating current device state
> - Renamed DispenserBase to ShooterBase
> - Temporarily removed ConfigurableDispenser logic
> #### Fabric
> - Started applying a fix for ModularFrame not sending its device item data to client after being out of render distance (Not tested)
>   - Added BlockEntityLoadHandler which sends an update request to the server
>> bb599bcb [14.05.2024 at 16:41]
