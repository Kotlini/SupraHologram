# SupraHologram

SupraHologram is a lightweight library for creating holograms in Minecraft. This library is currently compatible with Minecraft version 1.8.8.

## Features

- Create and manage holograms with ease.
- Different types of holograms supported:
  - HologramItem: Add floating items.
  - HologramText: Display text.
  - HologramTextPacket: Display text using packets.
- Simple API for adding, removing, and modifying holograms.
- Serialization and deserialization of holograms for easy saving and loading.

## | Installation

You can include SupraHologram in your project using [Jitpack](https://jitpack.io/).

### Maven

```xml
<dependency>
    <groupId>com.github.Kotlini</groupId>
    <artifactId>SupraHologram</artifactId>
    <version>0.0.14</version>
</dependency>
```
### Gradle
```kotlin
dependencies {
    implementation 'com.github.Kotlini:SupraHologram:0.0.14'
}
```

## | Usage
### Initialize SupraHolograms
In your main class, register SupraHolograms with Bukkit's PluginManager:
```java
SupraHolograms.register(this, Bukkit.getPluginManager());
```

## | Save and Load Holograms
### Save your holograms to a YAML file:
```java
final File holoFile = new File(getDataFolder() + File.separator + "holograms.yml");
SupraHolograms.getInstance().save(holoFile, YamlConfiguration.loadConfiguration(holoFile));
```

### Load holograms from the YAML file:
```java
SupraHolograms.getInstance().load(YamlConfiguration.loadConfiguration(holoFile));
```
## | Create Holograms
### Use the HologramBuilder to create holograms easily:

SupraHologram supports multiple types of holograms to suit various use cases in Minecraft.

### HologramItem
#### HologramItem allows you to display floating items in your Minecraft world. This is useful for showing off items or creating interactive elements.
```java
HologramBuilder builder = new HologramBuilder("itemBox", location);
builder.appendLineItem(itemStack)
       .addViewer(player)
       .build();
```

### HologramText
#### HologramText enables you to display text in your holograms. This is great for displaying messages, information, or labels.
```java
HologramBuilder builder = new HologramBuilder("textBox", location);
builder.appendLine("Welcome to the server!")
       .addViewer(player)
       .build();
```

### HologramTextPacket
#### HologramTextPacket allows you to create text holograms using packets for more advanced text formatting and animations.
```java
HologramBuilder builder = new HologramBuilder("packetBox", location);
builder.appendLinePacket("This is animated text")
       .addViewer(player)
       .build();
```

## | Access Holograms
#### You can access holograms using the following methods:

### To get an entire hologram box:
```java
HologramBox hologramBox = SupraHolograms.getInstance().getHologramsBox("personal");
```

### To get a specific hologram within a box:
```java
Hologram hologram = hologramBox.getHologram(0);
```
