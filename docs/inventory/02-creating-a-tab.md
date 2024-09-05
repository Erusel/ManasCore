# Tabs
Tabs are a great way to make your Containers available to players by showing a CreativeTab-like Button in the Players Inventory.

## Creating a Tab
To create a Tab, your need to create a new class that extends `AbstractInventoryTab`. 

```java
public class TestInventoryTab extends AbstractInventoryTab {
    private final ItemStack iconStack;

    public TestInventoryTab(Item icon) {
        super(Tooltip.create(Component.literal("Test Tab - " + index)));
        this.iconStack = icon.getDefaultInstance();
    }

    @Override
    protected void renderIcon(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        var isTopRow = this.getCurrentTabIndex() < TABS_PER_ROW;
        guiGraphics.renderFakeItem(this.iconStack, this.getX() + 5, this.getY() + 8 + (isTopRow ? 1 : -1));
    }

    @Override
    public void sendOpenContainerPacket() {
        // Send a packet to the server to request the container to be opened
    }
}
```

The newly created Tab now needs to be registered.

### Architectury
```java
ClientLifecycleEvent.CLIENT_SETUP.register(instance -> InventoryTabsTest.init(19));
```
### Fabric
```java
ClientLifecycleEvents.CLIENT_STARTED.register(minecraft -> InventoryTabs.registerTab(new InventoryTabsTest.TestInventoryTab(Items.DIAMOND)));
```
### NeoForge
```java
// On NeoForge
private void clientInit(FMLClientSetupEvent e) {
    InventoryTabs.registerTab(new InventoryTabsTest.TestInventoryTab(Items.DIAMOND));
}
```

## Showing Tabs in your Container
To show the Tab in your Container, you need to implement the `InventoryTabScreen` in your ContainerScreen class.

```java
public class CustomContainerScreen<T> extends AbstractContainerScreen<T> implements InventoryTabScreen {
    private static final Class<TestInventoryTab> TAB_CLASS = TestInventoryTab.class;

    @Override
    public Class<TestInventoryTab> getTabClass() {
        return TAB_CLASS;
    }
}
```
