# Registering Storages

Registering a custom Storage attaches an instance of your custom Storage class to a specific object.

To register a custom Storage, you need to choose where to attach your data to.
This can be an [Entity](#registering-a-storage-to-an-entity), a [Chunk](#registering-a-storage-to-a-chunk) or a [World / Level](#registering-a-storage-to-a-level).

## Registering a Storage to an Entity

### Architectury
To register a Storage to an Entity in Architectury, you need to listen to the `StorageEvents.REGISTER_ENTITY_STORAGE` Event.
```java
StorageKey<ExampleStorage> STORAGE_KEY;

StorageEvents.REGISTER_ENTITY_STORAGE.register(registry -> {
    STORAGE_KEY = registry.register(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, STORAGE_ID), // Your custom Storage ID
            ExampleStorage.class,  // Your custom Storage class
            entity -> entity instanceof Player, // Predicate to check if the Storage should be attached to the Entity
            ExampleStorage::new // Supplier to create a new instance of your custom Storage
        );
});
```
This should be done in the common package at entrypoint invocation (In fabric the `ModInitializer#onInitialize` and on NeoForge the mod constructor).

### Fabric
To register a Storage to an Entity in Fabric, you need to listen to the `StorageEvents.REGISTER_ENTITY_STORAGE` Event.
```java
StorageKey<ExampleStorage> STORAGE_KEY;

StorageEvents.REGISTER_ENTITY_STORAGE.register(registry -> {
    STORAGE_KEY = registry.register(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, STORAGE_ID), // Your custom Storage ID
            ExampleStorage.class,  // Your custom Storage class
            entity -> entity instanceof Player, // Predicate to check if the Storage should be attached to the Entity
            ExampleStorage::new // Supplier to create a new instance of your custom Storage
        );
});
```
This should be done in the `ModInitializer#onInitialize` method.

### NeoForge
To register a Storage to an Entity in NeoForge, you need to listen to the `StorageEvents.REGISTER_ENTITY_STORAGE` Event.
```java
StorageKey<ExampleStorage> STORAGE_KEY;

StorageEvents.REGISTER_ENTITY_STORAGE.register(registry -> {
    STORAGE_KEY = registry.register(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, STORAGE_ID), // Your custom Storage ID
            ExampleStorage.class,  // Your custom Storage class
            entity -> entity instanceof Player, // Predicate to check if the Storage should be attached to the Entity
            ExampleStorage::new // Supplier to create a new instance of your custom Storage
        );
});
```
This should be done in the mod constructor.

## Registering a Storage to a Chunk

### Architectury
To register a Storage to an Entity in Architectury, you need to listen to the `StorageEvents.REGISTER_CHUNK_STORAGE` Event.
```java
StorageKey<ExampleStorage> STORAGE_KEY;

StorageEvents.REGISTER_CHUNK_STORAGE.register(registry -> {
    STORAGE_KEY = registry.register(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, STORAGE_ID), // Your custom Storage ID
            ExampleStorage.class,  // Your custom Storage class
            entity -> entity instanceof Player, // Predicate to check if the Storage should be attached to the Entity
            ExampleStorage::new // Supplier to create a new instance of your custom Storage
        );
});
```
This should be done in the common package at entrypoint invocation (In fabric the `ModInitializer#onInitialize` and on NeoForge the mod constructor).

### Fabric
To register a Storage to an Entity in Fabric, you need to listen to the `StorageEvents.REGISTER_CHUNK_STORAGE` Event.
```java
StorageKey<ExampleStorage> STORAGE_KEY;

StorageEvents.REGISTER_CHUNK_STORAGE.register(registry -> {
    STORAGE_KEY = registry.register(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, STORAGE_ID), // Your custom Storage ID
            ExampleStorage.class,  // Your custom Storage class
            entity -> entity instanceof Player, // Predicate to check if the Storage should be attached to the Entity
            ExampleStorage::new // Supplier to create a new instance of your custom Storage
        );
});
```
This should be done in the `ModInitializer#onInitialize` method.

### NeoForge
To register a Storage to an Entity in NeoForge, you need to listen to the `StorageEvents.REGISTER_CHUNK_STORAGE` Event.
```java
StorageKey<ExampleStorage> STORAGE_KEY;

StorageEvents.REGISTER_CHUNK_STORAGE.register(registry -> {
    STORAGE_KEY = registry.register(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, STORAGE_ID), // Your custom Storage ID
            ExampleStorage.class,  // Your custom Storage class
            entity -> entity instanceof Player, // Predicate to check if the Storage should be attached to the Entity
            ExampleStorage::new // Supplier to create a new instance of your custom Storage
        );
});
```
This should be done in the mod constructor.

## Registering a Storage to a World

### Architectury
To register a Storage to an Entity in Architectury, you need to listen to the `StorageEvents.REGISTER_WORLD_STORAGE` Event.
```java
StorageKey<ExampleStorage> STORAGE_KEY;

StorageEvents.REGISTER_WORLD_STORAGE.register(registry -> {
    STORAGE_KEY = registry.register(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, STORAGE_ID), // Your custom Storage ID
            ExampleStorage.class,  // Your custom Storage class
            entity -> entity instanceof Player, // Predicate to check if the Storage should be attached to the Entity
            ExampleStorage::new // Supplier to create a new instance of your custom Storage
        );
});
```
This should be done in the common package at entrypoint invocation (In fabric the `ModInitializer#onInitialize` and on NeoForge the mod constructor).

### Fabric
To register a Storage to an Entity in Fabric, you need to listen to the `StorageEvents.REGISTER_WORLD_STORAGE` Event.
```java
StorageKey<ExampleStorage> STORAGE_KEY;

StorageEvents.REGISTER_WORLD_STORAGE.register(registry -> {
    STORAGE_KEY = registry.register(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, STORAGE_ID), // Your custom Storage ID
            ExampleStorage.class,  // Your custom Storage class
            entity -> entity instanceof Player, // Predicate to check if the Storage should be attached to the Entity
            ExampleStorage::new // Supplier to create a new instance of your custom Storage
        );
});
```
This should be done in the `ModInitializer#onInitialize` method.

### NeoForge
To register a Storage to an Entity in NeoForge, you need to listen to the `StorageEvents.REGISTER_WORLD_STORAGE` Event.
```java
StorageKey<ExampleStorage> STORAGE_KEY;

StorageEvents.REGISTER_WORLD_STORAGE.register(registry -> {
    STORAGE_KEY = registry.register(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, STORAGE_ID), // Your custom Storage ID
            ExampleStorage.class,  // Your custom Storage class
            entity -> entity instanceof Player, // Predicate to check if the Storage should be attached to the Entity
            ExampleStorage::new // Supplier to create a new instance of your custom Storage
        );
});
```
This should be done in the mod constructor.