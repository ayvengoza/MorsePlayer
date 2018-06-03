package com.devchallenge12.morseplayer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MorseMidi {

    static final int DOT = 8;
    static final int DASH = 24;
    static final int INSP = 8;
    static final int LSP = 24;
    static final int WSP = 56;
    static final int NOTE = 80;
    static final int INSTRUMENT = 1;
    static final int VELOCITY = 127;

    static final int header[] = new int[]{
                    0x4d, 0x54, 0x68, 0x64, 0x00, 0x00, 0x00, 0x06,
                    0x00, 0x00,
                    0x00, 0x01,
                    0x00, 0x10, // set 16 ticks per quarter note
                    0x4d, 0x54, 0x72, 0x6B
            };

    static final int footer[] = new int[]{
                    0x01, 0xFF, 0x2F, 0x00
            };

    static final int tempoEvent[] = new int[]{
                    0x00, 0xFF, 0x51, 0x03,
                    0x07, 0xA1, 0x20 // Tempo 200000
            };

    static final int keySigEvent[] = new int[]
            {
                    0x00, 0xFF, 0x59, 0x02,
                    0x00,
                    0x00
            };

    static final int timeSigEvent[] = new int[]
            {
                    0x00, 0xFF, 0x58, 0x04,
                    0x04,
                    0x02,
                    0x30,
                    0x08
            };

    public static byte[] intArrayToByteArray (int[] ints)
    {
        int l = ints.length;
        byte[] out = new byte[ints.length];
        for (int i = 0; i < l; i++)
        {
            out[i] = (byte) ints[i];
        }
        return out;
    }

    protected List<int[]> playEvents;

    public MorseMidi(){
        playEvents = new ArrayList<>();
        progChange(INSTRUMENT);
    }

    public void writeToFile (String filename) throws IOException
    {
        FileOutputStream fos = new FileOutputStream(filename);

        fos.write (intArrayToByteArray (header));

        int size = tempoEvent.length + keySigEvent.length + timeSigEvent.length + footer.length;

        for (int i = 0; i < playEvents.size(); i++)
            size += playEvents.get(i).length;

        // Write out the track data size in big-endian format
        int high = size / 256;
        int low = size - (high * 256);
        fos.write ((byte) 0);
        fos.write ((byte) 0);
        fos.write ((byte) high);
        fos.write ((byte) low);

        fos.write (intArrayToByteArray (tempoEvent));
        fos.write (intArrayToByteArray (keySigEvent));
        fos.write (intArrayToByteArray (timeSigEvent));

        // Write out the note and events
        for (int i = 0; i < playEvents.size(); i++)
        {
            fos.write (intArrayToByteArray (playEvents.get(i)));
        }

        // Write the footer and close
        fos.write (intArrayToByteArray (footer));
        fos.close();
    }

    public void setDot(){
        setNote(DOT, NOTE, VELOCITY);
    }

    public void setDash(){
        setNote(DASH, NOTE, VELOCITY);
    }

    public void setInsp(){
        setNote(INSP, NOTE, 0);
    }

    public void setLsp(){
        setNote(LSP, NOTE, 0);
    }

    public void setWsp(){
        setNote(WSP, NOTE, 0);
    }

    //Change midi instruments from 0 to 127
    private void progChange (int prog)
    {
        int[] data = new int[3];
        data[0] = 0;
        data[1] = 0xC0;
        data[2] = prog;
        playEvents.add (data);
    }

    private void setNote (int duration, int note, int velocity)
    {
        noteStart (0, note, velocity);
        noteEnd (duration, note);
    }

    private void noteStart (int delta, int note, int velocity)
    {
        int[] data = new int[4];
        data[0] = delta;
        data[1] = 0x90;
        data[2] = note;
        data[3] = velocity;
        playEvents.add (data);
    }

    private void noteEnd (int delta, int note)
    {
        int[] data = new int[4];
        data[0] = delta;
        data[1] = 0x80;
        data[2] = note;
        data[3] = 0;
        playEvents.add (data);
    }
}
