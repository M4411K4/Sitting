
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

public class EntitySitting extends OEntityFishHook
{
	private final BaseEntity BASE_ENTITY;
	
	private final double OFFSET_Y;
	private final int BLOCK_ID;
	private final SitType[] TYPES;
	private int ticks = 0;
	
	public EntitySitting(SitType[] types, OWorldServer oworld, double x, double y, double z, double offsety)
	{
		super(oworld);
		c(x, y, z);
		
		BASE_ENTITY = new BaseEntity(this);
		
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
		
		int blockX = OMathHelper.b(BASE_ENTITY.getX());
		int blockY = OMathHelper.b(BASE_ENTITY.getY());
		int blockZ = OMathHelper.b(BASE_ENTITY.getZ());
		
		World world = new World(oworld);
		
		BLOCK_ID = world.getBlockIdAt(blockX, blockY, blockZ);
		
		if(!canSitOnBlock(world, blockX, blockY, blockZ))
			BASE_ENTITY.destroy();
	}
	
	public static boolean isChairBlock(int id)
	{
		switch(id)
		{
			case 53:
			case 67:
			case 108:
			case 109:
			case 114:
				return true;
		}
		return false;
	}
	
	public static boolean canSitOnBlock(World world, int x, int y, int z)
	{
		int id = world.getBlockIdAt(x, y, z);
		
		if(id == 0)
		{
			id = world.getBlockIdAt(x, y-1, z);
			if(id == 0)
				return false;
		}
		
		if(id == 34 //piston ext
			|| id == 36 //block moved
			|| id == 52 //mob spawner
			|| id == 65 //ladder
			|| id == 85 //fence
			|| id == 101 //iron bars
			|| id == 102 //glass pane
			|| id == 107 //fence gate
			|| id == 113 //nether brick fence
			|| OBlock.m[id].e(world.getWorld(), x, y, z) == null)
			return false;
		return true;
	}
	
	@Override
	//onUpdate
	public void G_()
	{
		if(this.bg != null && this.bg.bE)
		{
			//sitting player is dead
			if(this.bg.bh == this)
			{
				this.bg.bh = null;
			}
			this.bg = null;
		}
		
		int x = OMathHelper.b(BASE_ENTITY.getX());
		int y = OMathHelper.b(BASE_ENTITY.getY());
		int z = OMathHelper.b(BASE_ENTITY.getZ());
		
		World world = BASE_ENTITY.getWorld();
		
		if(this.bg == null || world.getBlockIdAt(x, y, z) != BLOCK_ID || !canSitOnBlock(world, x, y, z))
		{
			//dismounted
			BASE_ENTITY.destroy();
			return;
		}
		
		ticks++;
		if(ticks >= 1200)
		{
			EntitySitting esitting = new EntitySitting(this.TYPES, world.getWorld(), BASE_ENTITY.getX(), BASE_ENTITY.getY(), BASE_ENTITY.getZ(), this.OFFSET_Y);
			
			UtilEntity.spawnEntityInWorld(world.getWorld(), esitting);
			UtilEntity.mountEntity(this.bg, esitting);
			
			BASE_ENTITY.destroy();
			return;
		}
		
		if(this.bg instanceof OEntityPlayerMP)
		{
			OEntityPlayerMP eplayer = (OEntityPlayerMP)this.bg;
			for(SitType type : TYPES)
			{
				type.update(world.getWorld(), this, eplayer);
			}
		}
		
		BASE_ENTITY.setMotion(0.0D, 0.0D, 0.0D);
		a(0.0D, 0.0D, 0.0D);
	}
	
	@Override
	//getMountedYOffset
	public double x_()
	{
		return OFFSET_Y;
	}
	
	@Override
	protected boolean g_()
	{
		return false;
	}
	
	@Override
	//attackEntityFrom
	public boolean a(ODamageSource paramODamageSource, int paramInt)
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
