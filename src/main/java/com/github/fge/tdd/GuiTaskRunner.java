package com.github.fge.tdd;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class GuiTaskRunner
{
    private final Executor guiRunner;
    private final ExecutorService backgroundRunner;

    public GuiTaskRunner(final Executor guiRunner,
        final ExecutorService backgroundRunner)
    {
        this.guiRunner = guiRunner;
        this.backgroundRunner = backgroundRunner;
    }

    public void run(final Runnable task, final Runnable after)
    {
        backgroundRunner.execute(() -> {
            task.run();
            guiRunner.execute(after);
        });
    }

    public <T> void compute(final Supplier<? extends T> supplier,
        final Consumer<? super T> consumer)
    {
        backgroundRunner.execute(() -> {
            final T t = supplier.get();
            guiRunner.execute(() -> consumer.accept(t));
        });
    }
}
