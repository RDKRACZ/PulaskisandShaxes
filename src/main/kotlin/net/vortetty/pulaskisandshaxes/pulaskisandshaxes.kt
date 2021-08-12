package net.vortetty.pulaskisandshaxes

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.devtech.arrp.api.RRPCallback
import net.devtech.arrp.api.RuntimeResourcePack
import net.devtech.arrp.json.recipe.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ToolMaterials
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.vortetty.pulaskisandshaxes.block.ConfigPiston
import net.vortetty.pulaskisandshaxes.block.UselessBlock
import net.vortetty.pulaskisandshaxes.config.configuration
import net.vortetty.pulaskisandshaxes.enchant.lethalityEnchant
import net.vortetty.pulaskisandshaxes.items.PulaskiItem
import net.vortetty.pulaskisandshaxes.items.ShaxeItem
import net.vortetty.pulaskisandshaxes.items.bedrockBreaker
import net.vortetty.pulaskisandshaxes.items.uselessItem
import org.hjson.Stringify
import java.util.*

fun joinVoxels(vararg shapes: VoxelShape): VoxelShape {
    var tmp: VoxelShape = VoxelShapes.empty()
    for (shape in shapes){
        tmp = VoxelShapes.union(
            tmp,
            shape
        )
    }
    return tmp
}

fun createCuboidShape(minX: Double, minY: Double, minZ: Double, sizeX: Double, sizeY: Double, sizeZ: Double): VoxelShape {
    return VoxelShapes.cuboid(minX/16.0, minY/16.0, minZ/16.0, minX/16.0+sizeX/16.0, minY/16.0+sizeY/16.0, minZ/16.0+sizeZ/16.0)
}

