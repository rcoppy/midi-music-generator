import java.util.Random; 
import java.util.ArrayList; 
import java.util.List; 

public class ScaleDegrees { 
	/*// dorian
	W  H   W  W W  W   H
	+2 +3 +5 +7 +9 +11 +12

	// major
	W  W  H  W  W  W   H
	+2 +4 +5 +7 +9 +11 +12

	// minor
	D E  F  G  A  Bflat C   D
 	  +2 +3	+5 +7 +8    +10 +12

 	W H W W H W W
 	*/

 	/// TODO: Hook writer

 	int barInterval = 8; // can be 4, 12, or 16
 	int middleC = 60; 
 	int rootNote; 
 	ArrayList<Integer> chordRootsA = new ArrayList<Integer>(); // init is a workaround // chords for A section
 	ArrayList<Integer> chordRootsB = new ArrayList<Integer>(); // chords for B section
 	int[] mode = getMode(0); // lydian, phrygian, dorian, etc.
 	int tempoBPM = 120; 
 	int tempoMillsecs = (int) (1/(tempoBPM * (1/60.0) * (1/1000.0))); //(tempoBPM * minute/60 seconds * second/1000 mill) // milliseconds per beat
 	int[] list; 

 	public ScaleDegrees(int[] list)  {
 		this.list = list; 
 		defineSongStructure(this.list);
 	}

 	public int[] getMode(int input) {
		int[] intervalList; 

		int[] listDorian = { 2,3,5,7,9,11,12 };
		int[] listMajor  = { 2,4,5,7,9,11,12 }; 
		int[] listMinor  = { 2,3,5,7,8,10,12 };

		// classical minor
		intervalList = listMinor; // java doesn't like returning a list directly as { , }	 	

		switch (input) {
			case 0: // dorian mode
				intervalList = listDorian; 
				break;
			case 1: // classical major
				intervalList = listMajor;
				break;
		}	
 		
 		return intervalList;
 	}

 	public void defineSongStructure(int[] list) { // gives mode, tempo, root note, and chords roots that derive from the root note
 		// sets up the framework for the song
 		
 		Random rand = new Random();
 		int[] chords;

 		rootNote = list[rand.nextInt(list.length)] % 6 + middleC-24; //two octaves below middle c  
 		mode = getMode(rand.nextInt(3));

 		// define number of chord changes in A section, B section - ranges from 2 to 4
 		int maxChordsA = 2*(list[rand.nextInt(list.length)] % 2) + 2; 
 		int maxChordsB = 2*(list[rand.nextInt(list.length)] % 2) + 2;
 		
 		// now fill the chords in each section 
 		
 		//seed the initial indices first 
 		chordRootsA.add(list[rand.nextInt(list.length)] % 12);
 		chordRootsB.add(list[rand.nextInt(list.length)] % 12); 

 		// fill out the chords with varying scale degrees
 		for (int i = 0; i < maxChordsA-1; i ++) {
 			
 			// fill out A chords
 			int step = list[rand.nextInt(list.length)] % mode.length; // pick one of mode's scale degrees based off of a random index
 			 
 			int pitch = mode[step]; 

 			if (pitch > 6) {
 				//pitch -= 12; // //drop pitch an octave if it's above the 4th of the scale (avoid really annoying high-pitched notes) 
 			}
 			chordRootsA.add(chordRootsA.get(0) + pitch);

 			// fill out B chords
 			step = list[rand.nextInt(list.length)] % mode.length; // pick one of mode's scale degrees based off of a random index
 		    
 			pitch = mode[step]; 

 			if (pitch > 6) {
 				//pitch -= 12; // //drop pitch an octave if it's above the 4th of the scale (avoid really annoying high-pitched notes) 
 			}

 		    chordRootsB.add(chordRootsB.get(0) + pitch);
 		}

 		// having the number of chords in the B section differ from the A section didn't sound good; keep them the same
 		//for (int i = 0; i < maxChordsB-1; i ++) {
 		//	int step = list[rand.nextInt(list.length)] % mode.length; // pick one of mode's scale degrees based off of a random index
 		//	chordRootsB.add(chordRootsB.get(0) + mode[step]);
 		//}

 		int listTotal = 0; 
 		for (int i = 0; i < list.length; i ++) {
 			listTotal += list[i]; 
 		}

 		// set tempo by taking modulus of list total (all totally arbirtrary selection methods)
 		tempoBPM = 120 + (rand.nextInt(2)*-1) * (list.length % 60); // equiv. to 120 +/- rand(0 through 60)
 		tempoMillsecs = (int) (1/(tempoBPM * (1/60.) * (0.001))); //(tempoBPM * minute/60 seconds * second/1000 mill) // milliseconds per beat
 	
 		barInterval = 4 + (4* (list[rand.nextInt(list.length)]%4)); // bar interval ranges from 4 to 16 bars  

 		// last step: write a hook (will be repeated at beginning of every A section)
 		// hook will be 1/4 length of one bar interval 
 		// e.g. first 2 bars of every 8 will be hook

 		//List<List<String>> hookNotes = new ArrayList<List<String>>();
 		//ArrayList<ArrayList<int>> hookNotes = new ArrayList<ArrayList<int>>(); 
 		
 		//double beatsLeft = 1/4 * barInterval * 4; // assume we work in 4/4 time

 		// TODO: Finish this later. 
 		/*while (beatsLeft > 0) {
 			double phraseLength = 4 + 4*rand.nextInt(3); // phrases can be 3 bars long
 			
 			double beatLength = 1/4 + 1/4*rand.nextInt(16); // at minimum a 16th note, at maximum a whole note


 		}*/


 	}
}

