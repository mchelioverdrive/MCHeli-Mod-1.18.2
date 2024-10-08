package mchelio;


import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
//import net.minecraftforge.fml.common.Mod.EventHandler;
import java.io.File;
import java.util.Iterator;
import mchelio.MCH_Achievement;
import mchelio.MCH_CommonProxy;
import mchelio.MCH_Config;
import mchelio.MCH_CreativeTabs;
import mchelio.MCH_EventHook;
import mchelio.MCH_InvisibleItem;
import mchelio.MCH_ItemRecipe;
import mchelio.MCH_Lib;
import mchelio.MCH_PacketHandler;
import mchelio.MCH_SoundsJson;
import mchelio.aircraft.MCH_EntityHide;
import mchelio.aircraft.MCH_EntityHitBox;
import mchelio.aircraft.MCH_EntitySeat;
import mchelio.aircraft.MCH_ItemAircraft;
import mchelio.aircraft.MCH_ItemFuel;
import mchelio.block.MCH_DraftingTableBlock;
import mchelio.block.MCH_DraftingTableTileEntity;
import mchelio.chain.MCH_EntityChain;
import mchelio.chain.MCH_ItemChain;
import mchelio.command.MCH_Command;
import mchelio.container.MCH_EntityContainer;
import mchelio.container.MCH_ItemContainer;
import mchelio.flare.MCH_EntityFlare;
import mchelio.gltd.MCH_EntityGLTD;
import mchelio.gltd.MCH_ItemGLTD;
import mchelio.gui.MCH_GuiCommonHandler;
import mchelio.helicopter.MCH_EntityHeli;
import mchelio.helicopter.MCH_HeliInfo;
import mchelio.helicopter.MCH_HeliInfoManager;
import mchelio.helicopter.MCH_ItemHeli;
import mchelio.lweapon.MCH_ItemLightWeaponBase;
import mchelio.lweapon.MCH_ItemLightWeaponBullet;
import mchelio.parachute.MCH_EntityParachute;
import mchelio.parachute.MCH_ItemParachute;
import mchelio.plane.MCP_EntityPlane;
import mchelio.plane.MCP_ItemPlane;
import mchelio.plane.MCP_PlaneInfo;
import mchelio.plane.MCP_PlaneInfoManager;
import mchelio.tank.MCH_EntityTank;
import mchelio.tank.MCH_ItemTank;
import mchelio.tank.MCH_TankInfo;
import mchelio.tank.MCH_TankInfoManager;
import mchelio.throwable.MCH_EntityThrowable;
import mchelio.throwable.MCH_ItemThrowable;
import mchelio.throwable.MCH_ThrowableInfo;
import mchelio.throwable.MCH_ThrowableInfoManager;
import mchelio.tool.MCH_ItemWrench;
import mchelio.tool.rangefinder.MCH_ItemRangeFinder;
import mchelio.uav.MCH_EntityUavStation;
import mchelio.uav.MCH_ItemUavStation;
import mchelio.vehicle.MCH_EntityVehicle;
import mchelio.vehicle.MCH_ItemVehicle;
import mchelio.vehicle.MCH_VehicleInfo;
import mchelio.vehicle.MCH_VehicleInfoManager;
import mchelio.weapon.MCH_EntityA10;
import mchelio.weapon.MCH_EntityAAMissile;
import mchelio.weapon.MCH_EntityASMissile;
import mchelio.weapon.MCH_EntityATMissile;
import mchelio.weapon.MCH_EntityBomb;
import mchelio.weapon.MCH_EntityBullet;
import mchelio.weapon.MCH_EntityDispensedItem;
import mchelio.weapon.MCH_EntityMarkerRocket;
import mchelio.weapon.MCH_EntityRocket;
import mchelio.weapon.MCH_EntityTorpedo;
import mchelio.weapon.MCH_EntityTvMissile;
import mchelio.weapon.MCH_WeaponInfoManager;
import mchelio.wrapper.NetworkMod;
import mchelio.wrapper.W_Item;
import mchelio.wrapper.W_ItemList;
import mchelio.wrapper.W_LanguageRegistry;
import mchelio.wrapper.W_NetworkRegistry;
import net.minecraft.world.item.Item;
import mchelio.MCH_IChunkLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.ModList;
import java.nio.file.Path;
import java.io.File;

