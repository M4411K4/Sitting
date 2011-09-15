
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

public class EntitySitting extends OEntityFish
{
	private final double OFFSET_Y;
	private final int BLOCK_ID;
	private final SitType[] TYPES;
	private int ticks = 0;
	
	public EntitySitting(SitType[] types, OWorld oworld, double x, double y, double z, double offsety)
	{
		super(oworld);
		c(x, y, z);
		
		int nullcount = 0;
		for(SitType type : types)
		{
			if(type == null)
				nullcount++;
		}
		if(nullcount > 0)
		{
			SitType[] newtypes = new SitType[types.length - nullcount];
			int j = 0;
			for(int i = 0; i < types.length; i++)
			{
				if(types[i] == null)
					continue;
				newtypes[j] = types[i];
				j++;
			}
			TYPES = newtypes;
		}
		else
		{
			TYPES = types;
		}
		
		OFFSET_Y = offsety;
		
		int blockX = OMathHelper.b(this.aP);
		int blockY = OMathHelper.b(this.aQ);
		int blockZ = OMathHelper.b(this.aR);
		
		BLOCK_ID = oworld.a(blockX, blockY, blockZ);
		
		if(!canSitOnBlock(oworld, blockX, blockY, blockZ))
			this.J();
	}
	
	public static boolean isChairBlock(int id)
	{
		switch(id)
		{
			case 53:
			case 67:
				return true;
		}
		return false;
	}
	
	public static boolean canSitOnBlock(OWorld oworld, int x, int y, int z)
	{
		int id = oworld.a(x, y, z);
		
		if(id == 0)
		{
			id = oworld.a(x, y-1, z);
			if(id == 0)
				return false;
		}
		
		if(id == 34 //piston ext
			|| id == 36 //block moved
			|| id == 52 //mob spawner
			|| id == 65 //ladder
			|| id == 85 //fence
			|| OBlock.m[id].e(oworld, x, y, z) == null)
			return false;
		return true;
	}
	
	@Override
	//onUpdate
	public void m_()
	{
		if(this.aJ != null && this.aJ.bh)
		{
			//sitting player is dead
			this.aJ = null;
		}
		
		int x = OMathHelper.b(this.aP);
		int y = OMathHelper.b(this.aQ);
		int z = OMathHelper.b(this.aR);
		
		if(this.aJ == null || this.aL.a(x, y, z) != BLOCK_ID || !canSitOnBlock(this.aL, x, y, z))
		{
			//dismounted
			this.J();
			return;
		}
		
		ticks++;
		if(ticks >= 1200)
		{
			EntitySitting esitting = new EntitySitting(this.TYPES, this.aL, this.aP, this.aQ, this.aR, this.OFFSET_Y);
			this.aL.b(esitting);
			this.aJ.b(esitting);
			this.J();
			return;
		}
		
		if(this.aJ instanceof OEntityPlayerMP)
		{
			OEntityPlayerMP eplayer = (OEntityPlayerMP)this.aJ;
			for(SitType type : TYPES)
			{
				type.update(this.aL, this, eplayer);
			}
		}
		
		this.aS = 0.0D;
		this.aT = 0.0D;
		this.aU = 0.0D;
		this.aJ.aS = 0.0D;
		this.aJ.aT = 0.0D;
		this.aJ.aU = 0.0D;
		a(0.0D, 0.0D, 0.0D);
	}
	
	@Override
	//getMountedYOffset
	public double m()
	{
		return OFFSET_Y;
	}
	
	@Override
	protected boolean n()
	{
		return false;
	}
	
	@Override
	//attackEntityFrom
	public boolean a(OEntity paramOEntity, int paramInt)
	{
		return false;
	}
	
	public void b(ONBTTagCompound paramONBTTagCompound)
	{
		
	}
	
	public void a(ONBTTagCompound paramONBTTagCompound)
	{
		
	}
}
