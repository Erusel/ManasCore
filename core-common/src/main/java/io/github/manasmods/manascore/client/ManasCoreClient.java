package com.github.manasmods.manascore.client;

import com.github.manasmods.manascore.api.skill.ManasSkillInstance;
import com.github.manasmods.manascore.api.skill.SkillAPI;
import com.github.manasmods.manascore.api.skill.SkillEvents;
import com.github.manasmods.manascore.client.keybinding.KeybindingManager;
import com.github.manasmods.manascore.network.NetworkManager;
import com.github.manasmods.manascore.network.toserver.RequestSkillScrollPacket;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientRawInputEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ManasCoreClient {
    public static void init() {
        ClientLifecycleEvent.CLIENT_SETUP.register(instance -> KeybindingManager.init());
    }
}
