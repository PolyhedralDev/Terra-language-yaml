package com.dfsek.terra.addons.yaml;

import com.dfsek.tectonic.yaml.YamlConfiguration;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.ConfigurationDiscoveryEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;


@Addon("language-yaml")
@Version("1.0.0")
@Author("Terra")
public class YamlAddon extends TerraAddon {
    @Inject
    private Platform platform;
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigurationDiscoveryEvent.class)
                .then(event -> event.getLoader().open("", ".yml").thenEntries(entries -> entries.forEach(entry -> {
                platform.getDebugLogger().info("Discovered config " + entry.getKey());
                event.register(entry.getKey(), new YamlConfiguration(entry.getValue(), entry.getKey()));
            })))
                .failThrough();
    }
}
