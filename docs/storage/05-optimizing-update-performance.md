# Optimize update Package performance
By default ManasCore Storages fully deserialize and serialize the data on every update. 
This can be a performance bottleneck if you have a lot of data in your storage. 
To optimize the performance, you can override the `Storage#saveOutdated` method in your custom Storage class to only apply the changed data to the update tag.

```java
public static class ExampleStorage extends Storage {
    private int exampleInt = 0;
    
    public ExampleStorage(StorageHolder holder) {
        super(holder);
    }

    @Override
    public void save(CompoundTag data) {
        data.putInt("exampleInt", exampleInt);
    }

    @Override
    public void load(CompoundTag data) {
        exampleInt = data.getInt("exampleInt");
    }
    
    public int getExampleInt() {
        return exampleInt;
    }
    
    public void setExampleInt(int exampleInt) {
        this.exampleInt = exampleInt;
        markDirty(); // Tells the system that the storage has been modified
    }

    @Override
    public void saveOutdated(CompoundTag data) {
        data.putInt("exampleInt", exampleInt);
    }
}
```
