## 4160Final

### Classes

#### Main

Game loop
* Rendering

  * Player & Object
  
  * Static Scene & Background
  
  * Life/Points/Time text
* Update positions
* Create/Destroy objects

#### Engine

Handles logic:
* Player Life
* Generating Astroids
* Collision

#### Player

```
void      Render()        // Cannon?
```

#### Astroid

```
void      Render()        // Icosahedron?
void      Move()          // change x,y based on velocity & direction
                          // change vel based on acceleration
double    Size
double    Velocity
double    Acceleration
vector3   Direction       // normal vector 
```

#### Background

Renders a 'sky' behind everything

#### Octree

For checking collisions efficiently