class pulaskisandshaxes : ModInitializer {
    companion object {
        var config: configuration = configuration()

        //  _______          _
        // |__   __|        | |
        //    | | ___   ___ | |___
        //    | |/ _ \ / _ \| / __|
        //    | | (_) | (_) | \__ \
        //    |_|\___/ \___/|_|___/
        //
        val WOODEN_PULASKI = PulaskiItem(ToolMaterials.WOOD, 8F, -3.2f, Item.Settings().group(ItemGroup.TOOLS).maxDamage(64))
        val WOODEN_SHAXE = ShaxeItem(ToolMaterials.WOOD, 2, -2.8f, Item.Settings().group(ItemGroup.TOOLS).maxDamage(64))
        val STONE_PULASKI = PulaskiItem(ToolMaterials.STONE, 9f, -3.2f, Item.Settings().group(ItemGroup.TOOLS).maxDamage(131))
        val STONE_SHAXE = ShaxeItem(ToolMaterials.STONE, 2, -2.8f, Item.Settings().group(ItemGroup.TOOLS).maxDamage(131))
        val IRON_PULASKI = PulaskiItem(ToolMaterials.IRON, 8f, -3.1f, Item.Settings().group(ItemGroup.TOOLS).maxDamage(250))
        val IRON_SHAXE = ShaxeItem(ToolMaterials.IRON, 2, -2.8f, Item.Settings().group(ItemGroup.TOOLS).maxDamage(250))
        val GOLD_PULASKI = PulaskiItem(ToolMaterials.GOLD, 8f, (-3).toFloat(), Item.Settings().group(ItemGroup.TOOLS).maxDamage(32))
        val GOLD_SHAXE = ShaxeItem(ToolMaterials.GOLD, 2, -2.8f, Item.Settings().group(ItemGroup.TOOLS).maxDamage(32))
        val DIAMOND_PULASKI = PulaskiItem(ToolMaterials.DIAMOND, 7f, (-3).toFloat(), Item.Settings().group(ItemGroup.TOOLS).maxDamage(1561))
        val DIAMOND_SHAXE = ShaxeItem(ToolMaterials.DIAMOND, 2, -2.8f, Item.Settings().group(ItemGroup.TOOLS).maxDamage(1561))
        val NETHERITE_PULASKI = PulaskiItem(ToolMaterials.NETHERITE, 7f, (-3).toFloat(), Item.Settings().group(ItemGroup.TOOLS).maxDamage(2031))
        val NETHERITE_SHAXE = ShaxeItem(ToolMaterials.NETHERITE, 2, -2.8f, Item.Settings().group(ItemGroup.TOOLS).maxDamage(2031))
        val BROKEN_BEDROCK_BREAKER = uselessItem(Item.Settings().group(ItemGroup.TOOLS).fireproof().maxCount(1).maxDamage(0).rarity(Rarity.EPIC), true)
        val FUZED_CHAIN = uselessItem(Item.Settings().group(ItemGroup.MATERIALS).maxCount(1).maxDamage(0).rarity(Rarity.EPIC), false)
        val NETHER_CORE = uselessItem(Item.Settings().group(ItemGroup.MATERIALS).maxCount(1).maxDamage(0).rarity(Rarity.EPIC), true)
        val NETHERITE_CORNER = uselessItem(Item.Settings().group(ItemGroup.MATERIALS).maxCount(1).maxDamage(0).rarity(Rarity.EPIC), false)
        val NETHERITE_STICK = uselessItem(Item.Settings().group(ItemGroup.MATERIALS).fireproof().maxCount(1).maxDamage(0).rarity(Rarity.EPIC), false)

        //
        // Meme stuff
        //
        val EXPOSURE = Item(Item.Settings().group(ItemGroup.MATERIALS).rarity(Rarity.COMMON).fireproof())

        val BADAPPLESHAPE = joinVoxels(
            createCuboidShape(0.0, 10.0, 0.0, 16.0, 6.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 2.0, 10.0, 2.0),
            createCuboidShape(14.0, 0.0, 0.0, 2.0, 10.0, 2.0),
            createCuboidShape(14.0, 0.0, 14.0, 2.0, 10.0, 2.0),
            createCuboidShape(0.0, 0.0, 14.0, 2.0, 10.0, 2.0)
        )
        val BADAPPLE = UselessBlock(true, BADAPPLESHAPE, FabricBlockSettings.of(Material.STONE).breakByHand(true).breakByTool(TagRegistry.item(Identifier("fabric", "pickaxes"))).collidable(true))

        //
        // Test stuff
        //
        val NETHERITE_PULASKI_TEST = PulaskiItem(ToolMaterials.NETHERITE, 7f, (-3).toFloat(), Item.Settings().group(ItemGroup.TOOLS).maxDamage(2031))
        val NETHERITE_SHAXE_TEST = ShaxeItem(ToolMaterials.NETHERITE, 2, -2.8f, Item.Settings().group(ItemGroup.TOOLS).maxDamage(2031))

        //  ____  _            _
        // |  _ \| |          | |
        // | |_) | | ___   ___| | _____
        // |  _ <| |/ _ \ / __| |/ / __|
        // | |_) | | (_) | (__|   <\__ \
        // |____/|_|\___/ \___|_|\_|___/
        //
        val WOODENPISTON = ConfigPiston(false, FabricBlockSettings.of(Material.PISTON).breakByHand(true).breakByTool(TagRegistry.item(Identifier("fabric", "axes")), 0).collidable(true).hardness(1f), 6)
        val GOLDPISTON = ConfigPiston(false, FabricBlockSettings.of(Material.PISTON).breakByHand(true).breakByTool(TagRegistry.item(Identifier("fabric", "pickaxes")), 0).collidable(true).hardness(1.5f), 24)
        val DIAMONDPISTON = ConfigPiston(false, FabricBlockSettings.of(Material.PISTON).breakByHand(true).breakByTool(TagRegistry.item(Identifier("fabric", "pickaxes")), 0).collidable(true).hardness(2.5f), 48)
        val NETHERITEPISTON = ConfigPiston(false, FabricBlockSettings.of(Material.PISTON).breakByHand(true).breakByTool(TagRegistry.item(Identifier("fabric", "pickaxes")), 0).collidable(true).hardness(5f), 96)
        val SUPERPISTON = ConfigPiston(false, FabricBlockSettings.of(Material.PISTON).breakByHand(true).breakByTool(TagRegistry.item(Identifier("fabric", "pickaxes")), 2).collidable(true).hardness(10f), 864)
        val STICKYWOODENPISTON = ConfigPiston(true, FabricBlockSettings.of(Material.PISTON).breakByHand(true).breakByTool(TagRegistry.item(Identifier("fabric", "axes")), 0).collidable(true).hardness(1f), 6)
        val STICKYGOLDPISTON = ConfigPiston(true, FabricBlockSettings.of(Material.PISTON).breakByHand(true).breakByTool(TagRegistry.item(Identifier("fabric", "pickaxes")), 0).collidable(true).hardness(1.5f), 24)
        val STICKYDIAMONDPISTON = ConfigPiston(true, FabricBlockSettings.of(Material.PISTON).breakByHand(true).breakByTool(TagRegistry.item(Identifier("fabric", "pickaxes")), 0).collidable(true).hardness(2.5f), 48)
        val STICKYNETHERITEPISTON = ConfigPiston(true, FabricBlockSettings.of(Material.PISTON).breakByHand(true).breakByTool(TagRegistry.item(Identifier("fabric", "pickaxes")), 0).collidable(true).hardness(5f), 96)
        val STICKYSUPERPISTON = ConfigPiston(true, FabricBlockSettings.of(Material.PISTON).breakByHand(true).breakByTool(TagRegistry.item(Identifier("fabric", "pickaxes")), 2).collidable(true).hardness(10f), 864)
    }

