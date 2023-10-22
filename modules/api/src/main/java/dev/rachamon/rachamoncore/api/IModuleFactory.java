package dev.rachamon.rachamoncore.api;

import dev.rachamon.rachamoncore.api.utils.LoggerUtil;

import java.nio.file.Path;

public interface IModuleFactory<T> {
    LoggerUtil getModuleLogger();

    T getInstance();

    Path getDirectory();
}
