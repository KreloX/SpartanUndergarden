package krelox.spartanundergarden;

import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import krelox.spartantoolkit.SpartanAddon;
import krelox.spartantoolkit.SpartanMaterial;
import krelox.spartantoolkit.WeaponMap;
import krelox.spartantoolkit.WeaponType;
import krelox.spartanundergarden.trait.ChillTrait;
import krelox.spartanundergarden.trait.RotspawnDamageBonusTrait;
import krelox.spartanundergarden.trait.UndergardenDamageBonusTrait;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import quek.undergarden.registry.UGItemTiers;
import quek.undergarden.registry.UGItems;
import quek.undergarden.registry.UGTags;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Mod(SpartanUndergarden.MODID)
public class SpartanUndergarden extends SpartanAddon {
    public static final String MODID = "spartanundergarden";

    public static final WeaponMap WEAPONS = new WeaponMap();
    public static final DeferredRegister<Item> ITEMS = itemRegister(MODID);
    public static final DeferredRegister<WeaponTrait> TRAITS = traitRegister(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = tabRegister(MODID);

    // Traits
    public static final RegistryObject<WeaponTrait> CHILLY = registerTrait(TRAITS, new ChillTrait());
    public static final RegistryObject<WeaponTrait> ROTSPAWN_DAMAGE_BONUS = registerTrait(TRAITS, new RotspawnDamageBonusTrait().setMagnitude(1.5F));
    public static final RegistryObject<WeaponTrait> UNDERGARDEN_DAMAGE_BONUS = registerTrait(TRAITS, new UndergardenDamageBonusTrait().setMagnitude(1.5F));

    // Materials
    public static final ArrayList<SpartanMaterial> MATERIALS = new ArrayList<>();

    public static final SpartanMaterial CLOGGRUM = material(UGItemTiers.CLOGGRUM, UGTags.Items.INGOTS_CLOGGRUM);
    public static final SpartanMaterial FROSTSTEEL = material(UGItemTiers.FROSTSTEEL, UGTags.Items.INGOTS_FROSTSTEEL, CHILLY);
    public static final SpartanMaterial UTHERIUM = material(UGItemTiers.UTHERIUM, UGTags.Items.INGOTS_UTHERIUM, ROTSPAWN_DAMAGE_BONUS);
    public static final SpartanMaterial FORGOTTEN = material(UGItemTiers.FORGOTTEN, UGTags.Items.INGOTS_FORGOTTEN_METAL, UNDERGARDEN_DAMAGE_BONUS).setRarity(UGItems.FORGOTTEN);

    @SafeVarargs
    private static SpartanMaterial material(UGItemTiers tier, TagKey<Item> repairTag, RegistryObject<WeaponTrait>... traits) {
        SpartanMaterial material = new SpartanMaterial(tier.name().toLowerCase(), MODID, tier, repairTag, traits);
        MATERIALS.add(material);
        return material;
    }

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab> SPARTAN_UNDERGARDEN_TAB = registerTab(TABS, MODID,
            () -> WEAPONS.get(FORGOTTEN, WeaponType.FLANGED_MACE).get(),
            (parameters, output) -> ITEMS.getEntries().forEach(item -> output.accept(item.get())));

    public SpartanUndergarden() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        registerSpartanWeapons(ITEMS);
        ITEMS.register(bus);
        TRAITS.register(bus);
        TABS.register(bus);
    }

    @Override
    protected void addItemTags(ItemTagsProvider provider, Function<TagKey<Item>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>> tag) {
        Function<SpartanMaterial, Item[]> func = material -> WEAPONS.entrySet().stream()
                .filter(entry -> entry.getKey().first().equals(material))
                .map(entry -> entry.getValue().get())
                .toArray(Item[]::new);
        tag.apply(UGTags.Items.CLOGGRUM_ITEMS).add(func.apply(CLOGGRUM));
        tag.apply(UGTags.Items.FROSTSTEEL_ITEMS).add(func.apply(FROSTSTEEL));
        tag.apply(UGTags.Items.UTHERIUM_ITEMS).add(func.apply(UTHERIUM));
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        WEAPONS.forEach((key, item) -> {
            SpartanMaterial material = key.first();
            WeaponType type = key.second();
            if (material.equals(FORGOTTEN)) {
                SmithingTransformRecipeBuilder
                        .smithing(Ingredient.of(UGItems.FORGOTTEN_UPGRADE_TEMPLATE.get()), Ingredient.of(WEAPONS.get(CLOGGRUM, type).get()),
                                Ingredient.of(UGTags.Items.INGOTS_FORGOTTEN_METAL), RecipeCategory.COMBAT, item.get())
                        .unlocks("has_" + UGItems.FORGOTTEN_UPGRADE_TEMPLATE.getId().getPath(), has(UGItems.FORGOTTEN_UPGRADE_TEMPLATE.get()))
                        .save(consumer, item.getId().withSuffix("_smithing"));
            } else type.recipe.accept(WEAPONS, consumer, material);
        });
    }

    @Override
    public String modid() {
        return MODID;
    }

    @Override
    public List<SpartanMaterial> getMaterials() {
        return MATERIALS;
    }

    @Override
    public WeaponMap getWeaponMap() {
        return WEAPONS;
    }
}
