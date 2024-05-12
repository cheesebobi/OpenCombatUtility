dis = peripheral.find("opencu:modular_frame")

local idx = 0

pi = 3.14

function aim(value, angle)
    local sequence = idx % 2
    if sequence == value then
        p = 1
        dis.aim(angle, p)
        idx = (idx + 1)
        return true
    end
    return false
end

while true do
    if dis.isAligned() then
        if aim(0, 0) then
        elseif aim(1, pi) then
        end
    end
    sleep(0.05)
end
