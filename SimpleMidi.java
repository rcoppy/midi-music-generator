/*
Simple Midi Class based on code from:
http://stackoverflow.com/questions/380103/simple-java-midi-example-not-producing-any-sound

This simple midi class will allow you to play notes in a variety of ways. 

Most values in midi go from 0 through 127. 
	- You have 16 channels to play with
	- The duration is in milliseconds

For a list of various midi instruments, visit this site:
http://www.midi.org/techspecs/gm1sound.php

Note that if you are playing drums, you may not always hear sound 
if you play a note that is not mapped to a drum. The site above
also has a percussion key map at the bottom of the page.

Here is sample usage that would play a scale:

	SimpleMidi m = new SimpleMidi();

	m.open();

	m.setInstrument(25);

	m.playNote(60);
	m.playNote(62);
	m.playNote(64);
	m.playNote(65);
	m.playNote(67);
	m.playNote(69);
	m.playNote(71);
	m.playNote(72);

	m.close();
*/

import javax.sound.midi.*;
import java.lang.Thread;

public class SimpleMidi {

	private Synthesizer synthesizer;
	private MidiChannel[] channels;

	public SimpleMidi() {}

	public void open() {
		try {
			synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			System.out.println(synthesizer.getMaxPolyphony()); // debug - tells how many notes can be played at once

			channels = synthesizer.getChannels();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playNote(int channel, int note, int velocity, int duration) {
		try {
			channels[channel].noteOn(note, velocity);
			Thread.sleep(duration);
			channels[channel].noteOff(note);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playNote(int note, int velocity, int duration) {
		try {
			channels[0].noteOn(note, velocity);
			Thread.sleep(duration);
			channels[0].noteOff(note);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playNote(int note) {
		try {
			channels[0].noteOn(note, 100);
			Thread.sleep(200);
			channels[0].noteOff(note);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playChord(int channel, int note, int velocity, int duration) {
		try {
			channels[channel].noteOn(note, velocity);
			channels[channel].noteOn(note+4, velocity);
			channels[channel].noteOn(note+7, velocity);
			Thread.sleep(duration);
			channels[channel].noteOff(note);
			channels[channel].noteOff(note+4);
			channels[channel].noteOff(note+7);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try{
			synthesizer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setInstrument(int instrument) {
		if (instrument >=0 && instrument < 128) {
			channels[0].programChange(instrument);
		}
	}

	public void setInstrument(int channel, int instrument) {
		if (instrument >=0 && instrument < 128 && channel<channels.length) {
			channels[channel].programChange(instrument);
		}
	}
}
