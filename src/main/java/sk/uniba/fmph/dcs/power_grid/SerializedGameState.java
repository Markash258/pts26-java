package sk.uniba.fmph.dcs.power_grid;

public final class SerializedGameState {
    private final String state;

    public SerializedGameState(final String state) {
        assert state != null;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SerializedGameState)) {
            return false;
        }
        SerializedGameState other = (SerializedGameState) obj;
        return state.equals(other.state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public String toString() {
        return "SerializedGameState[state=" + state + "]";
    }
}
