/*
 * Copyright (c) 2024. ManasMods
 * GNU General Public License 3
 */

package io.github.manasmods.manascore.command.internal;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

import java.lang.annotation.Annotation;

public class CommandArgumentRegistry {
    private final Table<Class<?>, Class<? extends Annotation>, CommandArgumentFactory<? extends Annotation, ?>> ARGUMENT_FACTORIES = Tables.synchronizedTable(HashBasedTable.create());

    /**
     * Register a new argument annotation.
     *
     * @param argumentType       The class of the argument parameter.
     * @param argumentAnnotation The annotation class.
     * @param factory            The factory to create command nodes for the argument.
     */
    public <P, T extends Annotation> void register(Class<P> argumentType, Class<T> argumentAnnotation, CommandArgumentFactory<T, P> factory) {
        ARGUMENT_FACTORIES.put(argumentType, argumentAnnotation, factory);
    }

    @FunctionalInterface
    public interface CommandArgumentFactory<T extends Annotation, P> {
        void create(final T annotation, NodeCreator nodeCreator, ArgumentHandler<P> handler);
    }

    @FunctionalInterface
    public interface ArgumentGetter<T> {
        T get(CommandContext<CommandSourceStack> context);
    }

    @FunctionalInterface
    public interface NodeCreator {
        int add(ArgumentBuilder<CommandSourceStack, ?> node);
    }

    public interface ArgumentHandler<T> {
        void addToAllNodes(ArgumentGetter<T> valueExtractor);

        void addToLastNode(ArgumentGetter<T> valueExtractor);
    }
}
