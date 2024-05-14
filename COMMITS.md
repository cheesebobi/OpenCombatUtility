>> #### Added COMMITS.md
>
> Added COMMITS.md for more involved commit descriptions in the future 
>> 


> > #### ModularFrame rework pt2 - Device/State/API separation
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
