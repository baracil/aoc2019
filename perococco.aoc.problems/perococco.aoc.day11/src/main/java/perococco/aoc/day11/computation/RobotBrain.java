package perococco.aoc.day11.computation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import perococco.aoc.common.AOCException;
import perococco.aoc.common.Loop;
import perococco.aoc.computer.Execution;
import perococco.aoc.computer.Program;
import perococco.aoc.computer.io.*;

import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@RequiredArgsConstructor
public class RobotBrain {

    private final BlockingDeque<Instruction> instructions = new LinkedBlockingDeque<>();

    @NonNull
    private final Program program;

    private final BrainLoop loop = new BrainLoop();

    @Synchronized
    public void stop() {
        loop.stop();
    }

    @Synchronized
    public void start() {
        loop.start();
    }

    public void onColorRead(@NonNull Color color) {
        loop.onColorRead(color);
    }

    @NonNull
    public Instruction takeInstruction() {
        try {
            return instructions.takeFirst();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AOCException("Brain damaged",e);
        }
    }


    private class BrainLoop extends Loop {

        public void onColorRead(@NonNull Color color) {
            Optional.ofNullable(execution).ifPresent(e -> e.programInputAccessor().write(color));
        }

        private Execution<ProgramInput<Color>,ProgramOutput<String>> execution = null;


        @Override
        protected void beforeStarting() {
            super.beforeStarting();
            instructions.clear();
        }

        @Override
        protected void initializeRun() {
            super.initializeRun();
            execution = program.launch("Robot Brain", ProgramIO.duplex(Color::code, OutputTransformer.NONE));
            execution.whenDone(this::interrupt);
            instructions.addLast(Instruction.READ_COLOR);
        }

        @Override
        protected boolean performOneIteration() throws InterruptedException {
            final String color = execution.programOutputAccessor().read();
            final String turnDirection = execution.programOutputAccessor().read();

            instructions.addLast(Color.decode(color).instruction());
            instructions.addLast(TurnDirection.decode(turnDirection).instruction());
            instructions.addLast(Instruction.MOVE_FORWARD);
            instructions.addLast(Instruction.READ_COLOR);

            return execution.isDone();
        }

        @Override
        protected void beforeStopping() {
            super.beforeStopping();
            execution.interrupt();
            instructions.addLast(Instruction.HALT);
        }

        @Override
        protected void afterStopping() {
            super.afterStopping();
            execution = null;
        }

    }

}
