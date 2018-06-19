
import java.io.File;
import java.util.Hashtable;
import javax.sound.midi.*;

import com.leff.midi.MidiFile;


public class Musico {
	
	public static void main(String[] args)
	{
		Agent agent1 = new Agent();
		agent1.teach(Scales.CMajScale, 20);
		agent1.teach(Scales.bluesScale, 10);
		//agent1.printMemory();
		//agent1.printAdvancedMemory();
		playSong(agent1.compose(20));
	}

	static private void playSong(MidiFile song){
		try{
			System.out.println("Playing song");
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			File tempMidi = new File("tempMidi");
			song.writeToFile(tempMidi);
			sequencer.setSequence(MidiSystem.getSequence(tempMidi));
			sequencer.start();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
