/*
 *  SoundsApp.java
 *  Copyright (c) 2017 Simon Hern
 *  Contact: dishmoth@yahoo.co.uk, dishmoth.com, github.com/dishmoth
 */

package com.dishmoth.miniquests;

import com.dishmoth.miniquests.game.Env;
import com.dishmoth.miniquests.game.Sounds;

import java.io.IOException;

// class for controlling audio
public class SoundsApp extends Sounds {

  // all of the sound clips
  private SoundEffect mSounds[][];

  // index of the next version of a sound effect to play (usually zero)
  private int mNextVersion[];
  
  // constructor
  public SoundsApp() {

    super();

    mSounds = new SoundEffect[kNumSounds][];
    mNextVersion = new int[kNumSounds];
    
  } // constructor
  
  // prepare a sound resource
  @Override
  protected void loadSound(int id, String fileName, int numVersions) {
    
    assert( id >= 0 && id < kNumSounds );
    assert( fileName != null );
    assert( numVersions > 0 );
    
    if ( mSounds[id] != null ) return; // already loaded
    mSounds[id] = new SoundEffect[numVersions];
    mNextVersion[id] = 0;
    
    ResourcesApp resources = (ResourcesApp)Env.resources();
    try {

      for ( int k = 0 ; k < numVersions ; k++ ) {
        mSounds[id][k] = resources.loadSoundEffect(fileName);
      }

    } catch ( IOException ex ) {
      
      Env.debug(ex.getMessage());
      mSounds[id] = null;
      return;
      
    }
    
    if ( isLooped(id) ) {
      assert( numVersions == 1 );
      mSounds[id][0].setLooped(true);
    }
      
  } // Sounds.loadSound()

  // check that all sounds have loaded
  @Override
  protected void checkSounds() {

    mAvailable = true;
    
    for ( int id = 0 ; id < kNumSounds ; id++ ) {
      if ( mSounds[id] == null ) {
        mAvailable = false;
        return;
      }
    }
    
    try {
      ResourcesApp resources = (ResourcesApp)Env.resources();
      SoundEffect testSound = resources.loadSoundEffect("silence.ogg");
      testSound.play();
    } catch ( IOException ex ) {
      Env.debug(ex.getMessage());
    }
      
  } // Sounds.checkSound()

  // play a sound effect
  @Override
  public void play(int id) {
    
    if ( !mAvailable || mMuted ) return;
    
    assert( id >= 0 && id < kNumSounds );
    assert( !isLooped(id) );
    
    int version = mNextVersion[id];
    mSounds[id][version].play();
    version = ( (version+1) % mSounds[id].length );
    mNextVersion[id] = version;
    
  } // Sounds.play()

  // start a sound looping (if it isn't already)
  @Override
  public void loop(int id) {
    
    if ( !mAvailable || mMuted ) return;
    
    assert( id >= 0 && id < kNumSounds );
    assert( isLooped(id) );
    
    mSounds[id][0].play();
        
  } // Sounds.loop()
  
  // stop a looping sound
  @Override
  public void stop(int id) {

    if ( !mAvailable || mMuted ) return;
    
    assert( id >= 0 && id < kNumSounds );
    assert( isLooped(id) );
    
    mSounds[id][0].stop();
    
  } // Sounds.stop()

} // class SoundsApp
