function shoot(disArr, count)
    for i=1, count, 1 do
        for _,d in disArr do
            d.dispense()
        end
        os.sleep(0.25)
    end
end