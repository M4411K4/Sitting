
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



public class UtilEntity
{
	protected static OEntity riddenByEntity(OEntity oentity)
	{
		return oentity.bg;
	}
	
	protected static OEntity ridingEntity(OEntity oentity)
	{
		return oentity.bh;
	}
	
	protected static double getMountedYOffset(OEntity oentity)
	{
		return oentity.q();
	}
	
	/*
	 * oentity will mount mountEntity
	 */
	protected static void mountEntity(OEntity oentity, OEntity mountEntity)
	{
		oentity.b(mountEntity);
	}
	
	protected static boolean spawnEntityInWorld(OWorld oworld, OEntity oentity)
	{
		return oworld.b(oentity);
	}
}
