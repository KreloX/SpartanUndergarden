package krelox.spartanundergarden.trait;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import krelox.spartantoolkit.BetterWeaponTrait;
import krelox.spartanundergarden.SpartanUndergarden;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import quek.undergarden.Undergarden;

import java.util.Locale;

public class UndergardenDamageBonusTrait extends BetterWeaponTrait {
    public UndergardenDamageBonusTrait() {
        super("undergarden_damage_bonus", SpartanUndergarden.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public String getDescription() {
        return String.format(Locale.US, "+%.1f%% damage against non-boss Undergarden mobs", magnitude * 100 - 100);
    }

    @Override
    public float modifyDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        var type = victim.getType();
        if (ForgeRegistries.ENTITY_TYPES.getKey(type).getNamespace().equals(Undergarden.MODID) && !type.is(Tags.EntityTypes.BOSSES)) {
            return baseDamage * magnitude;
        }
        return baseDamage;
    }
}
