# 1.19.4
## 2.0.0.0

---
## TODO
### 1. Porting
   - [x] Split Forge and Common
   - [ ] Port to fabric
     - [ ] Repulsor
       - [x] Block
       - [ ] Peripheral
       - [ ] Renderer
     - [ ] Dispenser
       - [ ] Block
       - [ ] Peripheral
       - [ ] Renderer
     - [ ] Creative Tab
### 2. Features
  - [ ] Modular Frame (Replaces Dispenser Frame) 
     - Accepts repulsors as well as dispensers
     - Repulsors in the frame will cause vector based pulses
  - [ ] Remove vector pulse from normal repulsors
  - [ ] Make special Dispensers placeable
  - [ ] Energy overhaul
    - [ ] Capacitors
    - [ ] Plugs
  - [ ] Control array
    - [ ] TODO

---
1. Removed recalibrateByIdx(mode:int) from repulsor api, please use recalibrate(mode:string) instead
