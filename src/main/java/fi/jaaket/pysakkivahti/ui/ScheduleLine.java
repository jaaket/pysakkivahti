package fi.jaaket.pysakkivahti.ui;

public class ScheduleLine {
    private String arrivalTime;
    private boolean isHighlighted;

    public ScheduleLine(String arrivalTime, boolean isHighlighted) {
        this.arrivalTime = arrivalTime;
        this.isHighlighted = isHighlighted;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }
}
