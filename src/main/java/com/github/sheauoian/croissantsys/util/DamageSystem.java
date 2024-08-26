package com.github.sheauoian.croissantsys.util;

import javax.annotation.Nullable;

public interface DamageSystem {
    void getReceiveDamage(Damage d);
    void getInflictDamage(Damage d);
}
