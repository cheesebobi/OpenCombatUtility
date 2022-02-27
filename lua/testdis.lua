cmp = require("component")
dis = cmp.omnidispenser

_, res = dis.setForce(0.75)
if res then
    print(res)
end

_, res = dis.setForce(1.)
if res then
    print(res)
end

_, res = dis.setSpread(10)
if res then
    print(res)
end

os.sleep(2)

dis.aim(math.pi, math.pi/4)
if dis.isAligned() then
    print(true)
    dis.aim(0, math.pi/4)
else
    print(false)
end

while dis.isAligned() == false do
    print(dis.aimingStatus())
    os.sleep(0.1)
end

for i=1, 64, 1 do
    dis.dispense()
    os.sleep(0.05)
end