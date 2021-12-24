package be.codesolutions.dxf;

import be.codesolutions.dxf.beans.DxfPair;

import java.util.List;

public class DxfHeader {

    private List<DxfPair> vars;

    public List<DxfPair> getVars() {
        return vars;
    }

    public void setVars(List<DxfPair> vars) {
        this.vars = vars;
    }
}
