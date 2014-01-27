/*
 *  TreesRight.java
 *  Copyright Simon Hern 2011
 *  Contact: dishmoth@yahoo.co.uk, www.dishmoth.com
 */

package com.dishmoth.miniquests.game;

// boundary on the right-side (positive x) of a room
public class TreesRight extends Wall {

  // tree images
  private static final String kImageNames[] = { "TreesRight.png",
                                                "TreesRight2.png" };
  private static EgaImage kImages[] = null;
  
  // which image to show
  private int mImageIndex;
  
  // constructor
  public TreesRight(int x, int y, int z, int imageIndex) {

    super(x, y, z);

    assert( imageIndex >= 0 && imageIndex < kImageNames.length );
    mImageIndex = imageIndex;
    
    prepareImages();
    
  } // constructor

  // load all of the tree images
  static private void prepareImages() {
    
    if ( kImages != null ) return;
    kImages = new EgaImage[kImageNames.length];
    
    kImages[0] = prepareImage("TreesRight.png"); 
    kImages[1] = prepareImage("TreesRight2.png"); 
    
  } // prepareImages()
  
  // load the tree image and give it depth
  static private EgaImage prepareImage(String fileName) {
    
    EgaImage pic = Env.resources().loadEgaImage(fileName);
    
    final int width  = pic.width(),
              height = pic.height();
    assert( width == 2*Room.kSize );
    
    final int refXPos = width - 1,
              refYPos = height - 1;
    
    byte pixels[] = pic.pixels();

    float depths[] = new float[width*height];
    for ( int ix = 0 ; ix < width ; ix++ ) {
      depths[width-1-ix] = ix/2;
    }
    
    int index = width;
    for ( int iy = 1 ; iy < height ; iy++ ) {
      for ( int ix = 0 ; ix < width ; ix++ ) {
        depths[index++] = depths[ix];
      }
    }
    
    return new EgaImage(refXPos, refYPos, width, height, pixels, depths);
    
  } // prepareImage()
  
  // doors cannot be added to tree walls
  @Override
  public Door addDoor(int yPos, int zPos, byte floorColour[], int floorDrop) {
    
    assert( false );
    return null;
    
  } // Wall.addDoor()
  
  // whether the player can stand at the specified position
  @Override
  public boolean isPlatform(int x, int y, int z) {

    return false;
    
  } // Obstacle.isPlatform()

  // whether there is space at the specified position
  @Override
  public boolean isEmpty(int x, int y, int z) {

    return true;
  
  } // Obstacle.isEmpty()

  // whether the position is outside of the game world
  public boolean isVoid(int x, int y, int z) { 

    x -= mXPos;
    y -= mYPos;
    z -= mZPos;
    if ( x != Room.kSize+1 || (y < 0 || y >= Room.kSize) ) return false;
    return true;
    
  } // Obstacle.isVoid()

  // display the wall
  @Override
  public void draw(EgaCanvas canvas) {

    final int xOrigin = mXPos - mCamera.xPos(),
              yOrigin = mYPos - mCamera.yPos(),
              zOrigin = mZPos - mCamera.zPos();

    if ( xOrigin + yOrigin < -10 ) return;
    
    EgaImage image = kImages[mImageIndex];
    image.draw3D(canvas, 2*(xOrigin+Room.kSize), 2*yOrigin, zOrigin);
    
  } // Sprite.draw()

} // class TreesRight
