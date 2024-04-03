# 1.19.4
## 2.0.0.0

---
## TODO
### 1. Porting
   - [x] Split Forge and Common
   - [ ] Port to fabric
     - [x] Repulsor
       - [x] BlockF
       - [x] Peripheral
       - [x] Renderer
     - [ ] Dispenser
       - [ ] Block
       - [ ] Peripheral
       - [ ] Renderer
     - [ ] Creative Tab
   - [x] Port bugfixes from 1.18.2
### 2. Features
  - [ ] Modular Frame (Replaces Dispenser Frame)
     - Accepts repulsors as well as dispensers
     - Repulsors in the frame will cause vector based pulses
     - API: 
       - [ ] Add aimAt(x: number, y: number, z: number
  - [ ] Remove vector pulse from normal repulsors
  - [ ] Make special Dispensers placeable
  - [ ] Control array
    - [ ] TODO
  - [ ] Energy overhaul
    - [ ] Capacitors
    - [ ] Plugs
  - [ ] Blinker
### 3. Backend
  - [ ] Replace pitch and yaw in modular frame with direction vector
---
1. Removed recalibrateByIdx(mode:int) from repulsor api, please use recalibrate(mode:string) instead
