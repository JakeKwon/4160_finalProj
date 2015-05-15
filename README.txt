Souren Papazian (ssp2155)
Guanqi Luo (gl2483)
Jae Hyun Kwon (jk3655)

FINAL PROJECT

How to compile:
	Just run "ant" in the upper most directory (where the build.xml is).
	Note that our shader versions are not what we used in the assignments and might not run on any computer.
	But we tried to run it in the CLIC lab and it compiles and works there!
	Be sure to wait 5 seconds at the start for it to load. After that wait, it will all run smoothly.

How to play the game:
	Press A: move left
	Press S: move back
	Press W: move forward
	Press D: move right
	Press Shift Key: move up
	Press Space Key: move down
	Mouse movement: camera rotation
	Mouse Left Click: respawn a bird at the camera position if the camera is within the cage. 
			  Make sure your mouse is in the middle of the screen when you start, so that the click works
	G: Switch to geometry shader
	O: Switch back to original shader without geometry.

VIDEO EXPLANATION
	We initialize 500 boids in cube of size 800x800x800. They all have random locations and directions. They soon form flocks that fly around. 
	The yellow cube is the border, when they hit the border they loop to the other side so that they all stay inside the simulation cube.

