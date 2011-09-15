
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

public class ChairFormatUtil
{
	public static Sign[] isChair(Block stair, int data, Block left, Block right)
	{
		if(stair == null || left == null || right == null || !EntitySitting.isChairBlock(stair.getType()))
			return null;
		
		Sign[] signs;
		if((signs = isDefaultChair(stair, data, left, right)) != null
			|| (signs = isTwoBlockCouch(stair, data, left, right)) != null
			)
		{
			return signs;
		}
		
		return null;
	}
	
	private static Sign[] isDefaultChair(Block stair, int data, Block left, Block right)
	{
		World world = stair.getWorld();
		int dataL = world.getBlockData(left.getX(), left.getY(), left.getZ());
		int dataR = world.getBlockData(right.getX(), right.getY(), right.getZ());
		
		if(left.getType() == 68 && right.getType() == 68
			&& ((data == 0 && dataL == 3 && dataR == 2)
				|| (data == 1 && dataL == 2 && dataR == 3)
				|| (data == 2 && dataL == 5 && dataR == 4)
				|| (data == 3 && dataL == 4 && dataR == 5))
			)
		{
			Sign[] signs = new Sign[2];
			signs[0] = (Sign)world.getComplexBlock(left);
			signs[1] = (Sign)world.getComplexBlock(right);
			return signs;
		}
		
		return null;
	}
	
	private static Sign[] isTwoBlockCouch(Block stair, int data, Block left, Block right)
	{
		if((left.getType() != 68 && right.getType() != 68)
			|| (!EntitySitting.isChairBlock(left.getType()) && !EntitySitting.isChairBlock(right.getType()))
			)
			return null;
		
		World world = stair.getWorld();
		int dataL = world.getBlockData(left.getX(), left.getY(), left.getZ());
		int dataR = world.getBlockData(right.getX(), right.getY(), right.getZ());
		
		if(left.getType() == 68)
		{
			if(dataR != data)
				return null;
			
			right = getStairRightBlock(right, dataR);
			if(right == null || right.getType() != 68)
				return null;
			
			dataR = world.getBlockData(right.getX(), right.getY(), right.getZ());
		}
		else
		{
			if(dataL != data)
				return null;
			
			left = getStairLeftBlock(left, dataL);
			if(left == null || left.getType() != 68)
				return null;
			
			dataL = world.getBlockData(left.getX(), left.getY(), left.getZ());
		}
		
		if( (data == 0 && dataL == 3 && dataR == 2)
			|| (data == 1 && dataL == 2 && dataR == 3)
			|| (data == 2 && dataL == 5 && dataR == 4)
			|| (data == 3 && dataL == 4 && dataR == 5)
			)
		{
			Sign[] signs = new Sign[2];
			signs[0] = (Sign)world.getComplexBlock(left);
			signs[1] = (Sign)world.getComplexBlock(right);
			return signs;
		}
		
		return null;
	}
	
	protected static Block[] getStairSideBlocks(Block block, int data)
	{
		if(block == null)
			return null;
		
		Block[] blocks = new Block[2];
		blocks[0] = getStairLeftBlock(block, data);
		blocks[1] = getStairRightBlock(block, data);
		
		if(blocks[0] == null || blocks[1] == null)
			return null;
		
		return blocks;
	}
	
	protected static Block getStairLeftBlock(Block block, int data)
	{
		if(block == null)
			return null;
		
		Block left;
		switch(data)
		{
    		case 0x0: //south
    			left = block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()+1);
    			break;
    		case 0x1: //north
    			left = block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()-1);
    			break;
    		case 0x2: //west
    			left = block.getWorld().getBlockAt(block.getX()+1, block.getY(), block.getZ());
    			break;
    		case 0x3: //east
    			left = block.getWorld().getBlockAt(block.getX()-1, block.getY(), block.getZ());
    			break;
    		default:
    			return null;
		}
		
		return left;
	}
	
	protected static Block getStairRightBlock(Block block, int data)
	{
		if(block == null)
			return null;
		
		Block right;
		switch(data)
		{
    		case 0x0: //south
    			right = block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()-1);
    			break;
    		case 0x1: //north
    			right = block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()+1);
    			break;
    		case 0x2: //west
    			right = block.getWorld().getBlockAt(block.getX()-1, block.getY(), block.getZ());
    			break;
    		case 0x3: //east
    			right = block.getWorld().getBlockAt(block.getX()+1, block.getY(), block.getZ());
    			break;
    		default:
    			return null;
		}
		
		return right;
	}
}
