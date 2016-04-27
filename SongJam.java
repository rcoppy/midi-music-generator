/* 
	AMBIENT RELAPSE
	
	(This is the main method.)
	
	This is a sloppily written mess. I'd like to do a better job, but it's 3:56 and I need to sleep. 

	The original idea was to generate the skeleton of a song structure in the scale degrees class (the chord progression), 
	and then have the two sorting algorithms riff on top of it. 

	That's kind of what happens. 

	The underlying chords play correctly, but the melody/harmony threads don't really harmonize with them.

	ScaleDegrees picks a mode for the song. (Dorian, Classical Major, Classical Minor.)
	It defines the 2-4 chords from a scale in that mode the song will use. 

	Builds thread 1 off of AABA song progression. These are chords. They sound good. (Except for the B part. Not usually.)

	Thread 2 and thread 3 are melody and harmony. Built off of thread 1, technically supposed to select notes from within predetermined song chords. 
	Don't sound so good. Notes here are selected using an arbitrary sorting algorithm; one track uses bubble, one using insert. Rhythm is determined by another even more arbitrary convoluted algorithm.

	A thread runs three tracks at once. (The thread code is directly copy-pasted from source listed, but has been modified significantly.)
	
	Each one holds an instance of SongJam, and so can access MIDI controls. 

	Because of design of melody playback, melody and harmony terminate on time with chords.

	Sounds meh, (sometimes if lucky better than meh), and all of it is coded sloppily, but it works. 

*/ 



import java.util.Arrays;
import java.util.ArrayList; 
import java.util.Random; 

public class SongJam { 
	
	// global vars
	int chordsInstrument = 96; // sweep
	int melodyInstrument = 28; // electric guitar clean
	int harmonyInstrument = 34; // electric base - finger

	SimpleMidi m = new SimpleMidi();
	int[] list = {5,25,7,6,8,5,3,45,68,3568,456,56,65,756,4}; 
	ScaleDegrees struct = new ScaleDegrees(list);
	int timeMill = getTotalSongLength(struct); // song length in milliseconds

	public static void main(String[] args) { 

		/*// make a completely new list
		Random r = new Random(); 
		for (int i = 0; i < list.length; i ++) {
			list[i] = r.nextInt(4000); 
		}
		// new scale degrees
		struct = new ScaleDegrees(list); 
		*/ // complications with being static, it's 3:18 AM, no time to deal with it

		SongJam song = new SongJam(); 

		//System.out.println(Arrays.toString(list));
		
		// song intro 
	    
	    song.m.setInstrument(54); // voice oohs; halo and sweep are cool  
	    int beatLength = 8*song.struct.tempoMillsecs;
		song.m.playChord(0,song.struct.rootNote+song.struct.chordRootsA.get(0),95,beatLength);

		// threads
		RunnableDemo R1 = new RunnableDemo("Thread - Chords", song, 0);
	    R1.start();
	      
	    RunnableDemo R2 = new RunnableDemo("Thread - Insertion", song, 1);
	    R2.start();

	    RunnableDemo R3 = new RunnableDemo("Thread - Bubble", song, 2);
	    R3.start();


	    // song conclusion
	    // why u no play? u play before i run threads. :P
	    // QUICK FIX: move it to the case 0 thread 
	    /*song.m.setInstrument(54); // voice oohs; halo and sweep are cool  
	    beatLength = 8*song.struct.tempoMillsecs;
		song.m.playChord(0,song.struct.rootNote+song.struct.chordRootsA.get(0),95,beatLength);*/
	}

	public SongJam() {
		

		m.open();
		m.setInstrument(96); // voice oohs; halo and sweep are cool  

		/* Other instruments: 
			54 - voice oohs
			62 - overdriven guitar
			90 - Pad 2 (warm)

			89.	Pad 1 (new age)
			26.	Acoustic Guitar (steel)	90.	Pad 2 (warm)
			27.	Electric Guitar (jazz)	91.	Pad 3 (polysynth)
			28.	Electric Guitar (clean)	92.	Pad 4 (choir)
			29.	Electric Guitar (muted)	93.	Pad 5 (bowed)
			30.	Overdriven Guitar	94.	Pad 6 (metallic)
			31.	Distortion Guitar	95.	Pad 7 (halo)
			32.	Guitar harmonics	96.	Pad 8 (sweep)

			12. - vibraphone
			
		*/ 

		


			/*
		int[] list = {5,25,7,6,8,5,3,45,68,3568,456,56,65,756,4}; 

		System.out.println(Arrays.toString(list));

		ScaleDegrees struct = new ScaleDegrees(list); 

		int timeMill = playBaseChords(m,struct,0,chordsInstrument); // save length in milliseconds of underlying chords

		playMelody(m,struct,1,melodyInstrument,timeMill); 
			*/
	}

	public int playBaseChords(SimpleMidi m, ScaleDegrees struct, int channel, int instrument) { 
		
		m.setInstrument(channel,instrument);

		int totalMillSecs = 0; 
		int beatLength; 
		int velocity = 200; 

		// A section
		for (int i = 0; i < struct.chordRootsA.size(); i ++) { 
				beatLength = 4*struct.tempoMillsecs;
				m.playChord(channel,struct.rootNote+struct.chordRootsA.get(i),velocity,beatLength);
				totalMillSecs += beatLength;
		}

		// A section
		for (int i = 0; i < struct.chordRootsA.size(); i ++) { 
				beatLength = 4*struct.tempoMillsecs;
				m.playChord(channel,struct.rootNote+struct.chordRootsA.get(i),velocity,beatLength);
				totalMillSecs += beatLength;
		}

		// B section
		for (int i = 0; i < struct.chordRootsB.size(); i ++) { 
				beatLength = 4*struct.tempoMillsecs;
				m.playChord(channel,struct.rootNote+struct.chordRootsB.get(i),velocity,beatLength);
				totalMillSecs += beatLength;
		}

		// A section
		for (int i = 0; i < struct.chordRootsA.size(); i ++) { 
				beatLength = 4*struct.tempoMillsecs;
				m.playChord(channel,struct.rootNote+struct.chordRootsA.get(i),velocity,beatLength);
				totalMillSecs += beatLength;
		}

		return totalMillSecs; // how much time will elapse with this part - lets us synchronize with other instruments
	}

