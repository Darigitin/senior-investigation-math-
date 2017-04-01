/*
 * Copyright (C) 2015 Matt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Scanner;

/**
 *
 * @author Matt
 */
public class EncryptWav
{
    //test
    private static String key, IV;
    public static void main(String[] args)
    {
        readAndRewriteWav(args[0]);
    }    

    private static void readAndRewriteWav(String filePathToReadFromMinusExtension)
    {
        getKeys();
        boolean[] keyArray = sizeKeys(key);
        boolean[] IVArray = sizeKeys(IV);
        String fileExtension = ".wav";

        String filePathToReadFrom =
            filePathToReadFromMinusExtension
            + fileExtension;

        WavFile wavFileToTest = WavFile.readFromFilePath(filePathToReadFrom, keyArray, IVArray);

        wavFileToTest.filePath = filePathToReadFromMinusExtension
        + "-ReadThenWritten"
        + fileExtension;

        wavFileToTest.writeToFilePath();
    }
    
    private static void getKeys(){
        Scanner keyboard = new Scanner(System.in);
        
        System.out.println("Please enter a private key");
        key = keyboard.nextLine();
        System.out.println("Please enter a public key");
        IV = keyboard.nextLine();
    }
    
    private static boolean[] sizeKeys(String key){
        
        int flag;
        if (key.length() < 10){
            flag = 1;
        }
        else if (key.length() > 10){
            flag = 2;
        }
        else
            flag = 3;
        
        switch (flag){
                case 1:
                    key = padKey(key);
                    break;
                case 2:
                    key = truncKey(key);
                    break;
                default:
                    break;
        }
        
        return StringToBooleanArray(key);
                    
    }
    
    private static String padKey(String key){
        while (key.length() < 10){
            key = key + "a";
        }
        return key;
    }
    
    private static String truncKey(String key){
        return key.substring(0, 10);
    }
    
    private static boolean[] StringToBooleanArray(String key){
        
        boolean[] booleanArray = new boolean[80];
        
        for (int i = 0; i < key.length(); i++){
            char c = key.charAt(i);
            int ascii = (int) c;
            for (int j = 0; j < 8; j++){
                int bit = 128/(2^7);
                if (bit < ascii){
                    booleanArray[i*8 + j] = true;
                    ascii = ascii - bit;
                }
                else
                    booleanArray[i*j] = false;
                
            }
        }
        return booleanArray;
    }
}