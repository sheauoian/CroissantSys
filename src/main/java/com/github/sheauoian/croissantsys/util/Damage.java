package com.github.sheauoian.croissantsys.util;

import javax.annotation.Nullable;

public class Damage {
    public static void getDamage(Damage d, DamageSystem attacker, DamageSystem victim) {
        if (attacker != null) attacker.getInflictDamage(d);
        if (victim != null) victim.getReceiveDamage(d);
    }
    public double damage;
    public final Element[] element;
    public Damage(double d, Element[] e) {
        if (d < 0) d = 0;
        this.damage = d;
        this.element = e;
    }
}
