package sk.uniba.fmph.dcs.power_grid;

public record RequestError(String description) {
    public String getDescription() {
        return description;
    }
}

/* this is just a fake version */