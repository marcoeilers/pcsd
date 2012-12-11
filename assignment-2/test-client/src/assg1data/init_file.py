import random

f = open("init_file", "w")

data = []
for i in range (10000):
	data.append(random.randint(0, 1000))

data.sort()

for item in data:
	f.write(str(item) + " " + str(random.randint(0, 99999)) + "\n")
