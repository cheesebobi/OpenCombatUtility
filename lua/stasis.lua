rep = peripheral.find("opencu:repulsor")

os.sleep(2)

rep.recalibrate(2)
rep.setForce(1)
rep.setRadius(3)
redstone.setOutput("right", true)
for i=0, 20, 1 do
    rep.pulse(0,0,0)
end
redstone.setOutput("right", false)