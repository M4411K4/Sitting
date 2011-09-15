
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



public class SittingListener extends PluginListener
{
	private final String COMMAND_MAIN = "/sitting";
	private final String COMMAND_SIT = "/sit";
	private final String COMMAND_STAND = "/stand";
	private final String COMMAND_RIGHT_CLICK_SIT = "/canrightclicksit";
	
	private final Sitting sittingPlugin;
	private final PropertiesFile properties;
	
	private boolean requireRightClickPermission;
	private boolean requiresChairFormats;
	private HealingType healWhileSitting;
	protected static int globalHealingRate;
	
	private enum HealingType
	{
		NONE,
		CHAIRONLY,
		SITCOMMANDONLY,
		ALL
	};
	
	public SittingListener(Sitting sittingPlugin, PropertiesFile properties)
	{
		super();
		
		this.sittingPlugin = sittingPlugin;
		this.properties = properties;
		
		requireRightClickPermission = false;
		if(this.properties.containsKey("require-permission-to-right-click-sit"))
			requireRightClickPermission = this.properties.getBoolean("require-permission-to-right-click-sit", false);
		
		requiresChairFormats = false;
		if(this.properties.containsKey("right-click-sit-on-any-stair"))
			requiresChairFormats = !this.properties.getBoolean("right-click-sit-on-any-stair", true);
		
		healWhileSitting = HealingType.NONE;
		if(this.properties.containsKey("heal-while-sitting"))
		{
			try
			{
				String type = this.properties.getString("heal-while-sitting", "NONE").toUpperCase();
				healWhileSitting = HealingType.valueOf(type);
			}
			catch(IllegalArgumentException e)
			{
				healWhileSitting = HealingType.NONE;
				Sitting.log.warning("[Sitting]: \"heal-while-sitting\" value not recognized. Setting value to NONE disabling healing.");
			}
		}
		
		globalHealingRate = 20;
		if(this.properties.containsKey("heal-while-sitting-rate"))
		{
			int rate = this.properties.getInt("heal-while-sitting-rate", 20);
			if(rate < 2)
			{
				Sitting.log.warning("[Sitting]: \"heal-while-sitting-rate\" setting less than 2. Setting value to 2.");
				globalHealingRate = 2;
			}
			else if(rate > 100)
			{
				Sitting.log.warning("[Sitting]: \"heal-while-sitting-rate\" setting greater than 100. Setting value to 100.");
				globalHealingRate = 100;
			}
			else
			{
				globalHealingRate = rate;
			}
		}
	}
	
	@Override
	public boolean onCommand(Player player, String[] split)
	{
		if(split[0].equalsIgnoreCase(COMMAND_SIT) && player.canUseCommand(COMMAND_SIT))
		{
			OEntityPlayerMP eplayer = (OEntityPlayerMP) player.getEntity();
			if(eplayer.aK != null)
			{
				stand(eplayer, 0, eplayer.aK.m(), 0);
			}
			else
			{
				SitType[] types = new SitType[1];
				switch(healWhileSitting)
				{
					case ALL:
					case SITCOMMANDONLY:
						types[0] = SittingType.SIT_HEAL.getType();
						break;
					default:
						types[0] = null;
				}
				sit(eplayer, types, player.getWorld(), player.getX(), player.getY(), player.getZ(), player.getRotation(), -0.05D);
			}
			
			return true;
		}
		if(split[0].equalsIgnoreCase(COMMAND_STAND))
		{
			OEntityPlayerMP eplayer = (OEntityPlayerMP) player.getEntity();
			if(eplayer.aK == null)
        		return true;
			stand(eplayer, 0, eplayer.aK.m(), 0);
			
			return true;
		}
		if(split[0].equalsIgnoreCase(COMMAND_MAIN))
		{
			player.sendMessage(Colors.Gold+sittingPlugin.NAME+" mod version: "+Colors.White+sittingPlugin.VERSION);
			player.sendMessage(Colors.Rose+"available commands: ");
			if(player.canUseCommand(COMMAND_SIT))
				player.sendMessage("  "+Colors.LightBlue+COMMAND_SIT+Colors.White+" - toggles sitting and standing");
			player.sendMessage("  "+Colors.LightBlue+COMMAND_STAND+Colors.White+" - makes you stand if you are sitting");
			if(!requireRightClickPermission || player.canUseCommand(COMMAND_RIGHT_CLICK_SIT))
				player.sendMessage(Colors.Rose+"right-click to sit: "+Colors.White+"allowed");
			else
				player.sendMessage(Colors.Rose+"right-click to sit: "+Colors.White+"not allowed");
			return true;
		}
		
		return false;
	}
	
