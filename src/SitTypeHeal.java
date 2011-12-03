
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

public class SitTypeHeal extends SitType
{
	private final int RATE;
	
	public SitTypeHeal()
	{
		this.RATE = SittingListener.globalHealingRate;
	}
	
	public SitTypeHeal(Sign sign)
	{
		super(sign);
		
		if(!sign.getText(2).isEmpty())
			this.RATE = Integer.parseInt(sign.getText(2));
		else
			this.RATE = 20;
	}
	
	public static String validate(Sign sign)
	{
		if(!sign.getText(2).isEmpty())
		{
			try
			{
				int rate = Integer.parseInt(sign.getText(2));
				if(rate < 2 || rate > 100)
				{
					return "3rd line delay value must be a number from 2 to 100";
				}
			}
			catch(NumberFormatException e)
			{
				return "3rd line delay value must be a number";
			}
		}
		return null;
	}
	
	@Override
	public void update(OWorld oworld, EntitySitting sitentity, OEntityPlayerMP eplayer)
	{
		PluginLoader.HookResult autoHeal = etc.getInstance().autoHeal();
		if((oworld.v != 0 || autoHeal != PluginLoader.HookResult.DEFAULT_ACTION) && autoHeal != PluginLoader.HookResult.ALLOW_ACTION)
		{
			if(eplayer.ai() < 20 && eplayer.bQ % this.RATE * 12 == 0)
				eplayer.d(1);
		}
	}
}
