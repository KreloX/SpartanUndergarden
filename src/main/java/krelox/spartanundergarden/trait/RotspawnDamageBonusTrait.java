package krelox.spartanundergarden.trait;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import krelox.spartantoolkit.BetterWeaponTrait;
import krelox.spartanundergarden.SpartanUndergarden;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;
import quek.undergarden.network.CreateCritParticlePacket;
import quek.undergarden.network.UGPacketHandler;
import quek.undergarden.registry.UGParticleTypes;
import quek.undergarden.registry.UGTags;

import java.util.Locale;

public class RotspawnDamageBonusTrait extends BetterWeaponTrait {
    public RotspawnDamageBonusTrait() {
        super("rotspawn_damage_bonus", SpartanUndergarden.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public String getDescription() {
        return String.format(Locale.US, "+%.1f%% damage against Rotspawn", magnitude * 100 - 100);
    }

    @Override
    public float modifyDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        if (victim.getType().is(UGTags.Entities.ROTSPAWN)) {
            if (!victim.level().isClientSide()) {
                UGPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new CreateCritParticlePacket(victim.getId(), 2, UGParticleTypes.UTHERIUM_CRIT.get()));
            }
            return baseDamage * magnitude;
        }
        return baseDamage;
    }
}
