package krelox.spartanundergarden.trait;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import krelox.spartantoolkit.BetterWeaponTrait;
import krelox.spartanundergarden.SpartanUndergarden;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import quek.undergarden.registry.UGEffects;

public class ChillTrait extends BetterWeaponTrait {
    public ChillTrait() {
        super("chilly", SpartanUndergarden.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public String getDescription() {
        return "Slows down foes for 30 seconds";
    }

    @Override
    public float modifyDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        victim.addEffect(new MobEffectInstance(UGEffects.CHILLY.get(), 600, 2, false, false));
        return super.modifyDamageDealt(material, baseDamage, source, attacker, victim);
    }
}