    override fun onInitialize() {
        println("\n\n\n\npulaskisandshaxes initializing\n\n\n\n")
        config.loadConfig()
        println("\n\n\n\n\n" + config.config.toString(Stringify.HJSON) + "\n\n\n\n\n")
        config.initConfigObject()
        //
        //config
        //
        //AutoConfig.register(configuration::class.java) { definition: Config?, configClass: Class<configuration?>? -> GsonConfigSerializer(definition, configClass) }
        //config = AutoConfig.getConfigHolder<configuration>(configuration::class.java).config

        //
        //enchants
        //
        val LETHALITY_ENCHANT: Enchantment = Registry.register(
                Registry.ENCHANTMENT,
                Identifier("pulaskisandshaxes", "lethality"),
                lethalityEnchant()
        )

        //
        //bedrock breaker stuff
        //
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "bedrock_breaker"), bedrockBreaker(ToolMaterials.NETHERITE, Item.Settings().group(ItemGroup.TOOLS).fireproof().maxCount(1).maxDamage(100).rarity(Rarity.EPIC)))
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "broken_bedrock_breaker"), BROKEN_BEDROCK_BREAKER)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "fused_chain"), FUZED_CHAIN)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "nether_core"), NETHER_CORE)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "netherite_corner"), NETHERITE_CORNER)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "netherite_stick"), NETHERITE_STICK)

        //
        // Meme stuff
        //
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "exposure_bucks"), EXPOSURE)
        Registry.register(Registry.BLOCK, Identifier("pulaskisandshaxes", "bad_apple"), BADAPPLE)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "bad_apple"), BlockItem(BADAPPLE, Item.Settings().group(ItemGroup.MISC)))

        //
        // Test stuff
        //
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "netherite_pulaski_test"), NETHERITE_PULASKI_TEST)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "netherite_shaxe_test"), NETHERITE_SHAXE_TEST)

        //
        //pulaskis and shaxes
        //
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "wooden_pulaski"), WOODEN_PULASKI)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "wooden_shaxe"), WOODEN_SHAXE)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "stone_pulaski"), STONE_PULASKI)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "stone_shaxe"), STONE_SHAXE)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "iron_pulaski"), IRON_PULASKI)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "iron_shaxe"), IRON_SHAXE)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "gold_pulaski"), GOLD_PULASKI)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "gold_shaxe"), GOLD_SHAXE)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "diamond_pulaski"), DIAMOND_PULASKI)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "diamond_shaxe"), DIAMOND_SHAXE)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "netherite_pulaski"), NETHERITE_PULASKI)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "netherite_shaxe"), NETHERITE_SHAXE)

        //
        //pistons
        //
        Registry.register(Registry.BLOCK, Identifier("pulaskisandshaxes", "wooden_piston"), WOODENPISTON)
        Registry.register(Registry.BLOCK, Identifier("pulaskisandshaxes", "sticky_wooden_piston"), STICKYWOODENPISTON)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "wooden_piston"), BlockItem(WOODENPISTON, Item.Settings().group(ItemGroup.REDSTONE)))
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "sticky_wooden_piston"), BlockItem(STICKYWOODENPISTON, Item.Settings().group(ItemGroup.REDSTONE)))
        Registry.register(Registry.BLOCK, Identifier("pulaskisandshaxes", "gold_piston"), GOLDPISTON)
        Registry.register(Registry.BLOCK, Identifier("pulaskisandshaxes", "sticky_gold_piston"), STICKYGOLDPISTON)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "gold_piston"), BlockItem(GOLDPISTON, Item.Settings().group(ItemGroup.REDSTONE)))
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "sticky_gold_piston"), BlockItem(STICKYGOLDPISTON, Item.Settings().group(ItemGroup.REDSTONE)))
        Registry.register(Registry.BLOCK, Identifier("pulaskisandshaxes", "diamond_piston"), DIAMONDPISTON)
        Registry.register(Registry.BLOCK, Identifier("pulaskisandshaxes", "sticky_diamond_piston"), STICKYDIAMONDPISTON)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "diamond_piston"), BlockItem(DIAMONDPISTON, Item.Settings().group(ItemGroup.REDSTONE)))
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "sticky_diamond_piston"), BlockItem(STICKYDIAMONDPISTON, Item.Settings().group(ItemGroup.REDSTONE)))
        Registry.register(Registry.BLOCK, Identifier("pulaskisandshaxes", "netherite_piston"), NETHERITEPISTON)
        Registry.register(Registry.BLOCK, Identifier("pulaskisandshaxes", "sticky_netherite_piston"), STICKYNETHERITEPISTON)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "netherite_piston"), BlockItem(NETHERITEPISTON, Item.Settings().group(ItemGroup.REDSTONE)))
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "sticky_netherite_piston"), BlockItem(STICKYNETHERITEPISTON, Item.Settings().group(ItemGroup.REDSTONE)))
        Registry.register(Registry.BLOCK, Identifier("pulaskisandshaxes", "super_piston"), SUPERPISTON)
        Registry.register(Registry.BLOCK, Identifier("pulaskisandshaxes", "sticky_super_piston"), STICKYSUPERPISTON)
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "super_piston"), BlockItem(SUPERPISTON, Item.Settings().group(ItemGroup.REDSTONE)))
        Registry.register(Registry.ITEM, Identifier("pulaskisandshaxes", "sticky_super_piston"), BlockItem(STICKYSUPERPISTON, Item.Settings().group(ItemGroup.REDSTONE)))

        println("\n\n\n\npulaskisandshaxes initialized\n\n\n\n")
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        if (month == Calendar.APRIL && day == 1) {
            println("April Fools Mode Active, please view log in monospace font.")
            println("\n| |||||| |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n            |  ||||| |  |||   |||||||||lllllllllllllllllL|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n         |||| ||||||  | |||||||||||llllll$$@@@@$$$$$$$$$$$@@l||||||||||||||||||||||||||     | ||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n    |||||||||||||||   ||||||||||lll$$$@@$$@@@@@@$$$$$$$$$$$$@@@lL|||||||||||||||||||||| ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n       |||||||||||||  |||||||ll$$$$$$@@@@@@@$$$@@$$$$$$$$@@@@@$@@|||||||||||||||||||||||||| ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n           ||||  ||   |||||lll$$\$l$$$$$$$@@@@@@@@$@@@@@@@@@@@@@$@l|||||||||||||||||           ||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n                 ||   ||||l$$$$$@@@@@@@@@@@@@@@@@@$$$$$$$$$@@@@@@@|||||||||||||||||          |||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n      ||      || ||     ||ll$$$$$@@@@@@@@@@@@@$$$$$$$$$$$$$@$@@@@@L|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n|||    ||| |  ||      |||||l$$$$$@@@@@@@$$$$$$$\$llllllll$$$$$$$@@@@L||||||||||||||  ||| ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n    ||||||||||||||      ||||ll$$$$$$$$$$$$$$$\$lllllllllll$$$$$$@$$@@||||||||||||||||  ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||     ||||ll$$$$$$$$$$$$$$$\$lllllllllll$$$$$$$$@@ll||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n  |||||||||||||||||||||||||||l$$$$$$$$$$$$$$$$\$lllllllllllll$$$$$$$@llll||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n|||||||||||||||||||||||||||||l$\$l$$$$$$$$$$$$\$lllllllllllllll$$$$$@\$l||l||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||||||||||||jlll$$$$$$$$$$$$$$$$$$$$$\$llllll$$$\$lll\$L|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n|||||||||||||||||||||||||||||||llll$$$$$$$$$$$$$$$&$$$$$\$llllll$\$l$\$llL|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||||||||||||||lll$$$$$$$$$\$llll$$$\$llllllllllllll$\$l||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n|||||| |||||||||||||||||||||||||||l$$$$$$$$$$\$lllllll\$lllllllllllllll|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n|||||| ||||||||||||||||||||||||||||j$$$$$$$$$\$llllllllllllllllllllll||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n|||||||||||||||||||||||||||||||||||l$$$$$$$$$\$lllll$$\$lllllllllllll|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n|||||||||||||||||||||||||||||||||||ll$$$$$$$$$$$\$llllllllllllllllll|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||||||||||||||||||ll$$$$$$$$$$\$llllllllllllllllll|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||||||||||||||||||||ll$$$$$$$$$$$@@@@\$lllllllllllllllllll|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||||||||||||||||||||||l$$$$$$@@@@@$$\$lllllllllllll$$$@@@@@@@L|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||||||||||||||||||||||||l$$$$$$$$$$\$lllllllllllll$$$$$$$$$$\$llllllll||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||||||||||||||||||||||||lll$$$$$$$$$\$lllllllll$\$ll$$$$$$$$$\$llllllllllllll||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n|||||||||||||||||||||||||||||||||||||||||$$@Wl$$$$$$$$$\$lllllllllll$$$$$$$$$$$$@@@@@@@@ggg@lll|l||||||||||||||||||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||||||||||||||||||||ll$$@@@l$$$$$$$$$$$$$\$l\$llll$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@|||||||||||||||||||||||||||||||||||||||||||||||||||||\n|||||||||||||||||||||||||||||||||||ll$$@@@@@@l$$$$$$$$$$$$$$$$$\$ll$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@gg|||||||||||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||||||||||||||lll$$@@@@@@@@@\$l$$$$$$$$$$$$$$$\$ll$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@L|||||||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||||||||||||l$$@@@@@@@@@@@@@@llll$$$$$$$$$$$$$$$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@L||||||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||||||||ll$$$@@@@@@@@@@@@@@@@@\$llllM$$$$$$$$$$$$$\$MTl$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@||||||||||||||||||||||||||||||||||||||||||\n|||||||||||||||||||||||l$$$@@@@@@@@@@@@@@@@@@@@$$\$llllll$$$$$$$$$$\$Tlll$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ll|||||||||||||||||||||||||||||||||||||||\n||||||||||||||||||||l$$$$@@@@@@@@@@@@@@@@@@@@@@$$$\$lllllllll$$$$$\$llllll$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Llll|||||||lll|||||||||||||||||||||||||||\n||||||||||||||||ll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$\$llllllllllllllllllllll$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@llll||||lllll|||||||||||||||||||||||||||\n||||||||||||ll$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@llllll@@@@@@@@@@@@@\$Mlll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@llllll||lllll|||||||||||||||||||||||||||\n||||||||||l$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@g$$$$\$lll$$$$$\$lllgllll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@llllll||llllll||||||||||||||||||||||||||\n|||||||||l$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$@$$$\$MMMMllll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@lllllllllllllllll||||||||||||||||||||||\n||||||||ll$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\$gggg@@@@g@@@@@@@@l$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@llllllllllllll|||||||||||||||||||||||||\n||||||llll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$&MMM\$MTTTlll$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@lllllllllllllll||||||||||||||||||||||||\n||||llllll$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Llllllllllllllll|l|||||||||||||||||||||\n|||||||ll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$\$TT||llllllll$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Lllllllllllll|||||||||||||||||||||||||\n|||||llll$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@lllllllllllll||||||||||||||||||||||||\n||||lllll$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$\$lllllllgg$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@llllllllllllllll|||||||||||||||||||||\nllllllllj$@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$%%$%%$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Llllllllllllllllllll||||||||||||||||\nllllllllj$@@@$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$@ggg@@@@@@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@lllllllllllllllllllll|||||||||||||||\nllllllll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$\$MMMMMM$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\$Llllllllllllllllll|||||||||||||||||\n|lllllll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@llllllllllllll|||||||||||||||||||||\nllllllll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\$MM$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\$Lllllllllllllllllllllll|||||||||||\nllllllll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Lllllllllllllllllllllllll|||||||||\nllllllll$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$\$ll$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@llllllllllllllllllllllll||||||||||\nllllllll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@lLlllllllllllllllll||||||||||||||\n|lllllll$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$@@$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@lllllllllllllllllll|||||||||||||\nlllllll%$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@lllllllllllllllllllll|||||||||||\nlllllll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\$llllllllllllllllllllll||||||||||\nlllllll$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\$llllllllllllllllllllll||||||||||\nlllllll$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@llllllllllllllllllllll||||||||\nllllll$$@@@@@@@@@@$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@lllllllllllllllllllllll||||||\nllllll%$@@@@@@@$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@llllllllllllllllllllllll||||\nllllll$$$$$$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$@@@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\$lllllllllllllllllllllllllll\n|l|llll$$$$$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@llllllllllllllllllllllllll\nllllll$$$$$$\$lllll$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@lllllllllllllllllllllll\nllll$@@$$\$lllllll$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@llllllllllllllllllllll\nllll$$@$$\$lll\$llll\$lllll$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$@$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@lllllllllllllllllllll\nlllllj$$$\$llllllllllllll$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\$llllllllllllllllllll\nlllllll$$\$lll$$\$llllllll$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\$llllllllllllllllll\nllll||l$$$\$l$$$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@lllllllllllllll\nllll||l$$$$$$$$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$@@@@@@@@$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$$$$\$lllllllll\nlll|||l$$$$$$$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$@@@@@@@@$$$@@@@@@@@@@@@@@@@@@@@@@@@M$$%@@@@@@@@@@@@@@@@@$$$$$$$$$$$$$$$$\$llll\nlllll||l$$$$$$$@@@@$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@llllM$%@@@@@@@@@@@@@@@$$$$$$$$@@@@$$$\$llll\nllllll|lll$$$@@@@@@@$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@llllllM$$@@@@@@@@@@@@$$$$$$$$$$$$$$\$lllll\nlllllllllllllMMMMMMMMM$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@llllllllllM$$@@@@@@@@@$$$$$$$$$$$$$\$lllll\nllll|ll|||llllllllllll$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@llllllllllllll$$@@@@@@@$$$$$$$$$$$\$llllll\nllll||||||lllllllllllll$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@\$llllllllllllll&$$@@$@@$$$$$$$$$$\$lllllll\nllll|lllllllllllllllll$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@\$llllllllllllllllj$$@@@@$$$$$$\$llllllllll")
        }
        val RESOURCE_PACK = RuntimeResourcePack.create("pulaskisandshaxes:test_resources")
        //
        //wood
        //
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "woodpiston"),
                JRecipe.shaped(JPattern.pattern("ppp", "sbs", "srs"),
                        JKeys.keys()
                                .key("p", JIngredient.ingredient().tag("minecraft:planks"))
                                .key("s", JIngredient.ingredient().item("minecraft:cobblestone"))
                                .key("r", JIngredient.ingredient().item("minecraft:redstone"))
                                .key("b", JIngredient.ingredient().tag("minecraft:planks")),
                        JResult.result("pulaskisandshaxes:wooden_piston")))
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "goldpiston_sticky"),
                JRecipe.shapeless(
                        JIngredients.ingredients()
                                .add(JIngredient.ingredient().item("minecraft:slime_ball"))
                                .add(JIngredient.ingredient().item("pulaskisandshaxes:wooden_piston")),
                        JResult.result("pulaskisandshaxes:sticky_wooden_piston")))
        //
        //gold
        //
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "goldpiston"),
                JRecipe.shaped(JPattern.pattern("ppp", "sbs", "srs"),
                        JKeys.keys()
                                .key("p", JIngredient.ingredient().tag("minecraft:planks"))
                                .key("s", JIngredient.ingredient().item("minecraft:cobblestone"))
                                .key("r", JIngredient.ingredient().item("minecraft:redstone"))
                                .key("b", JIngredient.ingredient().item("minecraft:gold_ingot")),
                        JResult.result("pulaskisandshaxes:gold_piston")))
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "goldpiston_sticky"),
                JRecipe.shapeless(
                        JIngredients.ingredients()
                                .add(JIngredient.ingredient().item("minecraft:slime_ball"))
                                .add(JIngredient.ingredient().item("pulaskisandshaxes:gold_piston")),
                        JResult.result("pulaskisandshaxes:sticky_gold_piston")))
        //
        //diamond
        //
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "diamondpiston"),
                JRecipe.shaped(JPattern.pattern("ppp", "sbs", "srs"),
                        JKeys.keys()
                                .key("p", JIngredient.ingredient().tag("minecraft:planks"))
                                .key("s", JIngredient.ingredient().item("minecraft:cobblestone"))
                                .key("r", JIngredient.ingredient().item("minecraft:redstone"))
                                .key("b", JIngredient.ingredient().item("minecraft:diamond")),
                        JResult.result("pulaskisandshaxes:diamond_piston")))
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "goldpiston_sticky"),
                JRecipe.shapeless(
                        JIngredients.ingredients()
                                .add(JIngredient.ingredient().item("minecraft:slime_ball"))
                                .add(JIngredient.ingredient().item("pulaskisandshaxes:diamond_piston")),
                        JResult.result("pulaskisandshaxes:sticky_diamond_piston")))
        //
        //netherite
        //
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "netheritepiston"),
                JRecipe.shaped(JPattern.pattern("ppp", "sbs", "srs"),
                        JKeys.keys()
                                .key("p", JIngredient.ingredient().tag("minecraft:planks"))
                                .key("s", JIngredient.ingredient().item("minecraft:cobblestone"))
                                .key("r", JIngredient.ingredient().item("minecraft:redstone"))
                                .key("b", JIngredient.ingredient().item("minecraft:netherite_ingot")),
                        JResult.result("pulaskisandshaxes:netherite_piston")))
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "netheritepiston_sticky"),
                JRecipe.shapeless(
                        JIngredients.ingredients()
                                .add(JIngredient.ingredient().item("minecraft:slime_ball"))
                                .add(JIngredient.ingredient().item("pulaskisandshaxes:netherite_piston")),
                        JResult.result("pulaskisandshaxes:sticky_netherite_piston")))
        //
        //netherite block
        //
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "superpiston"),
                JRecipe.shaped(JPattern.pattern("ppp", "sbs", "srs"),
                        JKeys.keys()
                                .key("p", JIngredient.ingredient().tag("minecraft:planks"))
                                .key("s", JIngredient.ingredient().item("minecraft:cobblestone"))
                                .key("r", JIngredient.ingredient().item("minecraft:redstone"))
                                .key("b", JIngredient.ingredient().item("minecraft:netherite_block")),
                        JResult.result("pulaskisandshaxes:super_piston")))
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "superpiston_sticky"),
                JRecipe.shapeless(
                        JIngredients.ingredients()
                                .add(JIngredient.ingredient().item("minecraft:slime_ball"))
                                .add(JIngredient.ingredient().item("pulaskisandshaxes:super_piston")),
                        JResult.result("pulaskisandshaxes:sticky_super_piston")))
        //
        //recipes some more
        //
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "fusedchain"),
                JRecipe.shapeless(
                        JIngredients.ingredients()
                                .add(JIngredient.ingredient().item("minecraft:chain"))
                                .add(JIngredient.ingredient().item("minecraft:blaze_powder")),
                        JResult.result("pulaskisandshaxes:fused_chain")))
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "netheritestick"),
                JRecipe.shapeless(
                        JIngredients.ingredients()
                                .add(JIngredient.ingredient().item("minecraft:netherite_ingot"))
                                .add(JIngredient.ingredient().item("minecraft:netherite_ingot"))
                                .add(JIngredient.ingredient().item("minecraft:blaze_rod")),
                        JResult.stackedResult("pulaskisandshaxes:netherite_stick", 2)))
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "netheritecorner"),
                JRecipe.shaped(
                        JPattern.pattern("  r", "br ", "  r"),
                        JKeys.keys()
                                .key("r", JIngredient.ingredient().item("minecraft:blaze_rod"))
                                .key("b", JIngredient.ingredient().item("minecraft:netherite_ingot")),
                        JResult.result("pulaskisandshaxes:netherite_corner")))
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "nethercore"),
                JRecipe.shaped(
                        JPattern.pattern("bcb", "csc", "bcb"),
                        JKeys.keys()
                                .key("s", JIngredient.ingredient().item("minecraft:nether_star"))
                                .key("b", JIngredient.ingredient().item("pulaskisandshaxes:netherite_stick"))
                                .key("c", JIngredient.ingredient().item("pulaskisandshaxes:netherite_corner")),
                        JResult.result("pulaskisandshaxes:nether_core")))
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "bedrockbreaker"),
                JRecipe.shaped(
                        JPattern.pattern("pcp", "bfb", "pfp"),
                        JKeys.keys()
                                .key("p", JIngredient.ingredient().item("minecraft:blaze_powder"))
                                .key("b", JIngredient.ingredient().item("minecraft:netherite_block"))
                                .key("c", JIngredient.ingredient().item("pulaskisandshaxes:nether_core"))
                                .key("f", JIngredient.ingredient().item("pulaskisandshaxes:fused_chain")),
                        JResult.result("pulaskisandshaxes:bedrock_breaker")))
        RESOURCE_PACK.addRecipe(
                Identifier("pulaskisandshaxes", "fixbedrockbreaker"),
                JRecipe.shapeless(
                        JIngredients.ingredients()
                                .add(JIngredient.ingredient().item("minecraft:netherite_block"))
                                .add(JIngredient.ingredient().item("minecraft:netherite_block"))
                                .add(JIngredient.ingredient().item("pulaskisandshaxes:broken_bedrock_breaker")),
                        JResult.result("pulaskisandshaxes:bedrock_breaker")))
        /*
		//
		//handle names
		//
		String[] ids = {
				"bedrock_breaker",
				"broken_bedrock-breaker",
				"nether_core",
				"netherite_corner",
				"netherite_stick",
				"fused_chain"
		};

		JLang lang = new JLang();

		for(int i = 0; i < ids.length; i++){
			String[] str = ids[i].split("_");
			String[] out = Arrays.copyOf(ids, ids.length);;
			for(int x = 0; x < str.length; x++){
				out[x] = str[x].substring(0, 1).toUpperCase() + str[x].substring(1);
			}
			String finished = String.join(" ", out);
			lang.item(new Identifier("pulaskisandshaxes", ids[i]), finished);
		}

		RESOURCE_PACK.addLang(new Identifier("pulaskisandshaxes", "en_us"), lang);
		*/

        //
        //register pack
        //
        RRPCallback.EVENT.register(RRPCallback { a -> a.add(RESOURCE_PACK) })
    }
}