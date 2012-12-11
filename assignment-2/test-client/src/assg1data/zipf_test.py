import pylab as pl
import numpy as np

lines = open("test.txt").read().splitlines()
x = [int(line) for line in lines]

#pl.hist(x, bins=np.logspace(0.1, 1.0, 50))
#pl.gca().set_xscale("log")

pl.hist(x, 50)
pl.show()
