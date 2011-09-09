
/*
 * Minecraft Sitting Mod
 * Copyright (C) 2011  M4411K4
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public class FileUtil
{
	private static final Logger log = Logger.getLogger("Minecraft");
	private static final String SETTINGS_PATH = "plugins/Sitting/sitting.properties";
	
	private static String getDefault()
	{
		String output = ""+
		
		"#Add the /sit command in the groups.txt server file to allow players to type in-game /sit to sit down almost anywhere. \r\n"+
		"\r\n"+
		"\r\n"+
		"###################\r\n"+
		"\r\n"+
		"#true - only groups with the /canrightclicksit command in the groups.txt server file will be allowed to right click \r\n"+
		"#    blocks such as wooden stairs or cobble stairs to sit on them. \r\n"+
		"#false - (default) All players can right click to sit on sittable blocks. \r\n"+
		"require-permission-to-right-click-sit=false\r\n"+
		"\r\n"+
		"#true - (default) allows players to right click any wooden or cobble stairs to sit on it. \r\n"+
		"#false - players can only right-click-sit on wooden or cobble stairs that have been made to look like chairs or couches. \r\n"+
		"#    Chairs and couches require signs on both sides of the wooden/cobble stairs. \r\n"+
		"right-click-sit-on-any-stair=true\r\n"+
		"\r\n"+
		
		"";
		
		return output;
	}
	
	public static PropertiesFile loadSetting()
	{
		File file = new File(SETTINGS_PATH);
		
		if(!file.exists())
		{
			if(!saveSettings(SETTINGS_PATH, getDefault()))
			{
				return null;
			}
		}
		
		PropertiesFile properties = new PropertiesFile(SETTINGS_PATH);
		
		try
		{
			properties.load();
		}
		catch(IOException e)
		{
			return null;
		}
		
		return properties;
	}
	
	private static boolean saveSettings(String path, String output)
	{
		File file = new File(path);
		
		if(!file.exists())
		{
			File folder = file.getParentFile();
			if(!folder.exists()) 
			{
				if(!folder.mkdirs())
					return false;
			}
			
			
			try
			{
				file.createNewFile();
			}
			catch(IOException e)
			{
				return false;
			}
		}
		
		BufferedWriter buffWriter = null;
		
		try
		{
			buffWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF8"));
			
			buffWriter.write(output);
		}
		catch (UnsupportedEncodingException e)
		{
			log.warning("Sitting mod saveSettings Unsupported Encoding Error: "+e);
			return false;
		}
		catch (FileNotFoundException e)
		{
			log.warning("Sitting mod saveSettings: "+path+" could not be found. "+e);
			return false;
		}
		catch (IOException e)
		{
			log.warning("Sitting mod saveSettings Error: "+e);
			return false;
		}
		finally
		{
			if(buffWriter != null)
			{
				try
				{
					buffWriter.flush();
					buffWriter.close();
				}
				catch(IOException e)
				{
					log.warning("Sitting mod could not close BufferedWriter.");
				}
			}
		}
		
		return true;
	}
}
