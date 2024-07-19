package krelox.spartanundergarden;

import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import krelox.spartantoolkit.SpartanAddon;
import krelox.spartantoolkit.SpartanMaterial;
import krelox.spartantoolkit.WeaponMap;
import krelox.spartantoolkit.WeaponType;
import krelox.spartanundergarden.trait.ChillTrait;
import krelox.spartanundergarden.trait.RotspawnDamageBonusTrait;
import krelox.spartanundergarden.trait.UndergardenDamageBonusTrait;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import quek.undergarden.registry.UGItemTiers;
import quek.undergarden.registry.UGItems;
import quek.undergarden.registry.UGTags;

import java.util.List;

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
    public static final SpartanMaterial CLOGGRUM = material(UGItemTiers.CLOGGRUM, UGTags.Items.INGOTS_CLOGGRUM);
    public static final SpartanMaterial FROSTSTEEL = material(UGItemTiers.FROSTSTEEL, UGTags.Items.INGOTS_FROSTSTEEL, CHILLY);
    public static final SpartanMaterial UTHERIUM = material(UGItemTiers.UTHERIUM, UGTags.Items.INGOTS_UTHERIUM, ROTSPAWN_DAMAGE_BONUS);
    public static final SpartanMaterial FORGOTTEN = material(UGItemTiers.FORGOTTEN, UGTags.Items.INGOTS_FORGOTTEN_METAL, UNDERGARDEN_DAMAGE_BONUS).setRarity(UGItems.FORGOTTEN);

    @SafeVarargs
    private static SpartanMaterial material(UGItemTiers tier, TagKey<Item> repairTag, RegistryObject<WeaponTrait>... traits) {
        return new SpartanMaterial(tier.name().toLowerCase(), MODID, tier, repairTag, traits);
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
    public String modid() {
        return MODID;
    }

    @Override
    public List<SpartanMaterial> getMaterials() {
        return List.of(CLOGGRUM, FROSTSTEEL, UTHERIUM, FORGOTTEN);
    }

    @Override
    public WeaponMap getWeaponMap() {
        return WEAPONS;
    }
}
