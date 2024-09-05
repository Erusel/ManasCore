# ManasCore

Documentation: [ManasCore](https://manascore.github.io/ManasCore/)

## Contributing

### Creating a new Module

To create a new module, include it in the `settings.gradle` file.

This will generate the project folders for you.

Most of the base configuration is done in the project wide `build.gradle` file.

- Create an empty `build.gradle` in the `<module>-common` folder.
- Create an empty `build.gradle` in the `<module>-fabric` folder.
- Create an empty `build.gradle` in the `<module>-neoforge` folder.
- Create an `gradle.properties` in the `<module>-neoforge` folder.
    - File Content:
      ```properties
      loom.platform=neoforge
      ```

Example Commit for a module initiallization: https://github.com/ManasMods/ManasCore/commit/db92a894268b41977a364faff05e4ce055085976