import java.util.List;

@EventBusSubscriber(modid = MCH_MOD.MODID, bus = EventBusSubscriber.Bus.MOD)


//@Mod(
//        modid = "mchelio",
//        name = "mchelio",
//        dependencies = "required-after:Forge@[10.13.2.1230,)"
//)
//@NetworkMod(
//        clientSideRequired = true,
//        serverSideRequired = false
//)
@Mod("mchelio")
public class MCH_MOD {
    public static final String MODID = "mchelio";

    public static final String MOD_ID = "mchelio";
    public static final String DOMAIN = "mchelio";
    public static final String MCVER = "1.18.2";
    public static String VER = "";
    public static final String MOD_CH = "MCHeli_CH";
    //@Instance("mchelio")
    //public static MCH_MOD instance;
    public static final MCHClientProxy CLIENT_PROXY = new MCHClientProxy();
    public static final MCH_CommonProxy COMMON_PROXY = new MCH_CommonProxy();
    public static MCH_CommonProxy proxy;
    public static MCH_PacketHandler packetHandler = new MCH_PacketHandler();
    public static MCH_Config config;
    public static String sourcePath;
    public static MCH_InvisibleItem invisibleItem;
    public static MCH_ItemGLTD itemGLTD;
    public static MCH_ItemLightWeaponBullet itemStingerBullet;
    public static MCH_ItemLightWeaponBase itemStinger;
    public static MCH_ItemLightWeaponBullet itemJavelinBullet;
    public static MCH_ItemLightWeaponBase itemJavelin;
    public static MCH_ItemLightWeaponBase itemRpg;
    public static MCH_ItemLightWeaponBullet itemRpgBullet;
    public static MCH_ItemUavStation[] itemUavStation;
    public static MCH_ItemParachute itemParachute;
    public static MCH_ItemContainer itemContainer;
    public static MCH_ItemChain itemChain;
    public static MCH_ItemFuel itemFuel;
    public static MCH_ItemWrench itemWrench;
    public static MCH_ItemRangeFinder itemRangeFinder;
    public static MCH_CreativeTabs creativeTabs;
    public static MCH_CreativeTabs creativeTabsHeli;
    public static MCH_CreativeTabs creativeTabsPlane;
    public static MCH_CreativeTabs creativeTabsTank;
    public static MCH_CreativeTabs creativeTabsVehicle;
    public static MCH_DraftingTableBlock blockDraftingTable;
    public static MCH_DraftingTableBlock blockDraftingTableLit;
    public static Item sampleHelmet;


