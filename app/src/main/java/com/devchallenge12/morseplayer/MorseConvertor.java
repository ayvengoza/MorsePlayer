package com.devchallenge12.morseplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MorseConvertor {

    private static final int DOT_ID = 0;
    private static final int DASH_ID = 1;
    private static final int INSP_ID = 2;
    private static final int LSP_ID = 3;
    private static final int WSP_ID = 4;


    private static final String DOT = "·";
    private static final String DASH = "\u2014";
    private static final String INSP = " ";
    private static final String LSP = "   ";
    private static final String WSP = "     ";

    private static HashMap<Character, Integer[]> morseMap = new HashMap<Character, Integer[]>();
    static{
        morseMap.put('A', new Integer[]{
                DOT_ID, INSP_ID,
                DASH_ID});
        morseMap.put('B', new Integer[]{
                DASH_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID});
        morseMap.put('C', new Integer[]{
                DASH_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DOT_ID});
        morseMap.put('D', new Integer[]{
                DASH_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('E', new Integer[]{
                DOT_ID
        });
        morseMap.put('F', new Integer[]{
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('G', new Integer[]{
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('H', new Integer[]{
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('I', new Integer[]{
                DOT_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('J', new Integer[]{
                DOT_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('K', new Integer[]{
                DASH_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('L', new Integer[]{
                DOT_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('M', new Integer[]{
                DASH_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('N', new Integer[]{
                DASH_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('O', new Integer[]{
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('P', new Integer[]{
                DOT_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('Q', new Integer[]{
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('R', new Integer[]{
                DOT_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('S', new Integer[]{
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('T', new Integer[]{
                DASH_ID
        });
        morseMap.put('U', new Integer[]{
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('V', new Integer[]{
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('W', new Integer[]{
                DOT_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('X', new Integer[]{
                DASH_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('Y', new Integer[]{
                DASH_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('Z', new Integer[]{
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('1', new Integer[]{
                DOT_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('2', new Integer[]{
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('3', new Integer[]{
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('4', new Integer[]{
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DASH_ID
        });
        morseMap.put('5', new Integer[]{
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('6', new Integer[]{
                DASH_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('7', new Integer[]{
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('8', new Integer[]{
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DOT_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('9', new Integer[]{
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DOT_ID
        });
        morseMap.put('0', new Integer[]{
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID, INSP_ID,
                DASH_ID
        });
    }

    private static List<Integer> textToMorse(String msg){
        ArrayList<Integer> msgList = new ArrayList<>();
        char[] charsMsg = msg.toUpperCase().toCharArray();
        for(char c : charsMsg){
            if(morseMap.containsKey(c)){
                msgList.addAll(Arrays.asList(morseMap.get(c)));
                msgList.add(LSP_ID);
            } else {
                switch (c){
                    case ' ' :
                    case '\t':
                    case '\n':
                        msgList.add(WSP_ID);
                        break;
                }
            }
        }
        return msgList;
    }
    public static String textToMorseMsg(String msg){
        StringBuffer outMsg = new StringBuffer();
        List<Integer> intMsg = textToMorse(msg);
        for(int item : intMsg){
            switch (item){
                case DOT_ID:
                    outMsg.append(DOT);
                    break;
                case DASH_ID:
                    outMsg.append(DASH);
                    break;
                case INSP_ID:
                    outMsg.append(INSP);
                    break;
                case LSP_ID:
                    outMsg.append(LSP);
                    break;
                case WSP_ID:
                    outMsg.append(WSP);
                    break;
            }
        }
        return outMsg.toString();
    }

    public static String textToMorseAudio(String msg){
        return new String();
    }
}