	public static int getTotalSongLength(ScaleDegrees struct) { // get length of song in milliseconds
		// calculate duration of chords without actually playing them - sloppy way of doing this, but it's 2:42 AM
		int totalMillSecs = 0; 
		int beatLength; 

		// A section
		for (int i = 0; i < struct.chordRootsA.size(); i ++) { 
				beatLength = 4*struct.tempoMillsecs;
				
				totalMillSecs += beatLength;
		}

		// A section
		for (int i = 0; i < struct.chordRootsA.size(); i ++) { 
				beatLength = 4*struct.tempoMillsecs;
				
				totalMillSecs += beatLength;
		}

		// B section
		for (int i = 0; i < struct.chordRootsB.size(); i ++) { 
				beatLength = 4*struct.tempoMillsecs;
				
				totalMillSecs += beatLength;
		}

		// A section
		for (int i = 0; i < struct.chordRootsA.size(); i ++) { 
				beatLength = 4*struct.tempoMillsecs;
				
				totalMillSecs += beatLength;
		}

		return totalMillSecs; // how much time will elapse with this part - lets us synchronize with other instruments
	}

	public void playMelody(SimpleMidi m, ScaleDegrees struct, int channel, int instrument, int timeMill, boolean useInsert) { // length is constrained by timeMill

		m.setInstrument(channel,instrument); 

		int beatLength; // millisecond measure 
		int maxNotes = struct.list.length*8; 
		int timeLeft = timeMill; // how many milliseconds left? 

		int minNoteTime = timeLeft/maxNotes; // milliseconds per note if maximum number of notes will be played

		// play notes in multiples of 2, 4, 8 minNoteTime
		int multiple = 1; 
		int i = 0; 

		Sorter s = new Sorter(struct.list);

		Random r = new Random(); 

		// goal: fit maximum number of notes into allotted time
		while(timeLeft > 0) {
			i ++; 
			if (useInsert) { 
				s.insertionSort(i); 
			}
			else { 
				s.bubbleSort(i);
			}
			s.setUnsortedList(list); // globalvar list

			int[] newList = s.getSortedList();
			int rand = newList[r.nextInt(newList.length)] % struct.chordRootsA.size(); // take random value from list. Use value to get a psuedorandom index.
			System.out.println(Arrays.toString(newList)); 
			int pitch = struct.chordRootsA.get(rand); 

			multiple = rand%8 + 1; // ranges 1-4; dynamic note length

			beatLength = multiple*minNoteTime; 
			
			m.playNote(channel, struct.rootNote+pitch+(1*r.nextInt(2)*r.nextInt(2)*2), (i*5)%160, beatLength); // pitch +/- 2*(1 or 0)
			timeLeft -= beatLength; // decrease time by duration of note
		}
	}
}


// http://www.tutorialspoint.com/java/java_multithreading.htm
class RunnableDemo implements Runnable {
   private Thread t;
   private String threadName;
   
   private SongJam song; 
   private int track; 
   private ScaleDegrees struct;
   private int timeMill; 

   RunnableDemo( String name, SongJam song, int track) {
       threadName = name;
       this.song = song; 
       this.track = track; 
       System.out.println("Creating " +  threadName );
   }
   public void run() { // stuff you want to happen has to go in here 
      System.out.println("Running " +  threadName );
      try {
         //for(int i = 4; i > 0; i--) {
            //System.out.println("Thread: " + threadName + ", " + i);

            /* Our code */

            //song. 

			switch (track) {
				case 0: // chords
					System.out.println("I AM CHORDS");
					song.playBaseChords(song.m,song.struct,0,song.chordsInstrument); 

					// song conclusion
					song.m.setInstrument(54); // voice oohs; halo and sweep are cool  
	    			int beatLength = 12*song.struct.tempoMillsecs;
					song.m.playChord(0,song.struct.rootNote+song.struct.chordRootsA.get(0),95,beatLength);
					
					// these won't play - 'cause i'm using doubles in an int?
					//song.m.playChord(0,song.struct.rootNote+song.struct.chordRootsA.get(0),45,1/4*beatLength);
					//song.m.playChord(0,song.struct.rootNote+song.struct.chordRootsA.get(0),20,1/8*beatLength);
					//song.m.playChord(0,song.struct.rootNote+song.struct.chordRootsA.get(0),10,1/8*beatLength);
					break;
				case 1: // insertion sort melody
					System.out.println("I AM INSERTION MELODY");
					song.playMelody(song.m,song.struct,1,song.melodyInstrument,song.timeMill,true); 
					break;
				case 2: // bubble sort melody
					System.out.println("I AM BUBBLE MELODY");
					song.playMelody(song.m,song.struct,2,song.harmonyInstrument,song.timeMill,false); 
					break;
			}
            

            // Let the thread sleep for a while.
            Thread.sleep(50);
         //}
     } catch (InterruptedException e) {
         System.out.println("Thread " +  threadName + " interrupted.");
     }
     System.out.println("Thread " +  threadName + " exiting.");
   }
   
   public void start ()
   {
      System.out.println("Starting " +  threadName );
      if (t == null)
      {
         t = new Thread (this, threadName);
         t.start ();
      }
   }

}