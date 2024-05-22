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
>>

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
