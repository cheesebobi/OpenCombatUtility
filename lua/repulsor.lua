args = { ... }

rep = peripheral.find("opencu:repulsor")

os.sleep(2)

local pulses = { "opencu:repulsor", "opencu:vector", "opencu:stasis" }

rep.recalibrate(pulses[args[1] + 1])
if (args[1] + 0) == 1 then
    rep.setVector(args[3] + 0, args[4] + 0, args[5] + 0)
    rep.setForce(args[2] + 0)
else
    rep.setForce(args[2] + 0)
end
rep.setRadius(args[6] + 0 or 5)

while true do
    if redstone.getInput("right") then
        local _, message = rep.pulse(0,0,0)
        if message then
            print(message)
        end
        os.sleep(0.05)
    else
        os.sleep(1)
    end
    --
end