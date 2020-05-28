package dev.dewy.dqs.cache.data.entity;

import dev.dewy.dqs.protocol.game.entity.Effect;


public class PotionEffect
{
    public final Effect effect;
    public int amplifier;
    public int duration;
    public boolean ambient;
    public boolean showParticles;

    public PotionEffect(Effect effect)
    {
        this.effect = effect;
    }

    public PotionEffect(Effect effect, int amplifier, int duration, boolean ambient, boolean showParticles)
    {
        this.effect = effect;
        this.amplifier = amplifier;
        this.duration = duration;
        this.ambient = ambient;
        this.showParticles = showParticles;
    }

    public Effect getEffect()
    {
        return this.effect;
    }

    public int getAmplifier()
    {
        return this.amplifier;
    }

    public PotionEffect setAmplifier(int amplifier)
    {
        this.amplifier = amplifier;
        return this;
    }

    public int getDuration()
    {
        return this.duration;
    }

    public PotionEffect setDuration(int duration)
    {
        this.duration = duration;
        return this;
    }

    public boolean isAmbient()
    {
        return this.ambient;
    }

    public PotionEffect setAmbient(boolean ambient)
    {
        this.ambient = ambient;
        return this;
    }

    public boolean isShowParticles()
    {
        return this.showParticles;
    }

    public PotionEffect setShowParticles(boolean showParticles)
    {
        this.showParticles = showParticles;
        return this;
    }

    public String toString()
    {
        return "PotionEffect(effect=" + this.getEffect() + ", amplifier=" + this.getAmplifier() + ", duration=" + this.getDuration() + ", ambient=" + this.isAmbient() + ", showParticles=" + this.isShowParticles() + ")";
    }
}
