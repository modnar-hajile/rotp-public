/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import rotp.Rotp;
import rotp.util.LanguageManager;
import rotp.util.sound.SoundManager;

public class UserPreferences {
    public enum GraphicsSetting { NORMAL, MEDIUM, LOW;  }
    private static final String PREFERENCES_FILE = "Remnants.cfg";
    private static final String keyFormat = "%-20s: ";
    private static boolean showMemory = false;
    private static boolean playMusic = true;
    private static boolean playSounds = true;
    private static boolean displayYear = true;
    private static boolean textures = true;
	private static boolean techTrade = true; // modnar: add option to turn tech trading off (Dev-AI only)
	private static int techCostPct = 100; // modnar: add option to adjust tech cost (50% to 500%)
	private static boolean mapNebula = true; // modnar: add option to generate nebula on galaxy map
	private static boolean alwaysStarGates = false; // modnar: add option to always have Star Gates tech
	private static boolean challengeMode = false; // modnar: add option to give AI more initial resources
	private static boolean randomTechStart = false; // modnar: add option to start all Empires with 2 techs, no Artifacts
	private static boolean extraFertile = false; // modnar: add option to generate more hospitable planets
	private static boolean extraHostile = false; // modnar: add option to generate more hostile planets
	private static boolean extraRich = false; // modnar: add option to generate more Rich planets
	private static boolean extraPoor = false; // modnar: add option to generate more Poor planets
	private static boolean extraArtifact = false; // modnar: add option to generate more Artifact planets
    private static float uiTexturePct = 0.20f;
    private static int screenSizePct = 93;
    private static final HashMap<String, String> raceNames = new HashMap<>();
    private static GraphicsSetting graphicsLevel = GraphicsSetting.NORMAL;

    public static boolean showMemory()      { return showMemory; }
    public static void toggleMemory()       { showMemory = !showMemory; save(); }
    public static boolean playAnimations()  { return graphicsLevel != GraphicsSetting.LOW; }

    public static boolean antialiasing()    { return graphicsLevel == GraphicsSetting.NORMAL; }
    public static boolean playSounds()      { return playSounds; }
    public static void toggleSounds()       { playSounds = !playSounds;	save(); }
    public static boolean playMusic()       { return playMusic; }
    public static void toggleMusic()        { playMusic = !playMusic; save();  }
    public static boolean textures()        { return textures; }
    public static void toggleTextures()     { textures = !textures; save();  }
	
	public static boolean techTrade()        { return techTrade; } // modnar: add option to turn tech trading off (Dev-AI only)
	public static boolean mapNebula()        { return mapNebula; } // modnar: add option to generate nebula on galaxy map
	public static boolean alwaysStarGates()  { return alwaysStarGates; } // modnar: add option to always have Star Gates tech
	public static boolean challengeMode()    { return challengeMode; } // modnar: add option to give AI more initial resources
	public static boolean randomTechStart()  { return randomTechStart; } // modnar: add option to start all Empires with 2 techs, no Artifacts
	public static boolean extraFertile()     { return extraFertile; } // modnar: add option to generate more hospitable planets
	public static boolean extraHostile()     { return extraHostile; } // modnar: add option to generate more hostile planets
	public static boolean extraRich()        { return extraRich; } // modnar: add option to generate more Rich planets
	public static boolean extraPoor()        { return extraPoor; } // modnar: add option to generate more Poor planets
	public static boolean extraArtifact()    { return extraArtifact; } // modnar: add option to generate more Artifact planets
	
    public static int screenSizePct()       { return screenSizePct; }
    public static void screenSizePct(int i) { setScreenSizePct(i); }
	
	public static int techCostPct()         { return techCostPct; } // modnar: add option to adjust tech cost (50% to 500%)
    public static void techCostPct(int i)   { setTechCostPct(i); } // modnar: add option to adjust tech cost (50% to 500%)

    public static void toggleYearDisplay()    { displayYear = !displayYear; save(); }
    public static boolean displayYear()       { return displayYear; }
    public static void uiTexturePct(int i)    { uiTexturePct = i / 100.0f; }
    public static float uiTexturePct()        { return uiTexturePct; }
    public static GraphicsSetting graphicsLevel()    { return graphicsLevel; }

