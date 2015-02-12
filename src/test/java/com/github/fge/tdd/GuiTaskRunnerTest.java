package com.github.fge.tdd;

import org.mockito.InOrder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GuiTaskRunnerTest
{
    // src/main/java: production source files
    // src/main/resources: production resources (non Java files)
    // src/test/java: test source files
    // src/test/resources: test resources (non Java files)

    private Executor guiRunner;
    private ExecutorService backgroundRunner;
    private GuiTaskRunner taskRunner;

    @BeforeMethod
    public void init()
    {
        guiRunner = mock(Executor.class);
        doAnswer(invocation -> {
            ((Runnable) invocation.getArguments()[0]).run();
            return null;
        }).when(guiRunner).execute(any(Runnable.class));

        backgroundRunner = mock(ExecutorService.class);
        doAnswer(invocation -> {
            ((Runnable) invocation.getArguments()[0]).run();
            return null;
        }).when(backgroundRunner).execute(any(Runnable.class));

        taskRunner = new GuiTaskRunner(guiRunner, backgroundRunner);
    }

    @Test
    public void backgroundThenGui()
    {
        final Runnable after = mock(Runnable.class);
        final Runnable task = mock(Runnable.class);

        taskRunner.run(task, after);

        final InOrder inOrder
            = inOrder(guiRunner, backgroundRunner, task, after);

        inOrder.verify(backgroundRunner).execute(any(Runnable.class));
        inOrder.verify(task).run();
        inOrder.verify(guiRunner).execute(after);
        inOrder.verify(after).run();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void backgroundThenGuiWithValue()
    {
        final Supplier<Object> supplier = mock(Supplier.class);
        final Consumer<Object> consumer = mock(Consumer.class);

        final Object value = new Object();
        when(supplier.get()).thenReturn(value);

        taskRunner.compute(supplier, consumer);

        final InOrder inOrder = inOrder(backgroundRunner, supplier,
            guiRunner, consumer);

        inOrder.verify(backgroundRunner).execute(any(Runnable.class));
        inOrder.verify(supplier).get();
        inOrder.verify(guiRunner).execute(any(Runnable.class));
        inOrder.verify(consumer).accept(same(value));
    }
}
