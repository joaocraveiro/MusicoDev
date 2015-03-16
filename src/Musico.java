import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import javax.sound.midi.*;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;


public class Musico {

	static Hashtable<String, Integer> hits = new Hashtable<String,Integer>();
	
	public static void main(String[] args)
	{
		while(true)
			loop();
	}
	
	private static void loop()
	{
		// 1. Create some MidiTracks
		MidiTrack tempoTrack = new MidiTrack();
		MidiTrack noteTrack = new MidiTrack();

		// 2. Add events to the tracks
		// Track 0 is the tempo map
		TimeSignature ts = new TimeSignature();
		ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

		Tempo tempo = new Tempo();
		tempo.setBpm(60);

		tempoTrack.insertEvent(ts);
		tempoTrack.insertEvent(tempo);

		// Track 1 will have some notes in it
		final int NOTE_COUNT = 20;

		int[] notes = Blues.blueScale(NOTE_COUNT,hits);
		for(int i = 0; i < NOTE_COUNT; i++)
		{
		    int channel = 0;
		    int pitch = notes[i];
		    int velocity = 100;
		    long tick = i * 120;
		    long duration = 120;

		    noteTrack.insertNote(channel, pitch, velocity, tick, duration);
		}

		// 3. Create a MidiFile with the tracks we created
		List<MidiTrack> tracks = new ArrayList<MidiTrack>();
		tracks.add(tempoTrack);
		tracks.add(noteTrack);

		MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION);
		midi.addTrack(tempoTrack);
		midi.addTrack(noteTrack);

		// 4. Write the MIDI data to a file
		File output = new File("example.mid");
		try
		{
		    midi.writeToFile(output);		    		    
		}
		catch(IOException e)
		{
		    System.err.println(e);
		}		   
		output.setReadable(true, false);
		
		try{
	        Sequencer sequencer = MidiSystem.getSequencer();
	        sequencer.open();
	        InputStream is = new BufferedInputStream(new FileInputStream("example.mid"));
	        sequencer.setSequence(is);
	        sequencer.start();
		}
		catch(Exception e)
		{}
		Scanner input = new Scanner(System.in);
		System.out.println("Eval:");
		String eval = input.nextLine();        
        rank(Blues.memoryI,eval);
	}			

	private static void rank(int[] memory, String eval) {		
		int k;
		int patSize = 2;
		for(int i = 0; i < memory.length-patSize; i++)
		{			
			int j = i+1;
			k = j+1;
				Integer x = memory[i];
				Integer y = memory[j];
				String key = "" + x.toString() + y.toString();
				while(k<i+patSize)
					{
						Integer z = memory[k];	
						key += z.toString();
					}
				if(hits.containsKey(key) && eval.equals("y")) hits.put(key, hits.get(key)+1);
				else if(hits.containsKey(key) && !eval.equals("y")) hits.put(key, hits.get(key)-1);
				else if(!hits.containsKey(key) && eval.equals("y")) hits.put(key, 1);
				else if(!hits.containsKey(key) && !eval.equals("y")) hits.put(key, -1);
			}
		for(String pattern : hits.keySet())
		{
			System.out.println(pattern + ": " + hits.get(pattern));		
		}		
	}

	}