    public static void loadAndSave() {
        load();
        save();
    }
    public static void load() {
        String path = Rotp.jarPath();
        File configFile = new File(path, PREFERENCES_FILE);
		// modnar: change to InputStreamReader, force UTF-8
		try ( BufferedReader in = new BufferedReader( new InputStreamReader( new FileInputStream(configFile), "UTF-8"));) {
            String input;
            if (in != null) {
                while ((input = in.readLine()) != null)
                    loadPreferenceLine(input.trim());
            }
        }
        catch (FileNotFoundException e) {
            System.err.println(path+PREFERENCES_FILE+" not found.");
        }
        catch (IOException e) {
            System.err.println("UserPreferences.load -- IOException: "+ e.toString());
        }
    }
    public static void save() {
        String path = Rotp.jarPath();
        List<String> raceKeys = new ArrayList<>(raceNames.keySet());
        Collections.sort(raceKeys);
        try (FileOutputStream fout = new FileOutputStream(new File(path, PREFERENCES_FILE));
            // modnar: change to OutputStreamWriter, force UTF-8
			PrintWriter out = new PrintWriter(new OutputStreamWriter(fout, "UTF-8")); ) {
            out.println(keyFormat("GRAPHICS")+ graphicsLevel.toString());
            out.println(keyFormat("MUSIC")+ yesOrNo(playMusic));
            out.println(keyFormat("SOUNDS")+ yesOrNo(playSounds));
            out.println(keyFormat("MUSIC_VOLUME")+ SoundManager.musicLevel());
            out.println(keyFormat("SOUND_VOLUME")+ SoundManager.soundLevel());
            out.println(keyFormat("SHOW_MEMORY")+ yesOrNo(showMemory));
            out.println(keyFormat("DISPLAY_YEAR")+ yesOrNo(displayYear));
            out.println(keyFormat("SCREEN_SIZE_PCT")+ screenSizePct());
            out.println(keyFormat("UI_TEXTURES")+ yesOrNo(textures));
            out.println(keyFormat("UI_TEXTURE_LEVEL")+(int) (uiTexturePct()*100));
			out.println(keyFormat("TECH_TRADE")+ yesOrNo(techTrade)); // modnar: add option to turn tech trading off (Dev-AI only)
			out.println(keyFormat("TECH_COST_PCT")+ techCostPct()); // modnar: add option to adjust tech cost (50% to 500%)
			out.println(keyFormat("MAP_NEBULA")+ yesOrNo(mapNebula)); // modnar: add option to generate nebula on galaxy map
			out.println(keyFormat("ALWAYS_STAR_GATES")+ yesOrNo(alwaysStarGates)); // modnar: add option to always have Star Gates tech
			out.println(keyFormat("CHALLENGE_MODE")+ yesOrNo(challengeMode)); // modnar: add option to give AI more initial resources
			out.println(keyFormat("RANDOM_TECH_START")+ yesOrNo(randomTechStart)); // modnar: add option to start all Empires with 2 techs, no Artifacts
			out.println(keyFormat("EXTRA_FERTILE")+ yesOrNo(extraFertile)); // modnar: add option to generate more hospitable planets
			out.println(keyFormat("EXTRA_HOSTILE")+ yesOrNo(extraHostile)); // modnar: add option to generate more hostile planets
			out.println(keyFormat("EXTRA_RICH")+ yesOrNo(extraRich)); // modnar: add option to generate more Rich planets
			out.println(keyFormat("EXTRA_POOR")+ yesOrNo(extraPoor)); // modnar: add option to generate more Poor planets
			out.println(keyFormat("EXTRA_ARTIFACT")+ yesOrNo(extraArtifact)); // modnar: add option to generate more Artifact planets
            out.println(keyFormat("LANGUAGE")+ languageDir());
            for (String raceKey: raceKeys) 
              out.println(keyFormat(raceKey)+raceNames.get(raceKey));
        }
        catch (IOException e) {
            System.err.println("UserPreferences.save -- IOException: "+ e.toString());
        }
    }
    private static String keyFormat(String s)  { return String.format(keyFormat, s); }
    