	@Override
    public void onBlockRightClicked(Player player, Block blockClicked, Item itemInHand)
    {
    	if( (itemInHand == null || itemInHand.getItemId() == 0)
    		&& (!requireRightClickPermission || player.canUseCommand(COMMAND_RIGHT_CLICK_SIT))
    		&& EntitySitting.isChairBlock(blockClicked.getType()) )
    	{
    		Sign[] signs = isChair(blockClicked);
    		if(signs == null)
    		{
    			if(requiresChairFormats)
    				return;
				signs = new Sign[0];
    		}
    		
    		OEntityPlayerMP eplayer = (OEntityPlayerMP) player.getEntity();
    		World world = player.getWorld();
    		int data = world.getBlockData(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
    		if(eplayer.aK != null)
    		{
    			switch(data)
        		{
    	    		case 0x0: //south
    	    			stand(eplayer, -0.8D, 0, 0);
    	    			break;
    	    		case 0x1: //north
    	    			stand(eplayer, 0.8D, 0, 0);
    	    			break;
    	    		case 0x2: //west
    	    			stand(eplayer, 0, 0, -0.8D);
    	    			break;
    	    		case 0x3: //east
    	    			stand(eplayer, 0, 0, 0.8D);
    	    			break;
    	    		default:
    	    			stand(eplayer, 0, 0, 0);
        		}
    		}
    		else
    		{
    			float rotation;
        		double x = blockClicked.getX() + 0.5D;
        		double y = blockClicked.getY();
        		double z = blockClicked.getZ() + 0.5D;
        		
        		switch(data)
        		{
    	    		case 0x0: //south
    	    			rotation = 90F;
    	    			x -= 0.2D;
    	    			break;
    	    		case 0x1: //north
    	    			rotation = 270F;
    	    			x += 0.2D;
    	    			break;
    	    		case 0x2: //west
    	    			rotation = 180F;
    	    			z -= 0.2D;
    	    			break;
    	    		case 0x3: //east
    	    			rotation = 0F;
    	    			z += 0.2D;
    	    			break;
    	    		default:
    	    			rotation = 0F;
        		}
        		
        		SitType[] types = new SitType[signs.length + 1];
        		
        		boolean hasHealing = false;
        		for(int i = 0; i < signs.length; i++)
        		{
        			SittingType sittype = getSittingTypeFromSign(signs[i]);
        			if(sittype == null)
        				continue;
        			
        			types[i] = sittype.getType(signs[i]);
        			if(sittype == SittingType.SIT_HEAL)
        				hasHealing = true;
        		}
        		
        		if(!hasHealing)
        		{
					switch(healWhileSitting)
					{
						case ALL:
						case CHAIRONLY:
							types[types.length-1] = SittingType.SIT_HEAL.getType();
							break;
						default:
							types[types.length-1] = null;
					}
        		}
        		sit(eplayer, types, player.getWorld(), x, y, z, rotation, 0.5D);
    		}
    	}
    }
	
	@Override
	public boolean onSignChange(Player player, Sign sign)
	{
		if(sign.getBlock().getType() != 68 || !signHasSittingType(sign))
			return false;
		
		sign.setText(1, sign.getText(1).toUpperCase());
		
		World world = player.getWorld();
		SittingType sittype = getSittingTypeFromSign(sign);
		if(sittype == null || !player.canUseCommand(sittype.PERMISSION))
		{
			world.setBlockAt(0, sign.getX(), sign.getY(), sign.getZ());
			world.dropItem(sign.getX(), sign.getY(), sign.getZ(), 323);
			
			player.sendMessage(Colors.Rose+"You do not have permission to build that.");
			return true;
		}
		
		String valid = sittype.validate(sign);
		if(valid != null)
		{
			world.setBlockAt(0, sign.getX(), sign.getY(), sign.getZ());
			world.dropItem(sign.getX(), sign.getY(), sign.getZ(), 323);
			
			player.sendMessage(Colors.Rose+valid);
			return true;
		}
		
		sign.update();
		
		return false;
	}
	
	private static void sit(OEntityPlayerMP eplayer, SitType[] types, World world, double x, double y, double z, float rotation, double offsety)
	{
		eplayer.aP = x;
		eplayer.aQ = y;
		eplayer.aR = z;
		eplayer.aV = rotation;
		
		OWorldServer oworld = world.getWorld();
		EntitySitting esitting = new EntitySitting(types, oworld, eplayer.aP, eplayer.aQ, eplayer.aR, offsety);
		oworld.b(esitting);
		eplayer.b(esitting);
	}
	
	private static void stand(OEntityPlayerMP eplayer, double offsetx, double offsety, double offsetz)
	{
		if(!(eplayer.aK instanceof EntitySitting))
			return;
		
		OEntity nullEnt = null;
		eplayer.b(nullEnt);
		eplayer.a.a(eplayer.aP+offsetx, eplayer.aQ+offsety, eplayer.aR+offsetz, eplayer.aV, eplayer.aW);
	}
	
	/*
	 * @return Returns null if not a chair. If it is a chair, it will return a list of Sign which can have
	 *     a size of 0, meaning no signs found, but it is a chair.
	 */
	private static Sign[] isChair(Block block)
	{
		int data = block.getWorld().getBlockData(block.getX(), block.getY(), block.getZ());
		Block[] sides = ChairFormatUtil.getStairSideBlocks(block, data);
		if(sides == null)
			return null;
		
		return ChairFormatUtil.isChair(block, data, sides[0], sides[1]);
	}
	
	private static boolean signHasSittingType(Sign sign)
	{
		String line2 = sign.getText(1).toUpperCase();
		if(line2.isEmpty() || line2.charAt(0) != '[' || line2.charAt(line2.length()-1) != ']')
			return false;
		
		try
		{
			SittingType.valueOf(line2.substring(1, line2.length()-1).replace(' ', '_'));
		}
		catch(IllegalArgumentException e)
		{
			return false;
		}
		return true;
	}
	
	private static SittingType getSittingTypeFromSign(Sign sign)
	{
		String line2 = sign.getText(1);
		if(line2.isEmpty() || line2.charAt(0) != '[' || line2.charAt(line2.length()-1) != ']')
			return null;
		
		SittingType sittype = null;
		try
		{
			sittype = SittingType.valueOf(line2.substring(1, line2.length()-1).replace(' ', '_'));
		}
		catch(IllegalArgumentException e)
		{
			return null;
		}
		return sittype;
	}
}
