import numpy as np
# required to plot a representation of Bin and contained items 
from matplotlib.patches import Rectangle,Circle
import matplotlib.pyplot as plt
import mpl_toolkits.mplot3d.art3d as art3d
from collections import Counter
import random


def _plotCube(ax, x, y, z, dx, dy, dz, color='red',mode=2):
	""" Auxiliary function to plot a cube. code taken somewhere from the web.  """
	# fig = plt.figure()
	# ax = plt.axes(projection='3d')

	xx = [x, x, x+dx, x+dx, x]
	yy = [y, y+dy, y+dy, y, y]
	
	kwargs = {'alpha': 1, 'color': color,'linewidth':1 }
	if mode == 1 :
		ax.plot3D(xx, yy, [z]*5, **kwargs)
		ax.plot3D(xx, yy, [z+dz]*5, **kwargs)
		ax.plot3D([x, x], [y, y], [z, z+dz], **kwargs)
		ax.plot3D([x, x], [y+dy, y+dy], [z, z+dz], **kwargs)
		ax.plot3D([x+dx, x+dx], [y+dy, y+dy], [z, z+dz], **kwargs)
		ax.plot3D([x+dx, x+dx], [y, y], [z, z+dz], **kwargs)
	else :
		p = Rectangle((x,y),dx,dy,fc=color,ec='black')
		p2 = Rectangle((x,y),dx,dy,fc=color,ec='black')
		p3 = Rectangle((y,z),dy,dz,fc=color,ec='black')
		p4 = Rectangle((y,z),dy,dz,fc=color,ec='black')
		p5 = Rectangle((x,z),dx,dz,fc=color,ec='black')
		p6 = Rectangle((x,z),dx,dz,fc=color,ec='black')
		ax.add_patch(p)
		ax.add_patch(p2)
		ax.add_patch(p3)
		ax.add_patch(p4)
		ax.add_patch(p5)
		ax.add_patch(p6)
		art3d.pathpatch_2d_to_3d(p, z=z, zdir="z")
		art3d.pathpatch_2d_to_3d(p2, z=z+dz, zdir="z")
		art3d.pathpatch_2d_to_3d(p3, z=x, zdir="x")
		art3d.pathpatch_2d_to_3d(p4, z=x + dx, zdir="x")
		art3d.pathpatch_2d_to_3d(p5, z=y, zdir="y")
		art3d.pathpatch_2d_to_3d(p6, z=y + dy, zdir="y")
	
	# plt.show()




def plotBoxAndItems(items,bin_w,bin_h,bin_l,title=""):
	""" side effective. Plot the Bin and the items it contains. """
	fig = plt.figure()
	axGlob = plt.axes(projection='3d')

	colors = ["blue", "green", "red", "cyan", "magenta", "yellow"]
	counter = 0
	# fit rotation type
	for item in items:
		colVal = random.randint(0,5)

		x,y,z = item[0],item[1],item[2]
		w,h,l = item[3],item[4],item[5]
		_plotCube(axGlob, float(x), float(z), float(y), float(w),float(l),float(h),color=colors[colVal],mode=2)
		
		counter = counter + 1
	# plot bin 
	_plotCube(axGlob,0, 0, 0, float(bin_w), float(bin_l), float(bin_h),color='black',mode=1)

	plt.title('result')
	setAxesEqual(axGlob)
	plt.show()

def setAxesEqual(ax):
	'''Make axes of 3D plot have equal scale so that spheres appear as spheres,
	cubes as cubes, etc..  This is one possible solution to Matplotlib's
	ax.set_aspect('equal') and ax.axis('equal') not working for 3D.
	Input
	ax: a matplotlib axis, e.g., as output from plt.gca().'''
	x_limits = ax.get_xlim3d()
	y_limits = ax.get_ylim3d()
	z_limits = ax.get_zlim3d()

	x_range = abs(x_limits[1] - x_limits[0])
	x_middle = np.mean(x_limits)
	y_range = abs(y_limits[1] - y_limits[0])
	y_middle = np.mean(y_limits)
	z_range = abs(z_limits[1] - z_limits[0])
	z_middle = np.mean(z_limits)

	# The plot bounding box is a sphere in the sense of the infinity
	# norm, hence I call half the max range the plot radius.
	plot_radius = 0.5 * max([x_range, y_range, z_range])

	ax.set_xlim3d([x_middle - plot_radius, x_middle + plot_radius])
	ax.set_ylim3d([y_middle - plot_radius, y_middle + plot_radius])
	ax.set_zlim3d([z_middle - plot_radius, z_middle + plot_radius])




#input the item coount
itemCount = 7
javaoutput = open("C://scripts//java.txt",'r') #the path of the output from Java
content = javaoutput.read()
javaoutput.close()
items = []

for i in range(itemCount+3):
	content = content[content.index("\n")+1:]

for i in range(itemCount):
	w = content[content.index("w") + 4 : content.index("h")]
	h = content[content.index("h") + 4 : content.index("l")]
	l = content[content.index("l") + 4 : content.index("x")]

	x = content[content.index("x") + 4 : content.index("y")]
	y = content[content.index("y") + 4 : content.index("z")]
	z = content[content.index("z") + 4 : content.index("v")]

	item = [x,y,z,w,h,l]
	items.append(item)
	content = content[content.index("\n")+1:]



binDim = [100,110,200] #enter the bin dimensions

plotBoxAndItems(items, binDim[0],binDim[1],binDim[2]) #plots the bin with items
