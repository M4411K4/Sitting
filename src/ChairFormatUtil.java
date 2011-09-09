
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
	public static boolean isChair(Block stair, int data, Block left, Block right)
	{
		if(stair == null || left == null || right == null || !EntitySitting.isChairBlock(stair.getType()))
			return false;
		
		if(isDefaultChair(stair, data, left, right)
			|| isTwoBlockCouch(stair, data, left, right)
			)
		{
			return true;
		}
		
		return false;
	}
	
	private static boolean isDefaultChair(Block stair, int data, Block left, Block right)
	{
		int dataL = left.getWorld().getBlockData(left.getX(), left.getY(), left.getZ());
		int dataR = right.getWorld().getBlockData(right.getX(), right.getY(), right.getZ());
		
		if(left.getType() == 68 && right.getType() == 68
			&& ((data == 0 && dataL == 3 && dataR == 2)
				|| (data == 1 && dataL == 2 && dataR == 3)
				|| (data == 2 && dataL == 5 && dataR == 4)
				|| (data == 3 && dataL == 4 && dataR == 5))
			)
		{
			return true;
		}
		
		return false;
	}
	
	private static boolean isTwoBlockCouch(Block stair, int data, Block left, Block right)
	{
		if((left.getType() != 68 && right.getType() != 68)
			|| (!EntitySitting.isChairBlock(left.getType()) && !EntitySitting.isChairBlock(right.getType()))
			)
			return false;
		
		World world = stair.getWorld();
		int dataL = world.getBlockData(left.getX(), left.getY(), left.getZ());
		int dataR = world.getBlockData(right.getX(), right.getY(), right.getZ());
		
		if(left.getType() == 68)
		{
			if(dataR != data)
				return false;
			
			Block rightBlock = getStairRightBlock(right, dataR);
			if(rightBlock == null || rightBlock.getType() != 68)
				return false;
			
			dataR = world.getBlockData(rightBlock.getX(), rightBlock.getY(), rightBlock.getZ());
		}
		else
		{
			if(dataL != data)
				return false;
			
			Block leftBlock = getStairLeftBlock(left, dataL);
			if(leftBlock == null || leftBlock.getType() != 68)
				return false;
			
			dataL = world.getBlockData(leftBlock.getX(), leftBlock.getY(), leftBlock.getZ());
		}
		
		if( (data == 0 && dataL == 3 && dataR == 2)
			|| (data == 1 && dataL == 2 && dataR == 3)
			|| (data == 2 && dataL == 5 && dataR == 4)
			|| (data == 3 && dataL == 4 && dataR == 5)
			)
		{
			return true;
		}
		
		return false;
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
