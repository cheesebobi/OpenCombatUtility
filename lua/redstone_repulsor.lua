c = require("component")

Rep = c.repulsor
Io = c.redstone

Rep.recalibrate(1)
Rep.setForce(1)
Rep.setRadius(4)
Rep.setWhitelist(false)
Rep.setVector(1, 1, 0)

while true do
	r = Io.getInput(1)
	if r > 7 then
		for _=1, 10 do
			Rep.pulse(0,2,0)
		end
	end
	os.sleep(0.3)
end