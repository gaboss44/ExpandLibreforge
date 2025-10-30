package com.github.gaboss44.expandlibreforge.prerequisites

import com.willfp.eco.core.Prerequisite
import com.willfp.eco.util.ClassUtils

object PrerequisiteHasSmashAttemptEvent : Prerequisite(
    { ClassUtils.exists("io.papermc.paper.event.entity.EntityAttemptSmashAttackEvent") },
    "Requires server to have Paper's EntityAttemptSmashAttackEvent class available."
)