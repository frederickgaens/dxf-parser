package be.codesolutions.dxf.entities;

import be.codesolutions.enums.DxfGraph;

public class DxfLine extends DxfEntity {

    private String entityType = DxfGraph.LINE.name();
    private DxfPoint start;
    private DxfPoint end;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public DxfPoint getStart() {
        return start;
    }

    public void setStart(DxfPoint start) {
        this.start = start;
    }

    public DxfPoint getEnd() {
        return end;
    }

    public void setEnd(DxfPoint end) {
        this.end = end;
    }
}
