dis = peripheral.find("opencu:modular_frame")

local idx = 0

pi = 3.14

function aim(value, angle)
    local sequence = idx % 4
    local up = idx % 3
    if sequence == value then
        p = 0
        if up == 1 then
            p = 1
        end
        dis.aim(angle, p)
        idx = (idx + 1)
        return true
    end
    return false
end

while true do
    if dis.isAligned() then
        if aim(0, 0) then
        elseif aim(1, pi / 2) then
        elseif aim(2, pi) then
        elseif aim(3, 3 * pi / 2) then
        --[[ elseif aim(4, 0) then
        elseif aim(5, 3 * pi / 2) then
        elseif aim(6, pi) then
        elseif aim(7, pi / 2) then ]]
        end
    end
    sleep(0.05)
end