    private static void loadPreferenceLine(String line) {
        if (line.isEmpty())
            return;

        String[] args = line.split(":");
        if (args.length < 2)
            return;

        String key = args[0].toUpperCase().trim();
        String val = args[1].trim();
        if (key.isEmpty() || val.isEmpty())
                return;

        System.out.println("Key:"+key+"  value:"+val);
        switch(key) {
            case "GRAPHICS":     graphicsLevel(val); return;
            case "MUSIC":        playMusic = yesOrNo(val); return;
            case "SOUNDS":       playSounds = yesOrNo(val); return;
            case "MUSIC_VOLUME": SoundManager.musicLevel(Integer.valueOf(val)); return;
            case "SOUND_VOLUME": SoundManager.soundLevel(Integer.valueOf(val)); return;
            case "SHOW_MEMORY":  showMemory = yesOrNo(val); return;
            case "DISPLAY_YEAR": displayYear = yesOrNo(val); return;
            case "SCREEN_SIZE_PCT": screenSizePct(Integer.valueOf(val)); return;
            case "UI_TEXTURES":  textures = yesOrNo(val); return;
            case "UI_TEXTURE_LEVEL": uiTexturePct(Integer.valueOf(val)); return;
			case "TECH_TRADE":   techTrade = yesOrNo(val); return; // modnar: add option to turn tech trading off (Dev-AI only)
			case "TECH_COST_PCT": techCostPct(Integer.valueOf(val)); return; // modnar: add option to adjust tech cost (50% to 500%)
			case "MAP_NEBULA":   mapNebula = yesOrNo(val); return; // modnar: add option to generate nebula on galaxy map
			case "ALWAYS_STAR_GATES": alwaysStarGates = yesOrNo(val); return; // modnar: add option to always have Star Gates tech
			case "CHALLENGE_MODE": challengeMode = yesOrNo(val); return; // modnar: add option to give AI more initial resources
			case "RANDOM_TECH_START": randomTechStart = yesOrNo(val); return; // modnar: add option to start all Empires with 2 techs, no Artifacts
			case "EXTRA_FERTILE":   extraFertile = yesOrNo(val); return; // modnar: add option to generate more hospitable planets
			case "EXTRA_HOSTILE":   extraHostile = yesOrNo(val); return; // modnar: add option to generate more hostile planets
			case "EXTRA_RICH":   extraRich = yesOrNo(val); return; // modnar: add option to generate more Rich planets
			case "EXTRA_POOR":   extraPoor = yesOrNo(val); return; // modnar: add option to generate more Poor planets
			case "EXTRA_ARTIFACT":   extraArtifact = yesOrNo(val); return; // modnar: add option to generate more Artifact planets
            case "LANGUAGE":     selectLanguage(val); return;
            default:
                raceNames.put(key, val); break;
        }
    }
    private static String yesOrNo(boolean b) {
        return b ? "YES" : "NO";
    }
    private static boolean yesOrNo(String s) {
        return s.equalsIgnoreCase("YES");
    }
    private static void selectLanguage(String s) {
        LanguageManager.selectLanguage(s);
    }
    private static String languageDir() {
        return LanguageManager.selectedLanguageDir();
    }
    private static void setScreenSizePct(int i) {
        screenSizePct = Math.max(50,Math.min(i,100));
    }
    public static boolean shrinkFrame() {
        int oldSize = screenSizePct;
        setScreenSizePct(screenSizePct-5);
        return oldSize != screenSizePct;
    }
    public static boolean expandFrame() {
        int oldSize = screenSizePct;
        setScreenSizePct(screenSizePct+5);
        return oldSize != screenSizePct;
    }
	// modnar: adjust tech cost, limit to 50% to 500%
	private static void setTechCostPct(int i) {
        techCostPct = Math.max(50,Math.min(i,500));
    }
    public static void graphicsLevel(String s) {
        String level = s.toUpperCase();
        switch(level) {
            case "NORMAL" : graphicsLevel = GraphicsSetting.NORMAL; break;
            case "MEDIUM" : graphicsLevel = GraphicsSetting.MEDIUM; break;
            case "LOW"    : graphicsLevel = GraphicsSetting.LOW; break;
        }
    }
    public static void toggleGraphicsLevel() {
        switch(graphicsLevel) {
            case NORMAL:  graphicsLevel = GraphicsSetting.MEDIUM; save(); break;
            case MEDIUM:  graphicsLevel = GraphicsSetting.LOW; save(); break;
            case LOW:     graphicsLevel = GraphicsSetting.NORMAL; save(); break;
        }
    }
    public static String raceNames(String id, String defaultNames) {
        String idUpper = id.toUpperCase();
        if (raceNames.containsKey(idUpper))
            return raceNames.get(idUpper);
        
        raceNames.put(idUpper, defaultNames);
        return defaultNames;
    }
}
