package mchelio;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mchelio.MCH_Config;
import mchelio.MCH_ItemRendererDummy;
import mchelio.MCH_MOD;
import mchelio.MCH_Vector2;
import mchelio.MCH_ViewEntityDummy;
import mchelio.wrapper.W_Block;
import mchelio.wrapper.W_McClient;
import mchelio.wrapper.W_Reflection;
import mchelio.wrapper.W_Vec3;
import mchelio.wrapper.W_WorldFunc;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class MCH_Lib {

    private static HashMap mapMaterial = new HashMap();
    public static final String[] AZIMUTH_8 = new String[]{"S", "SW", "W", "NW", "N", "NE", "E", "SE"};
    public static final int AZIMUTH_8_ANG = 360 / AZIMUTH_8.length;


    public static void init() {
        mapMaterial.clear();
        mapMaterial.put("air", Material.AIR);
        mapMaterial.put("grass", Material.GRASS);
        mapMaterial.put("ground", Material.DIRT);
        mapMaterial.put("wood", Material.WOOD);
        mapMaterial.put("rock", Material.STONE);
        mapMaterial.put("iron", Material.METAL);
        //mapMaterial.put("anvil", Material.anvil);
        mapMaterial.put("water", Material.WATER);
        mapMaterial.put("lava", Material.LAVA);
        mapMaterial.put("leaves", Material.LEAVES);
        mapMaterial.put("plants", Material.PLANT);
        mapMaterial.put("vine", Material.REPLACEABLE_PLANT);
        mapMaterial.put("sponge", Material.SPONGE);
        mapMaterial.put("cloth", Material.CLOTH_DECORATION);
        mapMaterial.put("fire", Material.FIRE);
        mapMaterial.put("sand", Material.SAND);
        mapMaterial.put("circuits", Material.PISTON);
        mapMaterial.put("carpet", Material.CLOTH_DECORATION);
        mapMaterial.put("glass", Material.GLASS);
        //mapMaterial.put("redstoneLight", Material.redstoneLight);
        //mapMaterial.put("tnt", Material.tnt);
        mapMaterial.put("coral", Material.WATER_PLANT);
        mapMaterial.put("ice", Material.ICE);
        mapMaterial.put("packedIce", Material.ICE_SOLID);
        mapMaterial.put("snow", Material.SNOW);
        mapMaterial.put("craftedSnow", Material.TOP_SNOW);
        mapMaterial.put("cactus", Material.CACTUS);
        mapMaterial.put("clay", Material.CLAY);
        mapMaterial.put("gourd", Material.DECORATION);
        mapMaterial.put("dragonEgg", Material.EGG);
        mapMaterial.put("portal", Material.PORTAL);
        mapMaterial.put("cake", Material.CAKE);
        mapMaterial.put("web", Material.WEB);
        mapMaterial.put("piston", Material.PISTON);
    }

    public static Material getMaterialFromName(String name) {
        return mapMaterial.containsKey(name)?(Material)mapMaterial.get(name):null;
    }

    public static Vec3 calculateFaceNormal(Vec3[] vertices) {
        Vec3 v1 = new Vec3(vertices[1].x - vertices[0].x, vertices[1].y - vertices[0].y, vertices[1].z - vertices[0].z);
        Vec3 v2 = new Vec3(vertices[2].x - vertices[0].x, vertices[2].y - vertices[0].y, vertices[2].z - vertices[0].z);
        return v1.cross(v2).normalize();
    }

    public static double parseDouble(String s) {
        return s == null?0.0D:Double.parseDouble(s.replace(',', '.'));
    }

    public static float RNG(float a, float min, float max) {
        return a < min?min:(a > max?max:a);
    }

    public static double RNG(double a, double min, double max) {
        return a < min?min:(a > max?max:a);
    }

    public static float smooth(float rot, float prevRot, float tick) {
        return prevRot + (rot - prevRot) * tick;
    }

    public static float smoothRot(float rot, float prevRot, float tick) {
        if(rot - prevRot < -180.0F) {
            prevRot -= 360.0F;
        } else if(prevRot - rot < -180.0F) {
            prevRot += 360.0F;
        }

        return prevRot + (rot - prevRot) * tick;
    }

    public static double getRotateDiff(double base, double target) {
        base = getRotate360(base);
        target = getRotate360(target);
        if(target - base < -180.0D) {
            target += 360.0D;
        } else if(target - base > 180.0D) {
            base += 360.0D;
        }

        return target - base;
    }

    public static float getPosAngle(double tx, double tz, double cx, double cz) {
        double length_A = Math.sqrt(tx * tx + tz * tz);
        double length_B = Math.sqrt(cx * cx + cz * cz);
        double cos_sita = (tx * cx + tz * cz) / (length_A * length_B);
        double sita = Math.acos(cos_sita);
        return (float)(sita * 180.0D / 3.141592653589793D);
    }

    public static boolean canPlayerCreateItem(Recipe<?> recipe, Inventory inventory) {
        if(recipe != null) {
            Map<Item, Integer> itemMap = getItemMapFromRecipe((CraftingRecipe) recipe);

            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack itemStack = inventory.getItem(i);
                if (!itemStack.isEmpty()) {
                    Item item = itemStack.getItem();
                    if (itemMap.containsKey(item)) {
                        itemMap.put(item, itemMap.get(item) - itemStack.getCount());
                    }
                }
            }

            //Iterator var6 = map.values().iterator();

            for (int count : itemMap.values()) {
                if (count > 0) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static void applyEntityHurtResistantTimeConfig(Entity entity) {
        if(entity instanceof LivingEntity) {
            LivingEntity elb = (LivingEntity)entity;
            MCH_Config var10000 = MCH_MOD.config;
            double h_time = MCH_Config.HurtResistantTime.prmDouble * (double)elb.hurtResistantTime;
            elb.hurtResistantTime = (int)h_time;
        }

    }

    public static int round(double d) {
        return (int)(d + 0.5D);
    }

    public static Vec3 Rot2Vec3(float yaw, float pitch) {
        return Vec3.createVectorHelper((double)(-MathHelper.sin(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F)), (double)(-MathHelper.sin(pitch / 180.0F * 3.1415927F)), (double)(MathHelper.cos(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F)));
    }

    public static Vec3 RotVec3(double x, double y, double z, float yaw, float pitch) {
        Vec3 v = Vec3.createVectorHelper(x, y, z);
        v.rotateAroundX(pitch / 180.0F * 3.1415927F);
        v.rotateAroundY(yaw / 180.0F * 3.1415927F);
        return v;
    }

    public static Vec3 RotVec3(double x, double y, double z, float yaw, float pitch, float roll) {
        Vec3 v = Vec3.createVectorHelper(x, y, z);
        W_Vec3.rotateAroundZ(roll / 180.0F * 3.1415927F, v);
        v.rotateAroundX(pitch / 180.0F * 3.1415927F);
        v.rotateAroundY(yaw / 180.0F * 3.1415927F);
        return v;
    }

    public static Vec3 RotVec3(Vec3 vin, float yaw, float pitch) {
        Vec3 v = Vec3.createVectorHelper(vin.xCoord, vin.yCoord, vin.zCoord);
        v.rotateAroundX(pitch / 180.0F * 3.1415927F);
        v.rotateAroundY(yaw / 180.0F * 3.1415927F);
        return v;
    }

    public static Vec3 RotVec3(Vec3 vin, float yaw, float pitch, float roll) {
        Vec3 v = Vec3.createVectorHelper(vin.xCoord, vin.yCoord, vin.zCoord);
        W_Vec3.rotateAroundZ(roll / 180.0F * 3.1415927F, v);
        v.rotateAroundX(pitch / 180.0F * 3.1415927F);
        v.rotateAroundY(yaw / 180.0F * 3.1415927F);
        return v;
    }

    public static Vec3 _Rot2Vec3(float yaw, float pitch, float roll) {
        return Vec3.createVectorHelper((double)(-MathHelper.sin(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F)), (double)(-MathHelper.sin(pitch / 180.0F * 3.1415927F)), (double)(MathHelper.cos(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F)));
    }

    public static double getRotate360(double r) {
        r %= 360.0D;
        return r >= 0.0D?r:r + 360.0D;
    }

    public static void Log(String format, Object ... data) {
        String side = MCH_MOD.proxy.isRemote()?"[Client]":"[Server]";
        System.out.printf("[" + getTime() + "][" + "mcheli" + "]" + side + " " + format + "\n", data);
    }

    public static void Log(World world, String format, Object ... data) {
        if(world != null) {
            Log((world.isRemote?"[ClientWorld]":"[ServerWorld]") + " " + format, data);
        } else {
            Log("[UnknownWorld]" + format, data);
        }

    }

    public static void Log(Entity entity, String format, Object ... data) {
        if(entity != null) {
            Log(entity.worldObj, format, data);
        } else {
            Log((World)null, format, data);
        }

    }

    public static void DbgLog(boolean isRemote, String format, Object ... data) {
        MCH_Config var10000 = MCH_MOD.config;
        if(MCH_Config.DebugLog) {
            String t = getTime();
            if(isRemote) {
                String playerName = "null";
                if(getClientPlayer() instanceof EntityPlayer) {
                    playerName = ((EntityPlayer)getClientPlayer()).getDisplayName();
                }

                System.out.println(String.format(format, data));
            } else {
                System.out.println(String.format(format, data));
            }
        }

    }

    public static void DbgLog(World w, String format, Object ... data) {
        DbgLog(w.isRemote, format, data);
    }

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        return sdf.format(new Date());
    }

    public static String getAzimuthStr8(int dir) {
        dir %= 360;
        if(dir < 0) {
            dir += 360;
        }

        dir /= AZIMUTH_8_ANG;
        return AZIMUTH_8[dir];
    }

    public static void rotatePoints(double[] points, float r) {
        r = r / 180.0F * 3.1415927F;

        for(int i = 0; i + 1 < points.length; i += 2) {
            double x = points[i + 0];
            double y = points[i + 1];
            points[i + 0] = x * (double)MathHelper.cos(r) - y * (double)MathHelper.sin(r);
            points[i + 1] = x * (double)MathHelper.sin(r) + y * (double)MathHelper.cos(r);
        }

    }

    public static void rotatePoints(ArrayList points, float r) {
        r = r / 180.0F * 3.1415927F;

        for(int i = 0; i + 1 < points.size(); i += 2) {
            double x = ((MCH_Vector2)points.get(i + 0)).x;
            double y = ((MCH_Vector2)points.get(i + 0)).y;
            ((MCH_Vector2)points.get(i + 0)).x = x * (double)MathHelper.cos(r) - y * (double)MathHelper.sin(r);
            ((MCH_Vector2)points.get(i + 0)).y = x * (double)MathHelper.sin(r) + y * (double)MathHelper.cos(r);
        }

    }

    public static String[] listupFileNames(String path) {
        File dir = new File(path);
        return dir.list();
    }

    public static boolean isBlockInWater(World w, int x, int y, int z) {
        int[][] offset = new int[][]{{0, -1, 0}, {0, 0, 0}, {0, 0, -1}, {0, 0, 1}, {-1, 0, 0}, {1, 0, 0}, {0, 1, 0}};
        if(y <= 0) {
            return false;
        } else {
            int[][] arr$ = offset;
            int len$ = offset.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                int[] o = arr$[i$];
                if(W_WorldFunc.isBlockWater(w, x + o[0], y + o[1], z + o[2])) {
                    return true;
                }
            }

            return false;
        }
    }

    public static int getBlockIdY(World w, double posX, double posY, double posZ, int size, int lenY, boolean canColliableOnly) {
        Block block = getBlockY(w, posX, posY, posZ, size, lenY, canColliableOnly);
        return block == null?0:W_Block.getIdFromBlock(block);
    }

    public static int getBlockIdY(Entity entity, int size, int lenY) {
        return getBlockIdY(entity, size, lenY, true);
    }

    public static int getBlockIdY(Entity entity, int size, int lenY, boolean canColliableOnly) {
        Block block = getBlockY(entity, size, lenY, canColliableOnly);
        return block == null?0:W_Block.getIdFromBlock(block);
    }

    public static Block getBlockY(Entity entity, int size, int lenY, boolean canColliableOnly) {
        return getBlockY(entity.worldObj, entity.posX, entity.posY, entity.posZ, size, lenY, canColliableOnly);
    }

    public static Block getBlockY(World world, Vec3 pos, int size, int lenY, boolean canColliableOnly) {
        return getBlockY(world, pos.xCoord, pos.yCoord, pos.zCoord, size, lenY, canColliableOnly);
    }

    public static Block getBlockY(World world, double posX, double posY, double posZ, int size, int lenY, boolean canColliableOnly) {
        if(lenY == 0) {
            return Blocks.air;
        } else {
            int px = (int)(posX + 0.5D);
            int py = (int)(posY + 0.5D);
            int pz = (int)(posZ + 0.5D);
            int cntY = lenY > 0?lenY:-lenY;

            for(int y = 0; y < cntY; ++y) {
                if(py + y < 0 || py + y > 255) {
                    return Blocks.air;
                }

                for(int x = -size / 2; x <= size / 2; ++x) {
                    for(int z = -size / 2; z <= size / 2; ++z) {
                        Block block = W_WorldFunc.getBlock(world, px + x, py + (lenY > 0?y:-y), pz + z);
                        if(block != null && block != Blocks.air) {
                            if(!canColliableOnly) {
                                return block;
                            }

                            if(block.canCollideCheck(0, true)) {
                                return block;
                            }
                        }
                    }
                }
            }

            return Blocks.air;
        }
    }

    public static Vec3 getYawPitchFromVec(Vec3 v) {
        return getYawPitchFromVec(v.xCoord, v.yCoord, v.zCoord);
    }

    public static Vec3 getYawPitchFromVec(double x, double y, double z) {
        double p = (double)MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0D / 3.141592653589793D);
        float roll = (float)(Math.atan2(y, p) * 180.0D / 3.141592653589793D);
        return Vec3.createVectorHelper(0.0D, (double)yaw, (double)roll);
    }

    public static float getAlpha(int argb) {
        return (float)(argb >> 24) / 255.0F;
    }

    public static float getRed(int argb) {
        return (float)(argb >> 16 & 255) / 255.0F;
    }

    public static float getGreen(int argb) {
        return (float)(argb >> 8 & 255) / 255.0F;
    }

    public static float getBlue(int argb) {
        return (float)(argb & 255) / 255.0F;
    }

    public static void enableFirstPersonItemRender() {
        MCH_Config var10000 = MCH_MOD.config;
        switch(MCH_Config.DisableItemRender.prmInt) {
            case 1:
            default:
                break;
            case 2:
                MCH_ItemRendererDummy.disableDummyItemRenderer();
                break;
            case 3:
                W_Reflection.restoreCameraZoom();
        }

    }

    public static void disableFirstPersonItemRender(ItemStack itemStack) {
        if(itemStack == null || !(itemStack.getItem() instanceof ItemMapBase) || W_McClient.getRenderEntity() instanceof MCH_ViewEntityDummy) {
            disableFirstPersonItemRender();
        }
    }

    public static void disableFirstPersonItemRender() {
        MCH_Config var10000 = MCH_MOD.config;
        switch(MCH_Config.DisableItemRender.prmInt) {
            case 1:
                W_Reflection.setItemRenderer_ItemToRender(new ItemStack(MCH_MOD.invisibleItem));
                break;
            case 2:
                MCH_ItemRendererDummy.enableDummyItemRenderer();
                break;
            case 3:
                W_Reflection.setCameraZoom(1.01F);
        }

    }

    public static Entity getClientPlayer() {
        return MCH_MOD.proxy.getClientPlayer();
    }

    public static void setRenderViewEntity(EntityLivingBase entity) {
        MCH_Config var10000 = MCH_MOD.config;
        if(MCH_Config.ReplaceRenderViewEntity.prmBool) {
            W_McClient.setRenderEntity(entity);
        }

    }

    public static Map getItemMapFromRecipe(Recipe recipe) {
        HashMap map = new HashMap();
        if(recipe instanceof ShapedRecipes) {
            ItemStack[] i$ = ((ShapedRecipes)recipe).recipeItems;
            int o = i$.length;

            for(int is = 0; is < o; ++is) {
                ItemStack item = i$[is];
                if(item != null) {
                    Item item1 = item.getItem();
                    if(map.containsKey(item1)) {
                        map.put(item1, Integer.valueOf(((Integer)map.get(item1)).intValue() + 1));
                    } else {
                        map.put(item1, Integer.valueOf(1));
                    }
                }
            }
        } else if(recipe instanceof ShapelessRecipes) {
            Iterator var7 = ((ShapelessRecipes)recipe).recipeItems.iterator();

            while(var7.hasNext()) {
                Object var8 = var7.next();
                ItemStack var9 = (ItemStack)var8;
                if(var9 != null) {
                    Item var10 = var9.getItem();
                    if(map.containsKey(var10)) {
                        map.put(var10, Integer.valueOf(((Integer)map.get(var10)).intValue() + 1));
                    } else {
                        map.put(var10, Integer.valueOf(1));
                    }
                }
            }
        }

        return map;
    }

}
