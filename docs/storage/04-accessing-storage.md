# Accessing a Storage
Storages can be accessed using the `StorageHolder#manasCore$getStorageOptional` method and your `StorageKey` instance you get when registering a storage.

```java
entity.manasCore$getStorageOptional(STORAGE_KEY);
level.manasCore$getStorageOptional(STORAGE_KEY);
chunk.manasCore$getStorageOptional(STORAGE_KEY);
```

## Modifying Data
To modify data inside of a storage you can just mutate the storage instance you get from the `StorageHolder#manasCore$getStorageOptional` method.

It is recommended to create a getter and setter method for each field you want to modify within your storage to ensure that changes mark the storage as dirty:

```java
entity.manasCore$getStorageOptional(STORAGE_KEY).ifPresent(storage -> {
    storage.setExampleInt(420);
})
```
It is also possible to directly modify the storage data without using a setter method, but you have to call `markDirty` manually to ensure that the storage is marked as dirty:

```java
entity.manasCore$getStorageOptional(STORAGE_KEY).ifPresent(storage -> {
    storage.exampleInt = 420;
    storage.markDirty();
})
```