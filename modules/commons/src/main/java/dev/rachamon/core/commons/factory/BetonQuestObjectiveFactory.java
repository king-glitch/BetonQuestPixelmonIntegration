package dev.rachamon.core.commons.factory;

public abstract class BetonQuestObjectiveFactory<T> {
    public abstract void register(String objective, Class<? extends T> clazz);
}
