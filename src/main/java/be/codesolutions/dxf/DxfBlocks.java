package be.codesolutions.dxf;

import be.codesolutions.dxf.beans.DxfBlockEntity;

import java.util.List;

public class DxfBlocks {

    private List<DxfBlockEntity> blockEntities;

    public List<DxfBlockEntity> getBlockEntities() {
        return blockEntities;
    }

    public void setBlockEntities(List<DxfBlockEntity> blockEntities) {
        this.blockEntities = blockEntities;
    }
}
