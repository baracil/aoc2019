package perococco.aoc.computer._private;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.aoc.common.Tools;
import perococco.aoc.computer.AlterationConsumer;
import perococco.aoc.computer.Alterations;

@RequiredArgsConstructor
public class PrivateAlterations implements Alterations {

    private static final Alterations NONE = new PrivateAlterations();

    @NonNull
    private final ImmutableList<Alteration> actions;

    public PrivateAlterations() {
        this.actions = ImmutableList.of();
    }

    @Override
    public @NonNull Alterations addAlterations(int address, @NonNull String value) {
        return new PrivateAlterations(Tools.addValue(actions,new Alteration(address,value)));
    }

    @Override
    public @NonNull Alterations addAlterations(int address, @NonNull String value1, @NonNull String value2) {
        return new PrivateAlterations(Tools.addValues(actions, new Alteration(address,value1),new Alteration(address+1,value2)));
    }

    @Override
    public void handleAlterations(@NonNull AlterationConsumer consumer) {
        actions.forEach(a -> consumer.handleAlteration(a.address,a.value));
    }

    @NonNull
    public static Alterations none() {
        return NONE;
    }

    @RequiredArgsConstructor
    @Getter
    public static class Alteration {

        private final int address;

        @NonNull
        private final String value;

    }

}
