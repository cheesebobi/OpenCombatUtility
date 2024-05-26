dis = peripheral.find("opencu:modular_frame")
function circle(count, p)
    p = p or 0
    local pi2 = math.pi * 2
    for i=0,count,1 do
        dis.aim((i / count) * pi2 - math.pi, p)
        dis.dispense()
    end
end

function lerp(a, b, t)
    return a + (b - a) * t
end


function circles(count1, count2, pMax, pMin)
    pMin = pMin or 0
    pMax = pMax or 0
    local pi2 = math.pi * 2
    local p = pMin;
    for n=1,count2,1 do
        p = lerp(pMin, pMax, (n-1) / count2)
        for i=1,count1,1 do
            dis.aim(((i-1) / count1) * pi2 - math.pi, p)
            while not dis.isAligned() do
                os.sleep(0.2)
            end
            dis.dispense()
        end
    end
end

function calibrate()
    dis.aim(0,0)
    dis.color("r")
    dis.dispense()--Z+

    dis.aim(math.pi / 2, 0)
    dis.color("g")
    dis.dispense()--Z+ CL90

    dis.aim(0, math.pi / 2)
    dis.color("b")
    dis.dispense()--Y+
end

function looop(a, c)
    for i=1,c,1 do
        for j=1,a,1 do
            dis.aim(0,0)
        end
        dis.dispense()
    end
end

function shoot(c)
    for i=1,c,1 do
        dis.dispense()
    end
end

function lock_test(shot_count)
    dis.aim(math.pi/2, -math.pi)
    while true do
        if dis.isAligned() then
            break
        end
    end
    dis.aim(-math.pi/2, math.pi)

    for i=0, shot_count, 1 do
        dis.dispense()
        os.sleep(0.2)
    end
end
