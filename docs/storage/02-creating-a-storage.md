# Creating a custom Storage

Creating a custom Storage is fairly simple. All you need is to create a class that extends the `Storage` class and
implement the required methods.
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
}
```