    private void commonSetup(final FMLCommonSetupEvent event) {

        // try {
        //    ZipInputStream zis = new ZipInputStream(new FileInputStream("path/to/your/assets.zip"));
        //    ZipEntry entry = zis.getNextEntry();
        // }
        //sorry but we're gonna need a loader mod to unzip this crap
        MCH_Lib.init();
        MCH_Lib.Log("MC Ver:1.7.10 MOD Ver:" + VER + "", new Object[0]);
        MCH_Lib.Log("Start load...", new Object[0]);
        //sourcePath = Loader.instance().activeModContainer().getSource().getPath();
        MCH_Lib.Log("SourcePath: " + sourcePath, new Object[0]);
        MCH_Lib.Log("CurrentDirectory:" + (new File(".")).getAbsolutePath(), new Object[0]);
        //proxy.
        creativeTabs = new MCH_CreativeTabs("MC Heli Item");
        creativeTabsHeli = new MCH_CreativeTabs("MC Heli Helicopters");
        creativeTabsPlane = new MCH_CreativeTabs("MC Heli Planes");
        creativeTabsTank = new MCH_CreativeTabs("MC Heli Tanks");
        creativeTabsVehicle = new MCH_CreativeTabs("MC Heli Vehicles");
        W_ItemList.init();
        config = proxy.loadConfig("config/mcheli.cfg");
        proxy.loadHUD(sourcePath + "/assets/" + "mcheli" + "/hud");
        MCH_WeaponInfoManager.load(sourcePath + "/assets/" + "mcheli" + "/weapons");
        MCH_HeliInfoManager.getInstance().load(sourcePath + "/assets/" + "mcheli" + "/", "helicopters");
        MCP_PlaneInfoManager.getInstance().load(sourcePath + "/assets/" + "mcheli" + "/", "planes");
        MCH_TankInfoManager.getInstance().load(sourcePath + "/assets/" + "mcheli" + "/", "tanks");
        MCH_VehicleInfoManager.getInstance().load(sourcePath + "/assets/" + "mcheli" + "/", "vehicles");
        MCH_ThrowableInfoManager.load(sourcePath + "/assets/" + "mcheli" + "/throwable");
        MCH_SoundsJson.update(sourcePath + "/assets/" + "mcheli" + "/");
        MCH_Lib.Log("Register item", new Object[0]);
        this.registerItemRangeFinder();
        this.registerItemWrench();
        this.registerItemFuel();
        this.registerItemGLTD();
        this.registerItemChain();
        this.registerItemParachute();
        this.registerItemContainer();
        this.registerItemUavStation();
        this.registerItemInvisible();
        registerItemThrowable();
        this.registerItemLightWeaponBullet();
        this.registerItemLightWeapon();
        registerItemAircraft();
        MCH_DraftingTableBlock var10000 = new MCH_DraftingTableBlock(MCH_Config.BlockID_DraftingTableOFF.prmInt, false);
        MCH_Config var10002 = config;
        blockDraftingTable = var10000;
        blockDraftingTable.setBlockName("drafting_table");
        blockDraftingTable.setCreativeTab(creativeTabs);
        var10000 = new MCH_DraftingTableBlock(MCH_Config.BlockID_DraftingTableON.prmInt, true);
        var10002 = config;
        blockDraftingTableLit = var10000;
        blockDraftingTableLit.setBlockName("lit_drafting_table");
        GameRegistry.registerBlock(blockDraftingTable, "drafting_table");
        GameRegistry.registerBlock(blockDraftingTableLit, "lit_drafting_table");
        W_LanguageRegistry.addName(blockDraftingTable, "Drafting Table");
        W_LanguageRegistry.addNameForObject(blockDraftingTable, "ja_JP", "製図台");
        MCH_Achievement.PreInit();
        MCH_Lib.Log("Register system", new Object[0]);
        W_NetworkRegistry.registerChannel(packetHandler, "MCHeli_CH");
        MinecraftForge.EVENT_BUS.register(new MCH_EventHook());

        proxy.registerClientTick();

        W_NetworkRegistry.registerGuiHandler(this, new MCH_GuiCommonHandler());
        MCH_Lib.Log("Register entity", new Object[0]);
        this.registerEntity();
        MCH_Lib.Log("Register renderer", new Object[0]);
        proxy.registerRenderer();
        MCH_Lib.Log("Register models", new Object[0]);
        proxy.registerModels();
        MCH_Lib.Log("Register Sounds", new Object[0]);
        proxy.registerSounds();
        W_LanguageRegistry.updateLang(sourcePath + "/assets/" + "mcheli" + "/lang/");
        MCH_Lib.Log("End load", new Object[0]);


        try {
            ForgeChunkManager.setForcedChunkLoadingCallback(this, new ForgeChunkManager.LoadingCallback() {

                @Override
                public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
                    for (ForgeChunkManager.Ticket ticket : tickets) {
                        if (ticket.getEntity() instanceof MCH_EntityBullet) {
                            ((MCH_IChunkLoader) ticket.getEntity()).init(ticket);
                        }
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("error loading chunk");
        }

    }




    @EventHandler
    public void init(FMLInitializationEvent evt) {
        GameRegistry.registerTileEntity(MCH_DraftingTableTileEntity.class, "drafting_table");
        proxy.registerBlockRenderer();

    }



    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        MCH_Config var10001 = config;
        creativeTabs.setFixedIconItem(MCH_Config.CreativeTabIcon.prmString);
        var10001 = config;
        creativeTabsHeli.setFixedIconItem(MCH_Config.CreativeTabIconHeli.prmString);
        var10001 = config;
        creativeTabsPlane.setFixedIconItem(MCH_Config.CreativeTabIconPlane.prmString);
        var10001 = config;
        creativeTabsTank.setFixedIconItem(MCH_Config.CreativeTabIconTank.prmString);
        var10001 = config;
        creativeTabsVehicle.setFixedIconItem(MCH_Config.CreativeTabIconVehicle.prmString);



        MCH_ItemRecipe.registerItemRecipe();
        MCH_WeaponInfoManager.setRoundItems();
        proxy.readClientModList();
    }

    @EventHandler
    public void onStartServer(FMLServerStartingEvent event) {
        proxy.registerServerTick();

    }



    public void registerEntity() {
        EntityRegistry.registerModEntity(MCH_EntitySeat.class, "MCH.E.Seat", 100, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityHeli.class, "MCH.E.Heli", 101, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityGLTD.class, "MCH.E.GLTD", 102, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCP_EntityPlane.class, "MCH.E.Plane", 103, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityChain.class, "MCH.E.Chain", 104, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityHitBox.class, "MCH.E.PSeat", 105, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityParachute.class, "MCH.E.Parachute", 106, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityContainer.class, "MCH.E.Container", 107, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityVehicle.class, "MCH.E.Vehicle", 108, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityUavStation.class, "MCH.E.UavStation", 109, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityHitBox.class, "MCH.E.HitBox", 110, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityHide.class, "MCH.E.Hide", 111, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityTank.class, "MCH.E.Tank", 112, this, 200, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityRocket.class, "MCH.E.Rocket", 200, this, 530, 5, true);
        EntityRegistry.registerModEntity(MCH_EntityTvMissile.class, "MCH.E.TvMissle", 201, this, 530, 5, true);
        EntityRegistry.registerModEntity(MCH_EntityBullet.class, "MCH.E.Bullet", 202, this, 530, 5, true);
        EntityRegistry.registerModEntity(MCH_EntityA10.class, "MCH.E.A10", 203, this, 530, 5, true);
        EntityRegistry.registerModEntity(MCH_EntityAAMissile.class, "MCH.E.AAM", 204, this, 530, 5, true);
        EntityRegistry.registerModEntity(MCH_EntityASMissile.class, "MCH.E.ASM", 205, this, 530, 5, true);
        EntityRegistry.registerModEntity(MCH_EntityTorpedo.class, "MCH.E.Torpedo", 206, this, 530, 5, true);
        EntityRegistry.registerModEntity(MCH_EntityATMissile.class, "MCH.E.ATMissle", 207, this, 530, 5, true);
        EntityRegistry.registerModEntity(MCH_EntityBomb.class, "MCH.E.Bomb", 208, this, 530, 5, true);
        EntityRegistry.registerModEntity(MCH_EntityMarkerRocket.class, "MCH.E.MkRocket", 209, this, 530, 5, true);
        EntityRegistry.registerModEntity(MCH_EntityDispensedItem.class, "MCH.E.DispItem", 210, this, 530, 5, true);
        EntityRegistry.registerModEntity(MCH_EntityFlare.class, "MCH.E.Flare", 300, this, 330, 10, true);
        EntityRegistry.registerModEntity(MCH_EntityThrowable.class, "MCH.E.Throwable", 400, this, 330, 10, true);
    }

    @EventHandler
    public void registerCommand(FMLServerStartedEvent e) {
        CommandHandler handler = (CommandHandler)FMLCommonHandler.instance().getSidedDelegate().getServer().getCommandManager();
        handler.registerCommand(new MCH_Command());
    }

    private void registerItemRangeFinder() {
        String name = "rangefinder";
        MCH_ItemRangeFinder var10000 = new MCH_ItemRangeFinder(MCH_Config.ItemID_RangeFinder.prmInt);
        MCH_Config var10002 = config;
        MCH_ItemRangeFinder item = var10000;
        itemRangeFinder = item;
        registerItem(item, "rangefinder", creativeTabs);
        W_LanguageRegistry.addName(item, "Laser Rangefinder");
        W_LanguageRegistry.addNameForObject(item, "ja_JP", "レーザー レンジ ファインダー");
    }

    private void registerItemWrench() {
        String name = "wrench";
        MCH_ItemWrench var10000 = new MCH_ItemWrench(MCH_Config.ItemID_Wrench.prmInt, ToolMaterial.IRON);
        MCH_Config var10002 = config;
        MCH_ItemWrench item = var10000;
        itemWrench = item;
        registerItem(item, "wrench", creativeTabs);
        W_LanguageRegistry.addName(item, "Wrench");
        W_LanguageRegistry.addNameForObject(item, "ja_JP", "レンチ");
    }

    public void registerItemInvisible() {
        String name = "internal";
        MCH_InvisibleItem var10000 = new MCH_InvisibleItem(MCH_Config.ItemID_InvisibleItem.prmInt);
        MCH_Config var10002 = config;
        MCH_InvisibleItem item = var10000;
        invisibleItem = item;
        registerItem(item, "internal", (MCH_CreativeTabs)null);
    }

    public void registerItemUavStation() {
        String[] dispName = new String[]{"UAV Station", "Portable UAV Controller"};
        String[] localName = new String[]{"UAVステーション", "携帯UAV制御端末"};
        itemUavStation = new MCH_ItemUavStation[MCH_ItemUavStation.UAV_STATION_KIND_NUM];
        String name = "uav_station";

        for(int i = 0; i < itemUavStation.length; ++i) {
            String nn = i > 0?"" + (i + 1):"";
            MCH_ItemUavStation var10000 = new MCH_ItemUavStation(MCH_Config.ItemID_UavStation[i].prmInt, 1 + i);
            MCH_Config var10002 = config;
            MCH_ItemUavStation item = var10000;
            itemUavStation[i] = item;
            registerItem(item, "uav_station" + nn, creativeTabs);
            W_LanguageRegistry.addName(item, dispName[i]);
            W_LanguageRegistry.addNameForObject(item, "ja_JP", localName[i]);
        }

    }

    public void registerItemParachute() {
        String name = "parachute";
        MCH_ItemParachute var10000 = new MCH_ItemParachute(MCH_Config.ItemID_Parachute.prmInt);
        MCH_Config var10002 = config;
        MCH_ItemParachute item = var10000;
        itemParachute = item;
        registerItem(item, "parachute", creativeTabs);
        W_LanguageRegistry.addName(item, "Parachute");
        W_LanguageRegistry.addNameForObject(item, "ja_JP", "パラシュート");
    }

    public void registerItemContainer() {
        String name = "container";
        MCH_ItemContainer var10000 = new MCH_ItemContainer(MCH_Config.ItemID_Container.prmInt);
        MCH_Config var10002 = config;
        MCH_ItemContainer item = var10000;
        itemContainer = item;
        registerItem(item, "container", creativeTabs);
        W_LanguageRegistry.addName(item, "Container");
        W_LanguageRegistry.addNameForObject(item, "ja_JP", "コンテナ");
    }

    public void registerItemLightWeapon() {
        String name = "fim92";
        MCH_ItemLightWeaponBase var10000 = new MCH_ItemLightWeaponBase(MCH_Config.ItemID_Stinger.prmInt, itemStingerBullet);
        MCH_Config var10002 = config;
        MCH_ItemLightWeaponBase item = var10000;
        itemStinger = item;
        registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName(item, "FIM-92 Stinger");
        name = "fgm148";
        var10000 = new MCH_ItemLightWeaponBase(MCH_Config.ItemID_Stinger.prmInt, itemJavelinBullet);
        var10002 = config;
        item = var10000;
        itemJavelin = item;
        registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName(item, "FGM-148 Javelin");
        name = "rpg7";
        var10000 = new MCH_ItemLightWeaponBase(MCH_Config.ItemID_Stinger.prmInt, itemRpgBullet);
        var10002 = config;
        item = var10000;
        itemRpg = item;
        registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName(item, "RPG-7");
    }

    public void registerItemLightWeaponBullet() {
        String name = "fim92_bullet";
        MCH_ItemLightWeaponBullet var10000 = new MCH_ItemLightWeaponBullet(MCH_Config.ItemID_StingerMissile.prmInt);
        MCH_Config var10002 = config;
        MCH_ItemLightWeaponBullet item = var10000;
        itemStingerBullet = item;
        registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName(item, "FIM-92 Stinger missile");
        name = "fgm148_bullet";
        var10000 = new MCH_ItemLightWeaponBullet(MCH_Config.ItemID_StingerMissile.prmInt);
        var10002 = config;
        item = var10000;
        itemJavelinBullet = item;
        registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName(item, "FGM-148 Javelin missile");
        name = "rpg7_bullet";
        var10000 = new MCH_ItemLightWeaponBullet(MCH_Config.ItemID_StingerMissile.prmInt);
        var10002 = config;
        item = var10000;
        itemRpgBullet = item;
        registerItem(item, name, creativeTabs);
        W_LanguageRegistry.addName(item, "RPG-7 Warhead");
    }

    public void registerItemChain() {
        String name = "chain";
        MCH_ItemChain var10000 = new MCH_ItemChain(MCH_Config.ItemID_Chain.prmInt);
        MCH_Config var10002 = config;
        MCH_ItemChain item = var10000;
        itemChain = item;
        registerItem(item, "chain", creativeTabs);
        W_LanguageRegistry.addName(item, "Chain");
        W_LanguageRegistry.addNameForObject(item, "ja_JP", "鎖");
    }

    public void registerItemFuel() {
        String name = "fuel";
        MCH_ItemFuel var10000 = new MCH_ItemFuel(MCH_Config.ItemID_Fuel.prmInt);
        MCH_Config var10002 = config;
        MCH_ItemFuel item = var10000;
        itemFuel = item;
        registerItem(item, "fuel", creativeTabs);
        W_LanguageRegistry.addName(item, "Fuel");
        W_LanguageRegistry.addNameForObject(item, "ja_JP", "燃料");
    }

    public void registerItemGLTD() {
        String name = "gltd";
        MCH_ItemGLTD var10000 = new MCH_ItemGLTD(MCH_Config.ItemID_GLTD.prmInt);
        MCH_Config var10002 = config;
        MCH_ItemGLTD item = var10000;
        itemGLTD = item;
        registerItem(item, "gltd", creativeTabs);
        W_LanguageRegistry.addName(item, "GLTD:Target Designator");
        W_LanguageRegistry.addNameForObject(item, "ja_JP", "GLTD:レーザー目標指示装置");
    }

    public static void registerItem(W_Item item, String name, MCH_CreativeTabs ct) {
        item.setUnlocalizedName("mcheli:" + name);
        item.setTexture(name);
        if(ct != null) {
            item.setCreativeTab(ct);
            ct.addIconItem(item);
        }

        GameRegistry.registerItem(item, name);
    }

    public static void registerItemThrowable() {
        Iterator i$ = MCH_ThrowableInfoManager.getKeySet().iterator();

        while(i$.hasNext()) {
            String name = (String)i$.next();
            MCH_ThrowableInfo info = MCH_ThrowableInfoManager.get(name);
            info.item = new MCH_ItemThrowable(info.itemID);
            info.item.setMaxStackSize(info.stackSize);
            registerItem(info.item, name, creativeTabs);
            MCH_ItemThrowable.registerDispenseBehavior(info.item);
            info.itemID = W_Item.getIdFromItem(info.item) - 256;
            W_LanguageRegistry.addName(info.item, info.displayName);
            Iterator i$1 = info.displayNameLang.keySet().iterator();

            while(i$1.hasNext()) {
                String lang = (String)i$1.next();
                W_LanguageRegistry.addNameForObject(info.item, lang, (String)info.displayNameLang.get(lang));
            }
        }

    }

    public static void registerItemAircraft() {
        Iterator i$ = MCH_HeliInfoManager.map.keySet().iterator();

        String name;
        Iterator i$1;
        String lang;
        while(i$.hasNext()) {
            name = (String)i$.next();
            MCH_HeliInfo info = (MCH_HeliInfo)MCH_HeliInfoManager.map.get(name);
            info.item = new MCH_ItemHeli(info.itemID);
            info.item.setMaxDamage(info.maxHp);
            if(!info.canRide && (info.ammoSupplyRange > 0.0F || info.fuelSupplyRange > 0.0F)) {
                registerItem(info.item, name, creativeTabs);
            } else {
                registerItem(info.item, name, creativeTabsHeli);
            }

            MCH_ItemAircraft.registerDispenseBehavior(info.item);
            info.itemID = W_Item.getIdFromItem(info.item) - 256;
            W_LanguageRegistry.addName(info.item, info.displayName);
            i$1 = info.displayNameLang.keySet().iterator();

            while(i$1.hasNext()) {
                lang = (String)i$1.next();
                W_LanguageRegistry.addNameForObject(info.item, lang, (String)info.displayNameLang.get(lang));
            }
        }

        i$ = MCP_PlaneInfoManager.map.keySet().iterator();

        while(i$.hasNext()) {
            name = (String)i$.next();
            MCP_PlaneInfo info1 = (MCP_PlaneInfo)MCP_PlaneInfoManager.map.get(name);
            info1.item = new MCP_ItemPlane(info1.itemID);
            info1.item.setMaxDamage(info1.maxHp);
            if(!info1.canRide && (info1.ammoSupplyRange > 0.0F || info1.fuelSupplyRange > 0.0F)) {
                registerItem(info1.item, name, creativeTabs);
            } else {
                registerItem(info1.item, name, creativeTabsPlane);
            }

            MCH_ItemAircraft.registerDispenseBehavior(info1.item);
            info1.itemID = W_Item.getIdFromItem(info1.item) - 256;
            W_LanguageRegistry.addName(info1.item, info1.displayName);
            i$1 = info1.displayNameLang.keySet().iterator();

            while(i$1.hasNext()) {
                lang = (String)i$1.next();
                W_LanguageRegistry.addNameForObject(info1.item, lang, (String)info1.displayNameLang.get(lang));
            }
        }

        i$ = MCH_TankInfoManager.map.keySet().iterator();

        while(i$.hasNext()) {
            name = (String)i$.next();
            MCH_TankInfo info2 = (MCH_TankInfo)MCH_TankInfoManager.map.get(name);
            info2.item = new MCH_ItemTank(info2.itemID);
            info2.item.setMaxDamage(info2.maxHp);
            if(!info2.canRide && (info2.ammoSupplyRange > 0.0F || info2.fuelSupplyRange > 0.0F)) {
                registerItem(info2.item, name, creativeTabs);
            } else {
                registerItem(info2.item, name, creativeTabsTank);
            }

            MCH_ItemAircraft.registerDispenseBehavior(info2.item);
            info2.itemID = W_Item.getIdFromItem(info2.item) - 256;
            W_LanguageRegistry.addName(info2.item, info2.displayName);
            i$1 = info2.displayNameLang.keySet().iterator();

            while(i$1.hasNext()) {
                lang = (String)i$1.next();
                W_LanguageRegistry.addNameForObject(info2.item, lang, (String)info2.displayNameLang.get(lang));
            }
        }

        i$ = MCH_VehicleInfoManager.map.keySet().iterator();

        while(i$.hasNext()) {
            name = (String)i$.next();
            MCH_VehicleInfo info3 = (MCH_VehicleInfo)MCH_VehicleInfoManager.map.get(name);
            info3.item = new MCH_ItemVehicle(info3.itemID);
            info3.item.setMaxDamage(info3.maxHp);
            if(!info3.canRide && (info3.ammoSupplyRange > 0.0F || info3.fuelSupplyRange > 0.0F)) {
                registerItem(info3.item, name, creativeTabs);
            } else {
                registerItem(info3.item, name, creativeTabsVehicle);
            }

            MCH_ItemAircraft.registerDispenseBehavior(info3.item);
            info3.itemID = W_Item.getIdFromItem(info3.item) - 256;
            W_LanguageRegistry.addName(info3.item, info3.displayName);
            i$1 = info3.displayNameLang.keySet().iterator();

            while(i$1.hasNext()) {
                lang = (String)i$1.next();
                W_LanguageRegistry.addNameForObject(info3.item, lang, (String)info3.displayNameLang.get(lang));
            }
        }

    }

}
