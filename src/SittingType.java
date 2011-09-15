
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

public enum SittingType
{
	SIT_HEAL("/sittingcanbuildheal")
	{
		@Override
		public String validate(Sign sign)
		{
			return SitTypeHeal.validate(sign);
		}
		
		@Override
		public SitType getType()
		{
			return new SitTypeHeal();
		}
		
		@Override
		public SitType getType(Sign sign)
		{
			return new SitTypeHeal(sign);
		}
	},
	;
	
	public final String PERMISSION;
	SittingType(String permission)
	{
		PERMISSION = permission;
	}
	
	public String validate(Sign sign)
	{
		return SitType.validate(sign);
	}
	
	public SitType getType()
	{
		return new SitType();
	}
	
	public SitType getType(Sign sign)
	{
		return new SitType(sign);
	}
}
